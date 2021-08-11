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


public class SectionHtmlEmitter {

    private static final String INDEX_FILE = "index.html";
    static final String HTML_EXTENSION = ".html";


    public static SectionHtmlEmitter build(SiteConfiguration configuration) throws IOException {
        return build(configuration, null, configuration.home(), Collections.emptyList());
    }



    private static SectionHtmlEmitter build(
        SiteConfiguration configuration,
        SectionHtmlEmitter root,
        Section section,
        List<SectionHtmlEmitter> ancestors
    ) throws IOException {
        SectionHtmlEmitter emitter = new SectionHtmlEmitter(configuration,root,section,ancestors);
        if (root == null) {
            root = emitter;
        }
        ancestors = new ArrayList<>(ancestors);
        ancestors.add(emitter);
        for (Section subsection : section.subsections()) {
            emitter.children.add(build(configuration, root, subsection, ancestors));
        }
        return emitter;
    }



    private final SiteConfiguration configuration;
    private final Section section;
    private final String source;
    private final SectionHtmlEmitter root;
    private final List<SectionHtmlEmitter> ancestors;
    private final List<SectionHtmlEmitter> children = new ArrayList<>();


    private SectionHtmlEmitter(
        SiteConfiguration configuration,
        SectionHtmlEmitter root,
        Section section,
        List<SectionHtmlEmitter> ancestors
    ) throws IOException {
        this.configuration = configuration;
        this.section = section;
        this.root = (root == null ? this : root);
        this.ancestors = ancestors;
        this.source = generateSourceIfNecessary(section,configuration.outputFolder());
    }




    public ContainerTag<?> createHeaderFromconfiguration() {

        ContainerTag<?> [] sectionLinks = root.children.stream()
            .filter(SectionHtmlEmitter::isValid)
            .map(mainSection -> mainSection.createSectionLink(mainSection == this))
            .toArray(ContainerTag<?>[]::new);

        return header().withClass("header").with(
            div(
                 inlink(configuration.title(),INDEX_FILE).withClass("title"),
                 span(configuration.description()).withClass("subtitle")
            ),
            nav().withClass("links").with(ol().with(sectionLinks))
        );
    }


    private ContainerTag<?> createSectionLink(boolean selected) {

        ContainerTag<?> dropdownMenu = ol().withClass("dropdown");
        for (SectionHtmlEmitter child : children) {
            dropdownMenu.with(li().with(child.sectionLink()));
        }
        return li()
            .withCondClass(!selected,"headerLink")
            .withCondClass(selected, "headerLink selected")
            .with(sectionLink())
            .with(dropdownMenu);
    }



    private ContainerTag<?> sectionLink() {
        return hasType(SectionType.GENERATED,SectionType.GROUP) ?
            inlink(name(), page()) :
            exlink(name(), page());
    }



    public ContainerTag<?> createBreadcrumbs() {
        if (this.ancestors.isEmpty()) {
            return div();
        }
        ContainerTag<?> container = ol();
        Iterator<SectionHtmlEmitter> iterator = this.ancestors.iterator();
        List<ContainerTag<?>> items = new ArrayList<>();
        // first section in ancestors is always the index
        SectionHtmlEmitter path;
        iterator.next();
        items.add(li().with(inlink(configuration.name(),INDEX_FILE)));

        while (iterator.hasNext()) {
            path = iterator.next();
            if (path.isValid()) {
                items.add(li().with(inlink(path.name(),path.page())));
            } else {
                items.add(li().with(a(path.name())));
            }
        }
        items.add(li().with(a(this.name())));
        container = container.with(items);
        return nav().withClass("breadcrumb").with(container);
    }


    public String source() {
        return this.source;
    }


    public Iterable<SectionHtmlEmitter> children() {
        return Collections.unmodifiableList(children);
    }


    public String page() {
        return hasType(SectionType.EMBEDDED_SITE) ? source : EmitterUtil.page(section.name());
    }


    public String href() {
        return EmitterUtil.href(section.name());
    }


    public String name() {
        return section.name();
    }


    public Path outputFile() {
        return configuration.outputFolder().resolve(page());
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
                writer.write("- ["+subsection.name()+"]("+EmitterUtil.href(subsection.name())+")");
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