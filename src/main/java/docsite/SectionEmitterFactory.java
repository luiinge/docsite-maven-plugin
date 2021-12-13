package docsite;

import java.nio.file.Path;
import java.util.*;
import docsite.emitters.*;
import org.jetbrains.annotations.NotNull;


public class SectionEmitterFactory {

    private final EmitterBuildParams buildParams;


    public SectionEmitterFactory(
        Docsite site,
        ImageResolver globalImages,
        ThemeColors themeColors,
        Path outputFolder,
        Logger logger
    ) {
        this.buildParams = new EmitterBuildParams()
            .site(site)
            .themeColors(themeColors)
            .outputFolder(outputFolder)
            .globalImages(globalImages)
            .logger(logger);
    }


    SectionEmitter createEmitter(Section section) {
        return createEmitter(section,null,new ArrayList<>());
    }


    SectionEmitter createEmitter(
        Section section,
        SectionEmitter rootEmitter,
        List<SectionEmitter> ancestorEmitters
    ) {
        SectionEmitter newEmitter = newEmitterInstance(buildParams
            .withRootEmitter(rootEmitter)
            .withSection(section)
            .withAncestorEmitters(ancestorEmitters)
        );
        SectionEmitter newRoot = Objects.requireNonNullElse(rootEmitter, newEmitter);
        List<SectionEmitter> newAncestors = new ArrayList<>(ancestorEmitters);
        newAncestors.add(newEmitter);
        for (Section subsection : section.subsections()) {
            newEmitter.addChildEmitter(createEmitter(subsection, newRoot, newAncestors));
        }
        return newEmitter;
    }




    private SectionEmitter newEmitterInstance(EmitterBuildParams params) {
        switch (params.section().type()) {
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
        } else {
            return new TextGeneratedSectionEmitter(params);
        }
    }


}
