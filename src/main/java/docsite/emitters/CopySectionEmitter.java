package docsite.emitters;


import docsite.*;
import j2html.tags.specialized.ATag;
import j2html.tags.specialized.AsideTag;
import j2html.tags.specialized.SectionTag;

import java.io.IOException;

import static docsite.util.EmitterUtil.externalLink;
import static docsite.util.EmitterUtil.externalLinkWithIcon;
import static j2html.TagCreator.aside;
import static j2html.TagCreator.section;

public class CopySectionEmitter extends SectionEmitter {

    public CopySectionEmitter(EmitterBuildParams params) {
        super(params);
    }



    @Override
    protected String url() {
        return section.source()+"/"+section.siteIndex();
    }


    @Override
    protected String url(SiteLanguage language) {
        return url();
    }


    @Override
    public ATag createLinkToSection(boolean withIcon) {
        return withIcon ?
            externalLinkWithIcon(baseDir, translate(section.name()), url(), section.icon(), globalImages) :
            externalLink(translate(section.name()), url());
    }


    @Override
    protected SectionTag createSectionContent() {
        return section();
    }


    @Override
    protected AsideTag createTableOfContents(SectionTag section) {
        return aside();
    }


    @Override
    public void emitHTML() throws IOException {
        //
    }

}