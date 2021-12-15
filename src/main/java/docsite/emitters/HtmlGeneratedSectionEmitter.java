package docsite.emitters;


import java.io.*;
import docsite.*;
import docsite.util.ResourceUtil;
import j2html.tags.specialized.*;
import static j2html.TagCreator.*;

public class HtmlGeneratedSectionEmitter extends GeneratedSectionEmitter {

    public HtmlGeneratedSectionEmitter(EmitterBuildParams params) {
        super(params);
    }


    @Override
    protected SectionTag createSectionContent() {
        try (InputStream htmlInputStream = ResourceUtil.open(origin)) {
            String html = ResourceUtil.read(htmlInputStream);
            html = generateHeadersId(html);
            html = normalizeLinks(html);
            html = replaceLocalImages(html);
            return section().with(rawHtml(html)).withClass("content");
        } catch (IOException e) {
            throw new DocsiteException(e);
        }
    }



}