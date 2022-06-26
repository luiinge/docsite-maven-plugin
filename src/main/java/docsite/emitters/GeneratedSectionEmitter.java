package docsite.emitters;


import docsite.util.*;
import j2html.tags.Tag;
import java.nio.file.*;
import java.util.regex.*;

import docsite.*;
import j2html.tags.specialized.*;
import static docsite.util.EmitterUtil.*;
import static j2html.TagCreator.*;

public abstract class GeneratedSectionEmitter extends SectionEmitter {

    private final String translatedOrigin;
    private final boolean translationIsMissing;


    GeneratedSectionEmitter(EmitterBuildParams params) {
        super(params);
        if (siteLanguage.isPrimary()) {
            translatedOrigin = super.origin();
            translationIsMissing = false;
        } else {
            String localizedOrigen = EmitterUtil.withLanguage(siteLanguage, super.origin());
            if (!Files.exists(Path.of(localizedOrigen))) {
                translatedOrigin = super.origin();
                translationIsMissing = true;
            } else {
                translatedOrigin = localizedOrigen;
                translationIsMissing = false;
            }
        }
    }


    protected abstract SectionTag generateSectionContent(Tag<?> before);


    @Override
    protected SectionTag createSectionContent() {
        if (translationIsMissing) {
            return generateSectionContent(blockquote("There is no localized version of this page"));
        } else {
            return generateSectionContent(div());
        }
    }


    @Override
    protected String url() {
        return EmitterUtil.page(section.name());
    }


    @Override
    protected String url(SiteLanguage language) {
        return EmitterUtil.page(section.name(), language);
    }


    @Override
    public ATag createLinkToSection(boolean withIcon) {
        return withIcon ?
            internalLinkWithIcon(baseDir, translate(section.name()), url(siteLanguage), section.icon(), globalImages) :
            internalLink(translate(section.name()), url(siteLanguage));
    }


    @Override
    protected AsideTag createTableOfContents(SectionTag section) {
        return createTableOfContentsFromHtml(section.render());
    }


    @Override
    protected String origin() {
        return translatedOrigin;
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


    protected String replaceMermaidDiagrams(String html) {
        return html.replaceAll(
            "<pre><code class=\"language-mermaid\">([^<]+)</code></pre>",
            "<div class=\"mermaid\">$1</div>"
        );
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


    protected String removeH1(String html) {
        return html.replaceAll("<h1>([^<]*)</h1>","");
    }


    protected String generateHeadersId(String html) {
        String[] patterns = {"<(h\\d)>([^<]*)", "<(h\\d)><code>([^<]*)"};
        for (String pattern : patterns) {
            Matcher matcher = Pattern.compile(pattern).matcher(html);
            while (matcher.find()) {
                String tag = matcher.group(1);
                String name = matcher.group(2);
                String id = hrefId(name);
                if (!id.isEmpty()) {
                    html = html.replace("<" + tag + ">" + name, "<" + tag + " id=\"" + id + "\">" + name);
                }
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