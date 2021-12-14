package docsite;

import java.io.*;
import java.nio.file.Path;

public class DocsiteEmitter {


    private static final Logger logger = Logger.instance();

    private final Docsite site;
    private final ImageResolver globalImages;
    private final ThemeColors themeColors;
    private final Path outputFolder;
    private final Path cssFile;


    public DocsiteEmitter(
        Docsite docsite,
        ThemeColors themeColors,
        Path cssFile,
        Path outputFolder
    ) {
        this.site = docsite;
        this.themeColors = themeColors;
        this.cssFile = cssFile;
        this.outputFolder = outputFolder;
        this.globalImages = new ImageResolver(outputFolder.resolve("images"));
    }


    public void generateSite() throws IOException {
        prepareCommonResources();
        SectionEmitterFactory emitterFactory = new SectionEmitterFactory(
            site,
            globalImages,
            themeColors,
            outputFolder
        );
        emitterFactory.createEmitter(site.home()).emitHTML(true);
   }



    private void prepareCommonResources() throws IOException {
        logger.debug("Copying common resources...");
        Path cssFolder = outputFolder.resolve("css");
        if (cssFile == null) {
            ResourceUtil.copyResource("style.css", cssFolder);
        } else {
            ResourceUtil.copyExternalFileWithAnotherName(cssFile, cssFolder, "style.css");
        }
        ResourceUtil.copyResourceFolder("css", outputFolder);
        ResourceUtil.copyResourceFolder("js",  outputFolder);
        ResourceUtil.copyResourceFolder("webfonts",  outputFolder);

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
