package docsite;

import static j2html.TagCreator.*;
import j2html.tags.ContainerTag;
import j2html.tags.specialized.*;

public final class EmitterUtil {

    private EmitterUtil() { /*avoid instantiation */ }


    public static ATag internalLink(String title, String url) {
        return a(title).withClass("internal").withHref(url);
    }


    public static ATag internalLinkWithIcon(String title, String url, String icon) {
        return a()
            .withClasses("label internal")
            .withHref(url)
            .with(
                icon(icon),
                span(title)
            );
    }


    static ATag externalLinkWithIcon(String title, String url, String icon) {
        return a()
            .withClasses("label external")
            .withHref(url)
            .withTarget("_blank")
            .withRel("external noreferrer noopener nofollow")
            .with(
                icon(icon),
                span(title)
            );
    }


    public static ITag icon(String icon) {
        return i().withClasses(icon == null || icon.isBlank() ? "hidden" : "fab fa-"+icon);
    }



    public static ATag externalLink(String title, String url) {
        return a(title)
            .withClass("external")
            .withHref(url)
            .withTarget("_blank")
            .withRel("external noreferrer noopener nofollow");
    }





    static String href(String name) {
        return name.strip().toLowerCase().replace(" ", "-");
    }


    static String href(Section section) {
        if (section.type() == Section.SectionType.EMBEDDED_SITE) {
            return href(section.name()+"/"+section.siteIndex());
        } else {
            return href(section.name());
        }
    }


    static String page(String name) {
        return href(name)+ SectionHtmlEmitter.HTML_EXTENSION;
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
