package docsite;

import java.nio.file.Path;
import java.util.*;
import lombok.*;
import lombok.experimental.Accessors;

@Builder @Getter @Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SiteConfiguration {

    private String name;
    private String title;
    private String description;
    private String logo;
    private Path cssFile;
    private String index;
    private List<Section> sections;
    private Path outputFolder;



    public String title() {
        return (title == null || title.isBlank()) ? name : title;
    }


    public Section home() {
        return Section.generated("index").source(index).subsections(sections()).build();
    }

}
