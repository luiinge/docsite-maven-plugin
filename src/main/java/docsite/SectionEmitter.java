package docsite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.*;
import com.vdurmont.emoji.EmojiParser;
import docsite.util.*;
import j2html.tags.specialized.*;
import static docsite.util.EmitterUtil.*;
import static j2html.TagCreator.*;

public abstract class SectionEmitter {

    protected static final int TOC_MIN_LEVEL = 2;
    protected static final int TOC_MAX_LEVEL = 3;
    protected static final String INDEX_FILE = "index.html";


    protected static final Logger logger = Logger.instance();

    protected final Docsite site;
    protected final Section section;
    private final String origin;
    protected final ImageResolver sectionImages;
    protected final ImageResolver globalImages;
    protected final boolean useCDN;

    protected final SectionEmitter rootEmitter;
    protected final List<SectionEmitter> ancestorEmitters;
    protected final List<SectionEmitter> childEmitters;

    protected final ThemeColors themeColors;
    protected final Path outputFolder;
    protected final Path baseDir;
    protected final Map<String,String> metadata;
    protected final List<Script> scripts;
    protected final List<SiteLanguage> availableLanguages;
    protected final SiteLanguage siteLanguage;
    protected final Map<String,String> localization;


    protected SectionEmitter(EmitterBuildParams params) {
        this.site = params.site();
        this.section = params.section();
        this.rootEmitter = Objects.requireNonNullElse(params.rootEmitter(), this);
        this.ancestorEmitters= List.copyOf(params.ancestorEmitters());
        this.childEmitters = new ArrayList<>();
        this.origin = section.source();
        this.globalImages = params.globalImages();
        this.themeColors = params.themeColors();
        this.outputFolder = params.outputFolder();
        this.baseDir = params.baseDir();
        this.metadata = params.metadata();
        this.scripts = params.scripts();
        this.sectionImages = origin == null ? null : new ImageResolver(
            outputFolder.resolve("images").resolve(section.name()),
            baseDir.resolve(origin)
        );
        this.useCDN = params.useCDN();
        this.availableLanguages = params.availableLanguages();
        this.siteLanguage = params.siteLanguage();
        if (params.localization() != null) {
            this.localization = params.localization().get(params.siteLanguage().language());
        } else {
            this.localization = null;
        }
    }


    protected abstract String url();

    protected abstract String url(SiteLanguage language);

    public abstract ATag createLinkToSection(boolean withIcon);

    protected abstract SectionTag createSectionContent();

    protected abstract AsideTag createTableOfContents(SectionTag section);


    public String href() {
        return EmitterUtil.href(section.name());
    }

    protected Path outputPath() {
        return outputFolder.resolve(url(siteLanguage));
    }

    protected String origin() {
        return origin;
    }

    public void emitHTML() throws IOException {
        emitHTML(false);
    }


    public void emitHTML(boolean includeFooter) throws IOException {

        if (!section.isValid(baseDir)) {
            logger.warn(
                "Section {} is not valid and would not be included: {}",
                section.name(),
                section.validation(baseDir)
            );
            return;
        }

        HeaderTag header = createHeader();
        NavTag burgerMenu = createBurgerMenu();
        DivTag info = div().withClass("info").with(createBreadcrumbs(), createLanguageSelection());
        SectionTag sectionContent = createSectionContent().withId("content");
        if (includeFooter) {
            sectionContent.with(rawHtml(
                "<div class=\"footer\">"+
                    "Generated with <a href=\"https://luiinge.github.io/docsite-maven-plugin/\" target=\"_blank_\">Docsite</a>. "+
                    "Last published on "+ LocalDate.now()+
                    "</div>"
            ));
        }

        AsideTag tableOfContents = createTableOfContents(sectionContent);
        if (tableOfContents.getNumChildren() == 0) {
            tableOfContents.withClass("empty");
        }
        tableOfContents.attr("onclick","hideTocIfVisible(event,this)");
        DivTag tocButton = createTableOfContentsButton(tableOfContents.getNumChildren() == 0);



        HeadTag head = htmlHead();
        if (useCDN) {
            addPrismComponents(head, sectionContent);
        }

        HtmlTag htmlObject = html().with(
            head,
            body()
                .withCondClass(tableOfContents.getNumChildren() == 0, "no-toc")
                .with(
                    jumpToContentButton(),
                    header,
                    burgerMenu,
                    info,
                    tocButton,
                    tableOfContents,
                    sectionContent
                )
        )
        .withLang(this.siteLanguage.language());
        writeToFile(htmlObject);

        for (SectionEmitter child : childEmitters) {
            child.emitHTML();
        }
    }


    private ATag jumpToContentButton() {
        return a().withId("jump-to-content").withText("jump to content").withHref("#content");
    }


    private HeaderTag createHeader() {
        return header().with(
            createLogoAndTitle(),
            div().withClass("filler"),
            createExpandedMenu(),
            createMenuButton(),
            createCompanyLogo()
        );
    }


    public NavTag createBreadcrumbs() {

        String index = EmitterUtil.withLanguage(siteLanguage, INDEX_FILE);
        OlTag container = ol();
        container.with(li().with(internalLink(translate(site.title()),index)));

        if (!ancestorEmitters.isEmpty()) {
            Iterator<SectionEmitter> iterator = ancestorEmitters.iterator();
            // first section in ancestors is always the index
            SectionEmitter path;
            iterator.next();

            while (iterator.hasNext()) {
                path = iterator.next();
                if (path.section.isValid(baseDir)) {
                    container.with(li().with(path.createLinkToSection(false)));
                } else {
                    container.with(li().with(a(translate(path.section.name()))));
                }
            }

            container.with(li().with(a(translate(section.name()))));
        }

       return nav().withClass("breadcrumbs").with(container);
    }




    private SpanTag createLanguageSelection() {
        var options = availableLanguages.stream()
            .map(it ->
                option(it.display())
                    .withValue(it.language())
                    .withCondSelected(it.equals(siteLanguage))
            )
            .toArray(OptionTag[]::new);
        if (options.length == 0) {
            return span();
        }
        var languageSelection = select().with(options);
        languageSelection.attr("onchange","redirectLanguage(this.value)");

        List<String> scriptLines = new ArrayList<>();
        scriptLines.add("function redirectLanguage(language) {");
        for (var language : availableLanguages) {
            scriptLines.add("if (language==='"+language.language()+"') location.href = '"+url(language)+"';");
        }
        scriptLines.add("}");
        ScriptTag scriptTag = script(String.join("\n", scriptLines));

        return span().withClass("language-selection").with(scriptTag,languageSelection);
    }



    private DivTag createCompanyLogo() {
        if (site.companyLogo() != null && site.companyLink() != null) {
            return div().withClass("company").with(
                EmitterUtil.externalLinkWithIcon(baseDir,"",site.companyLink(),site.companyLogo(),globalImages)
            );
        } else if (site.companyLink() == null) {
            return div().withClass("company").with(
                EmitterUtil.image(baseDir,site.companyLogo(),globalImages)
            );
        } else {
            return div();
        }
    }




    private HeadTag htmlHead() {
        String title = site.title();
        if (!section.name().equalsIgnoreCase(title) && !section.name().equals("index")) {
            title += " - "+section.name();
        }
        String description = section.description();
        if (description == null || description.isEmpty()) {
            description = site.description();
        }

        HeadTag head = head()
            .with(title(title))
            .with(meta().withName("description").withContent(description))
            .with(meta().withCharset("UTF-8"))
            .with(meta().withName("viewport").withContent("width=device-width, initial-scale=1.0"))
            .with(link().withRel("profile").withHref("http://www.w3.org/2005/10/profile"))
            ;
        if (site.favicon() != null) {
            head.with(
                link()
                    .withRel("icon")
                    .withType(globalImages.typeOf(site.favicon()))
                    .withHref(globalImages.imageFile(site.favicon()))
            );
        }
        if (useCDN) {
            CDNResources.css("fontawesome.min").ifPresent(head::with);
            CDNResources.css("prism.min").ifPresent(head::with);
            CDNResources.js("prism.min").ifPresent(head::with);
            CDNResources.js("mermaid").ifPresent(head::with);
            CDNResources.js("katex").ifPresent(head::with);
            CDNResources.css("katex").ifPresent(head::with);
        } else {
            head.with(stylesheet("css/font-awesome-all.css"));
            head.with(script().attr("src","js/prism.js"));
            head.with(stylesheet("css/prism.min.css"));
        }

        head.with(stylesheet("css/common.css"));
        head.with(stylesheet("css/layout.css"));
        head.with(stylesheet("css/theme.css"));
        head.with(stylesheet("css/extra-style.css"));
        head.with(script().attr("src","js/menu.js"));


        String themeStyle =
            ":root {\n"+
                "--menu-regular-background-color: "+themeColors.menuRegularBackgroundColor()+";\n"+
                "--menu-bold-background-color: "+themeColors.menuBoldBackgroundColor()+";\n"+
                "--menu-foreground-color: "+themeColors.menuForegroundColor()+";\n"+
                "--menu-decoration-color: "+themeColors.menuDecorationColor()+";\n"+
                "--gui-element-color: "+themeColors.guiElementColor()+";\n"+
                "}";
        head.with(style(themeStyle));

        this.metadata.forEach(
            (key,value) -> head.with(meta().withName(key).withContent(value))
        );

        for (Script script : this.scripts) {
            if (script.code() != null && !script.code().isBlank()) {
                head.with(script(script.code()));
            } else {
                head.with(script().withSrc(script.src()).withCondAsync(script.async()));
            }
        }


        return head;
    }



    void addChildEmitter(SectionEmitter child) {
        this.childEmitters.add(child);
    }





    private DivTag createLogoAndTitle() {
        boolean hasSubtitle = (site.description() != null && !site.description().isBlank());
        return div().withClass("title-and-subtitle")
            .with(
                EmitterUtil.icon(baseDir, site.logo(), globalImages),
                div().with(
                    h1(translate(site.title())).withClass(hasSubtitle ? "title" : "title no-subtitle")
                ).withClass("title-container"),
                span(translate(site.description())).withClass("subtitle")
            );
    }



    private DivTag createMenuButton() {
        if (rootEmitter.childEmitters.isEmpty()) {
            return div().withClass("hidden");
        }
        return div().withStyle("display: flex; align-items: center;")
            .with(a().withHref("#").withClasses("menu-button").attr("onclick","showOrHideMenu(event,this)"));
    }


    private NavTag createBurgerMenu() {
        return nav().withClasses("menu hidden burger-menu")
            .with(
                ul().with(
                    rootEmitter.childEmitters.stream()
                        .filter(it -> it.section.isValid(baseDir))
                        .map(it -> it.createMenuItem(it == this))
                        .toArray(LiTag[]::new)
                )
            );
    }


    private NavTag createExpandedMenu() {
        if (rootEmitter.childEmitters.isEmpty()) {
            return nav().withClass("hidden");
        }
        return nav().withClasses("expanded-menu menu")
            .with(
                ul().with(
                    rootEmitter.childEmitters.stream()
                        .filter(it -> it.section.isValid(baseDir))
                        .map(it -> it.createExpandedMenuItem(it == this, 0))
                        .toArray(LiTag[]::new)
                )
            );
    }


    private DivTag createTableOfContentsButton(boolean empty) {
        return div().withClass(empty ? "toc-button no-toc" : "toc-button").with(
            a().withHref("#")
        ).attr("onclick","showOrHideToc(event,this)");
    }


    private LiTag createMenuItem(boolean selected) {

        if (!childEmitters.isEmpty()) {
            UlTag dropdownMenu = ul().withClasses("dropdown", selected ? "visible" : "hidden");
            for (SectionEmitter child : childEmitters) {
                if (child.section.subsections() != null && !child.section.subsections().isEmpty()) {
                    dropdownMenu.with(child.createMenuItem(selected));
                } else {
                    dropdownMenu.with(li().with(child.createLinkToSection(true)));
                }
            }
            return li()
                .withClass(selected ? "selected expandable collapsed" : "expandable collapsed")
                .with(createLinkToSection(true).withHref("#"))
                .with(dropdownMenu)
                .attr("onclick","expandOrCollapse(event,this);");
        } else {
            return li()
                .withCondClass(selected, "selected")
                .with(createLinkToSection(true));
        }

    }



    private LiTag createExpandedMenuItem(boolean selected, int level) {
        if (!childEmitters.isEmpty()) {
            UlTag dropdownMenu = ul().withClasses("dropdown", "hidden");
            for (SectionEmitter child : childEmitters) {
                if (child.section.subsections() != null && !child.section.subsections().isEmpty()) {
                    dropdownMenu.with(child.createExpandedMenuItem(selected, level+1));
                } else {
                    dropdownMenu.with(li().with(child.createLinkToSection(true)));
                }
            }
            return li()
                .withClass(selected ? "selected expandable collapsed" : "expandable collapsed")
                .with(createLinkToSection(true).withHref("#"))
                .with(dropdownMenu)
                .attr("onclick",
                    level == 0 ?
                        "expandOrCollapseExpandedMenu(event,this);" :
                        "expandOrCollapse(event,this);"
                );
        } else {
            return li()
                .withCondClass(selected, "selected")
                .with(createLinkToSection(true));
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


    private void addPrismComponents(HeadTag head, SectionTag section) {
        Pattern pattern = Pattern.compile("<code\\s*class=\"\\s*language-([^\\s\"]+)");
        String html = section.render();
        Matcher matcher = pattern.matcher(html);
        Set<String> languages = new HashSet<>();
        while (matcher.find()) {
            languages.add("prism."+matcher.group(1));
        }
        languages.forEach(lang -> CDNResources.js(lang).ifPresent(head::with));
    }



    protected String translate(String value) {
        if (this.localization == null || !this.localization.containsKey(value)) {
            return value;
        } else {
            return this.localization.get(value);
        }
    }


}