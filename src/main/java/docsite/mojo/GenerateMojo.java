package docsite.mojo;

import docsite.util.*;
import static java.util.Objects.requireNonNullElse;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;


import docsite.*;

@Mojo(defaultPhase = LifecyclePhase.POST_SITE, name = "generate")
public class GenerateMojo extends docsite.mojo.AbstractMojo {



    /**
     * Skips the execution for this project
     * @since 1.2
     */
    @Parameter(name = "skip", defaultValue = "false", property = "docsite.skip")
    boolean skip;



    public void execute() throws MojoExecutionException, MojoFailureException {

        if (skip) {
            return;
        }

        DocsiteReader docsiteReader = new DocsiteReader();

        initializeLogger();

        File[] outputFolderContent = Objects.requireNonNullElse(
            outputFolder.listFiles(),
            new File[0]
        );

        try {
            if (outputFolder.exists() && outputFolderContent.length > 0) {
                if (forceDelete) {
                    Logger.instance().warn("The contents of output folder {} will be deleted", outputFolder);
                    ResourceUtil.deleteDirectory(outputFolder.toPath());
                } else {
                    Logger.instance().error(
                        "As a safety mechanism, the output folder must be empty. " +
                        "You can disable this check using -Ddocsite.forceDelete"
                    );
                    throw new MojoFailureException("The output folder "+outputFolder+" is not empty");
                }
            }
            Files.createDirectories(outputFolder.toPath());
            if (docsiteFile != null) {
                docsite = docsiteReader.read(docsiteFile.toPath());
            } else {
                docsite = autoconfigure();
            }

            if (docsite == null) {
                getLog().warn("Site generation skipped for this project");
                return;
            }

            themeColors = requireNonNullElse(themeColors, ThemeColors.DEFAULT);

            Map<String,Map<String,String>> localization = getLocalization();


            if (localization == null && localizationFile != null && Files.exists(localizationFile.toPath())) {
                localization = docsiteReader.readLocalization(localizationFile.toPath());
            }


            new DocsiteEmitter(
                docsite,
                themeColors,
                cssFile != null ? cssFile.toPath() : null,
                useCDN,
                project.getBasedir().toPath(),
                outputFolder.toPath(),
                metadata,
                scripts == null ? List.of() : Arrays.asList(scripts),
                SiteLanguage.of(languages),
                localization
            ).generateSite();

        } catch (IOException e) {
            throw new MojoFailureException("Error generating site",e);
        }


    }


    private Docsite autoconfigure() {
        try {
            Autoconfigurer autoconfigurer = new Autoconfigurer(project);
            return autoconfigurer.configuration(
                Objects.requireNonNullElse(docsite, new Docsite())
            );
        } catch (MojoExecutionException | RuntimeException e) {
            getLog().warn(e.getMessage());
            return null;
        }
    }


    private void initializeLogger() {
        Logger.initialize(new Logger(
            getLog()::debug,
            getLog()::debug,
            getLog()::info,
            getLog()::warn,
            getLog()::error
        ));
    }

}