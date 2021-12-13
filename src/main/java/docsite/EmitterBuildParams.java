package docsite;

import java.nio.file.Path;
import java.util.List;


public class EmitterBuildParams {

    private Docsite site;
    private SectionEmitter rootEmitter;
    private Section section;
    private List<SectionEmitter> ancestorEmitters;
    private ImageResolver globalImages;
    private ThemeColors themeColors;
    private Path outputFolder;
    private Logger logger;


    public EmitterBuildParams(
        Docsite site,
        SectionEmitter rootEmitter,
        Section section,
        List<SectionEmitter> ancestorEmitters,
        ImageResolver globalImages,
        ThemeColors themeColors,
        Path outputFolder,
        Logger logger
    ) {
        this.site = site;
        this.rootEmitter = rootEmitter;
        this.section = section;
        this.ancestorEmitters = ancestorEmitters;
        this.globalImages = globalImages;
        this.themeColors = themeColors;
        this.outputFolder = outputFolder;
        this.logger = logger;
    }


    public EmitterBuildParams() {
    }


    public Docsite site() {
        return this.site;
    }


    public SectionEmitter rootEmitter() {
        return this.rootEmitter;
    }


    public Section section() {
        return this.section;
    }


    public List<SectionEmitter> ancestorEmitters() {
        return this.ancestorEmitters;
    }


    public ThemeColors themeColors() {
        return themeColors;
    }


    public Path outputFolder() {
        return outputFolder;
    }


    public ImageResolver globalImages() {
        return this.globalImages;
    }


    public Logger logger() {
        return this.logger;
    }



    public EmitterBuildParams withRootEmitter(SectionEmitter rootEmitter) {
        return this.rootEmitter == rootEmitter ?
            this :
            new EmitterBuildParams(
                this.site,
                rootEmitter,
                this.section,
                this.ancestorEmitters,
                this.globalImages,
                this.themeColors,
                this.outputFolder,
                this.logger
            );
    }


    public EmitterBuildParams withSection(Section section) {
        return this.section == section ?
            this :
            new EmitterBuildParams(
                this.site,
                this.rootEmitter,
                section,
                this.ancestorEmitters,
                this.globalImages,
                this.themeColors,
                this.outputFolder,
                this.logger
            );
    }


    public EmitterBuildParams withAncestorEmitters(List<SectionEmitter> ancestorEmitters) {
        return this.ancestorEmitters == ancestorEmitters ?
            this :
            new EmitterBuildParams(
                this.site,
                this.rootEmitter,
                this.section,
                ancestorEmitters,
                this.globalImages,
                this.themeColors,
                this.outputFolder,
                this.logger
            );
    }



    public EmitterBuildParams site(Docsite site) {
        this.site = site;
        return this;
    }


    public EmitterBuildParams rootEmitter(SectionEmitter rootEmitter) {
        this.rootEmitter = rootEmitter;
        return this;
    }


    public EmitterBuildParams section(Section section) {
        this.section = section;
        return this;
    }


    public EmitterBuildParams themeColors(ThemeColors themeColors) {
        this.themeColors = themeColors;
        return this;
    }


    public EmitterBuildParams outputFolder(Path outputFolder) {
        this.outputFolder = outputFolder;
        return this;
    }


    public EmitterBuildParams globalImages(ImageResolver globalImages) {
        this.globalImages = globalImages;
        return this;
    }


    public EmitterBuildParams logger(Logger logger) {
        this.logger = logger;
        return this;
    }

}
