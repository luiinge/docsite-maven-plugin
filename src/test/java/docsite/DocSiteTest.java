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

        Docsite docsite = Docsite.builder()
        .name("jExt")
        .title("jExt - A Java library")
        .description("This is the description of the library")
        .logo("fab:accessible-icon")
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

        testSiteGeneration(docsite,"default");
    }


    @Test
    public void testExternalCss() throws IOException {

        Docsite docsite = Docsite.builder()
            .name("jExt")
            .title("jExt - A Java library")
            .description("This is the description of the library")
            .index("src/test/resources/README.md")
            .build();

        testSiteGeneration(
            docsite,
            ThemeColors.DEFAULT,
            Path.of("src/test/resources/external-layout.css"),
            "externalCss"
        );
    }


    @Test
    public void testCustomColors() throws IOException {

        Docsite docsite = Docsite.builder()
            .name("jExt")
            .title("jExt - A Java library")
            .description("This is the description of the library")
            .index("src/test/resources/README.md")
            .build();

        ThemeColors themeColors = ThemeColors.builder()
            .menuRegularBackgroundColor("red")
            .menuBoldBackgroundColor("green")
            .menuForegroundColor("black")
            .menuDecorationColor("yellow")
            .guiElementColor("orange")
            .build();

        testSiteGeneration(docsite, themeColors, null, "colors");

    }


    @Test
    public void testExternalIcons() throws IOException {

        Docsite docsite = Docsite.builder()
            .name("jExt")
            .title("jExt - A Java library")
            .logo("src/test/resources/external-icon.png")
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

        testSiteGeneration(docsite, "externalIcon");
    }


    @Test
    public void testHtmlIndex() throws IOException {

        Docsite docsite = Docsite.builder()
            .name("jExt")
            .index("src/test/resources/README.html")
            .build();

        testSiteGeneration(docsite, "htmlIndex");

    }



    @Test
    public void testTemplate() throws IOException {

        Docsite docsite = Docsite.builder()
            .name("jExt")
            .index("src/test/resources/README.html")
            .sections(List.of(
                Section.generated("maven")
                    .source("src/test/resources/plugin.xml")
                    .template("maven-plugin-descriptor")
                    .build()
            )).build();

        testSiteGeneration(docsite, "template");

    }


    private void testSiteGeneration(Docsite docsite, String outputFolderName) throws IOException {
        testSiteGeneration(docsite, ThemeColors.DEFAULT, null, outputFolderName);
    }


    private void testSiteGeneration(
        Docsite configuration,
        ThemeColors themeColors,
        Path cssFile,
        String outputFolderName
    ) throws IOException {

        Path outputFolder = Path.of("target/testsites").resolve(outputFolderName);
        if (Files.exists(outputFolder)) {
            Files.walk(outputFolder)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }

        if (!Files.exists(outputFolder)) {
            Files.createDirectories(outputFolder);
        }

        new DocsiteEmitter(configuration,themeColors,cssFile,outputFolder,LOGGER).generateSite();
    }
}
