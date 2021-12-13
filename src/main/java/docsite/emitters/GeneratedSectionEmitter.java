package docsite.emitters;


import java.util.regex.*;

import docsite.*;
import j2html.tags.specialized.*;
import static docsite.EmitterUtil.*;
import static j2html.TagCreator.*;

public abstract class GeneratedSectionEmitter extends SectionEmitter {

    GeneratedSectionEmitter(EmitterBuildParams params) {
        super(params);
    }


    @Override
    protected String url() {
        return EmitterUtil.page(section.name());
    }


    @Override
    public ATag createLinkToSection(boolean withIcon) {
        return withIcon ?
            internalLinkWithIcon(section.name(), url(), section.icon(), globalImages) :
            internalLink(section.name(), url());
    }


    @Override
    protected AsideTag createTableOfContents(SectionTag section) {
        return createTableOfContentsFromHtml(section.render());
    }


    protected String replaceLocalImages(String html) {
        Matcher matcher = Pattern.compile("<img .*src=\"([^\"]+)\".*>").matcher(html);
        while (matcher.find()) {
            String src = matcher.group(1);
            if (src.startsWith("http")) {
                continue;
            }
            html = html.replace("src=\""+src+"\"", "src=\""+sectionImages.imageFile(src)+"\"");
        }
        return html;
    }


    protected String hrefId(String name) {
        return name.strip().toLowerCase().replace(" ", "-");
    }


    protected String normalizeLinks(String html) {
        return html
            .replaceAll(
                "<a href([^>]*)>([^<]*)<([^<]*)</a>",
                "<a target=\"_blank\" rel=\"external noreferrer noopener nofollow\" href$1>$2<$3</a>"
            )
            .replaceAll(
                "<a href([^>]*)>([^<]*)</a>",
                "<a class=\"external\" target=\"_blank\" rel=\"external noreferrer noopener nofollow\" href$1>$2</a>"
            );
    }


    protected String generateHeadersId(String html) {
        Matcher matcher = Pattern.compile("<(h\\d)>([^<]*)").matcher(html);
        while (matcher.find()) {
            String tag = matcher.group(1);
            String name = matcher.group(2);
            String id = hrefId(name);
            if (!id.isEmpty()) {
                html = html.replace("<" + tag + ">" + name, "<" + tag + " id=\"" + id + "\">" + name);
            }
        }
        return html;
    }





    protected AsideTag createTableOfContentsFromHtml(String html) {

        Matcher matcher = Pattern.compile("<h(\\d)[^>]*>([^<]*)<").matcher(html);

        StringBuilder string = new StringBuilder();
        int previousLevel = TOC_MIN_LEVEL - 1;

        boolean empty = true;

        while (matcher.find()) {

            int level = Integer.parseInt(matcher.group(1));
            String name = matcher.group(2);
            String id = hrefId(name);

            if (level < TOC_MIN_LEVEL || level > TOC_MAX_LEVEL || id.isEmpty()) {
                continue;
            }

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
            empty = false;
        }

        if (empty) {
            return aside();
        } else {
            string.append("</ol>".repeat(previousLevel));
            return aside().with(nav().with(rawHtml(string.toString())).withClass("toc"));
        }

    }

}