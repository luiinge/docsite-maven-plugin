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
    protected ATag createLinkToSection() {
        return internalLinkWithIcon(section.name(), url(), section.icon(), globalImages);
    }


    @Override
    protected SectionTag createSectionContent() {
       return section().with(
            h1(section.name()),
            subsectionList(ul())
        );
    }


    @Override
    protected AsideTag createTableOfContents() {
        return aside().with(nav().with(subsectionList(ol())).withClass("toc"));
    }


    private <T extends ContainerTag<T>> ContainerTag<T> subsectionList(ContainerTag<T> container) {
        for (Section subsection : section.subsections()) {
            container.with(li(
                a(subsection.name()).withClass("internal").withHref(EmitterUtil.href(subsection))
            ));
        }
        return container;
    }

}