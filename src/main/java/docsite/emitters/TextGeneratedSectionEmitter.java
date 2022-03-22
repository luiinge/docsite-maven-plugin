package docsite.emitters;


import java.io.*;

import docsite.*;
import docsite.util.ResourceUtil;
import j2html.tags.specialized.*;
import static j2html.TagCreator.*;

public class TextGeneratedSectionEmitter extends GeneratedSectionEmitter {

    public TextGeneratedSectionEmitter(EmitterBuildParams params) {
        super(params);
    }



    @Override
    protected SectionTag createSectionContent() {
        try (InputStream text = ResourceUtil.open(baseDir, origin)) {
            return section().with(pre(ResourceUtil.read(text)));
        }  catch (IOException e) {
            throw new DocsiteException(e);
        }
    }


    @Override
    protected AsideTag createTableOfContents(SectionTag sectionTag) {
        return aside();
    }

}