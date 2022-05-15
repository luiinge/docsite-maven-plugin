package docsite.util;

import java.nio.file.*;
import java.util.regex.*;
import static j2html.TagCreator.*;
import docsite.*;
import j2html.tags.specialized.*;

public final class EmitterUtil {

    private EmitterUtil() { /*avoid instantiation */ }


    private static Pattern faPattern = Pattern.compile("(fa.?):([^:]+)");



    public static ATag internalLink(String title, String url) {
        return a(title).withClass("internal").withHref(url);
    }


    public static ATag internalLinkWithIcon(Path baseDir, String title, String url, String icon, ImageResolver images) {
        return a()
            .withClasses("label internal")
            .withHref(url)
            .with(
                icon(baseDir, icon,images),
                span(title)
            );
    }


    public static ATag externalLinkWithIcon(Path baseDir, String title, String url, String icon, ImageResolver images) {
        return a()
            .withClasses("label external")
            .withHref(url)
            .withTarget("_blank")
            .withRel("external noreferrer noopener nofollow")
            .with(
                icon(baseDir, icon, images),
                span(title)
            );
    }


    public static ATag externalLinkWithImage(Path baseDir, String title, String url, String icon, ImageResolver images) {
        return a()
            .withClasses("label external")
            .withHref(url)
            .withTarget("_blank")
            .withRel("external noreferrer noopener nofollow")
            .with(
                image(baseDir, icon, images),
                span(title)
            );
    }


    public static ITag icon(Path baseDir, String icon, ImageResolver images) {
        if (icon == null || icon.isBlank()) {
            return i().withClass("hidden");
        }
        Matcher faMatcher = faPattern.matcher(icon);
        if (faMatcher.matches()) {
            return i().withClasses(faMatcher.group(1)+" fa-"+faMatcher.group(2));
        }
        if (Files.exists(baseDir.resolve(icon))) {
            return i().withClass("external-icon").withStyle("background-image: url('"+images.imageFile(icon)+"')");
        }
        return i().withClass("external-icon").withStyle("background-image: url('"+icon+"')");
    }


    public static ImgTag image(Path baseDir, String source, ImageResolver images) {
        if (source == null || source.isBlank()) {
            return img().withClass("hidden");
        }
        if (Files.exists(baseDir.resolve(source))) {
            return img().withSrc(images.imageFile(source));
        }
        return img().withSrc(source);
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
        if (section.type() == Section.SectionType.embedded) {
            return href(section.name()+"/"+section.siteIndex());
        } else {
            return href(section.name());
        }
    }


    public static String page(String name) {
        return href(name)+ ".html";
    }

    public static String page(String name, SiteLanguage language) {
        return language.isPrimary() ? page(name) : href(name)+ "_"+language.language()+".html";
    }


    public static LinkTag stylesheet(String href) {
        return link().attr("href",href).attr("rel","stylesheet");
    }


    public static String withLanguage(SiteLanguage language, String path) {
        if (language.isPrimary()) {
            return path;
        }
        int extensionPosition = path.lastIndexOf('.');
        if (extensionPosition == -1) {
            return path+"_"+language.language();
        } else {
            return path.substring(0,extensionPosition)+"_"+language.language()+path.substring(extensionPosition);
        }
    }

}