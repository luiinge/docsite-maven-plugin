package docsite;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.junit.Test;
import static docsite.Section.*;

public class DocSiteTest {

    private static final Logger LOGGER = new Logger(
        System.out::println,
        System.out::println,
        System.out::println,
        System.err::println
    );

    @Test
    public void testMarkdownToHtml() throws IOException {

        SiteConfiguration configuration = SiteConfiguration.builder()
        .name("jExt")
        .title("jExt - A Java library")
        .description("This is the description of the library")
        .logo("accessible-icon")
        .outputFolder(Paths.get("target","site"))
        .index("src/test/resources/README.md")
        .sections(List.of(
            generated("Changelog")
                .source("src/test/resources/CHANGELOG.md")
                .build(),
            group("Report")
                .subsections(List.of(
                    generated("Metrics")
                        .source("src/test/resources/metrics.md")
                        .build(),
                    generated("Dependencies")
                        .source("src/test/resources/dependencies.html")
                        .build(),
                    site()
                        .name("Javadoc")
                        .source("src/test/resources/apidocs")
                        .siteIndex("index.html")
                        .icon("java")
                        .build()
                )).build(),
            generated("License")
                .source("src/test/resources/LICENSE")
                .build(),
            link()
                .name("Github")
                .source("https://github.com/luiinge/jext")
                .icon("github")
                .build()
        ))
        .build();




        if (Files.exists(configuration.outputFolder())) {
            Files.walk(configuration.outputFolder())
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
        }

        if (!Files.exists(configuration.outputFolder())) {
           Files.createDirectory(configuration.outputFolder());
        }

        new SiteHtmlEmitter(configuration,LOGGER).generateSite();
    }



}
