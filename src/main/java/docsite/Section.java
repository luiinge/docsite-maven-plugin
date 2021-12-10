package docsite;

import java.util.*;
import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor @Getter @Builder @Accessors(fluent = true)
public class Section {

    public static Section.SectionBuilder generated(String name) {
        return builder().type(SectionType.GENERATED).name(name);
    }

    public static Section.SectionBuilder site() {
        return builder().type(SectionType.EMBEDDED_SITE);
    }

    public static Section.SectionBuilder group(String name) {
        return builder().type(SectionType.GROUP).name(name);
    }

    public static Section.SectionBuilder link() {
        return builder().type(SectionType.LINK);
    }

    public enum SectionType {
        GENERATED,
        EMBEDDED_SITE,
        GROUP,
        LINK
    }


    private String name;
    private String icon;
    private String source;
    private SectionType type;
    private String siteIndex;
    private List<Section> subsections;
    private Boolean replaceEmojis;


    public List<Section> subsections() {
        return subsections == null ? List.of() : subsections;
    }


    public Boolean replaceEmojis() {
        return Objects.requireNonNullElse(replaceEmojis, Boolean.TRUE);
    }


    public boolean isValid() {
        switch (type) {
            case GENERATED:
            case EMBEDDED_SITE:
                return ResourceUtil.existsSource(source);
            case LINK:
                return source != null;
            case GROUP:
                return !subsections().isEmpty();
            default:
                return true;
        }
    }

}