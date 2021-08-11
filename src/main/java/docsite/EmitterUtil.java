package docsite;

import static j2html.TagCreator.a;
import j2html.tags.ContainerTag;

public final class EmitterUtil {

    private EmitterUtil() { /*avoid instantiation */ }


    static ContainerTag<?> inlink(String title, String url) {
        return a(title).withClass("internal").withHref(url);
    }

    static ContainerTag<?> exlink(String title, String url) {
        return a(title)
            .withClass("external")
            .withHref(url)
            .withTarget("_blank")
            .withRel("external noreferrer noopener nofollow");
    }


    static String href(String name) {
        return name.strip().toLowerCase().replace(" ", "-");
    }


    static String page(String name) {
        return href(name)+SectionHtmlEmitter.HTML_EXTENSION;
    }


    static String normalizeLinks(String html) {
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

}
