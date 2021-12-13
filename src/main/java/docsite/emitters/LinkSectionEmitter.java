package docsite.emitters;


import java.io.IOException;
import docsite.*;
import j2html.tags.specialized.*;
import static docsite.EmitterUtil.*;
import static j2html.TagCreator.*;

public class LinkSectionEmitter extends SectionEmitter {

    public LinkSectionEmitter(EmitterBuildParams params) {
        super(params);
    }



    @Override
    protected String url() {
        return section.source();
    }


    @Override
    public ATag createLinkToSection(boolean withIcon) {
        return withIcon ?
            externalLinkWithIcon(section.name(), url(), section.icon(), globalImages) :
            externalLink(section.name(), url());
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