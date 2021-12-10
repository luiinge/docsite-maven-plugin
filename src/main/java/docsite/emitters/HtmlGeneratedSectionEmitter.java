package docsite.emitters;


import java.io.*;
import java.util.regex.*;
import docsite.*;
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
            throw new RuntimeException(e);
        }
    }



    @Override
    protected AsideTag createTableOfContents() {
        try (InputStream inputStream = ResourceUtil.open(origin)) {

            String html = ResourceUtil.read(inputStream);
            Matcher matcher = Pattern.compile("<h(\\d)>([^<]*)").matcher(html);

            StringBuilder string = new StringBuilder();
            int previousLevel = TOC_MIN_LEVEL - 1;

            while (matcher.find()) {

                int level = Integer.parseInt(matcher.group(1));
                if (level < TOC_MIN_LEVEL || level > TOC_MAX_LEVEL) {
                    continue;
                }
                String name = matcher.group(2);
                String id = hrefId(name);


                if (previousLevel > level) {
                    string.append("</ol>".repeat(Math.max(0, previousLevel - level)));
                } else {
                    string.append("<ol>".repeat(Math.max(0, level - previousLevel)));
                }
                string
                    .append("<li><a class=\"internal\" href=\"#")
                    .append(id)
                    .append("\">")
                    .append(name)
                    .append("</a></li>");
                previousLevel = level;
            }
            string.append("</ol>".repeat(previousLevel));
            return aside().with(nav().with(rawHtml(string.toString())).withClass("toc"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}