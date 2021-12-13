Docsite Maven Plugin
================================================================================

![GitHub](https://img.shields.io/github/license/luiinge/docsite-maven-plugin?style=plastic)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/luiinge/docsite-maven-plugin/quality%20check/master?style=plastic)
![Maven Central](https://img.shields.io/maven-central/v/io.github.luiinge/docsite-maven-plugin?style=plastic)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=ncloc)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=bugs)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=code_smells)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=luiinge_docsite-maven-plugin)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=luiinge_docsite-maven-plugin&metric=sqale_index)](https://sonarcloud.io/dashboard?id=luiinge_maven-docsite-maven-plugin)




Docsite is a Maven plugin that generates static documentation sites
with minimal effort. It is an alternative to the standard Maven site generation, 
providing the following benefits:
- No extra configuration files required
- Markdown and HTML document integration (including *Table of Content* generation)
- Not bound to any Maven reporting plugin
- Easily customizable
- Responsiveness
- Aimed to get good SEO positioning


Usage
-----------------------------------------------------------------------------------------

Site configuration
=========================================================================================
You can provide the documentation site configuration just after the declaration of 
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
                        <name></name>
                        <title></title>
                        <description></description>
                        <logo></logo>
                        <cssFile></cssFile>
                        <index></index>
                        <themeColors>
                            <menuRegularBackgroundColor></menuRegularBackgroundColor>
                            <menuBoldBackgroundColor></menuBoldBackgroundColor>
                            <menuForegroundColor></menuForegroundColor>
                            <menuDecorationColor></menuDecorationColor>
                            <guiElementColor></guiElementColor>
                        </themeColors>
                        <index></index>
                        <outputFolder></outputFolder>
                        <sections>
                            <section>
                                <name></name>
                                <icon></icon>
                                <source></source>
                                <type></type>
                                <siteIndex></siteIndex>
                                <subsections></subsections>
                                <replaceEmojis></replaceEmojis>
                            </section>
                            ...
                        </sections>
                    </docsite>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

```
### Site logo and section icons
For the main property `logo`, as well as the section property `icon`, you can choose among
three different sources according the property value:
- Starting with `http:` or `https:`, the image source would be an external link to the given URL
- Starting with `fa:`, `fas:`, `far:` or `fab:`, the image would be a 
[Font Awesome 5](http://https://fontawesome.com/v5.15/icons) icon
- Otherwise, a local image file would be expected

Auto-configured sites
=========================================================================================
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
created within a main report section
- In presence of the `target/site/dependencies.html` file, a subsection *Dependencies* would be
  created within a main report section
- In presence of the `target/site/surefire-report.html` file, a subsection *Test Results* would be
  created within a main report section
- In presence of the `target/site/jacoco` directory, a subsection *Coverage* would be
  created within a main report section
- If the `scm` section of the `pom.xml` file is filled, a link to the corresponding version control 
site would be added (with custom icons for Github, Gitlab and Bitbucket)

## Contributing

## Authors

- Luis Iñesta Gelabert - luiinge@gmail.com


License
-----------------------------------------------------------------------------------------
MIT License

Copyright (c) 2021 Luis Iñesta Gelabert - luiinge@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.



References
-----------------------------------------------------------------------------------------

- [**1**] *Apache Commons Configuration* - https://commons.apache.org/proper/commons-configuration

[1]:  https://commons.apache.org/proper/commons-configuration
