package docsite;

import java.nio.file.Path;
import java.util.*;
import docsite.util.ImageResolver;


public class EmitterBuildParams {

    private Docsite site;
    private SectionEmitter rootEmitter;
    private Section section;
    private List<SectionEmitter> ancestorEmitters;
    private ImageResolver globalImages;
    private ThemeColors themeColors;
    private Path outputFolder;
    private Path baseDir;
    private boolean useCDN;
    private Map<String,String> metadata;
    private List<Script> scripts;
    private List<SiteLanguage> availableLanguages;
    private SiteLanguage siteLanguage;
    private Map<String,Map<String,String>> localization;

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


    public boolean useCDN() {
        return this.useCDN;
    }


    public Map<String, String> metadata() {
        return Objects.requireNonNullElseGet(metadata, HashMap::new);
    }


    public List<Script> scripts() {
        return Objects.requireNonNullElseGet(scripts, ArrayList::new);
    }


    public SiteLanguage siteLanguage() {
        return siteLanguage;
    }


    public List<SiteLanguage> availableLanguages() {
        return Objects.requireNonNullElseGet(availableLanguages, List::of);
    }


    public Map<String, Map<String, String>> localization() {
        return localization;
    }


    public Path baseDir() {
        return baseDir;
    }

    public EmitterBuildParams withRootEmitter(SectionEmitter rootEmitter) {
        return this.rootEmitter == rootEmitter ?
            this :
            new EmitterBuildParams()
                .site(this.site)
                .rootEmitter(rootEmitter)
                .section(this.section)
                .ancestorEmitters(this.ancestorEmitters)
                .globalImages(this.globalImages)
                .themeColors(this.themeColors)
                .outputFolder(this.outputFolder)
                .baseDir(this.baseDir)
                .useCDN(this.useCDN)
                .metadata(this.metadata)
                .scripts(this.scripts)
                .availableLanguages(this.availableLanguages)
                .siteLanguage(this.siteLanguage)
                .localization(this.localization)
            ;
    }


    public EmitterBuildParams withSiteLanguage(SiteLanguage siteLanguage) {
        return new EmitterBuildParams()
            .site(this.site)
            .rootEmitter(this.rootEmitter)
            .section(this.section)
            .ancestorEmitters(this.ancestorEmitters)
            .globalImages(this.globalImages)
            .themeColors(this.themeColors)
            .outputFolder(this.outputFolder)
            .baseDir(this.baseDir)
            .useCDN(this.useCDN)
            .metadata(this.metadata)
            .scripts(this.scripts)
            .availableLanguages(this.availableLanguages)
            .siteLanguage(siteLanguage)
            .localization(this.localization);
    }


    public EmitterBuildParams withSection(Section section) {
        return this.section == section ?
            this :
            new EmitterBuildParams()
                .site(this.site)
                .rootEmitter(this.rootEmitter)
                .section(section)
                .ancestorEmitters(this.ancestorEmitters)
                .globalImages(this.globalImages)
                .themeColors(this.themeColors)
                .outputFolder(this.outputFolder)
                .baseDir(this.baseDir)
                .useCDN(this.useCDN)
                .metadata(this.metadata)
                .scripts(this.scripts)
                .availableLanguages(this.availableLanguages)
                .siteLanguage(this.siteLanguage)
                .localization(this.localization)
            ;
    }


    public EmitterBuildParams withAncestorEmitters(List<SectionEmitter> ancestorEmitters) {
        return this.ancestorEmitters == ancestorEmitters ?
            this :
            new EmitterBuildParams()
                .site(this.site)
                .rootEmitter(this.rootEmitter)
                .section(this.section)
                .ancestorEmitters(ancestorEmitters)
                .globalImages(this.globalImages)
                .themeColors(this.themeColors)
                .outputFolder(this.outputFolder)
                .baseDir(this.baseDir)
                .useCDN(this.useCDN)
                .metadata(this.metadata)
                .scripts(this.scripts)
                .availableLanguages(this.availableLanguages)
                .siteLanguage(this.siteLanguage)
                .localization(this.localization)
            ;
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


    public EmitterBuildParams useCDN(boolean useCDN) {
        this.useCDN = useCDN;
        return this;
    }


    public EmitterBuildParams ancestorEmitters(List<SectionEmitter> ancestorEmitters) {
        this.ancestorEmitters = ancestorEmitters;
        return this;
    }


    public EmitterBuildParams scripts(List<Script> scripts) {
        this.scripts = scripts;
        return this;
    }


    public EmitterBuildParams availableLanguages(List<SiteLanguage> availableLanguages) {
        this.availableLanguages = availableLanguages;
        return this;
    }


    public EmitterBuildParams siteLanguage(SiteLanguage siteLanguage) {
        this.siteLanguage = siteLanguage;
        return this;
    }


    public EmitterBuildParams localization(Map<String, Map<String, String>> localization) {
        this.localization = localization;
        return this;
    }


    public EmitterBuildParams metadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }


    public EmitterBuildParams baseDir(Path baseDir) {
        this.baseDir = baseDir;
        return this;
    }
}