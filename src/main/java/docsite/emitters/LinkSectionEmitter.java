package docsite.emitters;


import java.io.IOException;
import docsite.*;
import j2html.tags.specialized.*;
import static docsite.EmitterUtil.externalLinkWithIcon;
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
    protected ATag createLinkToSection() {
        return externalLinkWithIcon(section.name(), url(), section.icon(), globalImages);
    }


    @Override
    protected SectionTag createSectionContent() {
        return section();
    }


    @Override
    protected AsideTag createTableOfContents() {
        return aside();
    }


    @Override
    public void emitHTML() throws IOException {
        //
    }

}