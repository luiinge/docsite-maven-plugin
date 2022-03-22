package docsite.emitters;


import java.io.*;
import java.util.stream.*;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.*;
import com.vladsch.flexmark.util.data.MutableDataSet;
import docsite.*;
import docsite.util.ResourceUtil;
import j2html.tags.specialized.*;
import static j2html.TagCreator.*;

public class MarkdownGeneratedSectionEmitter extends GeneratedSectionEmitter {


    protected static final MutableDataSet options = new MutableDataSet();
    protected static final Parser parser = Parser.builder(options).build();
    protected static final HtmlRenderer renderer = HtmlRenderer.builder(options).build();


    public MarkdownGeneratedSectionEmitter(EmitterBuildParams params) {
        super(params);
    }



    @Override
    protected SectionTag createSectionContent() {
        try (InputStream markdown = ResourceUtil.open(baseDir, origin)) {
            String markdownContent = ResourceUtil.read(markdown);
            Node document = parser.parse(markdownContent);
            StreamSupport.stream(document.getChildren().spliterator(), false)
                .filter(Heading.class::isInstance)
                .map(Heading.class::cast)
                .forEach(heading -> heading.setAnchorRefId(hrefId(heading.getAnchorRefText())));
            String html = renderer.render(document);
            html = generateHeadersId(html);
            html = normalizeLinks(html);
            html = replaceLocalImages(html);
            return section().with(rawHtml(html)).withClass("content");
        }  catch (IOException e) {
            throw new DocsiteException(e);
        }
    }




}