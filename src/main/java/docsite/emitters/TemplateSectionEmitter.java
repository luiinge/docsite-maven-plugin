package docsite.emitters;

import j2html.tags.Tag;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import docsite.*;
import docsite.util.*;
import freemarker.core.PlainTextOutputFormat;
import freemarker.template.*;
import j2html.tags.specialized.*;
import org.jetbrains.annotations.NotNull;
import static j2html.TagCreator.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

public class TemplateSectionEmitter extends GeneratedSectionEmitter {

    private final Path templatePath;

    public TemplateSectionEmitter(EmitterBuildParams params) {
        super(params);
        this.templatePath = locateTemplate(section.template());
    }


    @Override
    protected SectionTag generateSectionContent(Tag<?> before) {
        try (StringWriter output = new StringWriter()) {
            Configuration cfg = markerfreeConfiguration();
            Template template = cfg.getTemplate(templatePath.getFileName().toString());
            template.process(documentMap(), output);
            return section().with(before).with(rawHtml(output.toString()));
        } catch (IOException | TemplateException e) {
            throw new DocsiteException(e);
        }
    }


    @NotNull
    private Configuration markerfreeConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDirectoryForTemplateLoading(templatePath.getParent().toFile());
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.setAutoEscapingPolicy(Configuration.DISABLE_AUTO_ESCAPING_POLICY);
        cfg.setOutputFormat(PlainTextOutputFormat.INSTANCE);
        return cfg;
    }


    @SuppressWarnings("unchecked")
    private Map<String,Object> documentMap() throws IOException {
        String origin = origin();
        String extension = origin.substring(origin.indexOf(".")+1).toLowerCase();
        try (InputStream inputStream = ResourceUtil.open(baseDir, origin)) {
            switch (extension) {
                case "xml":
                    return new XmlParser().parse(inputStream);
                case "json":
                    return new ObjectMapper().readValue(inputStream, Map.class);
                case "yaml":
                case "yml":
                    return new Yaml().loadAs(inputStream, Map.class);
                default:
                    throw new IOException("Templating cannot be applied to format of "+origin);
            }
        }
    }



    private static Path locateTemplate(String fromTemplate) {
        String internalTemplate = "templates/"+fromTemplate+".ftl";
        if (ResourceUtil.existsResource(internalTemplate)) {
            try {
                Path tempFile = Files.createTempDirectory("docsite").resolve(internalTemplate);
                ResourceUtil.copyResource(internalTemplate, tempFile.getParent());
                return tempFile;
            } catch (IOException e) {
                throw new DocsiteException(e);
            }
        } else {
            if (!Files.exists(Path.of(fromTemplate))) {
                throw new DocsiteException("Template "+fromTemplate+" is neither an existing file nor a built-in template");
            }
            return Path.of(fromTemplate);
        }
    }
}