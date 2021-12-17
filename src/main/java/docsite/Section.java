package docsite;

import java.util.*;
import docsite.util.ResourceUtil;


public class Section {

    public enum SectionType {
        generated,
        embedded,
        group,
        link
    }

    private String name;
    private String description;
    private String icon;
    private String source;
    private SectionType type;
    private String siteIndex;
    private List<Section> subsections;
    private Boolean replaceEmojis;
    private String template;



    public Section() {

    }

    public Section(
        String name,
        String description,
        String icon,
        String source,
        SectionType type,
        String siteIndex,
        List<Section> subsections,
        Boolean replaceEmojis,
        String template
    ) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.source = source;
        this.type = type;
        this.siteIndex = siteIndex;
        this.subsections = subsections;
        this.replaceEmojis = replaceEmojis;
        this.template = template;
    }


    public static Section.SectionBuilder generated(String name) {
        return builder().type(SectionType.generated).name(name);
    }

    public static Section.SectionBuilder site() {
        return builder().type(SectionType.embedded);
    }

    public static Section.SectionBuilder group(String name) {
        return builder().type(SectionType.group).name(name);
    }

    public static Section.SectionBuilder link() {
        return builder().type(SectionType.link);
    }




    public static SectionBuilder builder() {
        return new SectionBuilder();
    }


    public String name() {
        return Objects.requireNonNullElse(this.name,"");
    }


    public String description() {
        return this.description;
    }


    public String icon() {
        return this.icon;
    }


    public String source() {
        return this.source;
    }


    public SectionType type() {
        return this.type;
    }


    public String template() {
        return this.template;
    }



    public List<Section> subsections() {
        return subsections == null ? List.of() : subsections;
    }


    public Boolean replaceEmojis() {
        return Objects.requireNonNullElse(replaceEmojis, Boolean.TRUE);
    }


    public String siteIndex() {
        return Objects.requireNonNullElse(siteIndex, "index.html");
    }


    public boolean isValid() {
        switch (type) {
            case generated:
            case embedded:
                return ResourceUtil.existsSource(source);
            case link:
                return source != null;
            case group:
                return !subsections().isEmpty();
            default:
                return true;
        }
    }


    public static class SectionBuilder {

        private String name;
        private String description;
        private String icon;
        private String source;
        private SectionType type;
        private String siteIndex;
        private List<Section> subsections;
        private Boolean replaceEmojis;
        private String template;


        SectionBuilder() {
        }


        public SectionBuilder name(String name) {
            this.name = name;
            return this;
        }


        public SectionBuilder description(String description) {
            this.description = description;
            return this;
        }


        public SectionBuilder icon(String icon) {
            this.icon = icon;
            return this;
        }


        public SectionBuilder source(String source) {
            this.source = source;
            return this;
        }


        public SectionBuilder type(SectionType type) {
            this.type = type;
            return this;
        }


        public SectionBuilder siteIndex(String siteIndex) {
            this.siteIndex = siteIndex;
            return this;
        }


        public SectionBuilder subsections(List<Section> subsections) {
            this.subsections = subsections;
            return this;
        }


        public SectionBuilder replaceEmojis(Boolean replaceEmojis) {
            this.replaceEmojis = replaceEmojis;
            return this;
        }


        public SectionBuilder template(String template) {
            this.template = template;
            return this;
        }


        public Section build() {
            return new Section(name, description, icon, source, type, siteIndex, subsections, replaceEmojis, template);
        }


    }

}