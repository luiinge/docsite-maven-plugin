package docsite;

import java.io.*;
import java.nio.file.Path;

public class DocsiteEmitter {


    private final Logger logger;
    private final Docsite site;
    private final ImageResolver globalImages;



    public DocsiteEmitter(Docsite site, Logger logger) {
        this.logger = logger;
        this.site = site;
        this.globalImages = new ImageResolver(site.outputImageFolder());
    }



    public void generateSite() throws IOException {
        prepareCommonResources();
        SectionEmitterFactory emitterFactory = new SectionEmitterFactory(
            site, globalImages, logger
        );
        emitterFactory.createEmitter(site.home()).emitHTML();
   }



    private void prepareCommonResources() throws IOException {
        if (site.cssFile() == null) {
            ResourceUtil.copyResource("layout.css", site.outputFolder().resolve("css"));
        } else {
            ResourceUtil.copyExternalFile(site.cssFile(), site.outputFolder().resolve("css"));
        }
        ResourceUtil.copyResourceFolder("css",  site.outputFolder());
        ResourceUtil.copyResourceFolder("js",  site.outputFolder());
        ResourceUtil.copyResourceFolder("webfonts",  site.outputFolder());

        site.sections().forEach(this::copyEmbeddedSites);
    }


    private void copyEmbeddedSites(Section section) {
        if (section.type() == Section.SectionType.EMBEDDED_SITE && section.isValid()) {
            ResourceUtil.copyFolder(
                Path.of(section.source()),
                site.outputFolder().resolve(EmitterUtil.href(section.name()))
            );
        }
        section.subsections().forEach(this::copyEmbeddedSites);
    }


}
