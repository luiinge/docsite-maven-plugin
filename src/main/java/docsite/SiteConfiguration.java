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
    private String theme;
    private String themeFile;
    private Map<String,String> meta;
    private Map<String,String> styles;
    private Section home;
    private Path outputFolder;



    public String title() {
        return (title == null || title.isBlank()) ? name : title;
    }


    public String themeFile() {
        return Objects.requireNonNullElse(themeFile,"theme-"+theme+".css");
    }

}
