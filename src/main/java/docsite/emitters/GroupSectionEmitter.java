package docsite.emitters;


import static docsite.EmitterUtil.*;
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
    public ATag createLinkToSection(boolean withIcon) {
        return withIcon ?
            internalLinkWithIcon(section.name(), url(), section.icon(), globalImages) :
            internalLink(section.name(), url());
    }


    @Override
    protected SectionTag createSectionContent() {
       return section().with(
            h1(section.name()),
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