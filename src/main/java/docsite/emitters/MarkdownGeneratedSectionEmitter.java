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


    @Override
    protected AsideTag createTableOfContents() {
        try (InputStream markdown = ResourceUtil.open(origin)) {
            Node document = parser.parse(ResourceUtil.read(markdown));

            List<Heading> children = StreamSupport.stream(document.getChildren().spliterator(), false)
                .filter(Heading.class::isInstance)
                .map(Heading.class::cast)
                .filter(child->child.getLevel()>=TOC_MIN_LEVEL)
                .filter(child->child.getLevel()<=TOC_MAX_LEVEL)
                .collect(Collectors.toList());

            StringBuilder string = new StringBuilder();
            int previousLevel = TOC_MIN_LEVEL - 1;
            for (Heading heading : children) {
                if (previousLevel > heading.getLevel()) {
                    string.append("</ol>".repeat(Math.max(0, previousLevel - heading.getLevel())));
                } else {
                    string.append("<ol>".repeat(Math.max(0, heading.getLevel() - previousLevel)));
                }
                string
                    .append("<li><a class=\"internal\" href=\"#")
                    .append(hrefId(heading.getAnchorRefText()))
                    .append("\">")
                    .append(heading.getAnchorRefText())
                    .append("</a></li>");
                previousLevel = heading.getLevel();
            }
            string.append("</ol>".repeat(Math.max(0, previousLevel)));
            return aside().with(nav().with(rawHtml(string.toString())).withClass("toc"));

        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}