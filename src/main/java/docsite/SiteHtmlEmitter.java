package docsite;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.regex.*;
import java.util.stream.*;
import com.vdurmont.emoji.EmojiParser;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import docsite.Section.SectionType;
import j2html.tags.ContainerTag;
import static docsite.EmitterUtil.normalizeLinks;
import static j2html.TagCreator.*;

public class SiteHtmlEmitter {



    private static final String CSS_FILE = "style.css";
    private static final String PRISM_CSS_FILE = "prism-light.css";
    private static final String PRISM_JS_FILE = "prism.js";


    private static final int tocMinLevel = 2;
    private static final int tocMaxLevel = 3;

    private final MutableDataSet options = new MutableDataSet();
    private final Parser parser = Parser.builder(options).build();
    private final HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    private final Logger logger;




    public SiteHtmlEmitter(Logger logger) {
        this.logger = logger;
    }




    public void generateSite(SiteConfiguration configuration) throws IOException {
        SectionHtmlEmitter home = SectionHtmlEmitter.build(configuration);
        writePage(configuration,home);
        copyResource(CSS_FILE, configuration.outputFolder());
        copyResource(PRISM_CSS_FILE, configuration.outputFolder());
        copyResource(PRISM_JS_FILE, configuration.outputFolder());
    }



    private void writePage(SiteConfiguration site, SectionHtmlEmitter section) throws IOException {
        if (!section.isValid()) {
            warn("Section {} is not valid and would not be included", section.name());
            return;
        }
        if (section.hasType(SectionType.GENERATED, SectionType.GROUP)) {
            ContainerTag<?> heading = section.createHeaderFromconfiguration();
            ContainerTag<?> breadcrumbs = section.createBreadcrumbs();
            ContainerTag<?> content = createContentFromSource(section);
            ContainerTag<?> toc = section.hasMarkdownSource() ?
                createTOCFromMarkdown(section.source()) :
                div();
            ContainerTag<?> page = buildPage(site, heading, breadcrumbs, toc, content);

            write(section.outputFile(), page, section.hasMarkdownSource());
        }
        for (SectionHtmlEmitter subsection : section.children()) {
            writePage(site, subsection);
        }
    }







    private ContainerTag<?> createContentFromSource(SectionHtmlEmitter section) throws IOException {
        ContainerTag<?> content;
        if (section.hasMarkdownSource()) {
            content = createContentFromMarkdown(section.source());
         } else if (section.hasHTMLSource()){
            content = createContentFromHTML(section.source());
        } else {
            content = createContentFromText(section.source());
        }
        return content;
    }




    private void write(Path outputFile, ContainerTag<?> page, boolean withEmojis) throws IOException {
        String html = page.render();
        if (withEmojis) {
            html = EmojiParser.parseToUnicode(html);
        }
        Files.writeString(outputFile, html, StandardCharsets.UTF_8);
        logger.info.accept("Written file "+outputFile);
    }




    private ContainerTag<?> buildPage(
        SiteConfiguration site,
        ContainerTag<?> heading,
        ContainerTag<?> breadcrumbs,
        ContainerTag<?> sidebar,
        ContainerTag<?> content
    ) {
        return html().with(
            head().with(
                link().attr("href",CSS_FILE).attr("rel","stylesheet"),
                link().attr("href",PRISM_CSS_FILE).attr("rel","stylesheet"),
                meta().withCharset("UTF-8"),
                title(site.title())
            ),
            body()
            .with(heading)
            .with(breadcrumbs)
            .with(div().withClass("main").with(sidebar,content))
            .with(script().attr("src",PRISM_JS_FILE))
        );
    }



    private ContainerTag<?> createContentFromMarkdown(String markdownSource) throws IOException {
        try (InputStream markdown = open(markdownSource)) {
            String markdownContent = read(markdown);
            Node document = parser.parse(markdownContent);
            StreamSupport.stream(document.getChildren().spliterator(), false)
                .filter(node->node instanceof Heading)
                .map(Heading.class::cast)
                .forEach(heading -> heading.setAnchorRefId(hrefId(heading.getAnchorRefText())));
            String html = renderer.render(document);
            return div().with(rawHtml(normalizeLinks(generateHeadersId(html)))).withClass("content");
        }
    }


    private ContainerTag<?> createContentFromHTML(String htmlSource) throws IOException {
        try (InputStream html = open(htmlSource)) {
            return div().with(rawHtml(normalizeLinks(read(html)))).withClass("content");
        }
    }


    private ContainerTag<?> createContentFromText(String textSource) throws IOException {
        try (InputStream text = open(textSource)) {
            return div().with(pre(read(text)));
        }
    }




    private ContainerTag<?> createTOCFromMarkdown(String markdownSource)
    throws IOException {

        try (InputStream markdown = open(markdownSource)) {
            Node document = parser.parse(read(markdown));

            List<Heading> children = StreamSupport.stream(document.getChildren().spliterator(), false)
                .filter(node->node instanceof Heading)
                .map(Heading.class::cast)
                .filter(child->child.getLevel()>=tocMinLevel)
                .filter(child->child.getLevel()<=tocMaxLevel)
                .collect(Collectors.toList());

            StringBuilder string = new StringBuilder();
            int previousLevel = tocMinLevel - 1;
            for (Heading heading : children) {
                if (previousLevel > heading.getLevel()) {
                    string.append("</ol>".repeat(Math.max(0, previousLevel - heading.getLevel())));
                } else {
                    string.append("<ol>".repeat(Math.max(0, heading.getLevel() - previousLevel)));
                }
                string
                    .append("<li><a class=\"internal\" href=\"#")
                    .append(hrefId(heading.getAnchorRefText()))
                    .append("\">")
                    .append(heading.getAnchorRefText())
                    .append("</a></li>");
                previousLevel = heading.getLevel();
            }
            string.append("</ol>".repeat(Math.max(0, previousLevel)));
            return nav().with(rawHtml(string.toString())).withClass("toc");
        }
    }






    private String hrefId(String name) {
        return name.strip().toLowerCase().replace(" ", "-");
    }


    private String generateHeadersId(String html) {
        Matcher matcher = Pattern.compile("<(h\\d)>([^<]*)").matcher(html);
        while (matcher.find()) {
            String tag = matcher.group(1);
            String name = matcher.group(2);
            String id = hrefId(name);
            html = html.replace("<"+tag+">"+name,"<"+tag+" id=\""+id+"\">"+name);
        }
        return html;
    }



    private void copyResource(String resource, Path outputFolder) throws IOException {
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new FileNotFoundException(resource);
        }
        Files.copy(
            resourceUrl.openStream(),
            outputFolder.resolve(resource),
            StandardCopyOption.REPLACE_EXISTING
        );
    }




    private String read(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader( new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }


    private InputStream open(String source) throws IOException {
        try {
            return new URL(source).openStream();
        } catch (MalformedURLException e) {
            return Files.newInputStream(Path.of(source));
        }
    }



    private void warn(String message, Object...args) {
        logger.warn.accept(String.format(message.replace("{}", "%s"),args));
    }




}
