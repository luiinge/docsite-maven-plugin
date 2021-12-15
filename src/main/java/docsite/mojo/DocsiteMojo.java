package docsite.mojo;

import static java.util.Objects.requireNonNullElse;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;

import docsite.util.ResourceUtil;
import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import docsite.*;

@Mojo(defaultPhase = LifecyclePhase.POST_SITE, name = "generate")
public class DocsiteMojo extends AbstractMojo {

    /**
     * The folder where the documentation site would be generated.
     * @since 1.0
     */
    @Parameter(
        name = "outputFolder",
        required = true,
        defaultValue = "${project.build.directory}/docsite",
        property = "docsite.outputFolder"
    )
    File outputFolder;


    /**
     * Forces deletion of output folder.<br/>
     * A safety mechanism is implemented in order to prevent accidental deletion of directories,
     * but setting this property to <tt>true</tt> will bypass such feature.
     *
     * @since 1.0
     */
    @Parameter(name = "forceDelete", defaultValue = "false", property = "docsite.forceDelete")
    boolean forceDelete;


    /**
     * Defines the site estructure. If omitted, a default site estructure would be use instead.
     * <pre><code class="language-xml">&lt;title&gt;&lt;/title&gt;
     * &lt;description&gt;&lt;/description&gt;
     * &lt;logo&gt;&lt;/logo&gt;
     * &lt;index&gt;&lt;/index&gt;
     * &lt;sections&gt;
     *     &lt;section&gt;
     *         &lt;name&gt;&lt;/name&gt;
     *         &lt;description&gt;&lt;/description&gt;
     *         &lt;icon&gt;&lt;/icon&gt;
     *         &lt;source&gt;&lt;/source&gt;
     *         &lt;type&gt;&lt;/type&gt;
     *         &lt;siteIndex&gt;&lt;/siteIndex&gt;
     *         &lt;template&gt;&lt;/template&gt;
     *         &lt;replaceEmojis&gt;&lt;/replaceEmojis&gt;
     *         &lt;subsections&gt;
     *             &lt;section&gt;
     *                 ...
     *             &lt;/section&gt;
     *             ...
     *         &lt;/subsections&gt;
     *     &lt;/section&gt;
     *     ...
     * &lt;/sections&gt;
     * </code></pre>
     *
     * <table style="font-size: 0.8rem">
     * <tr><td><tt>title</tt></td><td> Title of the site. Used in page headers and metadata.</td><td>Required</<td></tr>
     * <tr><td><tt>description</tt></td><td>Brief description of the site. Used in page headers and metadata.</td><td></td></tr>
     * <tr><td><tt>logo</tt></td><td>File image or <a href="http://https://fontawesome.com/v5.15/icons">Font Awesome icon</a>. Used in page headers.</td><td></td></tr>
     * <tr><td><tt>index</tt></td><td>Document file (Markdown, HTML or raw text) that would be the landing page.</td><td>Required</td></tr>
     * <tr><td><tt>sections</tt></td><td>Main sections of the page. Used in the header navigation menu.</td><td></td></tr>
     * </table>
     *
     * <b>Section properties:</b>
     *
     * <table style="font-size: 0.8rem">
     * <tr><td><tt>name</tt></td><td>Name of the section. Used in menus, breadcrumbs and metadata.</td><td>Required</<td></tr>
     * <tr><td><tt>description</tt></td><td>Brief description of the section. Used in metadata.</td><td></td></tr>
     * <tr><td><tt>icon</tt></td><td>File image or <a href="http://https://fontawesome.com/v5.15/icons">Font Awesome icon</a>. Used in menus.</td><td></td></tr>
     * <tr><td><tt>type</tt></td><td>Type of the section. </td><td><tt>generated</tt><br/><tt>embedded</tt><br/><tt>group</tt><br/><tt>link</tt></td></tr>
     * <tr><td><tt>source</tt></td><td>Document file used as the site contents. Not used when <tt>type</tt> is <tt>group</tt></td><td></td></tr>
     * <tr><td><tt>siteIndex</tt></td><td>Main page of the embedded site. Required used when <tt>type</tt> is <tt>embedded</tt></td><td></td></tr>
     * <tr><td><tt>template</tt></td><td>A <a href="https://freemarker.apache.org/">Freemarker</a> template file. Only used when <tt>type</tt> is <tt>generated</tt></td><td></td></tr>
     * <tr><td><tt>replaceEmojis</tt></td><td>Set whether Github emoji markups would be replaced by images</td><td><tt>true</tt> (default)<br/><tt>false</tt></td></tr>
     * <tr><td><tt>subsections</tt></td><td>Sub-sections of the section. Only used when <tt>type</tt> is <tt>group</tt> </td><td></td></tr>
     * </table>
     *
     * @since 1.0
     */
    @Parameter(name = "docsite")
    Docsite docsite;

    /**

     * Set a custom color theme that would be applied to the site.
     * Value formats accepted are defined by
     * <a href="https://developer.mozilla.org/es/docs/Web/CSS/color_value">CSS colors</a>.
     * <pre><code class="language-xml">&lt;themeColors&gt;
     *   &lt;menuRegularBackgroundColor&gt;&lt;/menuRegularBackgroundColor&gt;
     *   &lt;menuBoldBackgroundColor&gt;&lt;/menuBoldBackgroundColor&gt;
     *   &lt;menuForegroundColor&gt;&lt;/menuForegroundColor&gt;
     *   &lt;menuDecorationColor&gt;&lt;/menuDecorationColor&gt;
     *   &lt;guiElementColor&gt;&lt;/guiElementColor&gt;
     * &lt;/themeColors&gt;
     * </code></pre>
     * @since 1.0
     */
    @Parameter(name = "themeColors")
    ThemeColors themeColors;

    /**
     * A local CSS file that would be used instead of the default style, in case you want
     * to use a totally different layout.
     * @since 1.0
     */
    @Parameter(name = "cssFile", property = "docsite.cssFile")
    File cssFile;



    @Parameter( defaultValue = "${project}", readonly = true )
    MavenProject project;


    public void execute() throws MojoExecutionException, MojoFailureException {

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

            if (docsite == null) {
                docsite = new Autoconfigurer(project).configuration();
            }

            new DocsiteEmitter(
                docsite,
                themeColors,
                cssFile != null ? cssFile.toPath() : null,
                outputFolder.toPath()
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
