package docsite;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
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
        .theme("lightcoral")
        .home(source("index", "src/test/resources/README.md")
            .withSections(
                source("changelog", "src/test/resources/CHANGELOG.md"),
                group("report").withSections(
                    source("metrics", "src/test/resources/metrics.md"),
                    source("Dependencies", "src/test/resources/dependencies.html"),
                    site("Javadoc","src/test/resources/apidocs","index.html")
                ),
                source("license", "src/test/resources/LICENSE"),
                link("Github","https://github.com/luiinge/jext")
            ))
        .outputFolder(Paths.get("target","site"))
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

        new SiteHtmlEmitter(LOGGER).generateSite(configuration);
    }



}
