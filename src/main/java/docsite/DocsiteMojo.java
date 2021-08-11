package docsite;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(defaultPhase = LifecyclePhase.POST_SITE, name = "generate")
public class DocsiteMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.name}")
    private String name;

    @Parameter(defaultValue = "${project.name}")
    private String title;

    @Parameter(defaultValue = "${project.description}")
    private String description;

    @Parameter
    private Map<String,String> meta = new LinkedHashMap<>();

    @Parameter
    private Map<String,String> styles = new LinkedHashMap<>();

    @Parameter
    private String cssFile;

    @Parameter(required = true)
    private Section home;

    @Parameter(defaultValue = "${project.build.outputDirectory}/site")
    private Path outputFolder;


    public void execute() throws MojoExecutionException, MojoFailureException {
        SiteConfiguration siteConfiguration = SiteConfiguration.builder()
        .name(name)
        .title(title)
        .description(description)
        .meta(meta)
        .styles(styles)
        .cssFile(cssFile)
        .home(home)
        .outputFolder(outputFolder)
        .build();

        try {
            new SiteHtmlEmitter(logger()).generateSite(siteConfiguration);
        } catch (IOException e) {
            throw new MojoFailureException("Error generating site",e);
        }


    }


    private Logger logger() {
        return new Logger(getLog()::debug,getLog()::info,getLog()::warn,getLog()::error);
    }

}
