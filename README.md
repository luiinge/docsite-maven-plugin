Docsite Maven Plugin
================================================================================

![GitHub](https://img.shields.io/github/license/luiinge/docsite-maven-plugin?style=plastic)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/luiinge/docsite-maven-plugin/quality%20check/master?style=plastic)
![Maven Central](https://img.shields.io/maven-central/v/io.github.luiinge/docsite-maven-plugin?style=plastic)


Docsite is a Maven plugin that generates static documentation sites
with minimal effort. It is an alternative to the standard Maven site generation, 
providing the following benefits:
- No extra configuration files required
- Several source formats including Markdown, HTML, XML, JSON and YAMl
- Automatic generation of ToCs (*Table of Content*)
- Not bound to any Maven reporting plugin
- Easily customizable
- Responsiveness
- Usage of HTML5 semantic tags (aimed to get good SEO positioning)

> This very site has been generated using Docsite (check [the site](https://luiinge.github.io/docsite-maven-plugin/) 
in case you are reading this document from Github).

Usage
-----------------------------------------------------------------------------------------

1. Add the plugin to your `build` configuration in the `pom.xml` file:
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>io.github.luiinge</groupId>
                <artifactId>docsite-maven-plugin</artifactId>
                <version>1.0.0</version>
            </plugin>
        </plugins>
    </build>
```
2. Create the origin resources if necessary (such as Javadocs or some Maven reports)
3. Run the `docsite:generate` goal
```shell
mvn docsite:generate
```

You can also provide custom configuration just after the declaration of 
the plugin within the `pom`. 
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>io.github.luiinge</groupId>
                <artifactId>docsite-maven-plugin</artifactId>
                <version>1.0.0</version>
                <configuration>
                    <docsite>
                        ...
                        <sections>
                            <section>
                                ...
                            </section>
                        </sections>
                    </docsite>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

Check the [goal documentation](plugin-goals.html) for an exhaustive 
list of every configuration parameter.


### Documentation sections
Apart from the landing page, you can define different sections for the 
documentation site. Each section would generate its own page and also 
an entry in the navigation menu at the header.
Each section must define the type of strategy that would be used in order 
to generate the proper output. The available section types are:

#### Generated page
A single HTML page would be generated from a source file. Markdown,
HTML and raw text sources are processed directly, whereas XML, JSON and YAML sources
requires a [Freemarker](https://freemarker.apache.org/) transformation template.
```xml
<section>
    <type>generated</type>
    <name>Changelog</name>
    <source>CHANGELOG.md</source>
</section>
```
```xml
<section>
    <type>generated</type>
    <name>Dependencies</name>
    <source>target/dependencies.xml</source>
    <template>templates/dependencies.ftl</template>
</section>
```
#### Embedded site
A complete site (from a local folder) would be copied into the 
documentation site, and shown inside an embedded `iframe` component. 
```xml
<section>
    <type>embedded</type>
    <name>Javadoc</name>
    <source>target/site/apidocs</source>
    <siteIndex>index.html</siteIndex>
</section>
```

#### Section Group
A simple HTML page would be generated including links to other sections
of the documentation site. These linked sections are defined hierarchically in 
the `subsections` property of the section. Also, a dropdown menu would be used
in the navigation header.
```xml
<section>
    <type>group</type>
    <name>Reports</name>
    <subsections>
        <section>
            ...
        </section>
        <section>
            ...
        </section>
    </subsections>
</section>
```
 
#### Link
No specific page is generated, but rather a simple link in the navigation
header.
```xml
<section>
    <type>link</type>
    <name>Company Site</name>
    <source>http://my.company.com</source>
</section>
```





### Auto-configured sites
In absence of a site configuration, the plugin would generate one for you.
Although not fully compliant with every posible scenario, it would make an attempt 
to generate a good-enough site according the resources found in the project:

- The project name and description set in the `pom.xml` file would be used as the title in 
the site header
- In presence of a main `README.*` file, it would be used as the landing 
page of the site.
- In presence of a `CHANGELOG.*` file, a *Changelog* section would be created
- In presence of a `LICENSE` file, a *License* section would be created
- In presence of the `target/site/apidocs` directory, a subsection *Javadoc* would be 
created within the main report section
- In presence of the `target/site/dependencies.html` file, a subsection *Dependencies* would be
  created within the main report section
- In presence of the `target/site/surefire-report.html` file, a subsection *Test Results* would be
  created within the main report section
- In presence of the `target/site/jacoco` directory, a subsection *Coverage* would be
  created within the main report section
- In presence of the `target/classes/META-INF/maven/plugin.xml` (specific for Maven plugin
  projects), a subsection *Plugin Goals* would be created within the main report section.
- If the `scm` section of the `pom.xml` file is filled, a link to the corresponding version control 
site would be added (with custom icons for Github, Gitlab and Bitbucket)

The following configuration would be equivalent to the autogenerated configuration:

```xml
<docsite>
    <title>${project.name}</title>
    <description>${project.description}</description>
    <index>README.md</index>
    <sections>
        <section>
            <type>generated</type>
            <name>Changelog</name>
            <description>Log of modifications on new versions</description>
            <source>CHANGELOG.md</source>
            <icon>fas:clipboard-list</icon>
        </section>
        <section>
            <type>group</type>
            <name>Reports</name>
            <description>Collection of technical reports about this project</description>
            <icon>fas:file-medical-alt</icon>
            <subsections>
                <section>
                    <type>embedded</type>
                    <name>Javadoc</name>
                    <source>${project.reporting.outputDirectory}/apidocs</source>
                    <icon>fab:java</icon>
                </section>
                <section>
                    <type>generated</type>
                    <name>Plugin Goals</name>
                    <source>${project.build.outputDirectory}/classes/META-INF/maven/plugin.xml</source>
                    <template>maven-plugin-descriptor</template>
                </section>
                <section>
                    <type>generated</type>
                    <name>Test Results</name>
                    <source>${project.reporting.outputDirectory}/surefire-report.html</source>
                </section>
                <section>
                    <type>embedded</type>
                    <name>Test Coverage</name>
                    <source>${project.reporting.outputDirectory}/jacoco</source>
                </section>
                <section>
                    <type>generated</type>
                    <name>Dependencies</name>
                    <source>${project.reporting.outputDirectory}/dependencies.html</source>
                    <replaceEmojis>false</replaceEmojis>
                </section>
            </subsections>
        </section>
        <section>
            <type>link</type>
            <name>Source</name>
            <source>https://github.com/luiinge/docsite-maven-plugin</source>
            <icon>fab:github</icon>
        </section>
        <section>
            <type>generated</type>
            <name>License</name>
            <source>LICENSE</source>
            <icon>fas:balance-scale</icon>
        </section>
    </sections>
</docsite>
```


Other considerations
-----------------------------------------------------------------------------------------

### Icons
For the main property `logo`, as well as the section property `icon`, you can choose among
three different sources according the property value:
- Starting with `http:` or `https:`, the image source would be an external link to the given URL
- Starting with `fa:`, `fas:`, `far:` or `fab:`, the image would be a
  [Font Awesome 5](http://https://fontawesome.com/v5.15/icons) icon
- Otherwise, a local image file would be expected


### Github emojis
[Github](https://github.com) uses a mechanism that replaces specific text markups with icons when 
displaying 
certain documents. The pages generated with Docsite would apply the same transformations by 
default in order to emulate such representation. However, it is possible that some documents 
use those markups without any emoji intention and hence the generated page would contain 
unexpected images. If you experience this problem, simply disable the emoji replacement by setting 
to `false` the property `replaceEmojis` in the problematic section.

### About CDN resources
The websites generated with Docsite make use of CDN (Content Distribution Network) for 
some required resources ([Font Awesome 5](http://https://fontawesome.com/v5.15/icon)
and [Prism](https://prismjs.com/) scripts and stylesheets). This is highly advisable 
to improve loading time and reducing traffic. However, there might be specific scenarios 
where a local copy would be preferred. 
You can instruct Docsite to use a local copy of such resources setting to `false` the property 
`useCDN`.

### Maven lifecycle
In contrast with the default Maven site generation, that is based in the concept of pluggable
reports, Docsite *does not* require to be executed in the context of a Maven site. The `generate`
goal is attached to the `post-site` phase by default, but you are not forced to invoke the `site`
goal at all. Rather, you can redefine the phase or invoke `docsite:generate` at will.
The only consideration is that in most cases you may require the result of some other report that 
is intended to be used with `site`. Some report plugins have goals that can be executed 
standalone, but most require the `site` context.

### Using Docsite without Maven
This piece of software is designed as a Maven plugin, but actually that is a loose requirement. 
As any regular Jar file, you can include it (along with its dependencies) in your classpath and 
make use of the `DocsiteEmitter` class without a Maven executor. It is, though, still advisable
that you peek the implementation of `DocsiteMojo` in order to get the idea of the minimum setup
required.


Metrics
-----------------------------------------------------------------------------------------

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)


[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=ncloc)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)


[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)


[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=bugs)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)


[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=code_smells)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)


[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)


[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=sqale_index)](https://sonarcloud.io/dashboard?id=luiinge_maven-docsite-maven-plugin)


Authors
-----------------------------------------------------------------------------------------

- Luis Iñesta Gelabert  |  luiinge@gmail.com


Contributions
-----------------------------------------------------------------------------------------
If you want to contribute to this project, visit the
[Github project](https://github.com/luiinge/docsite-maven-plugin). You can open a new issue / feature
request, or make a pull request to consider. If your contribution is worthy, you will be added
as a contributor in this very page.

Issue reporting
-----------------------------------------------------------------------------------------
If you have found any defect in this software, please report it
in [Github project Issues](https://github.com/luiinge/docsite-maven-plugin/issues).
There is no guarantee that it would be fixed in the following version but it would
be addressed as soon as possible.   
 