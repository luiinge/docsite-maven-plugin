package docsite;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import com.vdurmont.emoji.EmojiParser;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import j2html.tags.specialized.*;
import static docsite.EmitterUtil.*;
import static j2html.TagCreator.*;

public abstract class SectionEmitter {

    protected static final int TOC_MIN_LEVEL = 2;
    protected static final int TOC_MAX_LEVEL = 3;
    protected static final String INDEX_FILE = "index.html";


    protected static final MutableDataSet options = new MutableDataSet();
    protected static final Parser parser = Parser.builder(options).build();
    protected static final HtmlRenderer renderer = HtmlRenderer.builder(options).build();









    protected final Docsite site;
    protected final Section section;
    protected final String origin;
    protected final ImageResolver sectionImages;
    protected final ImageResolver globalImages;

    protected final SectionEmitter rootEmitter;
    protected final List<SectionEmitter> ancestorEmitters;
    protected final List<SectionEmitter> childEmitters;

    protected final Logger logger;


    protected SectionEmitter(EmitterBuildParams params) {
        this.site = params.site();
        this.section = params.section();
        this.rootEmitter = Objects.requireNonNullElse(params.rootEmitter(), this);
        this.ancestorEmitters= List.copyOf(params.ancestorEmitters());
        this.childEmitters = new ArrayList<>();
        this.origin = section.source();
        this.globalImages = params.globalImages();
        this.sectionImages = origin == null ? null : new ImageResolver(
            site.outputImageFolder().resolve(section.name()),
            Path.of(origin)
        );
        this.logger = params.logger();
    }


    protected abstract String url();

    protected abstract ATag createLinkToSection();

    protected abstract SectionTag createSectionContent();

    protected abstract AsideTag createTableOfContents();


    public String href() {
        return EmitterUtil.href(section.name());
    }

    protected Path outputPath() {
        return site.outputFolder().resolve(url());
    }

    public void emitHTML() throws IOException {

        if (!section.isValid()) {
            logger.warn("Section {} is not valid and would not be included", section.name());
            return;
        }

        HeaderTag header = createHeader();
        NavTag breadcrumbs = createBreadcrumbs();
        AsideTag tableOfContents = createTableOfContents();
        SectionTag sectionContent = createSectionContent();

        HtmlTag htmlObject = html().with(
            htmlHead(),
            body()
                .withCondClass(tableOfContents.getNumChildren() == 0, "empty-aside")
                .with(
                    header,
                    breadcrumbs,
                    tableOfContents,
                    sectionContent
                )
        );
        writeToFile(htmlObject);

        for (SectionEmitter child : childEmitters) {
            child.emitHTML();
        }
    }


    private HeaderTag createHeader() throws IOException {
        return header().with(
            createTitleAndSubtitle(),
            createNavigation()
        );
    }


    public NavTag createBreadcrumbs() {

        OlTag container = ol();
        container.with(li().with(internalLink(site.name(),INDEX_FILE)));

        if (!ancestorEmitters.isEmpty()) {
            Iterator<SectionEmitter> iterator = ancestorEmitters.iterator();
            // first section in ancestors is always the index
            SectionEmitter path;
            iterator.next();

            while (iterator.hasNext()) {
                path = iterator.next();
                if (path.section.isValid()) {
                    container.with(li().with(internalLink(path.section.name(),path.url())));
                } else {
                    container.with(li().with(a(path.section.name())));
                }
            }

            container.with(li().with(a(section.name())));
        }

        return nav().withClass("breadcrumbs").with(container);
    }





    private HeadTag htmlHead() throws IOException {
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



    void addChildEmitter(SectionEmitter child) {
        this.childEmitters.add(child);
    }





    private DivTag createTitleAndSubtitle() {
        return div().withClass("title-and-subtitle")
            .with(
                h1().withClasses("title label")
                    .with(
                        EmitterUtil.icon(site.logo(), globalImages),
                        span(site.title())
                    ),
                span(site.description()).withClass("subtitle")
            );
    }



    private NavTag createNavigation() {
        return nav().withClass("sections")
            .with(
                ul().with(
                    rootEmitter.childEmitters.stream()
                        .filter(it -> it.section.isValid())
                        .map(it -> it.createNavigationSection(it == this))
                        .toArray(LiTag[]::new)
                )
            );
    }


    private LiTag createNavigationSection(boolean selected) {

        if (!childEmitters.isEmpty()) {
            UlTag dropdownMenu = ul().withClass("dropdown");
            for (SectionEmitter child : childEmitters) {
                dropdownMenu.with(li().with(child.createLinkToSection()));
            }
            return li()
                .withCondClass(selected, "selected")
                .with(createLinkToSection())
                .with(dropdownMenu);
        } else {
            return li()
                .withCondClass(selected, "selected")
                .with(createLinkToSection());
        }

    }


    private static boolean existsSource (String source) {
        if (source == null || source.isBlank()) {
            return false;
        }
        if (Files.exists(Path.of(source))) {
            return true;
        }
        try (InputStream stream = new URL(source).openStream()) {
            return stream != null;
        } catch (IOException e) {
            return false;
        }
    }





    private void writeToFile(HtmlTag htmlObject) throws IOException {
        String html = htmlObject.render();
        if (Boolean.TRUE.equals(section.replaceEmojis())) {
            html = EmojiParser.parseToUnicode(html);
        }
        Files.writeString(outputPath(), html, StandardCharsets.UTF_8);
        logger.info("Written file {}", outputPath());
    }

}
