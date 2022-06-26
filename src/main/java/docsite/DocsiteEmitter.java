package docsite;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import docsite.util.*;

public class DocsiteEmitter {


    private static final Logger logger = Logger.instance();

    private final Docsite site;
    private final ImageResolver globalImages;
    private final ThemeColors themeColors;
    private final Path baseDir;
    private final Path outputFolder;
    private final Path cssFile;
    private final boolean useCDN;
    private final Map<String,String> metadata;
    private final List<Script> scripts;
    private final List<SiteLanguage> availableLanguages;
    private final Map<String, Map<String, String>> localization;


    public DocsiteEmitter(
        Docsite docsite,
        ThemeColors themeColors,
        Path cssFile,
        boolean useCDN,
        Path baseDir,
        Path outputFolder,
        Map<String,String> metadata,
        List<Script> scripts,
        List<SiteLanguage> availableLanguages,
        Map<String,Map<String,String>> localization
    ) {
        this.site = docsite;
        this.themeColors = themeColors;
        this.cssFile = cssFile;
        this.outputFolder = outputFolder;
        this.globalImages = new ImageResolver(outputFolder.resolve("images"));
        this.useCDN = useCDN;
        this.metadata = metadata;
        this.scripts = scripts;
        this.baseDir = baseDir;
        this.availableLanguages = availableLanguages;
        this.localization = localization;
    }


    public void generateSite() throws IOException {
        prepareCommonResources();
        SectionEmitterFactory emitterFactory = new SectionEmitterFactory(
            site,
            globalImages,
            themeColors,
            baseDir,
            outputFolder,
            useCDN,
            metadata,
            scripts,
            availableLanguages,
            localization
        );
        if (availableLanguages.isEmpty()) {
            emitterFactory.createEmitter(site.home(),SiteLanguage.UNDEFINED).emitHTML(true);
        } else {
            for (SiteLanguage language : availableLanguages) {
                emitterFactory.createEmitter(site.home(),language).emitHTML(true);
            }
        }
   }



    private void prepareCommonResources() throws IOException {
        logger.debug("Copying common resources...");
        Path cssFolder = outputFolder.resolve("css");
        Path jsFolder = outputFolder.resolve("js");

        ResourceUtil.copyResource("css/layout.css", cssFolder);
        ResourceUtil.copyResource("css/theme.css", cssFolder);
        ResourceUtil.copyResource("js/menu.js", jsFolder);

        if (cssFile != null) {
            ResourceUtil.copyExternalFileWithAnotherName(cssFile, cssFolder, "extra-style.css");
        }

        ResourceUtil.copyResource("css/common.css", cssFolder);
        ResourceUtil.copyResource("css/prism.min.css", cssFolder);

        if (!useCDN) {
            ResourceUtil.copyResource("js/prism.js", jsFolder);
            ResourceUtil.copyResourceFolder("webfonts",  outputFolder);
        }
        site.sections().forEach(this::copyEmbeddedSites);
        site.sections().forEach(this::copyLocalSites);

    }


    private void copyEmbeddedSites(Section section) {
        logger.debug("Copying embedded site {}", section.source());
        if (section.type() == Section.SectionType.embedded && section.isValid(baseDir)) {
            ResourceUtil.copyFolder(
                baseDir.resolve(section.source()),
                outputFolder.resolve(EmitterUtil.href(section.name()))
            );
        }
        section.subsections().forEach(this::copyEmbeddedSites);
    }


    private void copyLocalSites(Section section) {
        logger.debug("Copying local site {}", section.source());
        if (section.type() == Section.SectionType.copy && section.isValid(baseDir)) {
            ResourceUtil.copyFolder(
                baseDir.resolve(section.source()),
                outputFolder.resolve(section.source())
            );
        }
        section.subsections().forEach(this::copyLocalSites);
    }


}