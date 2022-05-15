package docsite.emitters;


import j2html.tags.Tag;
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
    protected SectionTag generateSectionContent(Tag<?> before) {
        try (InputStream htmlInputStream = ResourceUtil.open(baseDir,origin())) {
            String html = ResourceUtil.read(htmlInputStream);
            html = generateHeadersId(html);
            html = normalizeLinks(html);
            html = replaceLocalImages(html);
            return section().with(before).with(rawHtml(html)).withClass("content");
        } catch (IOException e) {
            throw new DocsiteException(e);
        }
    }



}