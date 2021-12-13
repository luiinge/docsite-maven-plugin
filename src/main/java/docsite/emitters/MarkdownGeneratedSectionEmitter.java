package docsite.emitters;


import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.*;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.util.ast.Node;
import docsite.*;
import j2html.tags.specialized.*;
import static j2html.TagCreator.*;

public class MarkdownGeneratedSectionEmitter extends GeneratedSectionEmitter {

    public MarkdownGeneratedSectionEmitter(EmitterBuildParams params) {
        super(params);
    }



    @Override
    protected SectionTag createSectionContent() {
        try (InputStream markdown = ResourceUtil.open(origin)) {
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
            throw new RuntimeException(e);
        }
    }




}