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

        Docsite configuration = Docsite.builder()
        .name("jExt")
        .title("jExt - A Java library")
        .description("This is the description of the library")
        .logo("fab:accessible-icon")
        .outputFolder(Paths.get("target/default","site"))
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
                        .icon("fab:java")
                        .build()
                )).build(),
            generated("License")
                .source("src/test/resources/LICENSE")
                .build(),
            link()
                .name("Github")
                .source("https://github.com/luiinge/jext")
                .icon("fab:github")
                .build()
        ))
        .build();

        testSiteGeneration(configuration);
    }


    @Test
    public void testExternalCss() throws IOException {

        Docsite configuration = Docsite.builder()
            .name("jExt")
            .title("jExt - A Java library")
            .description("This is the description of the library")
            .cssFile(Path.of("src/test/resources/external-layout.css"))
            .outputFolder(Paths.get("target/externalCss","site"))
            .index("src/test/resources/README.md")
            .build();

        testSiteGeneration(configuration);
    }


    @Test
    public void testCustomColors() throws IOException {

        Docsite configuration = Docsite.builder()
            .name("jExt")
            .title("jExt - A Java library")
            .description("This is the description of the library")
            .outputFolder(Paths.get("target/colors","site"))
            .index("src/test/resources/README.md")
            .themeColors(ThemeColors.builder()
                .menuRegularBackgroundColor("red")
                .menuBoldBackgroundColor("green")
                .menuForegroundColor("black")
                .menuDecorationColor("yellow")
                .guiElementColor("orange")
                .build()
            )
            .build();

        testSiteGeneration(configuration);
    }


    @Test
    public void testExternalIcons() throws IOException {

        Docsite configuration = Docsite.builder()
            .name("jExt")
            .title("jExt - A Java library")
            .logo("src/test/resources/external-icon.png")
            .outputFolder(Paths.get("target/externalIcon","site"))
            .index("src/test/resources/README.md")
            .sections(List.of(
                link()
                    .name("Github")
                    .source("https://github.com/luiinge/jext")
                    .icon("fab:github")
                    .build(),
                link()
                    .name("Other link")
                    .source("https://github.com")
                    .icon("src/test/resources/external-icon.png")
                    .build()
            ))
            .build();

        testSiteGeneration(configuration);
    }


    @Test
    public void testHtmlIndex() throws IOException {

        Docsite configuration = Docsite.builder()
            .name("jExt")
            .outputFolder(Paths.get("target/htmlIndex","site"))
            .index("src/test/resources/README.html")
            .build();

        testSiteGeneration(configuration);
    }

    private void testSiteGeneration(Docsite configuration) throws IOException {

        if (Files.exists(configuration.outputFolder())) {
            Files.walk(configuration.outputFolder())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }

        if (!Files.exists(configuration.outputFolder())) {
            Files.createDirectories(configuration.outputFolder());
        }

        new DocsiteEmitter(configuration,LOGGER).generateSite();
    }
}
