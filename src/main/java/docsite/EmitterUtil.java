package docsite;

import java.nio.file.*;
import java.util.regex.*;
import static j2html.TagCreator.*;
import j2html.tags.specialized.*;

public final class EmitterUtil {

    private EmitterUtil() { /*avoid instantiation */ }

    private static Pattern faPattern = Pattern.compile("(fa.?):([^:]+)");

    public static ATag internalLink(String title, String url) {
        return a(title).withClass("internal").withHref(url);
    }


    public static ATag internalLinkWithIcon(String title, String url, String icon, ImageResolver images) {
        return a()
            .withClasses("label internal")
            .withHref(url)
            .with(
                icon(icon,images),
                span(title)
            );
    }


    public static ATag externalLinkWithIcon(String title, String url, String icon, ImageResolver images) {
        return a()
            .withClasses("label external")
            .withHref(url)
            .withTarget("_blank")
            .withRel("external noreferrer noopener nofollow")
            .with(
                icon(icon, images),
                span(title)
            );
    }


    public static ITag icon(String icon, ImageResolver images) {
        if (icon == null || icon.isBlank()) {
            return i().withClass("hidden");
        }
        Matcher faMatcher = faPattern.matcher(icon);
        if (faMatcher.matches()) {
            return i().withClasses(faMatcher.group(1)+" fa-"+faMatcher.group(2));
        }
        if (Files.exists(Path.of(icon))) {
            return i().withClass("external-icon").withStyle("background-image: url('"+images.imageFile(icon)+"')");
        }
        return i().withClass("external-icon").withStyle("background-image: url('"+icon+"')");
    }



    public static ATag externalLink(String title, String url) {
        return a(title)
            .withClass("external")
            .withHref(url)
            .withTarget("_blank")
            .withRel("external noreferrer noopener nofollow");
    }



    public static String href(String name) {
        return name.strip().toLowerCase().replace(" ", "-");
    }


    public static String href(Section section) {
        if (section.type() == Section.SectionType.EMBEDDED_SITE) {
            return href(section.name()+"/"+section.siteIndex());
        } else {
            return href(section.name());
        }
    }


    public static String page(String name) {
        return href(name)+ ".html";
    }



}
