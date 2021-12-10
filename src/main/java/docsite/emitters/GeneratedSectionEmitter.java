package docsite.emitters;


import java.util.regex.*;

import docsite.*;
import j2html.tags.specialized.*;
import static docsite.EmitterUtil.*;

public abstract class GeneratedSectionEmitter extends SectionEmitter {

    GeneratedSectionEmitter(EmitterBuildParams params) {
        super(params);
    }


    @Override
    protected String url() {
        return EmitterUtil.page(section.name());
    }


    @Override
    protected ATag createLinkToSection() {
        return internalLinkWithIcon(section.name(), url(), section.icon(), globalImages);
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
            html = html.replace("<"+tag+">"+name,"<"+tag+" id=\""+id+"\">"+name);
        }
        return html;
    }
}