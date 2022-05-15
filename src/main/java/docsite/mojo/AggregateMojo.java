package docsite.mojo;

import docsite.*;
import docsite.util.DocsiteReader;
import docsite.util.ResourceUtil;
import java.util.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.Objects.requireNonNullElse;

@Mojo(defaultPhase = LifecyclePhase.POST_SITE, name = "aggregate")
public class AggregateMojo extends docsite.mojo.AbstractMojo {



    public void execute() throws MojoExecutionException, MojoFailureException {

         if (!session.getTopLevelProject().equals(project)) {
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

            themeColors = requireNonNullElse(themeColors, ThemeColors.DEFAULT);

            Autoconfigurer autoconfigurer = new Autoconfigurer(project);
            List<MavenProject> children = new ArrayList<>();
            for (MavenProject collected : session.getProjects()) {
                if (project.equals(collected.getParent())) {
                    children.add(collected);
                }
            }


            if (docsiteFile != null) {
                docsite = docsiteReader.read(docsiteFile.toPath());
            } else {
                docsite = autoconfigurer.aggregatedConfiguration(
                    Objects.requireNonNullElse(docsite, new Docsite()),
                    children
                );
            }

            Map<String,Map<String,String>> localization = getLocalization();
            if (localizations == null && localizationFile != null && Files.exists(localizationFile.toPath())) {
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