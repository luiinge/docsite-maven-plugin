package docsite;

import java.util.*;
import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor @Getter @Builder @Accessors(fluent = true)
public class Section {


    public static Section source(String name, String source) {
        return new Section(name,source,SectionType.GENERATED,null,new ArrayList<>());
    }

    public static Section group(String name) {
        return new Section(name,null,SectionType.GROUP,null,new ArrayList<>());
    }

    public static Section site(String name, String folder, String siteIndex) {
        return new Section(name,folder,SectionType.EMBEDDED_SITE,siteIndex,new ArrayList<>());
    }

    public static Section link(String name, String site) {
        return new Section(name,site,SectionType.LINK,null,new ArrayList<>());
    }


    public enum SectionType {
        GENERATED,
        EMBEDDED_SITE,
        GROUP,
        LINK
    }


    private String name;
    private String source;
    private SectionType type;
    private String siteIndex;
    private List<Section> subsections;



    public Section withSections(Section...sections) {
        this.subsections.addAll(Arrays.asList(sections));
        return this;
    }

}