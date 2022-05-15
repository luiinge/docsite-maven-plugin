package docsite.emitters;


import docsite.util.*;
import static docsite.util.EmitterUtil.*;
import static j2html.TagCreator.*;

import docsite.*;
import j2html.tags.ContainerTag;
import j2html.tags.specialized.*;

public class GroupSectionEmitter extends SectionEmitter {

    public GroupSectionEmitter(EmitterBuildParams params) {
        super(params);
    }


    @Override
    protected String url() {
        return EmitterUtil.page(section.name());
    }


    @Override
    protected String url(SiteLanguage language) {
        return EmitterUtil.page(section.name(), language);
    }


    @Override
    public ATag createLinkToSection(boolean withIcon) {
        return withIcon ?
            internalLinkWithIcon(baseDir, translate(section.name()), url(siteLanguage), section.icon(), globalImages) :
            internalLink(translate(section.name()), url(siteLanguage));
    }


    @Override
    protected SectionTag createSectionContent() {
       return section().with(
            h1(translate(section.name())),
            subsectionList(ul())
        );
    }


    @Override
    protected AsideTag createTableOfContents(SectionTag sectionTag) {
        return aside().with(nav().with(subsectionList(ol())).withClass("toc"));
    }

    private <T extends ContainerTag<T>> ContainerTag<T> subsectionList(ContainerTag<T> container) {
        for (SectionEmitter subsection : childEmitters) {
            container.with(li(subsection.createLinkToSection(false)));
        }
        return container;
    }

}