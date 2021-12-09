package docsite;

import static docsite.EmitterUtil.normalizeLinks;
import static j2html.TagCreator.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

import com.vdurmont.emoji.EmojiParser;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import docsite.Section.SectionType;
import j2html.tags.*;
import j2html.tags.specialized.*;

public class SiteHtmlEmitter {



    private static final int tocMinLevel = 2;
    private static final int tocMaxLevel = 3;

    private final MutableDataSet options = new MutableDataSet();
    private final Parser parser = Parser.builder(options).build();
    private final HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    private final Logger logger;
    private final SiteConfiguration site;
    private final ImageRegistry images;



    public SiteHtmlEmitter(SiteConfiguration site, Logger logger) {
        this.logger = logger;
        this.site = site;
        this.images = new ImageRegistry(site.outputFolder().resolve("images"));
    }




    public void generateSite() throws IOException {
        if (site.cssFile() == null) {
            ResourceUtil.copyResource("layout.css", site.outputFolder().resolve("css"));
        } else {
            ResourceUtil.copyExternalFile(site.cssFile(), site.outputFolder().resolve("css"));
        }
        ResourceUtil.copyResourceFolder("css",  site.outputFolder());
        ResourceUtil.copyResourceFolder("js",  site.outputFolder());
        ResourceUtil.copyResourceFolder("webfonts",  site.outputFolder());
        writePage(SectionHtmlEmitter.build(site,images));
    }





    private void writePage(SectionHtmlEmitter section) throws IOException {
        if (!section.isValid()) {
            warn("Section {} is not valid and would not be included", section.name());
            return;
        }
        if (section.hasType(SectionType.GENERATED, SectionType.GROUP)) {
            HeaderTag header = section.createHeader();
            NavTag breadcrumbs = section.createBreadcrumbs();
            SectionTag content = createContentFromSource(section);
            AsideTag toc = section.hasMarkdownSource() || section.hasHTMLSource() ?
                createTOCFromMarkdown(section.source()) :
                aside().withClass("hidden");
            ContainerTag<?> page = buildPage(header, breadcrumbs, toc, content);

            write(section.outputFile(), page, section.hasMarkdownSource());
        }
        for (SectionHtmlEmitter subsection : section.children()) {
            writePage(subsection);
        }
    }







    private SectionTag createContentFromSource(SectionHtmlEmitter section) throws IOException {
        SectionTag content;
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



    private HeadTag buildHtmlHead() throws IOException {
        HeadTag head = head()
            .with(title(site.title()))
            .with(meta().withCharset("UTF-8"))
            .with(meta().withName("viewport").withContent("width=device-width, initial-scale=1.0"))
            ;
        for (String css : ResourceUtil.getResourceFiles("css")) {
            head.with(link().attr("href","css/"+css).attr("rel","stylesheet"));
        }
        Path cssFile = Objects.requireNonNullElse(site.cssFile(), Path.of("layout.css"));
        head.with(link().attr("href","css/"+cssFile.getFileName().toString()).attr("rel","stylesheet"));

        for (String js : ResourceUtil.getResourceFiles("js")) {
            head.with(script().attr("src","js/"+js));
        }
        
        String themeStyle = 
            ":root {\n"+
            "--menu-regular-background-color: "+site.themeColors().menuRegularBackgroundColor()+";\n"+
            "--menu-bold-background-color: "+site.themeColors().menuBoldBackgroundColor()+";\n"+
            "--menu-foreground-color: "+site.themeColors().menuForegroundColor()+";\n"+
            "--menu-decoration-color: "+site.themeColors().menuDecorationColor()+";\n"+
            "--gui-element-color: "+site.themeColors().guiElementColor()+";\n"+
            "}";
        head.with(style(themeStyle));

        return head;
    }



    private BodyTag buildHtmlBody(HeaderTag header, NavTag breadcrumbs, AsideTag aside, SectionTag section) {
        return body().with(
            header,
            breadcrumbs,
            aside,
            section
        );
    }



    private ContainerTag<?> buildPage(
        HeaderTag header,
        NavTag breadcrumbs,
        AsideTag aside,
        SectionTag section
    ) throws IOException {
        return html().with(
            buildHtmlHead(),
            buildHtmlBody(header, breadcrumbs, aside, section)
        );
    }



    private SectionTag createContentFromMarkdown(String markdownSource) throws IOException {
        try (InputStream markdown = ResourceUtil.open(markdownSource)) {
            String markdownContent = ResourceUtil.read(markdown);
            Node document = parser.parse(markdownContent);
            StreamSupport.stream(document.getChildren().spliterator(), false)
                .filter(Heading.class::isInstance)
                .map(Heading.class::cast)
                .forEach(heading -> heading.setAnchorRefId(hrefId(heading.getAnchorRefText())));
            String html = renderer.render(document);
            return section().with(rawHtml(normalizeLinks(generateHeadersId(html)))).withClass("content");
        }
    }


    private SectionTag createContentFromHTML(String htmlSource) throws IOException {
        try (InputStream html = ResourceUtil.open(htmlSource)) {
            return section().with(rawHtml(normalizeLinks(ResourceUtil.read(html)))).withClass("content");
        }
    }


    private SectionTag createContentFromText(String textSource) throws IOException {
        try (InputStream text = ResourceUtil.open(textSource)) {
            return section().with(pre(ResourceUtil.read(text)));
        }
    }




    private AsideTag createTOCFromMarkdown(String markdownSource)
    throws IOException {

        try (InputStream markdown = ResourceUtil.open(markdownSource)) {
            Node document = parser.parse(ResourceUtil.read(markdown));

            List<Heading> children = StreamSupport.stream(document.getChildren().spliterator(), false)
                .filter(Heading.class::isInstance)
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
            return aside().with(nav().with(rawHtml(string.toString())).withClass("toc"));
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







    private void warn(String message, Object...args) {
        logger.warn.accept(String.format(message.replace("{}", "%s"),args));
    }




}
