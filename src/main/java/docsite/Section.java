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


    public List<Section> subsections() {
        return subsections == null ? List.of() : subsections;
    }

}