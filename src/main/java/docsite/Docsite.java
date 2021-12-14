package docsite;

import java.util.*;


public class Docsite {

    private String title;
    private String description;
    private String logo;
    private String index;
    private List<Section> sections;


    public Docsite() {

    }

    public Docsite(
        String title,
        String description,
        String logo,
        String index,
        List<Section> sections
    ) {
        this.title = title;
        this.description = description;
        this.logo = logo;
        this.index = index;
        this.sections = Objects.requireNonNullElse(sections, new ArrayList<>());
    }


    public static DocsiteBuilder builder() {
        return new DocsiteBuilder();
    }


    public String title() {
        return Objects.requireNonNull(title);
    }


    public Section home() {
        return Section.generated("index")
            .description(description)
            .source(index)
            .subsections(sections())
            .build();
    }


    public String description() {
        return Objects.requireNonNullElse(description,"");
    }


    public String logo() {
        return this.logo;
    }


    public String index() {
        return this.index;
    }


    public List<Section> sections() {
        return this.sections;
    }


    public Docsite title(String title) {
        this.title = title;
        return this;
    }


    public Docsite description(String description) {
        this.description = description;
        return this;
    }


    public Docsite logo(String logo) {
        this.logo = logo;
        return this;
    }


    public Docsite index(String index) {
        this.index = index;
        return this;
    }


    public Docsite sections(List<Section> sections) {
        this.sections = sections;
        return this;
    }



    public static class DocsiteBuilder {

        private String title;
        private String description;
        private String logo;
        private String index;
        private List<Section> sections;


        DocsiteBuilder() {
        }



        public DocsiteBuilder title(String title) {
            this.title = title;
            return this;
        }


        public DocsiteBuilder description(String description) {
            this.description = description;
            return this;
        }


        public DocsiteBuilder logo(String logo) {
            this.logo = logo;
            return this;
        }



        public DocsiteBuilder index(String index) {
            this.index = index;
            return this;
        }


        public DocsiteBuilder sections(List<Section> sections) {
            this.sections = sections;
            return this;
        }



        public Docsite build() {
            return new Docsite(
                title,
                description,
                logo,
                index,
                sections
            );
        }


    }

}
