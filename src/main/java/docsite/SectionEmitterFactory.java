package docsite;

import java.util.*;
import docsite.emitters.*;


public class SectionEmitterFactory {

    private final EmitterBuildParams buildParams;


    public SectionEmitterFactory(
        Docsite site,
        ImageResolver globalImages,
        Logger logger
    ) {
        this.buildParams = new EmitterBuildParams()
            .site(site)
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
            case LINK:
                return new LinkSectionEmitter(params);
            case GROUP:
                return new GroupSectionEmitter(params);
            case EMBEDDED_SITE:
                return new EmbeddedSiteSectionEmitter(params);
            case GENERATED:
                if (params.section().source().toLowerCase().endsWith(".md")) {
                    return new MarkdownGeneratedSectionEmitter(params);
                } else if (
                    params.section().source().toLowerCase().endsWith(".html") ||
                    params.section().source().toLowerCase().endsWith(".htm")
                ) {
                    return new HtmlGeneratedSectionEmitter(params);
                } else {
                    return new TextGeneratedSectionEmitter(params);
                }
            default:
                throw new UnsupportedOperationException();
        }
    }


}
