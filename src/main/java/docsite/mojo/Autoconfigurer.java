package docsite.mojo;

import java.nio.file.*;
import java.util.*;
import docsite.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.jetbrains.annotations.NotNull;

public class Autoconfigurer {


    private final MavenProject project;
    private final Path reportingFolder;

    public Autoconfigurer(MavenProject project) {
        this.project = project;
        this.reportingFolder = Path.of(project.getModel().getReporting().getOutputDirectory());
    }



    public Docsite configuration(Docsite docsite) throws MojoExecutionException {
        docsite.title(nonNull(project.getName(),project.getArtifactId()));
        docsite.description(nonNull(project.getDescription(),""));
        docsite.index(searchIndex());
        docsite.sections(createSections());
        if (docsite.companyLink() == null || docsite.companyLink().isBlank()) {
            fillVcsAsCompany(docsite);
        }
        return docsite;
    }


    public Docsite aggregatedConfiguration(Docsite docsite, List<MavenProject> children) throws MojoExecutionException {
        docsite.title(nonNull(project.getName(),project.getArtifactId()));
        docsite.description(nonNull(project.getDescription(),""));
        docsite.index(searchIndex());
        List<Section> sections = new ArrayList<>();
        changelogSection().ifPresent(sections::add);
        List<Section> childrenSections = new ArrayList<>();
        for (MavenProject child : children) {
            String source = project.getBasedir().toPath().relativize(Path.of(child.getBasedir()+"/target/docsite")).toString();
            childrenSections.add(
                Section.copy().source(source).name(child.getName()).siteIndex("index.html").build()
            );
        }
        sections.add(Section.group("Components").icon("fas:stream").subsections(childrenSections).build());
        licenseSection().ifPresent(sections::add);
        docsite.sections(sections);

        if (docsite.companyLink() == null || docsite.companyLink().isBlank()) {
            fillVcsAsCompany(docsite);
        }
        return docsite;
    }


    @NotNull
    private List<Section> createSections() {
        List<Section> sections = new ArrayList<>();
        changelogSection().ifPresent(sections::add);
        reportsSection().ifPresent(sections::add);
        licenseSection().ifPresent(sections::add);
        return sections;
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
                        .description("Log of modifications on new versions")
                        .source(file)
                        .icon("fas:clipboard-list")
                        .build()
                );
            }
        }
        return Optional.empty();
    }


    private void fillVcsAsCompany(Docsite docsite) {
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
            docsite.companyLink(url);
            docsite.companyLogo(icon);
        }
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
                    .description("Collection of technical reports about this project")
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
                    .description("Javadoc documentation for this project")
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
                    .description("Collection of artifacts that this project uses")
                    .source(dependenciesFile.toString())
                    .replaceEmojis(false)
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
                    .name("Test Coverage")
                    .description("Report of code coverage according the executed tests")
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
                    .description("Results of the automatic tests executed in this project")
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
                    .description("Description of goals and available parameters for this plugin")
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