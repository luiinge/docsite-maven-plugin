package docsite.emitters;


import static j2html.TagCreator.*;

import java.io.*;

import docsite.*;
import docsite.util.ResourceUtil;
import j2html.tags.Tag;
import j2html.tags.specialized.SectionTag;
import org.asciidoctor.*;

public class AsciidocGeneratedSectionEmitter extends GeneratedSectionEmitter {

    private final Asciidoctor asciidoctor = Asciidoctor.Factory.create();
    private final Options options = Options.builder().build();



    public AsciidocGeneratedSectionEmitter(EmitterBuildParams params) {
        super(params);
    }



    @Override
    protected SectionTag generateSectionContent(Tag<?> before) {
        try (Reader reader = new InputStreamReader(ResourceUtil.open(baseDir, origin()))) {
            StringWriter writer = new StringWriter();
            asciidoctor.convert(reader, writer, options);
            String html = writer.toString();
            html = removeH1(html);
            html = generateHeadersId(html);
            html = normalizeLinks(html);
            html = replaceLocalImages(html);
            html = replaceMermaidDiagrams(html);
            return section().with(before).with(rawHtml(html)).withClass("content");
        }  catch (IOException e) {
            throw new DocsiteException(e);
        }
    }

    @Override
    protected String replaceMermaidDiagrams(String html) {
        return html.replaceAll(
            "<pre class=\"highlight\"><code class=\"language-mermaid\" data-lang=\"mermaid\">([^<]+)</code></pre>",
            "<div class=\"mermaid\">$1</div>"
        );
    }

}