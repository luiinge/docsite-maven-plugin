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
    private Map<String,String> meta;
    private Map<String,String> styles;
    private String cssFile;
    private Section home;
    private Path outputFolder;


    public String title() {
        return (title == null || title.isBlank()) ? name : title;
    }

}
