package docsite.mojo;

import java.nio.file.*;
import java.util.*;
import docsite.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

public class Autoconfigurer {


    private final MavenProject project;
    private final Path reportingFolder;

    public Autoconfigurer(MavenProject project) {
        this.project = project;
        this.reportingFolder = Path.of(project.getModel().getReporting().getOutputDirectory());
    }


    public Docsite configuration() throws MojoExecutionException {
        Docsite.DocsiteBuilder builder = Docsite.builder();

        builder.name(nonNull(project.getName(),project.getArtifactId()));
        builder.title(nonNull(project.getName(),project.getArtifactId()));
        builder.description(nonNull(project.getDescription(),""));
        builder.index(searchIndex());

        List<Section> sections = new ArrayList<>();

        changelogSection().ifPresent(sections::add);
        reportsSection().ifPresent(sections::add);
        vcsSection().ifPresent(sections::add);
        licenseSection().ifPresent(sections::add);

        builder.sections(sections);

        return builder.build();

    }





    private String searchIndex() throws MojoExecutionException {
        for (String file : Objects.requireNonNull(project.getBasedir().list())) {
           if (file.toLowerCase().startsWith("readme.") ||
               file.toLowerCase().startsWith("index.")
           ) {
               return file;
           }
        }
        throw new MojoExecutionException("Cannot determine home page for the docsite");
    }


    private Optional<Section> licenseSection() {
        for (String file : Objects.requireNonNull(project.getBasedir().list())) {
            if (file.equalsIgnoreCase("license") ||
                file.toLowerCase().startsWith("license.")
            ) {
                return Optional.of(
                    Section.generated("License")
                        .source(file)
                        .icon("fas:balance-scale")
                        .build()
                );
            }
        }
        return Optional.empty();
    }


    private Optional<Section> changelogSection() {
        for (String file : Objects.requireNonNull(project.getBasedir().list())) {
            if (file.equalsIgnoreCase("changelog") ||
                file.toLowerCase().startsWith("changelog.")
            ) {
                return Optional.of(
                    Section.generated("Changelog")
                        .source(file)
                        .icon("fas:clipboard-list")
                        .build()
                );
            }
        }
        return Optional.empty();
    }


    private Optional<Section> vcsSection() {
        if (project.getScm() != null && project.getScm().getUrl() != null) {
            String url = project.getScm().getUrl();
            String icon;
            if (url.contains("github")) {
                icon = "fab:github";
            } else if (url.contains("gitlab")) {
                icon = "fab:gitlab";
            } else if (url.contains("bitbucket")) {
                icon = "fab:gitbucket";
            } else if (url.contains("git")) {
                icon = "fab:git-alt";
            } else {
                icon = "fas:code-branch";
            }
            return Optional.of(Section.link().name("Source").source(url).icon(icon).build());
        }
        return Optional.empty();
    }


    private Optional<Section> reportsSection() {
        List<Section> reports = new ArrayList<>();
        mavenPluginSection().ifPresent(reports::add);
        javadocSection().ifPresent(reports::add);
        dependenciesSection().ifPresent(reports::add);
        jacocoSection().ifPresent(reports::add);
        surefireSection().ifPresent(reports::add);
        return reports.isEmpty() ?
            Optional.empty() :
            Optional.of(
                Section.group("Reports")
                    .icon("fas:file-medical-alt")
                    .subsections(reports)
                    .build()
            );
    }


    private Optional<Section> javadocSection() {
        Path javadocFolder = reportingFolder.resolve("apidocs");
        if (Files.exists(javadocFolder)) {
            return Optional.of(
                Section.site()
                    .name("Javadoc")
                    .source(javadocFolder.toString())
                    .siteIndex("index.html")
                    .icon("fab:java")
                    .build()
            );
        }
        return Optional.empty();
    }


    private Optional<Section> dependenciesSection() {
        Path dependenciesFile = reportingFolder.resolve("dependencies.html");
        if (Files.exists(dependenciesFile)) {
            return Optional.of(
                Section.generated("Dependencies")
                    .source(dependenciesFile.toString())
                    .build()
            );
        }
        return Optional.empty();
    }


    private Optional<Section> jacocoSection() {
        Path jacocoFolder = reportingFolder.resolve("jacoco");
        if (Files.exists(jacocoFolder)) {
            return Optional.of(
                Section.site()
                    .name("Coverage")
                    .source(jacocoFolder.toString())
                    .siteIndex("index.html")
                    .build()
            );
        }
        return Optional.empty();
    }


    private Optional<Section> surefireSection() {
        Path report = reportingFolder.resolve("surefire-report.html");
        if (Files.exists(report)) {
            return Optional.of(
                Section.generated("Test Results")
                    .source(report.toString())
                    .build()
            );
        }
        return Optional.empty();
    }



    private Optional<Section> mavenPluginSection() {
        Path pluginDescriptor = Path.of(project.getBuild().getDirectory())
            .resolve("classes/META-INF/maven/plugin.xml");

        if (Files.exists(pluginDescriptor)) {
            return Optional.of(
                Section.generated("Plugin Goals")
                    .template("maven-plugin-descriptor")
                    .source(pluginDescriptor.toString())
                    .build()
            );
        }
        return Optional.empty();
    }



    private static String nonNull(String valueA, String valueB) {
        return valueA == null ? valueB : valueA;
    }




}
