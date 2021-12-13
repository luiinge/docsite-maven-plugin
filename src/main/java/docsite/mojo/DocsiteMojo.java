package docsite.mojo;

import static java.util.Objects.requireNonNullElse;

import java.io.*;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import docsite.*;

@Mojo(defaultPhase = LifecyclePhase.POST_SITE, name = "generate")
public class DocsiteMojo extends AbstractMojo {

    @Parameter(name = "docsite", required = false)
    Docsite docsite;

    /**
     * Set the color theme that would be applied to the site
     * @since 1.0
     */
    @Parameter(name = "themeColors", required = false)
    ThemeColors themeColors;

    @Parameter(name = "cssFile", required = false)
    File cssFile;

    @Parameter(name = "outputFolder", defaultValue = "${project.build.directory}/docsite")
    File outputFolder;

    @Parameter( defaultValue = "${project}", readonly = true )
    MavenProject project;


    public void execute() throws MojoExecutionException, MojoFailureException {

        themeColors = requireNonNullElse(themeColors, ThemeColors.DEFAULT);

        if (docsite == null) {
            docsite = new Autoconfigurer(project).configuration();
        }

        try {
            new DocsiteEmitter(
                docsite,
                themeColors,
                cssFile != null ? cssFile.toPath() : null,
                outputFolder.toPath(),
                logger()
            ).generateSite();
        } catch (IOException e) {
            throw new MojoFailureException("Error generating site",e);
        }


    }


    private Logger logger() {
        return new Logger(getLog()::debug,getLog()::info,getLog()::warn,getLog()::error);
    }

}
