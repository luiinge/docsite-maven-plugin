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
    private final Path outputFolder;
    private final Path cssFile;
    private final boolean useCDN;
    private final Map<String,String> metadata;
    private final List<Script> scripts;


    public DocsiteEmitter(
        Docsite docsite,
        ThemeColors themeColors,
        Path cssFile,
        boolean useCDN,
        Path outputFolder,
        Map<String,String> metadata,
        List<Script> scripts
    ) {
        this.site = docsite;
        this.themeColors = themeColors;
        this.cssFile = cssFile;
        this.outputFolder = outputFolder;
        this.globalImages = new ImageResolver(outputFolder.resolve("images"));
        this.useCDN = useCDN;
        this.metadata = metadata;
        this.scripts = scripts;
    }


    public void generateSite() throws IOException {
        prepareCommonResources();
        SectionEmitterFactory emitterFactory = new SectionEmitterFactory(
            site,
            globalImages,
            themeColors,
            outputFolder,
            useCDN,
            metadata,
            scripts
        );
        emitterFactory.createEmitter(site.home()).emitHTML(true);
   }



    private void prepareCommonResources() throws IOException {
        logger.debug("Copying common resources...");
        Path cssFolder = outputFolder.resolve("css");
        Path jsFolder = outputFolder.resolve("js");
        if (cssFile == null) {
            ResourceUtil.copyResource("css/style.css", cssFolder);
        } else {
            ResourceUtil.copyExternalFileWithAnotherName(cssFile, cssFolder, "style.css");
        }
        ResourceUtil.copyResource("css/common.css", cssFolder);
        ResourceUtil.copyResource("css/prism.min.css", cssFolder);
        if (!useCDN) {
            ResourceUtil.copyResource("js/prism.js", jsFolder);
            ResourceUtil.copyResourceFolder("webfonts",  outputFolder);
        }
        site.sections().forEach(this::copyEmbeddedSites);
    }


    private void copyEmbeddedSites(Section section) {
        logger.debug("Copying embedded site {}", section.source());
        if (section.type() == Section.SectionType.embedded && section.isValid()) {
            ResourceUtil.copyFolder(
                Path.of(section.source()),
                outputFolder.resolve(EmitterUtil.href(section.name()))
            );
        }
        section.subsections().forEach(this::copyEmbeddedSites);
    }


}
