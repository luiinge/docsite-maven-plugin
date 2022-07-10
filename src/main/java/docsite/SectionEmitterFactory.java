package docsite;

import java.nio.file.Path;
import java.util.*;
import docsite.emitters.*;
import docsite.util.ImageResolver;
import org.jetbrains.annotations.NotNull;


public class SectionEmitterFactory {

    private final EmitterBuildParams buildParams;


    public SectionEmitterFactory(
        Docsite site,
        ImageResolver globalImages,
        ThemeColors themeColors,
        Path baseDir,
        Path outputFolder,
        boolean useCDN,
        Map<String, String> metadata,
        List<Script> scripts,
        List<SiteLanguage> availableLanguages,
        Map<String, Map<String, String>> localization
    ) {
        this.buildParams = new EmitterBuildParams()
            .site(site)
            .themeColors(themeColors)
            .baseDir(baseDir)
            .outputFolder(outputFolder)
            .globalImages(globalImages)
            .useCDN(useCDN)
            .metadata(metadata)
            .scripts(scripts)
            .availableLanguages(availableLanguages)
            .localization(localization)
        ;
    }


    SectionEmitter createEmitter(Section section, SiteLanguage siteLanguage) {
        return createEmitter(section,null,new ArrayList<>(), siteLanguage);
    }


    SectionEmitter createEmitter(
        Section section,
        SectionEmitter rootEmitter,
        List<SectionEmitter> ancestorEmitters,
        SiteLanguage siteLanguage
    ) {
        SectionEmitter newEmitter = newEmitterInstance(buildParams
            .withRootEmitter(rootEmitter)
            .withSection(section)
            .withAncestorEmitters(ancestorEmitters)
            .withSiteLanguage(siteLanguage)
        );
        SectionEmitter newRoot = Objects.requireNonNullElse(rootEmitter, newEmitter);
        List<SectionEmitter> newAncestors = new ArrayList<>(ancestorEmitters);
        newAncestors.add(newEmitter);
        for (Section subsection : section.subsections()) {
            newEmitter.addChildEmitter(createEmitter(subsection, newRoot, newAncestors, siteLanguage));
        }
        return newEmitter;
    }




    private SectionEmitter newEmitterInstance(EmitterBuildParams params) {
        switch (params.section().type()) {
            case copy:
                return new CopySectionEmitter(params);
            case link:
                return new LinkSectionEmitter(params);
            case group:
                return new GroupSectionEmitter(params);
            case embedded:
                return new EmbeddedSiteSectionEmitter(params);
            case generated:
                return newGeneratedSectionEmitter(params);
            default:
                throw new UnsupportedOperationException();
        }
    }


    @NotNull
    private GeneratedSectionEmitter newGeneratedSectionEmitter(EmitterBuildParams params) {

        if (params.section().template() != null) {
            return new TemplateSectionEmitter(params);
        }

        String source = params.section().source().toLowerCase();
        if (source.endsWith(".md")) {
            return new MarkdownGeneratedSectionEmitter(params);
        } else if (source.endsWith(".html") || source.endsWith(".htm")) {
            return new HtmlGeneratedSectionEmitter(params);
        } else if (source.endsWith(".adoc") || source.endsWith(".asciidoc") || source.endsWith(".asc")) {
            return new AsciidocGeneratedSectionEmitter(params);
        }else {
            return new TextGeneratedSectionEmitter(params);
        }
    }


}