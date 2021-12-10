package docsite.emitters;


import java.io.IOException;
import java.nio.file.Path;
import static docsite.EmitterUtil.*;
import static j2html.TagCreator.*;

import docsite.*;
import j2html.tags.specialized.*;

public class EmbeddedSiteSectionEmitter extends SectionEmitter {

    public EmbeddedSiteSectionEmitter(EmitterBuildParams params) {
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
        return section().withClass("embedded").with(iframe().withSrc(
            href()+"/"+section.siteIndex()
        ));
    }


    @Override
    protected AsideTag createTableOfContents() {
        return aside();
    }


    @Override
    protected Path outputPath() {
        return site.outputFolder().resolve(href()+".html");
    }


}