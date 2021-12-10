package docsite;

import java.nio.file.Path;
import java.util.*;
import lombok.*;
import lombok.experimental.Accessors;

@Builder @Getter @Setter @Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Docsite {

    private String name;
    private String title;
    private String description;
    private String logo;
    private Path cssFile;
    private ThemeColors themeColors;
    private String index;
    private List<Section> sections;
    private Path outputFolder;



    public String title() {
        return Objects.requireNonNullElse(title,name);
    }


    public Section home() {
        return Section.generated("index").source(index).subsections(sections()).build();
    }


    public String description() {
        return Objects.requireNonNullElse(description,"");
    }


    public ThemeColors themeColors() {
        return Objects.requireNonNullElse(themeColors,ThemeColors.DEFAULT);
    }


    public Path outputImageFolder() {
        return outputFolder.resolve("images");
    }

}
