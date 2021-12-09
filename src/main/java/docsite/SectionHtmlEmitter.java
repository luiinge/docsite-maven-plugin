package docsite;

import static docsite.EmitterUtil.*;
import static j2html.TagCreator.*;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import docsite.Section.SectionType;
import j2html.tags.ContainerTag;
import j2html.tags.specialized.*;


public class SectionHtmlEmitter {

    private static final String INDEX_FILE = "index.html";
    static final String HTML_EXTENSION = ".html";


    public static SectionHtmlEmitter build(SiteConfiguration configuration, ImageRegistry images) throws IOException {
        return build(configuration, null, configuration.home(), Collections.emptyList(), images);
    }



    private static SectionHtmlEmitter build(
        SiteConfiguration configuration,
        SectionHtmlEmitter root,
        Section section,
        List<SectionHtmlEmitter> ancestors,
        ImageRegistry images
    ) throws IOException {
        SectionHtmlEmitter emitter = new SectionHtmlEmitter(configuration,root,section,ancestors,images);
        if (root == null) {
            root = emitter;
        }
        ancestors = new ArrayList<>(ancestors);
        ancestors.add(emitter);
        for (Section subsection : section.subsections()) {
            emitter.children.add(build(configuration, root, subsection, ancestors,images));
        }
        return emitter;
    }



    private final SiteConfiguration site;
    private final Section section;
    private final String source;
    private final SectionHtmlEmitter root;
    private final List<SectionHtmlEmitter> ancestors;
    private final List<SectionHtmlEmitter> children = new ArrayList<>();
    private final ImageRegistry images;


    private SectionHtmlEmitter(
        SiteConfiguration site,
        SectionHtmlEmitter root,
        Section section,
        List<SectionHtmlEmitter> ancestors,
        ImageRegistry images
    ) throws IOException {
        this.site = site;
        this.section = section;
        this.root = (root == null ? this : root);
        this.ancestors = ancestors;
        this.source = generateSourceIfNecessary(section,site.outputFolder());
        this.images = images;
    }



    public HeaderTag createHeader() throws IOException {
        return header().with(
            createTitleAndSubtitle(),
            createNavigation()
        );
    }



    private DivTag createTitleAndSubtitle() throws IOException {
        return div().withClass("title-and-subtitle")
            .with(
                h1().withClasses("title label")
                    .with(
                        EmitterUtil.icon(site.logo(), images),
                        span(site.title())
                    ),
                span(site.description()).withClass("subtitle")
            );
    }



    private NavTag createNavigation() {
        return nav().withClass("sections")
            .with(
                ul().with(
                    root.children.stream()
                        .filter(SectionHtmlEmitter::isValid)
                        .map(it -> it.createNavigationSection(it == this))
                        .toArray(LiTag[]::new)
                )
            );
    }




    private LiTag createNavigationSection(boolean selected) {

        if (!children.isEmpty()) {
            UlTag dropdownMenu = ul().withClass("dropdown");
            for (SectionHtmlEmitter child : children) {
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



    private ATag createLinkToSection() {
        return hasType(SectionType.GENERATED,SectionType.GROUP) ?
            internalLinkWithIcon(name(), page(), icon(), images) :
            externalLinkWithIcon(name(), page(), icon(), images);
    }



    public NavTag createBreadcrumbs() {


        OlTag container = ol();
        container.with(li().with(internalLink(site.name(),INDEX_FILE)));


        if (!this.ancestors.isEmpty()) {
            Iterator<SectionHtmlEmitter> iterator = this.ancestors.iterator();
            // first section in ancestors is always the index
            SectionHtmlEmitter path;
            iterator.next();

            while (iterator.hasNext()) {
                path = iterator.next();
                if (path.isValid()) {
                    container.with(li().with(internalLink(path.name(),path.page())));
                } else {
                    container.with(li().with(a(path.name())));
                }
            }
            container.with(li().with(a(this.name())));
        }

        return nav().withClass("breadcrumbs").with(container);
    }


    public String source() {
        return this.source;
    }


    public Iterable<SectionHtmlEmitter> children() {
        return Collections.unmodifiableList(children);
    }


    public String page() {
        switch (section.type()) {
            case EMBEDDED_SITE:
                return source;
            case LINK:
                return section.source();
            default:
                return EmitterUtil.page(section.name());
        }
    }


    public String href() {
        return EmitterUtil.href(section.name());
    }


    public String name() {
        return section.name();
    }


    public String icon() {
        return section.icon();
    }

    public Path outputFile() {
        return site.outputFolder().resolve(page());
    }


    public boolean isValid() {
        if (hasType(SectionType.GENERATED,SectionType.EMBEDDED_SITE)) {
            return hasSource();
        }
        if (hasType(SectionType.GROUP)) {
            return !this.section.subsections().isEmpty();
        }
        if (hasType(SectionType.LINK)) {
            return hasSource();
        }
        return false;
    }


    public boolean hasSource() {
        return existsSource(this.source);
    }


    public boolean hasMarkdownSource() {
        return source.endsWith(".md") || source.endsWith(".MD") || source.endsWith(".markdown");
    }


    public boolean hasHTMLSource() {
        return source.endsWith(HTML_EXTENSION) || source.endsWith(".htm");
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


    private static String generateSourceIfNecessary(Section section, Path outputFolder)
    throws IOException {
        if (section.source() == null && section.type() == SectionType.GROUP) {
            return generateMarkdownFileForGroup(section).toString();
        }
        if (existsSource(section.source()) && section.type() == SectionType.EMBEDDED_SITE) {

            Path outputSiteFolder = outputFolder.resolve(EmitterUtil.href(section.name()));
            copySite(Path.of(section.source()), outputSiteFolder);
            String index = (section.siteIndex() == null ? INDEX_FILE : section.siteIndex());
            return Path.of(EmitterUtil.href(section.name())).resolve(index).toString();
        }
        return section.source();
    }



    private static Path generateMarkdownFileForGroup(Section section) throws IOException {
        String title = section.name();
        Path file = Files.createTempFile("docsite", ".md");
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write("# "+title);
            writer.newLine();
            for (Section subsection : section.subsections()) {
                writer.write("## ["+subsection.name()+"]("+EmitterUtil.href(subsection)+")");
                writer.newLine();
            }
        }
        return file;
    }



    private static void copySite(Path siteFolder, Path outputFolder) throws IOException {
        Files.createDirectory(outputFolder);
        try(Stream<Path> walker = Files.walk(siteFolder)) {
            walker.forEach(sourcePath -> {
                try {
                    Path targetPath = outputFolder.resolve(siteFolder.relativize(sourcePath));
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


    public boolean hasType(SectionType...types) {
        for (SectionType type : types) {
            if (section.type() == type) {
                return true;
            }
        }
        return false;
    }







}