Docsite Configuration Reference
================================================================================

The following describe the overall structure of a site configuration.
Further explanation of each section is provided below.

```xml
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
            <subsections>
                <section>...</section>
                ...
            </subsections>
        </section>
        ...
    </sections>
</docsite>
```

## `docsite` properties
### `name`
**Required**: no
### `title`
**Required**: no
### `description`
**Required**: no
### `logo`
**Required**: no
### `cssFile`
**Required**: no
### `index`
**Type**: path to file
**Required**: yes