package docsite.emitters;


import java.nio.file.Path;
import static docsite.util.EmitterUtil.*;
import static j2html.TagCreator.*;

import docsite.*;
import docsite.util.EmitterUtil;
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
    public ATag createLinkToSection(boolean withIcon) {
        return withIcon ?
            internalLinkWithIcon(baseDir, section.name(), url(), section.icon(), globalImages) :
            internalLink(section.name(), url());
    }


    @Override
    protected SectionTag createSectionContent() {
        return section().withClass("embedded").with(iframe().withSrc(
            href()+"/"+section.siteIndex()
        ));
    }


    @Override
    protected AsideTag createTableOfContents(SectionTag section) {
        return aside();
    }


    @Override
    protected Path outputPath() {
        return outputFolder.resolve(href()+".html");
    }


}