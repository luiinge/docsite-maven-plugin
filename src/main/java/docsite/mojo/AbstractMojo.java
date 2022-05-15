package docsite.mojo;


import java.io.*;
import java.util.*;

import java.util.stream.Stream;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import docsite.*;


public abstract class AbstractMojo extends org.apache.maven.plugin.AbstractMojo {

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
     * Defines the site structure. If omitted, a default site structure would be use instead.
     * <pre><code class="language-xml">&lt;title&gt;&lt;/title&gt;
     * &lt;description&gt;&lt;/description&gt;
     * &lt;logo&gt;&lt;/logo&gt;
     * &lt;companyLogo&gt;&lt;/companyLogo&gt;
     * &lt;companyLink&gt;&lt;/companyLink&gt;
     * &lt;favicon&gt;&lt;/favicon&gt;
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
     * <tr><td><tt>companyLogo</tt></td><td>File image. Used in page headers.</td><td></td></tr>
     * <tr><td><tt>companyLink</tt></td><td>URL link. Used in page headers.</td><td></td></tr>
     * <tr><td><tt>favicon</tt></td><td>File image. Used by browsers as the page icon.</td><td></td></tr>
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
     * <tr><td><tt>type</tt></td><td>Type of the section. </td><td><tt>generated</tt><br/><tt>embedded</tt><br/><tt>copy</tt><br/><tt>group</tt><br/><tt>link</tt></td></tr>
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
     * A JSON file containing the object structure of the docsite
     * (instead of including it in the pom)
     * @since 1.2
     * */
    @Parameter(name = "docsiteFile", property = "docsite.docsiteFile")
    File docsiteFile;


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
     * Set extra metadata that would be included in the <tt>head</tt> section.
     * <pre><code class="language-xml">&lt;metadata&gt;
     *   &lt;keyA&gt;valueA&lt;/keyA&gt;
     *   &lt;keyB&gt;valueB&lt;/keyB&gt;
     *   ...
     * &lt;/metadata&gt;
     * </code></pre>
     * @since 1.1
     */
    @Parameter(name = "metadata")
    Map<String,String> metadata;


    /**
     * Set scripts that would be included in the <tt>head</tt> section
     * <pre><code class="language-xml">&lt;scripts&gt;
     *  &lt;script&gt;
     *     &lt;src&gt;https://remote-script.js&lt;/src&gt;
     *     &lt;async&gt;true&lt;/async&gt;
     *  &lt;/script&gt;
     *  &lt;script&gt;
     *     &lt;code&gt;
     *       // JS code
     *       // ...
     *     &lt;/code&gt;
     *  &lt;/script&gt;
     * &lt;/scripts&gt;
     * </code></pre>
     * @since 1.1
     */
    @Parameter(name = "scripts")
    Script[] scripts;

    /**
     * A local CSS file that would be used instead of the default style, in case you want
     * to use a totally different layout.
     * @since 1.0
     */
    @Parameter(name = "cssFile", property = "docsite.cssFile")
    File cssFile;


    /**
     * Set whether the site uses CDN (Content Delivery Network) for some resources
     */
    @Parameter(name = "useCDN", defaultValue = "true", property = "docsite.useCDN")
    boolean useCDN;


    /**
     * The provided language translations for the site. You can also define the country
     *  flag representing the language using the expression <code>language:country</code>,
     *  being <code>country</code> a
     *  <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166-1 alpha-2</a> code.
     *  <p>
     *  First language would be the default.
     * @since 1.3
     *  */
    @Parameter(name = "languages", property = "docsite.languages")
    String[] languages;


    /**
     * The translations for section names and descriptions (given that
     * they are provided with keys instead of literals).
     * <p>
     * The content must abide by the following structure:
     * <localizations>
     *     <localization>
     *         <language>language-a</language>
     *         <values>
     *             <key-a>value-a</key-a>
     *             <key-b>value-b</key-b>
     *         </values>
     *     </localization>
     *     <localization>
     *         ...
     *     </localization>
     * </localizations>
     * @since 1.3
     * */
    @Parameter(name = "localizations")
    Localization[] localizations;



    /**
     * A JSON file containing the translations for section names and descriptions (given that
     * they are provided with keys instead of literals).
     * <p>
     * The content of the file must abide by the following structure:
     * <pre><code class="language-json">
     *     {
     *         "language-a": {
     *             "key-a":"translation-a",
     *             "key-b":"translation-b",
     *             ...
     *         },
     *         "language-b": {
     *           ...
     *         },
     *         ...
     *     }
     * </code></pre>
     * @since 1.3
     * */
    @Parameter(name = "localizationFile", property = "docsite.localizationFile")
    File localizationFile;


    @Parameter( defaultValue = "${project}", readonly = true )
    MavenProject project;

    @Parameter( defaultValue = "${session}", readonly = true )
    MavenSession session;



    protected Map<String,Map<String,String>> getLocalization() {
        if (this.localizations == null) {
            return null;
        }
        Map<String,Map<String,String>> map = new HashMap<>();
        for (var localization : this.localizations) {
            map.put(localization.language,localization.values);
        }
        return map;
    }

}