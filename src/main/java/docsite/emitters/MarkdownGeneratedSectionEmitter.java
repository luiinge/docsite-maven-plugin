package docsite.emitters;


import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.util.data.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.*;
import docsite.*;
import docsite.util.ResourceUtil;
import j2html.tags.specialized.*;
import static j2html.TagCreator.*;

public class MarkdownGeneratedSectionEmitter extends GeneratedSectionEmitter {


    protected static final DataHolder options = new MutableDataSet()
        .set(TablesExtension.COLUMN_SPANS, false)
        .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
        .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
        .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
        .set(Parser.EXTENSIONS, List.of(TablesExtension.create()))
        .toImmutable();
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