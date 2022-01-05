package docsite;

import java.util.*;


public class Docsite {

    private String title;
    private String description;
    private String logo;
    private String favicon;
    private String index;
    private List<Section> sections;



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


    public String favicon() {
        return favicon;
    }


    public String index() {
        return this.index;
    }


    public List<Section> sections() {
        return Objects.requireNonNullElseGet(this.sections, ArrayList::new);
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


    public Docsite favicon(String favicon) {
        this.favicon = favicon;
        return this;
    }

}
