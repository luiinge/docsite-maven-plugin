package docsite;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.junit.*;
import static docsite.Section.*;

public class DocSiteTest {

    @BeforeClass
    public static void setUp() {
        Logger.initialize(new Logger(
            Throwable::printStackTrace,
            System.out::println,
            System.out::println,
            System.out::println,
            System.err::println
        ));
    }


    
    

    
    @Test
    public void testMarkdownToHtml() throws IOException {

        Docsite docsite = new Docsite()
        .title("jExt - A Java library")
        .description("This is the description of the library")
        .logo("fab:accessible-icon")
        .companyLogo("src/test/resources/external-icon.png")
        .companyLink("http://company.com")
        .index("src/test/resources/README.md")
        .sections(List.of(
            generated("Changelog")
                .source("src/test/resources/CHANGELOG.md")
                .build(),

            generated("License")
                .source("src/test/resources/LICENSE")
                .build(),
            generated("License")
                .source("src/test/resources/LICENSE")
                .build(),
            group("Report 3")
                .subsections(List.of(
                    generated("Metrics 3")
                        .source("src/test/resources/metrics.md")
                        .build(),
                    generated("Dependencies 3")
                        .source("src/test/resources/dependencies.html")
                        .build(),
                    site()
                        .name("Javadoc 3")
                        .source("src/test/resources/apidocs")
                        .siteIndex("index.html")
                        .icon("fab:java")
                        .build()
                )).build(),
            generated("License")
                .source("src/test/resources/LICENSE")
                .build(),
            link()
                .name("Github")
                .source("https://github.com/luiinge/jext")
                .icon("fab:github")
                .build(),
            group("Report")
                .subsections(List.of(
                    generated("Metrics")
                        .source("src/test/resources/metrics.md")
                        .build(),
                    generated("Dependencies")
                        .source("src/test/resources/dependencies.html")
                        .build(),
                    site()
                        .name("Javadoc")
                        .source("src/test/resources/apidocs")
                        .siteIndex("index.html")
                        .icon("fab:java")
                        .build(),
                    group("More Reports")
                        .subsections(List.of(
                            generated("More Metrics")
                                .source("src/test/resources/metrics.md")
                                .build(),
                            generated("More Dependencies")
                                .source("src/test/resources/dependencies.html")
                                .build(),
                            site()
                                .name("More Javadoc")
                                .source("src/test/resources/apidocs")
                                .siteIndex("index.html")
                                .icon("fab:java")
                                .build()
                        )).build()
                )).build(),
            link()
                .name("Github")
                .source("https://github.com/luiinge/jext")
                .icon("fab:github")
                .build(),
            link()
                .name("Github")
                .source("https://github.com/luiinge/jext")
                .icon("fab:github")
                .build()
        ));

        testSiteGeneration(docsite,"default",true);
    }





    @Test
    public void testLocalization() throws IOException {

        Docsite docsite = new Docsite()
            .title("jExt - A Java library")
            .description("title.description")
            .logo("fab:accessible-icon")
            .companyLogo("src/test/resources/external-icon.png")
            .companyLink("http://company.com")
            .index("src/test/resources/README.md")
            .sections(List.of(
                generated("changelog")
                    .source("src/test/resources/CHANGELOG.md")
                    .build(),
                generated("License")
                    .source("src/test/resources/LICENSE")
                    .build(),
                group("Report")
                    .subsections(List.of(
                        generated("Metrics")
                            .source("src/test/resources/metrics.md")
                            .build(),
                        generated("Dependencies")
                            .source("src/test/resources/dependencies.html")
                            .build(),
                        site()
                            .name("Javadoc")
                            .source("src/test/resources/apidocs")
                            .siteIndex("index.html")
                            .icon("fab:java")
                            .build()
                    )).build()

            ));
        testSiteGeneration(docsite,"localization",true,List.of("en:English","es:Español"));
    }







    @Test
    public void testNoCDNl() throws IOException {

        Docsite docsite = new Docsite()
            .title("jExt - A Java library")
            .description("This is the description of the library")
            .logo("fab:accessible-icon")
            .index("src/test/resources/README.md")
            ;

        testSiteGeneration(docsite,"nocdn",false);
    }



    @Test
    public void testExternalCss() throws IOException {

        Docsite docsite = new Docsite()
            .title("jExt - A Java library")
            .description("This is the description of the library")
            .index("src/test/resources/README.md")
            ;

        testSiteGeneration(
            docsite,
            ThemeColors.DEFAULT,
            Path.of("src/test/resources/external-layout.css"),
            "externalCss",
            true,
            List.of()
        );
    }


    @Test
    public void testCustomColors() throws IOException {

        Docsite docsite = new Docsite()
            .title("jExt - A Java library")
            .description("This is the description of the library")
            .index("src/test/resources/README.md")
            ;

        ThemeColors themeColors = ThemeColors.builder()
            .menuRegularBackgroundColor("red")
            .menuBoldBackgroundColor("green")
            .menuForegroundColor("black")
            .menuDecorationColor("yellow")
            .guiElementColor("orange")
            .build();

        testSiteGeneration(docsite, themeColors, null, "colors", true, List.of());

    }


    @Test
    public void testExternalIcons() throws IOException {

        Docsite docsite = new Docsite()
            .title("jExt - A Java library")
            .logo("src/test/resources/external-icon.png")
            .favicon("src/test/resources/external-icon.png")
            .index("src/test/resources/README.md")
            .sections(List.of(
                link()
                    .name("Github")
                    .source("https://github.com/luiinge/jext")
                    .icon("fab:github")
                    .build(),
                link()
                    .name("Other link")
                    .source("https://github.com")
                    .icon("src/test/resources/external-icon.png")
                    .build(),
                link()
                    .name("SVG Icon")
                    .source("https://web.com")
                    .icon("data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcKICAgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIgogICB4bWxuczpjYz0iaHR0cDovL2NyZWF0aXZlY29tbW9ucy5vcmcvbnMjIgogICB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiCiAgIHhtbG5zOnN2Zz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciCiAgIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIKICAgeG1sbnM6c29kaXBvZGk9Imh0dHA6Ly9zb2RpcG9kaS5zb3VyY2Vmb3JnZS5uZXQvRFREL3NvZGlwb2RpLTAuZHRkIgogICB4bWxuczppbmtzY2FwZT0iaHR0cDovL3d3dy5pbmtzY2FwZS5vcmcvbmFtZXNwYWNlcy9pbmtzY2FwZSIKICAgd2lkdGg9IjYwLjE3MjQzNm1tIgogICBoZWlnaHQ9IjE3LjMwMjQ0MW1tIgogICB2aWV3Qm94PSIwIDAgNjAuMTcyNDM2IDE3LjMwMjQ0MSIKICAgdmVyc2lvbj0iMS4xIgogICBpZD0ic3ZnMTcwNiIKICAgaW5rc2NhcGU6dmVyc2lvbj0iMS4wLjIgKDEuMC4yK3I3NSsxKSIKICAgc29kaXBvZGk6ZG9jbmFtZT0ia3VrdW1vLXRpdHVsby5zdmciPgogIDxkZWZzCiAgICAgaWQ9ImRlZnMxNzAwIiAvPgogIDxzb2RpcG9kaTpuYW1lZHZpZXcKICAgICBpZD0iYmFzZSIKICAgICBwYWdlY29sb3I9IiNmZmZmZmYiCiAgICAgYm9yZGVyY29sb3I9IiM2NjY2NjYiCiAgICAgYm9yZGVyb3BhY2l0eT0iMS4wIgogICAgIGlua3NjYXBlOnBhZ2VvcGFjaXR5PSIwLjAiCiAgICAgaW5rc2NhcGU6cGFnZXNoYWRvdz0iMiIKICAgICBpbmtzY2FwZTp6b29tPSIxLjk3OTg5OSIKICAgICBpbmtzY2FwZTpjeD0iMTMzLjYwNDg3IgogICAgIGlua3NjYXBlOmN5PSI4MC42MTg4ODQiCiAgICAgaW5rc2NhcGU6ZG9jdW1lbnQtdW5pdHM9Im1tIgogICAgIGlua3NjYXBlOmN1cnJlbnQtbGF5ZXI9ImxheWVyMSIKICAgICBpbmtzY2FwZTpkb2N1bWVudC1yb3RhdGlvbj0iMCIKICAgICBzaG93Z3JpZD0iZmFsc2UiCiAgICAgZml0LW1hcmdpbi10b3A9IjAiCiAgICAgZml0LW1hcmdpbi1sZWZ0PSIwIgogICAgIGZpdC1tYXJnaW4tcmlnaHQ9IjAiCiAgICAgZml0LW1hcmdpbi1ib3R0b209IjAiCiAgICAgaW5rc2NhcGU6d2luZG93LXdpZHRoPSIxNDgzIgogICAgIGlua3NjYXBlOndpbmRvdy1oZWlnaHQ9Ijk2OSIKICAgICBpbmtzY2FwZTp3aW5kb3cteD0iMTkyMCIKICAgICBpbmtzY2FwZTp3aW5kb3cteT0iNDkiCiAgICAgaW5rc2NhcGU6d2luZG93LW1heGltaXplZD0iMCIgLz4KICA8bWV0YWRhdGEKICAgICBpZD0ibWV0YWRhdGExNzAzIj4KICAgIDxyZGY6UkRGPgogICAgICA8Y2M6V29yawogICAgICAgICByZGY6YWJvdXQ9IiI+CiAgICAgICAgPGRjOmZvcm1hdD5pbWFnZS9zdmcreG1sPC9kYzpmb3JtYXQ+CiAgICAgICAgPGRjOnR5cGUKICAgICAgICAgICByZGY6cmVzb3VyY2U9Imh0dHA6Ly9wdXJsLm9yZy9kYy9kY21pdHlwZS9TdGlsbEltYWdlIiAvPgogICAgICAgIDxkYzp0aXRsZT48L2RjOnRpdGxlPgogICAgICA8L2NjOldvcms+CiAgICA8L3JkZjpSREY+CiAgPC9tZXRhZGF0YT4KICA8ZwogICAgIGlua3NjYXBlOmxhYmVsPSJDYXBhIDEiCiAgICAgaW5rc2NhcGU6Z3JvdXBtb2RlPSJsYXllciIKICAgICBpZD0ibGF5ZXIxIgogICAgIHRyYW5zZm9ybT0idHJhbnNsYXRlKC0zOC44NDU0MTcsLTY4Ljk2MTE1OSkiPgogICAgPGcKICAgICAgIGlkPSJwYXRoMjI5NiIKICAgICAgIHN0eWxlPSJmb250LXN0eWxlOm5vcm1hbDtmb250LXZhcmlhbnQ6bm9ybWFsO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zdHJldGNoOm5vcm1hbDtmb250LXNpemU6MTAuNTgzM3B4O2xpbmUtaGVpZ2h0OjEuMjU7Zm9udC1mYW1pbHk6TnVuaXRvOy1pbmtzY2FwZS1mb250LXNwZWNpZmljYXRpb246J051bml0bywgQm9sZCc7Zm9udC12YXJpYW50LWxpZ2F0dXJlczpub3JtYWw7Zm9udC12YXJpYW50LWNhcHM6bm9ybWFsO2ZvbnQtdmFyaWFudC1udW1lcmljOm5vcm1hbDtmb250LXZhcmlhbnQtZWFzdC1hc2lhbjpub3JtYWw7d2hpdGUtc3BhY2U6cHJlO2ZpbGw6IzIyOGMzYTtmaWxsLW9wYWNpdHk6MTtzdHJva2U6IzBiNWUwNjtzdHJva2Utd2lkdGg6MC4yODY2MDU7c3Ryb2tlLWxpbmVjYXA6cm91bmQ7c3Ryb2tlLWxpbmVqb2luOnJvdW5kO3N0cm9rZS1taXRlcmxpbWl0OjQ7c3Ryb2tlLWRhc2hhcnJheTpub25lO3N0cm9rZS1vcGFjaXR5OjEiCiAgICAgICB0cmFuc2Zvcm09Im1hdHJpeCgxLjQ1Mzc2NjQsMCwwLDEuNjM1OTkyOSwxNy41NTU4MjYsLTQ4Ljc2NjI2MSkiPgogICAgICA8cGF0aAogICAgICAgICBzdHlsZT0iY29sb3I6IzAwMDAwMDtmb250LXN0eWxlOm5vcm1hbDtmb250LXZhcmlhbnQ6bm9ybWFsO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zdHJldGNoOm5vcm1hbDtmb250LXNpemU6MTAuNTgzM3B4O2xpbmUtaGVpZ2h0OjEuMjU7Zm9udC1mYW1pbHk6TnVuaXRvOy1pbmtzY2FwZS1mb250LXNwZWNpZmljYXRpb246J051bml0bywgQm9sZCc7Zm9udC12YXJpYW50LWxpZ2F0dXJlczpub3JtYWw7Zm9udC12YXJpYW50LXBvc2l0aW9uOm5vcm1hbDtmb250LXZhcmlhbnQtY2Fwczpub3JtYWw7Zm9udC12YXJpYW50LW51bWVyaWM6bm9ybWFsO2ZvbnQtdmFyaWFudC1hbHRlcm5hdGVzOm5vcm1hbDtmb250LXZhcmlhbnQtZWFzdC1hc2lhbjpub3JtYWw7Zm9udC1mZWF0dXJlLXNldHRpbmdzOm5vcm1hbDtmb250LXZhcmlhdGlvbi1zZXR0aW5nczpub3JtYWw7dGV4dC1pbmRlbnQ6MDt0ZXh0LWFsaWduOnN0YXJ0O3RleHQtZGVjb3JhdGlvbjpub25lO3RleHQtZGVjb3JhdGlvbi1saW5lOm5vbmU7dGV4dC1kZWNvcmF0aW9uLXN0eWxlOnNvbGlkO3RleHQtZGVjb3JhdGlvbi1jb2xvcjojMDAwMDAwO2xldHRlci1zcGFjaW5nOm5vcm1hbDt3b3JkLXNwYWNpbmc6bm9ybWFsO3RleHQtdHJhbnNmb3JtOm5vbmU7d3JpdGluZy1tb2RlOmxyLXRiO2RpcmVjdGlvbjpsdHI7dGV4dC1vcmllbnRhdGlvbjptaXhlZDtkb21pbmFudC1iYXNlbGluZTphdXRvO2Jhc2VsaW5lLXNoaWZ0OmJhc2VsaW5lO3RleHQtYW5jaG9yOnN0YXJ0O3doaXRlLXNwYWNlOm5vcm1hbDtzaGFwZS1wYWRkaW5nOjA7c2hhcGUtbWFyZ2luOjA7aW5saW5lLXNpemU6MDtjbGlwLXJ1bGU6bm9uemVybztkaXNwbGF5OmlubGluZTtvdmVyZmxvdzp2aXNpYmxlO3Zpc2liaWxpdHk6dmlzaWJsZTtpc29sYXRpb246YXV0bzttaXgtYmxlbmQtbW9kZTpub3JtYWw7Y29sb3ItaW50ZXJwb2xhdGlvbjpzUkdCO2NvbG9yLWludGVycG9sYXRpb24tZmlsdGVyczpsaW5lYXJSR0I7c29saWQtY29sb3I6IzAwMDAwMDtzb2xpZC1vcGFjaXR5OjE7dmVjdG9yLWVmZmVjdDpub25lO2ZpbGw6IzIyOGMzYTtmaWxsLW9wYWNpdHk6MTtmaWxsLXJ1bGU6bm9uemVybztzdHJva2U6bm9uZTtzdHJva2Utd2lkdGg6MC4yODY2MDU7c3Ryb2tlLWxpbmVjYXA6cm91bmQ7c3Ryb2tlLWxpbmVqb2luOnJvdW5kO3N0cm9rZS1taXRlcmxpbWl0OjQ7c3Ryb2tlLWRhc2hhcnJheTpub25lO3N0cm9rZS1kYXNob2Zmc2V0OjA7c3Ryb2tlLW9wYWNpdHk6MTtjb2xvci1yZW5kZXJpbmc6YXV0bztpbWFnZS1yZW5kZXJpbmc6YXV0bztzaGFwZS1yZW5kZXJpbmc6YXV0bzt0ZXh0LXJlbmRlcmluZzphdXRvO2VuYWJsZS1iYWNrZ3JvdW5kOmFjY3VtdWxhdGU7c3RvcC1jb2xvcjojMDAwMDAwO3N0b3Atb3BhY2l0eToxIgogICAgICAgICBkPSJtIDI1LjU3Njc4NCw4Mi4zMDYzMTUgYyAtMS4yNjk5OTYsMCAtMS45MDQ5OTQsLTAuNzEyNjA5IC0xLjkwNDk5NCwtMi4xMzc4MjYgViA3Ny42MDczMyBjIDAsLTAuNDQ0NDk5IDAuMjE4NzIxLC0wLjY2Njc0OCAwLjY1NjE2NCwtMC42NjY3NDggMC40NDQ0OTksMCAwLjY2Njc0OCwwLjIyMjI0OSAwLjY2Njc0OCwwLjY2Njc0OCB2IDIuNTgyMzI1IGMgMCwwLjM2Njg4OCAwLjA3NDA4LDAuNjM4NTI2IDAuMjIyMjQ5LDAuODE0OTE0IDAuMTQ4MTY3LDAuMTc2Mzg5IDAuMzg0NTI3LDAuMjY0NTgzIDAuNzA5MDgxLDAuMjY0NTgzIDAuMzUyNzc3LDAgMC42NDIwNTQsLTAuMTE5OTQ0IDAuODY3ODMxLC0wLjM1OTgzMiAwLjIyNTc3NywtMC4yNDY5NDQgMC4zMzg2NjYsLTAuNTcxNDk5IDAuMzM4NjY2LC0wLjk3MzY2NCBWIDc3LjYwNzMzIGMgMCwtMC40NDQ0OTkgMC4yMTg3MjEsLTAuNjY2NzQ4IDAuNjU2MTY0LC0wLjY2Njc0OCAwLjQ0NDQ5OSwwIDAuNjY2NzQ4LDAuMjIyMjQ5IDAuNjY2NzQ4LDAuNjY2NzQ4IHYgNC4wMDA0ODcgYyAwLDAuNDUxNTU0IC0wLjIxNTE5NCwwLjY3NzMzMSAtMC42NDU1ODEsMC42NzczMzEgLTAuNDMwMzg4LDAgLTAuNjQ1NTgyLC0wLjIyNTc3NyAtMC42NDU1ODIsLTAuNjc3MzMxIHYgLTAuMTc5OTE2IGMgLTAuMzQ1NzIxLDAuNTg1NjA5IC0wLjg3NDg4NSwwLjg3ODQxNCAtMS41ODc0OTQsMC44Nzg0MTQgeiIKICAgICAgICAgaWQ9InBhdGgyMzA5IiAvPgogICAgICA8cGF0aAogICAgICAgICBzdHlsZT0iY29sb3I6IzAwMDAwMDtmb250LXN0eWxlOm5vcm1hbDtmb250LXZhcmlhbnQ6bm9ybWFsO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zdHJldGNoOm5vcm1hbDtmb250LXNpemU6MTAuNTgzM3B4O2xpbmUtaGVpZ2h0OjEuMjU7Zm9udC1mYW1pbHk6TnVuaXRvOy1pbmtzY2FwZS1mb250LXNwZWNpZmljYXRpb246J051bml0bywgQm9sZCc7Zm9udC12YXJpYW50LWxpZ2F0dXJlczpub3JtYWw7Zm9udC12YXJpYW50LXBvc2l0aW9uOm5vcm1hbDtmb250LXZhcmlhbnQtY2Fwczpub3JtYWw7Zm9udC12YXJpYW50LW51bWVyaWM6bm9ybWFsO2ZvbnQtdmFyaWFudC1hbHRlcm5hdGVzOm5vcm1hbDtmb250LXZhcmlhbnQtZWFzdC1hc2lhbjpub3JtYWw7Zm9udC1mZWF0dXJlLXNldHRpbmdzOm5vcm1hbDtmb250LXZhcmlhdGlvbi1zZXR0aW5nczpub3JtYWw7dGV4dC1pbmRlbnQ6MDt0ZXh0LWFsaWduOnN0YXJ0O3RleHQtZGVjb3JhdGlvbjpub25lO3RleHQtZGVjb3JhdGlvbi1saW5lOm5vbmU7dGV4dC1kZWNvcmF0aW9uLXN0eWxlOnNvbGlkO3RleHQtZGVjb3JhdGlvbi1jb2xvcjojMDAwMDAwO2xldHRlci1zcGFjaW5nOm5vcm1hbDt3b3JkLXNwYWNpbmc6bm9ybWFsO3RleHQtdHJhbnNmb3JtOm5vbmU7d3JpdGluZy1tb2RlOmxyLXRiO2RpcmVjdGlvbjpsdHI7dGV4dC1vcmllbnRhdGlvbjptaXhlZDtkb21pbmFudC1iYXNlbGluZTphdXRvO2Jhc2VsaW5lLXNoaWZ0OmJhc2VsaW5lO3RleHQtYW5jaG9yOnN0YXJ0O3doaXRlLXNwYWNlOm5vcm1hbDtzaGFwZS1wYWRkaW5nOjA7c2hhcGUtbWFyZ2luOjA7aW5saW5lLXNpemU6MDtjbGlwLXJ1bGU6bm9uemVybztkaXNwbGF5OmlubGluZTtvdmVyZmxvdzp2aXNpYmxlO3Zpc2liaWxpdHk6dmlzaWJsZTtpc29sYXRpb246YXV0bzttaXgtYmxlbmQtbW9kZTpub3JtYWw7Y29sb3ItaW50ZXJwb2xhdGlvbjpzUkdCO2NvbG9yLWludGVycG9sYXRpb24tZmlsdGVyczpsaW5lYXJSR0I7c29saWQtY29sb3I6IzAwMDAwMDtzb2xpZC1vcGFjaXR5OjE7dmVjdG9yLWVmZmVjdDpub25lO2ZpbGw6IzBiNWUwNjtmaWxsLW9wYWNpdHk6MTtmaWxsLXJ1bGU6bm9uemVybztzdHJva2U6bm9uZTtzdHJva2UtbGluZWNhcDpyb3VuZDtzdHJva2UtbGluZWpvaW46cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eToxO2NvbG9yLXJlbmRlcmluZzphdXRvO2ltYWdlLXJlbmRlcmluZzphdXRvO3NoYXBlLXJlbmRlcmluZzphdXRvO3RleHQtcmVuZGVyaW5nOmF1dG87ZW5hYmxlLWJhY2tncm91bmQ6YWNjdW11bGF0ZTtzdG9wLWNvbG9yOiMwMDAwMDA7c3RvcC1vcGFjaXR5OjEiCiAgICAgICAgIGQ9Im0gMjQuMzI4MTI1LDc2Ljc5Njg3NSBjIC0wLjI0Mjk5LDAgLTAuNDUwODg4LDAuMDYzODIgLTAuNTkzNzUsMC4yMDg5ODQgLTAuMTQyODYyLDAuMTQ1MTY3IC0wLjIwNTA3OCwwLjM1NTgwOSAtMC4yMDUwNzgsMC42MDE1NjMgdiAyLjU2MDU0NyBjIDAsMC43MzM4OTEgMC4xNjI2NTEsMS4zMDc0ODcgMC41MTE3MTksMS42OTkyMTkgMC4zNDkwNjcsMC4zOTE3MzEgMC44NzMzNTUsMC41ODIwMzEgMS41MzUxNTYsMC41ODIwMzEgMC42MzE4OTYsMCAxLjExNjc0MSwtMC4yNzk0NjkgMS40ODQzNzUsLTAuNzIyNjU3IDAuMDE5NjUsMC4xODcxODMgMC4wNDkxMywwLjM2OTc0MyAwLjE2MjEwOSwwLjQ4ODI4MiAwLjE0MDUxNiwwLjE0NzQyNiAwLjM0NzYzOSwwLjIxMjg5IDAuNTg3ODkxLDAuMjEyODkgMC4yNDAyNTIsMCAwLjQ0NzM3NSwtMC4wNjU0NiAwLjU4Nzg5MSwtMC4yMTI4OSAwLjE0MDUxNSwtMC4xNDc0MjYgMC4yMDExNzEsLTAuMzU4ODgxIDAuMjAxMTcxLC0wLjYwNzQyMiB2IC00IGMgMCwtMC4yNDYxMzMgLTAuMDY0MDgsLTAuNDU2NjYxIC0wLjIwODk4NCwtMC42MDE1NjMgLTAuMTQ0OTAxLC0wLjE0NDkwMSAtMC4zNTU0MjksLTAuMjA4OTg0IC0wLjYwMTU2MywtMC4yMDg5ODQgLTAuMjQyOTksMCAtMC40NTA4ODgsMC4wNjM4MiAtMC41OTM3NSwwLjIwODk4NCAtMC4xNDI4NjEsMC4xNDUxNjcgLTAuMjA3MDMxLDAuMzU1ODA5IC0wLjIwNzAzMSwwLjYwMTU2MyB2IDIuMzI4MTI1IGMgMCwwLjM3NDM0NCAtMC4xMDAyNSwwLjY1Njg4NSAtMC4yOTg4MjgsMC44NzUgLTQuMzhlLTQsNC42NWUtNCAtMC4wMDE1LC00LjY0ZS00IC0wLjAwMiwwIC0zLjk3ZS00LDQuMzZlLTQgMy45OGUtNCwwLjAwMTUgMCwwLjAwMiAtMC4xOTk4OTIsMC4yMTE0MjQgLTAuNDQyNDEzLDAuMzEyNSAtMC43NjE3MTksMC4zMTI1IC0wLjI5ODU5MywwIC0wLjQ4NDU4NywtMC4wNzU5NiAtMC41OTk2MDksLTAuMjEyODkxIC0wLjExNjY5MywtMC4xMzg5MiAtMC4xODc1LC0wLjM3NTA1OSAtMC4xODc1LC0wLjcyMjY1NiB2IC0yLjU4MjAzMSBjIDAsLTAuMjQ2MTMzIC0wLjA2NDA4LC0wLjQ1NjY2MSAtMC4yMDg5ODQsLTAuNjAxNTYzIC0wLjE0NDkwMiwtMC4xNDQ5MDEgLTAuMzU1NDMsLTAuMjA4OTg0IC0wLjYwMTU2MywtMC4yMDg5ODQgeiBtIDAsMC4yODcxMDkgYyAwLjE5ODM2NiwwIDAuMzIxMDksMC4wNDc2NSAwLjM5ODQzNywwLjEyNSAwLjA3NzM1LDAuMDc3MzUgMC4xMjUwMDEsMC4yMDAwNzIgMC4xMjUsMC4zOTg0MzggdiAyLjU4MjAzMSBjIDAsMC4zODYxNzkgMC4wNzYyMiwwLjY5NDM0NyAwLjI1NTg2LDAuOTA4MjAzIDAuMTgxMzExLDAuMjE1ODQ3IDAuNDY3ODQ1LDAuMzE0NDUzIDAuODE4MzU5LDAuMzE0NDUzIDAuMzg1MjU5LDAgMC43MjEzNDksLTAuMTM3MjgzIDAuOTcyNjU3LC0wLjQwNDI5NyBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMiwtMC4wMDIgYyAwLjI1MTkxOCwtMC4yNzU1MzUgMC4zNzUsLTAuNjQxMzMgMC4zNzUsLTEuMDcwMzEyIHYgLTIuMzI4MTI1IGMgMCwtMC4xOTg3NDUgMC4wNDcxOSwtMC4zMjMzMDggMC4xMjMwNDcsLTAuNDAwMzkxIDAuMDc1ODYsLTAuMDc3MDggMC4xOTYxNzIsLTAuMTIzMDQ3IDAuMzkwNjI0LC0wLjEyMzA0NyAwLjE5ODM2NiwwIDAuMzIxMDksMC4wNDc2NSAwLjM5ODQzOCwwLjEyNSAwLjA3NzM1LDAuMDc3MzUgMC4xMjUsMC4yMDAwNzIgMC4xMjUsMC4zOTg0MzggdiA0IGMgMCwwLjIwMzAxMyAtMC4wNDgzNywwLjMzMTgwNSAtMC4xMjMwNDcsMC40MTAxNTYgLTAuMDc0NjgsMC4wNzgzNSAtMC4xODg3NzEsMC4xMjUgLTAuMzc4OTA2LDAuMTI1IC0wLjE5MDEzNiwwIC0wLjMwNjE4MSwtMC4wNDY2NSAtMC4zODA4NTksLTAuMTI1IC0wLjA3NDY4LC0wLjA3ODM1IC0wLjEyMzA0NywtMC4yMDcxNDMgLTAuMTIzMDQ3LC0wLjQxMDE1NiB2IC0wLjE3OTY4OCBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAtMC4yNjU2MjUsLTAuMDcyMjcgYyAtMC4zMjM4MDgsMC41NDg0OTEgLTAuNzkxNDksMC44MDY2NCAtMS40NjQ4NDQsMC44MDY2NCAtMC42MDgxOTUsMCAtMS4wMzQzODIsLTAuMTY1NDUxIC0xLjMyMDMxMywtMC40ODYzMjggQyAyMy45Njk5NzYsODEuMzU0ODUyIDIzLjgxNDUsODAuODU5MjQzIDIzLjgxNDUsODAuMTY3OTE3IFYgNzcuNjA3MzcgYyAwLC0wLjE5ODc0NSAwLjA0NzE5LC0wLjMyMzMwOCAwLjEyMzA0NywtMC40MDAzOTEgMC4wNzU4NiwtMC4wNzcwOCAwLjE5NjE3MywtMC4xMjMwNDcgMC4zOTA2MjUsLTAuMTIzMDQ3IHoiCiAgICAgICAgIGlkPSJwYXRoMjMxMSIgLz4KICAgIDwvZz4KICAgIDxnCiAgICAgICBpZD0icGF0aDIyOTgiCiAgICAgICBzdHlsZT0iZm9udC1zdHlsZTpub3JtYWw7Zm9udC12YXJpYW50Om5vcm1hbDtmb250LXdlaWdodDpib2xkO2ZvbnQtc3RyZXRjaDpub3JtYWw7Zm9udC1zaXplOjEwLjU4MzNweDtsaW5lLWhlaWdodDoxLjI1O2ZvbnQtZmFtaWx5Ok51bml0bzstaW5rc2NhcGUtZm9udC1zcGVjaWZpY2F0aW9uOidOdW5pdG8sIEJvbGQnO2ZvbnQtdmFyaWFudC1saWdhdHVyZXM6bm9ybWFsO2ZvbnQtdmFyaWFudC1jYXBzOm5vcm1hbDtmb250LXZhcmlhbnQtbnVtZXJpYzpub3JtYWw7Zm9udC12YXJpYW50LWVhc3QtYXNpYW46bm9ybWFsO3doaXRlLXNwYWNlOnByZTtmaWxsOiMyMjhjM2E7ZmlsbC1vcGFjaXR5OjE7c3Ryb2tlOiMwYjVlMDY7c3Ryb2tlLXdpZHRoOjAuMjg2NjA1O3N0cm9rZS1saW5lY2FwOnJvdW5kO3N0cm9rZS1saW5lam9pbjpyb3VuZDtzdHJva2UtbWl0ZXJsaW1pdDo0O3N0cm9rZS1kYXNoYXJyYXk6bm9uZTtzdHJva2Utb3BhY2l0eToxIgogICAgICAgdHJhbnNmb3JtPSJtYXRyaXgoMS40NTM3NjY0LDAsMCwxLjYzNTk5MjksMTcuNTU1ODI2LC00OC43NjYyNjEpIj4KICAgICAgPHBhdGgKICAgICAgICAgc3R5bGU9ImNvbG9yOiMwMDAwMDA7Zm9udC1zdHlsZTpub3JtYWw7Zm9udC12YXJpYW50Om5vcm1hbDtmb250LXdlaWdodDpib2xkO2ZvbnQtc3RyZXRjaDpub3JtYWw7Zm9udC1zaXplOjEwLjU4MzNweDtsaW5lLWhlaWdodDoxLjI1O2ZvbnQtZmFtaWx5Ok51bml0bzstaW5rc2NhcGUtZm9udC1zcGVjaWZpY2F0aW9uOidOdW5pdG8sIEJvbGQnO2ZvbnQtdmFyaWFudC1saWdhdHVyZXM6bm9ybWFsO2ZvbnQtdmFyaWFudC1wb3NpdGlvbjpub3JtYWw7Zm9udC12YXJpYW50LWNhcHM6bm9ybWFsO2ZvbnQtdmFyaWFudC1udW1lcmljOm5vcm1hbDtmb250LXZhcmlhbnQtYWx0ZXJuYXRlczpub3JtYWw7Zm9udC12YXJpYW50LWVhc3QtYXNpYW46bm9ybWFsO2ZvbnQtZmVhdHVyZS1zZXR0aW5nczpub3JtYWw7Zm9udC12YXJpYXRpb24tc2V0dGluZ3M6bm9ybWFsO3RleHQtaW5kZW50OjA7dGV4dC1hbGlnbjpzdGFydDt0ZXh0LWRlY29yYXRpb246bm9uZTt0ZXh0LWRlY29yYXRpb24tbGluZTpub25lO3RleHQtZGVjb3JhdGlvbi1zdHlsZTpzb2xpZDt0ZXh0LWRlY29yYXRpb24tY29sb3I6IzAwMDAwMDtsZXR0ZXItc3BhY2luZzpub3JtYWw7d29yZC1zcGFjaW5nOm5vcm1hbDt0ZXh0LXRyYW5zZm9ybTpub25lO3dyaXRpbmctbW9kZTpsci10YjtkaXJlY3Rpb246bHRyO3RleHQtb3JpZW50YXRpb246bWl4ZWQ7ZG9taW5hbnQtYmFzZWxpbmU6YXV0bztiYXNlbGluZS1zaGlmdDpiYXNlbGluZTt0ZXh0LWFuY2hvcjpzdGFydDt3aGl0ZS1zcGFjZTpub3JtYWw7c2hhcGUtcGFkZGluZzowO3NoYXBlLW1hcmdpbjowO2lubGluZS1zaXplOjA7Y2xpcC1ydWxlOm5vbnplcm87ZGlzcGxheTppbmxpbmU7b3ZlcmZsb3c6dmlzaWJsZTt2aXNpYmlsaXR5OnZpc2libGU7aXNvbGF0aW9uOmF1dG87bWl4LWJsZW5kLW1vZGU6bm9ybWFsO2NvbG9yLWludGVycG9sYXRpb246c1JHQjtjb2xvci1pbnRlcnBvbGF0aW9uLWZpbHRlcnM6bGluZWFyUkdCO3NvbGlkLWNvbG9yOiMwMDAwMDA7c29saWQtb3BhY2l0eToxO3ZlY3Rvci1lZmZlY3Q6bm9uZTtmaWxsOiMyMjhjM2E7ZmlsbC1vcGFjaXR5OjE7ZmlsbC1ydWxlOm5vbnplcm87c3Ryb2tlOm5vbmU7c3Ryb2tlLXdpZHRoOjAuMjg2NjA1O3N0cm9rZS1saW5lY2FwOnJvdW5kO3N0cm9rZS1saW5lam9pbjpyb3VuZDtzdHJva2UtbWl0ZXJsaW1pdDo0O3N0cm9rZS1kYXNoYXJyYXk6bm9uZTtzdHJva2UtZGFzaG9mZnNldDowO3N0cm9rZS1vcGFjaXR5OjE7Y29sb3ItcmVuZGVyaW5nOmF1dG87aW1hZ2UtcmVuZGVyaW5nOmF1dG87c2hhcGUtcmVuZGVyaW5nOmF1dG87dGV4dC1yZW5kZXJpbmc6YXV0bztlbmFibGUtYmFja2dyb3VuZDphY2N1bXVsYXRlO3N0b3AtY29sb3I6IzAwMDAwMDtzdG9wLW9wYWNpdHk6MSIKICAgICAgICAgZD0ibSAzMC40NzgxNzQsODIuMjg1MTQ4IGMgLTAuNDM3NDQzLDAgLTAuNjU2MTY0LC0wLjIyNTc3NyAtMC42NTYxNjQsLTAuNjc3MzMxIHYgLTYuMjg2NDggYyAwLC0wLjQ0NDQ5OSAwLjIxODcyMSwtMC42NjY3NDggMC42NTYxNjQsLTAuNjY2NzQ4IDAuNDQ0NDk5LDAgMC42NjY3NDgsMC4yMjIyNDkgMC42NjY3NDgsMC42NjY3NDggdiAzLjkwNTIzOCBoIDAuMDIxMTcgbCAxLjc0NjI0NCwtMS44NDE0OTQgYyAwLjE0MTExMSwtMC4xNDExMTEgMC4yNjQ1ODMsLTAuMjUwNDcyIDAuMzcwNDE2LC0wLjMyODA4MyAwLjEwNTgzMywtMC4wNzc2MSAwLjI1MDQ3MSwtMC4xMTY0MTYgMC40MzM5MTUsLTAuMTE2NDE2IDAuMTgzNDQ0LDAgMC4zMjEwMjcsMC4wNDkzOSAwLjQxMjc0OSwwLjE0ODE2NiAwLjA5ODc4LDAuMDkxNzIgMC4xNDgxNjYsMC4yMDQ2MTEgMC4xNDgxNjYsMC4zMzg2NjYgMCwwLjEzNDA1NSAtMC4wNjM1LDAuMjY4MTEgLTAuMTkwNSwwLjQwMjE2NSBsIC0xLjU4NzQ5NSwxLjY3MjE2MiAxLjc2NzQxMiwxLjkwNDk5NCBjIDAuMTI2OTk5LDAuMTM0MDU1IDAuMTgzNDQzLDAuMjcxNjM3IDAuMTY5MzMyLDAuNDEyNzQ4IC0wLjAwNzEsMC4xMzQwNTUgLTAuMDYzNSwwLjI0Njk0NCAtMC4xNjkzMzIsMC4zMzg2NjYgLTAuMTA1ODMzLDAuMDg0NjcgLTAuMjM5ODg5LDAuMTI2OTk5IC0wLjQwMjE2NiwwLjEyNjk5OSAtMC4xOTc1NTUsMCAtMC4zNTYzMDQsLTAuMDM4ODEgLTAuNDc2MjQ4LC0wLjExNjQxNiAtMC4xMTI4ODksLTAuMDc3NjEgLTAuMjM5ODg4LC0wLjE5NDAyNyAtMC4zODA5OTksLTAuMzQ5MjQ5IGwgLTEuODQxNDk0LC0xLjkyNjE2IGggLTAuMDIxMTcgdiAxLjcxNDQ5NCBjIDAsMC40NTE1NTQgLTAuMjIyMjQ5LDAuNjc3MzMxIC0wLjY2Njc0OCwwLjY3NzMzMSB6IgogICAgICAgICBpZD0icGF0aDIzMTUiIC8+CiAgICAgIDxwYXRoCiAgICAgICAgIHN0eWxlPSJjb2xvcjojMDAwMDAwO2ZvbnQtc3R5bGU6bm9ybWFsO2ZvbnQtdmFyaWFudDpub3JtYWw7Zm9udC13ZWlnaHQ6Ym9sZDtmb250LXN0cmV0Y2g6bm9ybWFsO2ZvbnQtc2l6ZToxMC41ODMzcHg7bGluZS1oZWlnaHQ6MS4yNTtmb250LWZhbWlseTpOdW5pdG87LWlua3NjYXBlLWZvbnQtc3BlY2lmaWNhdGlvbjonTnVuaXRvLCBCb2xkJztmb250LXZhcmlhbnQtbGlnYXR1cmVzOm5vcm1hbDtmb250LXZhcmlhbnQtcG9zaXRpb246bm9ybWFsO2ZvbnQtdmFyaWFudC1jYXBzOm5vcm1hbDtmb250LXZhcmlhbnQtbnVtZXJpYzpub3JtYWw7Zm9udC12YXJpYW50LWFsdGVybmF0ZXM6bm9ybWFsO2ZvbnQtdmFyaWFudC1lYXN0LWFzaWFuOm5vcm1hbDtmb250LWZlYXR1cmUtc2V0dGluZ3M6bm9ybWFsO2ZvbnQtdmFyaWF0aW9uLXNldHRpbmdzOm5vcm1hbDt0ZXh0LWluZGVudDowO3RleHQtYWxpZ246c3RhcnQ7dGV4dC1kZWNvcmF0aW9uOm5vbmU7dGV4dC1kZWNvcmF0aW9uLWxpbmU6bm9uZTt0ZXh0LWRlY29yYXRpb24tc3R5bGU6c29saWQ7dGV4dC1kZWNvcmF0aW9uLWNvbG9yOiMwMDAwMDA7bGV0dGVyLXNwYWNpbmc6bm9ybWFsO3dvcmQtc3BhY2luZzpub3JtYWw7dGV4dC10cmFuc2Zvcm06bm9uZTt3cml0aW5nLW1vZGU6bHItdGI7ZGlyZWN0aW9uOmx0cjt0ZXh0LW9yaWVudGF0aW9uOm1peGVkO2RvbWluYW50LWJhc2VsaW5lOmF1dG87YmFzZWxpbmUtc2hpZnQ6YmFzZWxpbmU7dGV4dC1hbmNob3I6c3RhcnQ7d2hpdGUtc3BhY2U6bm9ybWFsO3NoYXBlLXBhZGRpbmc6MDtzaGFwZS1tYXJnaW46MDtpbmxpbmUtc2l6ZTowO2NsaXAtcnVsZTpub256ZXJvO2Rpc3BsYXk6aW5saW5lO292ZXJmbG93OnZpc2libGU7dmlzaWJpbGl0eTp2aXNpYmxlO2lzb2xhdGlvbjphdXRvO21peC1ibGVuZC1tb2RlOm5vcm1hbDtjb2xvci1pbnRlcnBvbGF0aW9uOnNSR0I7Y29sb3ItaW50ZXJwb2xhdGlvbi1maWx0ZXJzOmxpbmVhclJHQjtzb2xpZC1jb2xvcjojMDAwMDAwO3NvbGlkLW9wYWNpdHk6MTt2ZWN0b3ItZWZmZWN0Om5vbmU7ZmlsbDojMGI1ZTA2O2ZpbGwtb3BhY2l0eToxO2ZpbGwtcnVsZTpub256ZXJvO3N0cm9rZTpub25lO3N0cm9rZS1saW5lY2FwOnJvdW5kO3N0cm9rZS1saW5lam9pbjpyb3VuZDtzdHJva2UtbWl0ZXJsaW1pdDo0O3N0cm9rZS1kYXNoYXJyYXk6bm9uZTtzdHJva2UtZGFzaG9mZnNldDowO3N0cm9rZS1vcGFjaXR5OjE7Y29sb3ItcmVuZGVyaW5nOmF1dG87aW1hZ2UtcmVuZGVyaW5nOmF1dG87c2hhcGUtcmVuZGVyaW5nOmF1dG87dGV4dC1yZW5kZXJpbmc6YXV0bztlbmFibGUtYmFja2dyb3VuZDphY2N1bXVsYXRlO3N0b3AtY29sb3I6IzAwMDAwMDtzdG9wLW9wYWNpdHk6MSIKICAgICAgICAgZD0ibSAzMC40Nzg1MTYsNzQuNTExNzE5IGMgLTAuMjQyOTkxLDAgLTAuNDUwODg5LDAuMDYzODIgLTAuNTkzNzUsMC4yMDg5ODQgLTAuMTQyODYyLDAuMTQ1MTY2IC0wLjIwNzAzMiwwLjM1NTgwOSAtMC4yMDcwMzIsMC42MDE1NjMgdiA2LjI4NTE1NiBjIDAsMC4yNDg5MTQgMC4wNjI1LDAuNDYwMjQyIDAuMjA1MDc4LDAuNjA3NDIyIDAuMTQyNTgxLDAuMTQ3MTggMC4zNTIzMjgsMC4yMTI4OSAwLjU5NTcwNCwwLjIxMjg5IDAuMjQ2NTEyLDAgMC40NTY5MzIsLTAuMDY0MDEgMC42MDE1NjIsLTAuMjEwOTM3IDAuMTQ0NjMsLTAuMTQ2OTI2IDAuMjA4OTg0LC0wLjM2MDA4OCAwLjIwODk4NCwtMC42MDkzNzUgdiAtMS4zNzg5MDYgbCAxLjYxNTIzNSwxLjY4OTQ1MyBjIDAuMTQ1MDQ5LDAuMTU5MzIxIDAuMjc2MzEsMC4yODI0OTIgMC40MDIzNDQsMC4zNjkxNCBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMzksMC4wMDIgYyAwLjE1MDA3MSwwLjA5NzEgMC4zMzgzNjcsMC4xMzg2NzIgMC41NTQ2ODcsMC4xMzg2NzIgMC4xODcyLDAgMC4zNTg0NTUsLTAuMDUxMjIgMC40OTIxODgsLTAuMTU4MjAzIGEgMC4xNDMzMTY4MywwLjE0MzMxNjgzIDAgMCAwIDAuMDAzOSwtMC4wMDM5IGMgMC4xMjkxMjYsLTAuMTExOTA4IDAuMjA2MjUxLC0wLjI2NjE1MyAwLjIxNjc5NywtMC40MzM1OTQgMC4wMTgxLC0wLjE4NzMwMSAtMC4wNjE0MSwtMC4zNjk3MzEgLTAuMjA3MDMxLC0wLjUyMzQzNyBMIDMyLjY5NTMsNzkuNTAyMDA2IDM0LjE5MTM5NCw3Ny45Mjc3ODcgYyAwLjE0MzQwMiwtMC4xNTEzNjcgMC4yMjg1MTYsLTAuMzIwNjkxIDAuMjI4NTE2LC0wLjUgMCwtMC4xNjcwOTkgLTAuMDcwMDcsLTAuMzIyNDY4IC0wLjE5MTQwNiwtMC40Mzc1IC0wLjEyNDA3OCwtMC4xMzA1NzUgLTAuMzA0ODg4LC0wLjE5MzM1OSAtMC41MTE3MTksLTAuMTkzMzU5IC0wLjIwMzY1MywwIC0wLjM4MTg1OCwwLjA0MzU3IC0wLjUxOTUzMSwwLjE0NDUzMSAtMC4xMTQ3NCwwLjA4NDE0IC0wLjI0MjIzMSwwLjE5NzMwOSAtMC4zODY3MTksMC4zNDE3OTcgYSAwLjE0MzMxNjgzLDAuMTQzMzE2ODMgMCAwIDAgLTAuMDAyLDAuMDAzOSBsIC0xLjUxOTUzMiwxLjYwMzUxNiB2IC0zLjU2ODM1OSBjIDAsLTAuMjQ2MTMzIC0wLjA2NDA4LC0wLjQ1NjY2MSAtMC4yMDg5ODQsLTAuNjAxNTYzIC0wLjE0NDkwMSwtMC4xNDQ5MDEgLTAuMzU1NDI5LC0wLjIwODk4NCAtMC42MDE1NjIsLTAuMjA4OTg0IHogbSAwLDAuMjg3MTA5IGMgMC4xOTgzNjUsMCAwLjMyMTA4OSwwLjA0NTcgMC4zOTg0MzcsMC4xMjMwNDcgMC4wNzczNSwwLjA3NzM1IDAuMTI1LDAuMjAyMDI1IDAuMTI1LDAuNDAwMzkxIHYgMy45MDQyOTYgYSAwLjE0MzMxNjgzLDAuMTQzMzE2ODMgMCAwIDAgMC4xNDI1NzgsMC4xNDI1NzkgaCAwLjAyMTQ4IGEgMC4xNDMzMTY4MywwLjE0MzMxNjgzIDAgMCAwIDAuMTAzNTE1LC0wLjA0NDkyIGwgMS43NDYwOTQsLTEuODM5ODQ0IGMgMC4xMzY3MTUsLTAuMTM2NTUxIDAuMjU1MiwtMC4yNDE4MzQgMC4zNTE1NjMsLTAuMzEyNSAwLjA3Mzk5LC0wLjA1NDI2IDAuMTg2Mzc0LC0wLjA4Nzg5IDAuMzQ5NjA5LC0wLjA4Nzg5IDAuMTU3NzIzLDAgMC4yNDczMzMsMC4wMzc2OSAwLjMwNjY0MSwwLjEwMTU2MyBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwNzgsMC4wMDc4IGMgMC4wNzMyNywwLjA2ODAzIDAuMTAzNTE2LDAuMTM1NTE3IDAuMTAzNTE2LDAuMjM0Mzc1IDAsMC4wODg4IC0wLjA0MTc1LDAuMTg1OTkzIC0wLjE1MjM0NCwwLjMwMjczNSBsIC0xLjU4NTkzOCwxLjY3MTg3NSBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAtMC4wMDIsMC4xOTcyNjUgbCAxLjc2NzU3OCwxLjkwNDI5NyBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLDAuMDAyIGMgMC4xMDc5MTIsMC4xMTM5MDcgMC4xNDAyODksMC4yMDQ1NDIgMC4xMzA4NiwwLjI5ODgyOSBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLDAuMDA3OCBjIC0wLjAwNTEsMC4wOTY2IC0wLjAzOTExLDAuMTY1NDEgLTAuMTE3MTg4LDAuMjM0Mzc1IC0wLjA3NzcxLDAuMDYxNjYgLTAuMTczOTc2LDAuMDk1NyAtMC4zMTA1NDcsMC4wOTU3IC0wLjE3NjkzMiwwIC0wLjMwNDgyNCwtMC4wMzQ3NSAtMC4zOTQ1MzEsLTAuMDkxOCBsIC0wLjAwMzksLTAuMDAyIGMgLTAuMDk4NjIsLTAuMDY4MjUgLTAuMjE4MzgzLC0wLjE3NzUyNSAtMC4zNTM1MTYsLTAuMzI2MTcyIGEgMC4xNDMzMTY4MywwLjE0MzMxNjgzIDAgMCAwIC0wLjAwMiwtMC4wMDIgTCAzMS4yNjk1MzEsNzkuNzk0OTIyIEEgMC4xNDMzMTY4MywwLjE0MzMxNjgzIDAgMCAwIDMxLjE2NjAxNiw3OS43NSBoIC0wLjAyMTQ4IGEgMC4xNDMzMTY4MywwLjE0MzMxNjgzIDAgMCAwIC0wLjE0MjU3OCwwLjE0MjU3OCB2IDEuNzE0ODQ0IGMgMCwwLjIwMjI2NiAtMC4wNDczOCwwLjMyOTM1MiAtMC4xMjUsMC40MDgyMDMgLTAuMDc3NjIsMC4wNzg4NSAtMC4yMDA0NTEsMC4xMjY5NTMgLTAuMzk4NDM3LDAuMTI2OTUzIC0wLjE5NDA2NywwIC0wLjMxMjUzMSwtMC4wNDgzNiAtMC4zODg2NzIsLTAuMTI2OTUzIC0wLjA3NjE0LC0wLjA3ODYgLTAuMTI1LC0wLjIwNTU2MyAtMC4xMjUsLTAuNDA4MjAzIHYgLTYuMjg1MTU2IGMgMCwtMC4xOTg3NDUgMC4wNDcxOSwtMC4zMjMzMDggMC4xMjMwNDcsLTAuNDAwMzkxIDAuMDc1ODYsLTAuMDc3MDggMC4xOTYxNzIsLTAuMTIzMDQ3IDAuMzkwNjI1LC0wLjEyMzA0NyB6IgogICAgICAgICBpZD0icGF0aDIzMTciIC8+CiAgICA8L2c+CiAgICA8ZwogICAgICAgaWQ9InBhdGgyMzAwIgogICAgICAgc3R5bGU9ImZvbnQtc3R5bGU6bm9ybWFsO2ZvbnQtdmFyaWFudDpub3JtYWw7Zm9udC13ZWlnaHQ6Ym9sZDtmb250LXN0cmV0Y2g6bm9ybWFsO2ZvbnQtc2l6ZToxMC41ODMzcHg7bGluZS1oZWlnaHQ6MS4yNTtmb250LWZhbWlseTpOdW5pdG87LWlua3NjYXBlLWZvbnQtc3BlY2lmaWNhdGlvbjonTnVuaXRvLCBCb2xkJztmb250LXZhcmlhbnQtbGlnYXR1cmVzOm5vcm1hbDtmb250LXZhcmlhbnQtY2Fwczpub3JtYWw7Zm9udC12YXJpYW50LW51bWVyaWM6bm9ybWFsO2ZvbnQtdmFyaWFudC1lYXN0LWFzaWFuOm5vcm1hbDt3aGl0ZS1zcGFjZTpwcmU7ZmlsbDojMjI4YzNhO2ZpbGwtb3BhY2l0eToxO3N0cm9rZTojMGI1ZTA2O3N0cm9rZS13aWR0aDowLjI4NjYwNTtzdHJva2UtbGluZWNhcDpyb3VuZDtzdHJva2UtbGluZWpvaW46cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLW9wYWNpdHk6MSIKICAgICAgIHRyYW5zZm9ybT0ibWF0cml4KDEuNDUzNzY2NCwwLDAsMS42MzU5OTI5LDE3LjU1NTgyNiwtNDguNzY2MjYxKSI+CiAgICAgIDxwYXRoCiAgICAgICAgIHN0eWxlPSJjb2xvcjojMDAwMDAwO2ZvbnQtc3R5bGU6bm9ybWFsO2ZvbnQtdmFyaWFudDpub3JtYWw7Zm9udC13ZWlnaHQ6Ym9sZDtmb250LXN0cmV0Y2g6bm9ybWFsO2ZvbnQtc2l6ZToxMC41ODMzcHg7bGluZS1oZWlnaHQ6MS4yNTtmb250LWZhbWlseTpOdW5pdG87LWlua3NjYXBlLWZvbnQtc3BlY2lmaWNhdGlvbjonTnVuaXRvLCBCb2xkJztmb250LXZhcmlhbnQtbGlnYXR1cmVzOm5vcm1hbDtmb250LXZhcmlhbnQtcG9zaXRpb246bm9ybWFsO2ZvbnQtdmFyaWFudC1jYXBzOm5vcm1hbDtmb250LXZhcmlhbnQtbnVtZXJpYzpub3JtYWw7Zm9udC12YXJpYW50LWFsdGVybmF0ZXM6bm9ybWFsO2ZvbnQtdmFyaWFudC1lYXN0LWFzaWFuOm5vcm1hbDtmb250LWZlYXR1cmUtc2V0dGluZ3M6bm9ybWFsO2ZvbnQtdmFyaWF0aW9uLXNldHRpbmdzOm5vcm1hbDt0ZXh0LWluZGVudDowO3RleHQtYWxpZ246c3RhcnQ7dGV4dC1kZWNvcmF0aW9uOm5vbmU7dGV4dC1kZWNvcmF0aW9uLWxpbmU6bm9uZTt0ZXh0LWRlY29yYXRpb24tc3R5bGU6c29saWQ7dGV4dC1kZWNvcmF0aW9uLWNvbG9yOiMwMDAwMDA7bGV0dGVyLXNwYWNpbmc6bm9ybWFsO3dvcmQtc3BhY2luZzpub3JtYWw7dGV4dC10cmFuc2Zvcm06bm9uZTt3cml0aW5nLW1vZGU6bHItdGI7ZGlyZWN0aW9uOmx0cjt0ZXh0LW9yaWVudGF0aW9uOm1peGVkO2RvbWluYW50LWJhc2VsaW5lOmF1dG87YmFzZWxpbmUtc2hpZnQ6YmFzZWxpbmU7dGV4dC1hbmNob3I6c3RhcnQ7d2hpdGUtc3BhY2U6bm9ybWFsO3NoYXBlLXBhZGRpbmc6MDtzaGFwZS1tYXJnaW46MDtpbmxpbmUtc2l6ZTowO2NsaXAtcnVsZTpub256ZXJvO2Rpc3BsYXk6aW5saW5lO292ZXJmbG93OnZpc2libGU7dmlzaWJpbGl0eTp2aXNpYmxlO2lzb2xhdGlvbjphdXRvO21peC1ibGVuZC1tb2RlOm5vcm1hbDtjb2xvci1pbnRlcnBvbGF0aW9uOnNSR0I7Y29sb3ItaW50ZXJwb2xhdGlvbi1maWx0ZXJzOmxpbmVhclJHQjtzb2xpZC1jb2xvcjojMDAwMDAwO3NvbGlkLW9wYWNpdHk6MTt2ZWN0b3ItZWZmZWN0Om5vbmU7ZmlsbDojMjI4YzNhO2ZpbGwtb3BhY2l0eToxO2ZpbGwtcnVsZTpub256ZXJvO3N0cm9rZTpub25lO3N0cm9rZS13aWR0aDowLjI4NjYwNTtzdHJva2UtbGluZWNhcDpyb3VuZDtzdHJva2UtbGluZWpvaW46cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eToxO2NvbG9yLXJlbmRlcmluZzphdXRvO2ltYWdlLXJlbmRlcmluZzphdXRvO3NoYXBlLXJlbmRlcmluZzphdXRvO3RleHQtcmVuZGVyaW5nOmF1dG87ZW5hYmxlLWJhY2tncm91bmQ6YWNjdW11bGF0ZTtzdG9wLWNvbG9yOiMwMDAwMDA7c3RvcC1vcGFjaXR5OjEiCiAgICAgICAgIGQ9Im0gMzcuMzU4OTczLDgyLjMwNjMxNSBjIC0xLjI2OTk5NiwwIC0xLjkwNDk5NCwtMC43MTI2MDkgLTEuOTA0OTk0LC0yLjEzNzgyNiBWIDc3LjYwNzMzIGMgMCwtMC40NDQ0OTkgMC4yMTg3MjIsLTAuNjY2NzQ4IDAuNjU2MTY1LC0wLjY2Njc0OCAwLjQ0NDQ5OCwwIDAuNjY2NzQ3LDAuMjIyMjQ5IDAuNjY2NzQ3LDAuNjY2NzQ4IHYgMi41ODIzMjUgYyAwLDAuMzY2ODg4IDAuMDc0MDgsMC42Mzg1MjYgMC4yMjIyNSwwLjgxNDkxNCAwLjE0ODE2NiwwLjE3NjM4OSAwLjM4NDUyNiwwLjI2NDU4MyAwLjcwOTA4MSwwLjI2NDU4MyAwLjM1Mjc3NywwIDAuNjQyMDUzLC0wLjExOTk0NCAwLjg2NzgzLC0wLjM1OTgzMiAwLjIyNTc3NywtMC4yNDY5NDQgMC4zMzg2NjYsLTAuNTcxNDk5IDAuMzM4NjY2LC0wLjk3MzY2NCBWIDc3LjYwNzMzIGMgMCwtMC40NDQ0OTkgMC4yMTg3MjIsLTAuNjY2NzQ4IDAuNjU2MTY1LC0wLjY2Njc0OCAwLjQ0NDQ5OCwwIDAuNjY2NzQ3LDAuMjIyMjQ5IDAuNjY2NzQ3LDAuNjY2NzQ4IHYgNC4wMDA0ODcgYyAwLDAuNDUxNTU0IC0wLjIxNTE5NCwwLjY3NzMzMSAtMC42NDU1ODEsMC42NzczMzEgLTAuNDMwMzg3LDAgLTAuNjQ1NTgxLC0wLjIyNTc3NyAtMC42NDU1ODEsLTAuNjc3MzMxIHYgLTAuMTc5OTE2IGMgLTAuMzQ1NzIxLDAuNTg1NjA5IC0wLjg3NDg4NiwwLjg3ODQxNCAtMS41ODc0OTUsMC44Nzg0MTQgeiIKICAgICAgICAgaWQ9InBhdGgyMzIxIiAvPgogICAgICA8cGF0aAogICAgICAgICBzdHlsZT0iY29sb3I6IzAwMDAwMDtmb250LXN0eWxlOm5vcm1hbDtmb250LXZhcmlhbnQ6bm9ybWFsO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zdHJldGNoOm5vcm1hbDtmb250LXNpemU6MTAuNTgzM3B4O2xpbmUtaGVpZ2h0OjEuMjU7Zm9udC1mYW1pbHk6TnVuaXRvOy1pbmtzY2FwZS1mb250LXNwZWNpZmljYXRpb246J051bml0bywgQm9sZCc7Zm9udC12YXJpYW50LWxpZ2F0dXJlczpub3JtYWw7Zm9udC12YXJpYW50LXBvc2l0aW9uOm5vcm1hbDtmb250LXZhcmlhbnQtY2Fwczpub3JtYWw7Zm9udC12YXJpYW50LW51bWVyaWM6bm9ybWFsO2ZvbnQtdmFyaWFudC1hbHRlcm5hdGVzOm5vcm1hbDtmb250LXZhcmlhbnQtZWFzdC1hc2lhbjpub3JtYWw7Zm9udC1mZWF0dXJlLXNldHRpbmdzOm5vcm1hbDtmb250LXZhcmlhdGlvbi1zZXR0aW5nczpub3JtYWw7dGV4dC1pbmRlbnQ6MDt0ZXh0LWFsaWduOnN0YXJ0O3RleHQtZGVjb3JhdGlvbjpub25lO3RleHQtZGVjb3JhdGlvbi1saW5lOm5vbmU7dGV4dC1kZWNvcmF0aW9uLXN0eWxlOnNvbGlkO3RleHQtZGVjb3JhdGlvbi1jb2xvcjojMDAwMDAwO2xldHRlci1zcGFjaW5nOm5vcm1hbDt3b3JkLXNwYWNpbmc6bm9ybWFsO3RleHQtdHJhbnNmb3JtOm5vbmU7d3JpdGluZy1tb2RlOmxyLXRiO2RpcmVjdGlvbjpsdHI7dGV4dC1vcmllbnRhdGlvbjptaXhlZDtkb21pbmFudC1iYXNlbGluZTphdXRvO2Jhc2VsaW5lLXNoaWZ0OmJhc2VsaW5lO3RleHQtYW5jaG9yOnN0YXJ0O3doaXRlLXNwYWNlOm5vcm1hbDtzaGFwZS1wYWRkaW5nOjA7c2hhcGUtbWFyZ2luOjA7aW5saW5lLXNpemU6MDtjbGlwLXJ1bGU6bm9uemVybztkaXNwbGF5OmlubGluZTtvdmVyZmxvdzp2aXNpYmxlO3Zpc2liaWxpdHk6dmlzaWJsZTtpc29sYXRpb246YXV0bzttaXgtYmxlbmQtbW9kZTpub3JtYWw7Y29sb3ItaW50ZXJwb2xhdGlvbjpzUkdCO2NvbG9yLWludGVycG9sYXRpb24tZmlsdGVyczpsaW5lYXJSR0I7c29saWQtY29sb3I6IzAwMDAwMDtzb2xpZC1vcGFjaXR5OjE7dmVjdG9yLWVmZmVjdDpub25lO2ZpbGw6IzBiNWUwNjtmaWxsLW9wYWNpdHk6MTtmaWxsLXJ1bGU6bm9uemVybztzdHJva2U6bm9uZTtzdHJva2UtbGluZWNhcDpyb3VuZDtzdHJva2UtbGluZWpvaW46cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eToxO2NvbG9yLXJlbmRlcmluZzphdXRvO2ltYWdlLXJlbmRlcmluZzphdXRvO3NoYXBlLXJlbmRlcmluZzphdXRvO3RleHQtcmVuZGVyaW5nOmF1dG87ZW5hYmxlLWJhY2tncm91bmQ6YWNjdW11bGF0ZTtzdG9wLWNvbG9yOiMwMDAwMDA7c3RvcC1vcGFjaXR5OjEiCiAgICAgICAgIGQ9Im0gMzYuMTA5Mzc1LDc2Ljc5Njg3NSBjIC0wLjI0Mjk5MSwwIC0wLjQ1MDg4OCwwLjA2MzgyIC0wLjU5Mzc1LDAuMjA4OTg0IC0wLjE0Mjg2MiwwLjE0NTE2NyAtMC4yMDUwNzgsMC4zNTU4MDkgLTAuMjA1MDc4LDAuNjAxNTYzIHYgMi41NjA1NDcgYyAwLDAuNzMzODkxIDAuMTYyNjUxLDEuMzA3NDg3IDAuNTExNzE5LDEuNjk5MjE5IDAuMzQ5MDY3LDAuMzkxNzMxIDAuODc1MzA4LDAuNTgyMDMxIDEuNTM3MTA5LDAuNTgyMDMxIDAuNjMxMzc4LDAgMS4xMTQ4NDIsLTAuMjgwMTgxIDEuNDgyNDIyLC0wLjcyMjY1NyAwLjAxOTY1LDAuMTg3MTgzIDAuMDQ5MTMsMC4zNjk3NDMgMC4xNjIxMDksMC40ODgyODIgMC4xNDA1MTYsMC4xNDc0MjYgMC4zNDc2MzksMC4yMTI4OSAwLjU4Nzg5MSwwLjIxMjg5IDAuMjQwMjUyLDAgMC40NDczNzUsLTAuMDY1NDYgMC41ODc4OTEsLTAuMjEyODkgMC4xNDA1MTUsLTAuMTQ3NDI2IDAuMjAxMTcxLC0wLjM1ODg4MSAwLjIwMTE3MSwtMC42MDc0MjIgdiAtNCBjIDAsLTAuMjQ2MTMzIC0wLjA2NDA4LC0wLjQ1NjY2MSAtMC4yMDg5ODQsLTAuNjAxNTYzIC0wLjE0NDkwMSwtMC4xNDQ5MDEgLTAuMzU1NDMsLTAuMjA4OTg0IC0wLjYwMTU2MywtMC4yMDg5ODQgLTAuMjQyOTksMCAtMC40NTA4ODgsMC4wNjM4MiAtMC41OTM3NSwwLjIwODk4NCAtMC4xNDI4NjEsMC4xNDUxNjcgLTAuMjA1MDc4LDAuMzU1ODA5IC0wLjIwNTA3OCwwLjYwMTU2MyB2IDIuMzI4MTI1IGMgMCwwLjM3NDM0NCAtMC4xMDAyNSwwLjY1Njg4NSAtMC4yOTg4MjgsMC44NzUgLTQuMzhlLTQsNC42NWUtNCAtMC4wMDE1LC00LjY0ZS00IC0wLjAwMiwwIC0zLjk3ZS00LDQuMzZlLTQgMy45OGUtNCwwLjAwMTUgMCwwLjAwMiAtMC4xOTk4OTIsMC4yMTE0MjQgLTAuNDQyNDEzLDAuMzEyNSAtMC43NjE3MTksMC4zMTI1IC0wLjI5ODU5NCwwIC0wLjQ4NDU4OCwtMC4wNzU5NiAtMC41OTk2MDksLTAuMjEyODkxIC0wLjExNjY5MywtMC4xMzg5MiAtMC4xODk0NTMsLTAuMzc1MDU5IC0wLjE4OTQ1MywtMC43MjI2NTYgdiAtMi41ODIwMzEgYyAwLC0wLjI0NjEzMyAtMC4wNjQwOCwtMC40NTY2NjEgLTAuMjA4OTg0LC0wLjYwMTU2MyAtMC4xNDQ5MDIsLTAuMTQ0OTAxIC0wLjM1NTQzLC0wLjIwODk4NCAtMC42MDE1NjMsLTAuMjA4OTg0IHogbSAwLDAuMjg3MTA5IGMgMC4xOTgzNjUsMCAwLjMyMzA0MywwLjA0NzY1IDAuNDAwMzkxLDAuMTI1IDAuMDc3MzUsMC4wNzczNSAwLjEyMzA0NywwLjIwMDA3MiAwLjEyMzA0NiwwLjM5ODQzOCB2IDIuNTgyMDMxIGMgMCwwLjM4NjE3OSAwLjA3NjIyLDAuNjk0MzQ3IDAuMjU1ODYsMC45MDgyMDMgMC4xODEzMTEsMC4yMTU4NDcgMC40Njk3OTcsMC4zMTQ0NTMgMC44MjAzMTIsMC4zMTQ0NTMgMC4zODUyNTksMCAwLjcxOTM5NywtMC4xMzcyODMgMC45NzA3MDQsLTAuNDA0Mjk3IGEgMC4xNDMzMTY4MywwLjE0MzMxNjgzIDAgMCAwIDAuMDAyLC0wLjAwMiBjIDAuMjUxOTE4LC0wLjI3NTUzNSAwLjM3Njk1MywtMC42NDEzMyAwLjM3Njk1MywtMS4wNzAzMTIgdiAtMi4zMjgxMjUgYyAwLC0wLjE5ODc0NSAwLjA0NzE5LC0wLjMyMzMwOCAwLjEyMzA0NywtMC40MDAzOTEgMC4wNzU4NiwtMC4wNzcwOCAwLjE5NDIxOSwtMC4xMjMwNDcgMC4zODg2NzEsLTAuMTIzMDQ3IDAuMTk4MzY2LDAgMC4zMjEwOSwwLjA0NzY1IDAuMzk4NDM4LDAuMTI1IDAuMDc3MzUsMC4wNzczNSAwLjEyNSwwLjIwMDA3MiAwLjEyNSwwLjM5ODQzOCB2IDQgYyAwLDAuMjAzMDEzIC0wLjA0NjQyLDAuMzMxODA1IC0wLjEyMTA5NCwwLjQxMDE1NiAtMC4wNzQ2OCwwLjA3ODM1IC0wLjE5MDcyNCwwLjEyNSAtMC4zODA4NTksMC4xMjUgLTAuMTkwMTM1LDAgLTAuMzA2MTgxLC0wLjA0NjY1IC0wLjM4MDg1OSwtMC4xMjUgLTAuMDc0NjgsLTAuMDc4MzUgLTAuMTIxMDk0LC0wLjIwNzE0MyAtMC4xMjEwOTQsLTAuNDEwMTU2IHYgLTAuMTc5Njg4IGEgMC4xNDMzMTY4MywwLjE0MzMxNjgzIDAgMCAwIC0wLjI2NzU3OCwtMC4wNzIyNyBjIC0wLjMyMzgwOSwwLjU0ODQ5MSAtMC43ODk1MzcsMC44MDY2NCAtMS40NjI4OTEsMC44MDY2NCAtMC42MDgxOTUsMCAtMS4wMzYzMzUsLTAuMTY1NDUxIC0xLjMyMjI2NiwtMC40ODYzMjggLTAuMjg1OTMsLTAuMzIwODc3IC0wLjQzOTQ1MywtMC44MTY0ODYgLTAuNDM5NDUzLC0xLjUwNzgxMiBWIDc3LjYwNzM3IGMgMCwtMC4xOTg3NDUgMC4wNDcxOSwtMC4zMjMzMDggMC4xMjMwNDcsLTAuNDAwMzkxIDAuMDc1ODYsLTAuMDc3MDggMC4xOTQyMTksLTAuMTIzMDQ3IDAuMzg4NjcyLC0wLjEyMzA0NyB6IgogICAgICAgICBpZD0icGF0aDIzMjMiIC8+CiAgICA8L2c+CiAgICA8ZwogICAgICAgaWQ9InBhdGgyMzAyIgogICAgICAgc3R5bGU9ImZvbnQtc3R5bGU6bm9ybWFsO2ZvbnQtdmFyaWFudDpub3JtYWw7Zm9udC13ZWlnaHQ6Ym9sZDtmb250LXN0cmV0Y2g6bm9ybWFsO2ZvbnQtc2l6ZToxMC41ODMzcHg7bGluZS1oZWlnaHQ6MS4yNTtmb250LWZhbWlseTpOdW5pdG87LWlua3NjYXBlLWZvbnQtc3BlY2lmaWNhdGlvbjonTnVuaXRvLCBCb2xkJztmb250LXZhcmlhbnQtbGlnYXR1cmVzOm5vcm1hbDtmb250LXZhcmlhbnQtY2Fwczpub3JtYWw7Zm9udC12YXJpYW50LW51bWVyaWM6bm9ybWFsO2ZvbnQtdmFyaWFudC1lYXN0LWFzaWFuOm5vcm1hbDt3aGl0ZS1zcGFjZTpwcmU7ZmlsbDojMjI4YzNhO2ZpbGwtb3BhY2l0eToxO3N0cm9rZTojMGI1ZTA2O3N0cm9rZS13aWR0aDowLjI4NjYwNTtzdHJva2UtbGluZWNhcDpyb3VuZDtzdHJva2UtbGluZWpvaW46cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLW9wYWNpdHk6MSIKICAgICAgIHRyYW5zZm9ybT0ibWF0cml4KDEuNDUzNzY2NCwwLDAsMS42MzU5OTI5LDE3LjU1NTgyNiwtNDguNzY2MjYxKSI+CiAgICAgIDxwYXRoCiAgICAgICAgIHN0eWxlPSJjb2xvcjojMDAwMDAwO2ZvbnQtc3R5bGU6bm9ybWFsO2ZvbnQtdmFyaWFudDpub3JtYWw7Zm9udC13ZWlnaHQ6Ym9sZDtmb250LXN0cmV0Y2g6bm9ybWFsO2ZvbnQtc2l6ZToxMC41ODMzcHg7bGluZS1oZWlnaHQ6MS4yNTtmb250LWZhbWlseTpOdW5pdG87LWlua3NjYXBlLWZvbnQtc3BlY2lmaWNhdGlvbjonTnVuaXRvLCBCb2xkJztmb250LXZhcmlhbnQtbGlnYXR1cmVzOm5vcm1hbDtmb250LXZhcmlhbnQtcG9zaXRpb246bm9ybWFsO2ZvbnQtdmFyaWFudC1jYXBzOm5vcm1hbDtmb250LXZhcmlhbnQtbnVtZXJpYzpub3JtYWw7Zm9udC12YXJpYW50LWFsdGVybmF0ZXM6bm9ybWFsO2ZvbnQtdmFyaWFudC1lYXN0LWFzaWFuOm5vcm1hbDtmb250LWZlYXR1cmUtc2V0dGluZ3M6bm9ybWFsO2ZvbnQtdmFyaWF0aW9uLXNldHRpbmdzOm5vcm1hbDt0ZXh0LWluZGVudDowO3RleHQtYWxpZ246c3RhcnQ7dGV4dC1kZWNvcmF0aW9uOm5vbmU7dGV4dC1kZWNvcmF0aW9uLWxpbmU6bm9uZTt0ZXh0LWRlY29yYXRpb24tc3R5bGU6c29saWQ7dGV4dC1kZWNvcmF0aW9uLWNvbG9yOiMwMDAwMDA7bGV0dGVyLXNwYWNpbmc6bm9ybWFsO3dvcmQtc3BhY2luZzpub3JtYWw7dGV4dC10cmFuc2Zvcm06bm9uZTt3cml0aW5nLW1vZGU6bHItdGI7ZGlyZWN0aW9uOmx0cjt0ZXh0LW9yaWVudGF0aW9uOm1peGVkO2RvbWluYW50LWJhc2VsaW5lOmF1dG87YmFzZWxpbmUtc2hpZnQ6YmFzZWxpbmU7dGV4dC1hbmNob3I6c3RhcnQ7d2hpdGUtc3BhY2U6bm9ybWFsO3NoYXBlLXBhZGRpbmc6MDtzaGFwZS1tYXJnaW46MDtpbmxpbmUtc2l6ZTowO2NsaXAtcnVsZTpub256ZXJvO2Rpc3BsYXk6aW5saW5lO292ZXJmbG93OnZpc2libGU7dmlzaWJpbGl0eTp2aXNpYmxlO2lzb2xhdGlvbjphdXRvO21peC1ibGVuZC1tb2RlOm5vcm1hbDtjb2xvci1pbnRlcnBvbGF0aW9uOnNSR0I7Y29sb3ItaW50ZXJwb2xhdGlvbi1maWx0ZXJzOmxpbmVhclJHQjtzb2xpZC1jb2xvcjojMDAwMDAwO3NvbGlkLW9wYWNpdHk6MTt2ZWN0b3ItZWZmZWN0Om5vbmU7ZmlsbDojMjI4YzNhO2ZpbGwtb3BhY2l0eToxO2ZpbGwtcnVsZTpub256ZXJvO3N0cm9rZTpub25lO3N0cm9rZS13aWR0aDowLjI4NjYwNTtzdHJva2UtbGluZWNhcDpyb3VuZDtzdHJva2UtbGluZWpvaW46cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eToxO2NvbG9yLXJlbmRlcmluZzphdXRvO2ltYWdlLXJlbmRlcmluZzphdXRvO3NoYXBlLXJlbmRlcmluZzphdXRvO3RleHQtcmVuZGVyaW5nOmF1dG87ZW5hYmxlLWJhY2tncm91bmQ6YWNjdW11bGF0ZTtzdG9wLWNvbG9yOiMwMDAwMDA7c3RvcC1vcGFjaXR5OjEiCiAgICAgICAgIGQ9Im0gNDIuMjgxNTMsODIuMjg1MTQ4IGMgLTAuNDM3NDQzLDAgLTAuNjU2MTY0LC0wLjIyNTc3NyAtMC42NTYxNjQsLTAuNjc3MzMxIFYgNzcuNjA3MzMgYyAwLC0wLjQ0NDQ5OSAwLjIxNTE5NCwtMC42NjY3NDggMC42NDU1ODEsLTAuNjY2NzQ4IDAuNDMwMzg3LDAgMC42NDU1ODEsMC4yMjIyNDkgMC42NDU1ODEsMC42NjY3NDggdiAwLjIxMTY2NiBjIDAuMTU1MjIyLC0wLjI4MjIyMSAwLjM3MDQxNiwtMC41MDA5NDMgMC42NDU1ODEsLTAuNjU2MTY1IDAuMjc1MTY2LC0wLjE2MjI3NyAwLjU5MjY2NSwtMC4yNDM0MTUgMC45NTI0OTcsLTAuMjQzNDE1IDAuNzc2MTA5LDAgMS4yODQxMDcsMC4zMzg2NjUgMS41MjM5OTYsMS4wMTU5OTYgMC4xNjIyNzcsLTAuMzE3NDk5IDAuMzk1MTA5LC0wLjU2NDQ0MiAwLjY5ODQ5NywtMC43NDA4MzEgMC4zMDMzODgsLTAuMTgzNDQzIDAuNjQ5MTA5LC0wLjI3NTE2NSAxLjAzNzE2NCwtMC4yNzUxNjUgMS4xNjQxNjMsMCAxLjc0NjI0NCwwLjcwOTA4MSAxLjc0NjI0NCwyLjEyNzI0MyB2IDIuNTYxMTU4IGMgMCwwLjQ1MTU1NCAtMC4yMjIyNDksMC42NzczMzEgLTAuNjY2NzQ4LDAuNjc3MzMxIC0wLjQzNzQ0MywwIC0wLjY1NjE2NCwtMC4yMjU3NzcgLTAuNjU2MTY0LC0wLjY3NzMzMSB2IC0yLjUwODI0MiBjIDAsLTAuMzk1MTA5IC0wLjA2NzAzLC0wLjY4NDM4NiAtMC4yMDEwODMsLTAuODY3ODMgLTAuMTI2OTk5LC0wLjE4MzQ0NCAtMC4zNDU3MjEsLTAuMjc1MTY2IC0wLjY1NjE2NSwtMC4yNzUxNjYgLTAuMzQ1NzIxLDAgLTAuNjE3MzU5LDAuMTIzNDcyIC0wLjgxNDkxNCwwLjM3MDQxNSAtMC4xOTc1NTUsMC4yMzk4ODkgLTAuMjk2MzMyLDAuNTc1MDI3IC0wLjI5NjMzMiwxLjAwNTQxNCB2IDIuMjc1NDA5IGMgMCwwLjQ1MTU1NCAtMC4yMTg3MjIsMC42NzczMzEgLTAuNjU2MTY1LDAuNjc3MzMxIC0wLjQ0NDQ5OCwwIC0wLjY2Njc0NywtMC4yMjU3NzcgLTAuNjY2NzQ3LC0wLjY3NzMzMSB2IC0yLjUwODI0MiBjIDAsLTAuMzk1MTA5IC0wLjA2NzAzLC0wLjY4NDM4NiAtMC4yMDEwODMsLTAuODY3ODMgLTAuMTI3LC0wLjE4MzQ0NCAtMC4zNDIxOTQsLTAuMjc1MTY2IC0wLjY0NTU4MSwtMC4yNzUxNjYgLTAuMzQ1NzIxLDAgLTAuNjE3MzYsMC4xMjM0NzIgLTAuODE0OTE1LDAuMzcwNDE1IC0wLjE5NzU1NSwwLjIzOTg4OSAtMC4yOTYzMzIsMC41NzUwMjcgLTAuMjk2MzMyLDEuMDA1NDE0IHYgMi4yNzU0MDkgYyAwLDAuNDUxNTU0IC0wLjIyMjI0OSwwLjY3NzMzMSAtMC42NjY3NDgsMC42NzczMzEgeiIKICAgICAgICAgaWQ9InBhdGgyMzI3IiAvPgogICAgICA8cGF0aAogICAgICAgICBzdHlsZT0iY29sb3I6IzAwMDAwMDtmb250LXN0eWxlOm5vcm1hbDtmb250LXZhcmlhbnQ6bm9ybWFsO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zdHJldGNoOm5vcm1hbDtmb250LXNpemU6MTAuNTgzM3B4O2xpbmUtaGVpZ2h0OjEuMjU7Zm9udC1mYW1pbHk6TnVuaXRvOy1pbmtzY2FwZS1mb250LXNwZWNpZmljYXRpb246J051bml0bywgQm9sZCc7Zm9udC12YXJpYW50LWxpZ2F0dXJlczpub3JtYWw7Zm9udC12YXJpYW50LXBvc2l0aW9uOm5vcm1hbDtmb250LXZhcmlhbnQtY2Fwczpub3JtYWw7Zm9udC12YXJpYW50LW51bWVyaWM6bm9ybWFsO2ZvbnQtdmFyaWFudC1hbHRlcm5hdGVzOm5vcm1hbDtmb250LXZhcmlhbnQtZWFzdC1hc2lhbjpub3JtYWw7Zm9udC1mZWF0dXJlLXNldHRpbmdzOm5vcm1hbDtmb250LXZhcmlhdGlvbi1zZXR0aW5nczpub3JtYWw7dGV4dC1pbmRlbnQ6MDt0ZXh0LWFsaWduOnN0YXJ0O3RleHQtZGVjb3JhdGlvbjpub25lO3RleHQtZGVjb3JhdGlvbi1saW5lOm5vbmU7dGV4dC1kZWNvcmF0aW9uLXN0eWxlOnNvbGlkO3RleHQtZGVjb3JhdGlvbi1jb2xvcjojMDAwMDAwO2xldHRlci1zcGFjaW5nOm5vcm1hbDt3b3JkLXNwYWNpbmc6bm9ybWFsO3RleHQtdHJhbnNmb3JtOm5vbmU7d3JpdGluZy1tb2RlOmxyLXRiO2RpcmVjdGlvbjpsdHI7dGV4dC1vcmllbnRhdGlvbjptaXhlZDtkb21pbmFudC1iYXNlbGluZTphdXRvO2Jhc2VsaW5lLXNoaWZ0OmJhc2VsaW5lO3RleHQtYW5jaG9yOnN0YXJ0O3doaXRlLXNwYWNlOm5vcm1hbDtzaGFwZS1wYWRkaW5nOjA7c2hhcGUtbWFyZ2luOjA7aW5saW5lLXNpemU6MDtjbGlwLXJ1bGU6bm9uemVybztkaXNwbGF5OmlubGluZTtvdmVyZmxvdzp2aXNpYmxlO3Zpc2liaWxpdHk6dmlzaWJsZTtpc29sYXRpb246YXV0bzttaXgtYmxlbmQtbW9kZTpub3JtYWw7Y29sb3ItaW50ZXJwb2xhdGlvbjpzUkdCO2NvbG9yLWludGVycG9sYXRpb24tZmlsdGVyczpsaW5lYXJSR0I7c29saWQtY29sb3I6IzAwMDAwMDtzb2xpZC1vcGFjaXR5OjE7dmVjdG9yLWVmZmVjdDpub25lO2ZpbGw6IzBiNWUwNjtmaWxsLW9wYWNpdHk6MTtmaWxsLXJ1bGU6bm9uemVybztzdHJva2U6bm9uZTtzdHJva2UtbGluZWNhcDpyb3VuZDtzdHJva2UtbGluZWpvaW46cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eToxO2NvbG9yLXJlbmRlcmluZzphdXRvO2ltYWdlLXJlbmRlcmluZzphdXRvO3NoYXBlLXJlbmRlcmluZzphdXRvO3RleHQtcmVuZGVyaW5nOmF1dG87ZW5hYmxlLWJhY2tncm91bmQ6YWNjdW11bGF0ZTtzdG9wLWNvbG9yOiMwMDAwMDA7c3RvcC1vcGFjaXR5OjEiCiAgICAgICAgIGQ9Im0gNDQuNTEzNjcyLDc2Ljc3NTM5MSBjIC0wLjM4MTM3NCwwIC0wLjcyNDcyOSwwLjA4NzUxIC0xLjAyMzQzOCwwLjI2MzY3MSAtMC4xOTYxODcsMC4xMTEwNjEgLTAuMzIyNDE5LDAuMjk5MDYgLTAuNDYyODksMC40NzA3MDQgLTAuMDE3MDQsLTAuMTk0ODU4IC0wLjA1MjA2LC0wLjM4MDIyMyAtMC4xNjk5MjIsLTAuNTAxOTU0IC0wLjE0MDgwNywtMC4xNDU0MjMgLTAuMzQ2MDc3LC0wLjIxMDkzNyAtMC41ODU5MzgsLTAuMjEwOTM3IC0wLjIzOTg2LDAgLTAuNDQ3MDgzLDAuMDY1NTEgLTAuNTg3ODksMC4yMTA5MzcgLTAuMTQwODA3LDAuMTQ1NDI0IC0wLjIwMTE3MiwwLjM1NDIzNSAtMC4yMDExNzIsMC41OTk2MSB2IDQgYyAwLDAuMjQ4OTE0IDAuMDYwNTQsMC40NjAyNDIgMC4yMDMxMjUsMC42MDc0MjIgMC4xNDI1OCwwLjE0NzE4IDAuMzUyMzI3LDAuMjEyODkgMC41OTU3MDMsMC4yMTI4OSAwLjI0NjUxMiwwIDAuNDU2OTMyLC0wLjA2NDAxIDAuNjAxNTYyLC0wLjIxMDkzNyAwLjE0NDYzMSwtMC4xNDY5MjYgMC4yMDg5ODUsLTAuMzYwMDg4IDAuMjA4OTg1LC0wLjYwOTM3NSB2IC0yLjI3NTM5MSBjIDAsLTAuNDA4NDYxIDAuMDkyODUsLTAuNzA2NjM3IDAuMjYzNjcyLC0wLjkxNDA2MiBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMiwtMC4wMDIgYyAwLjE3MjA4MiwtMC4yMTUxMDIgMC4zODk1NywtMC4zMTY0MDcgMC43MDExNzIsLTAuMzE2NDA3IDAuMjc0NTA0LDAgMC40MzI2OTUsMC4wNzMzNSAwLjUyOTI5NywwLjIxMjg5MSBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMiwwLjAwMzkgYyAwLjEwNTEzOCwwLjE0Mzg3MyAwLjE3MzgyOCwwLjQwNDMwMSAwLjE3MzgyOCwwLjc4MzIwMyB2IDIuNTA3ODEzIGMgMCwwLjI0OTI4NyAwLjA2MjQsMC40NjI0NDkgMC4yMDcwMzEsMC42MDkzNzUgMC4xNDQ2MywwLjE0NjkyNiAwLjM1NTA1MSwwLjIxMDkzNyAwLjYwMTU2MywwLjIxMDkzNyAwLjI0MzM3NSwwIDAuNDUzMTIyLC0wLjA2NTcxIDAuNTk1NzAzLC0wLjIxMjg5IDAuMTQyNTgsLTAuMTQ3MTggMC4yMDUwNzgsLTAuMzU4NTA4IDAuMjA1MDc4LC0wLjYwNzQyMiB2IC0yLjI3NTM5MSBjIDAsLTAuNDA4NDYxIDAuMDkyODUsLTAuNzA2NjM3IDAuMjYzNjcyLC0wLjkxNDA2MiBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLC0wLjAwMiBjIDAuMTcyMDgyLC0wLjIxNTEwMiAwLjM5MTUyNCwtMC4zMTY0MDcgMC43MDMxMjUsLTAuMzE2NDA3IDAuMjgyMjE4LDAgMC40NDMxNjgsMC4wNzQzOCAwLjUzOTA2MiwwLjIxMjg5MSBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMiwwLjAwMzkgYyAwLjEwNTEzOCwwLjE0Mzg3MyAwLjE3MzgyOCwwLjQwNDMwMSAwLjE3MzgyOSwwLjc4MzIwMyB2IDIuNTA3ODEzIGMgMCwwLjI0ODkxNCAwLjA2MDU0LDAuNDYwMjQyIDAuMjAzMTI0LDAuNjA3NDIyIDAuMTQyNTgxLDAuMTQ3MTggMC4zNTIzMjgsMC4yMTI4OSAwLjU5NTcwNCwwLjIxMjg5IDAuMjQ2NTEyLDAgMC40NTY5MzIsLTAuMDY0MDEgMC42MDE1NjIsLTAuMjEwOTM3IDAuMTQ0NjMsLTAuMTQ2OTI2IDAuMjA4OTg1LC0wLjM2MDA4OCAwLjIwODk4NCwtMC42MDkzNzUgdiAtMi41NjA1NDcgYyAwLC0wLjcyODY4NyAtMC4xNDc5ODIsLTEuMjk2NzQ2IC0wLjQ2ODc1LC0xLjY4NzUgLTAuMzIwNzY3LC0wLjM5MDc1NCAtMC44MTA2OTgsLTAuNTgzOTg0IC0xLjQyMTg3NCwtMC41ODM5ODQgLTAuNDA5MTA5LDAgLTAuNzgxNzQ3LDAuMDk4OTUgLTEuMTA3NDIyLDAuMjk0OTIxIC04LjE4ZS00LDQuNzZlLTQgLTAuMDAxMSwwLjAwMTUgLTAuMDAyLDAuMDAyIC0wLjI2NDQyNywwLjE1NDIzNCAtMC40NDgxNzcsMC4zOTU3NSAtMC42MTMyODEsMC42NTAzOSAtMC4yODg2OTcsLTAuNTkwOTU5IC0wLjgwMDUzNSwtMC45NDcyNjUgLTEuNTM3MTA5LC0wLjk0NzI2NSB6IG0gMCwwLjI4NzEwOSBjIDAuNzM0NDIxLDAgMS4xNjY5NzIsMC4yODg0MzQgMS4zOTA2MjUsMC45MTk5MjIgQSAwLjE0MzMxNjgzLDAuMTQzMzE2ODMgMCAwIDAgNDYuMTY2MDE2LDc4IGMgMC4xNTA3LC0wLjI5NDg0OSAwLjM2MTg5LC0wLjUxODQ1IDAuNjQyNTc4LC0wLjY4MTY0MSBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMiwtMC4wMDIgYyAwLjI4MDIzOCwtMC4xNjk0NDYgMC41OTc0MTcsLTAuMjUzOTA2IDAuOTYyODkxLC0wLjI1MzkwNiAwLjU1Mjk4NiwwIDAuOTM3OTA1LDAuMTYyMTQyIDEuMTk5MjE4LDAuNDgwNDY5IDAuMjYxMzE0LDAuMzE4MzI3IDAuNDA0Mjk3LDAuODE0NDMxIDAuNDA0Mjk3LDEuNTAzOTA2IHYgMi41NjA1NDcgYyAwLDAuMjAyMjY2IC0wLjA0NzM4LDAuMzI5MzUyIC0wLjEyNSwwLjQwODIwMyAtMC4wNzc2MiwwLjA3ODg1IC0wLjIwMDQ1MSwwLjEyNjk1MyAtMC4zOTg0MzcsMC4xMjY5NTMgLTAuMTk0MDY3LDAgLTAuMzEyNTMxLC0wLjA0ODM2IC0wLjM4ODY3MiwtMC4xMjY5NTMgLTAuMDc2MTQsLTAuMDc4NiAtMC4xMjMwNDcsLTAuMjA1NTYzIC0wLjEyMzA0NywtMC40MDgyMDMgdiAtMi41MDc4MTMgYyAwLC0wLjQxMDUyIC0wLjA2ODE5LC0wLjcyODIyMSAtMC4yMzA0NjksLTAuOTUxMTcxIC0wLjE1ODUxLC0wLjIyNTk2NyAtMC40MzQ1NiwtMC4zMzU5MzggLTAuNzcxNDg0LC0wLjMzNTkzOCAtMC4zNzk4NDEsMCAtMC43MDI3NTMsMC4xNDY5OTYgLTAuOTI1NzgyLDAuNDI1NzgxIC0wLjIyMzUzMSwwLjI3MjIzMiAtMC4zMjgxMjQsMC42NDIxNzcgLTAuMzI4MTI0LDEuMDkzNzUgdiAyLjI3NTM5MSBjIDAsMC4yMDI2MzkgLTAuMDQ2OTEsMC4zMjk2MDYgLTAuMTIzMDQ3LDAuNDA4MjAzIC0wLjA3NjE0LDAuMDc4NiAtMC4xOTY1NTgsMC4xMjY5NTMgLTAuMzkwNjI1LDAuMTI2OTUzIC0wLjE5Nzk4NiwwIC0wLjMxODg2NiwtMC4wNDgxIC0wLjM5NjQ4NSwtMC4xMjY5NTMgLTAuMDc3NjIsLTAuMDc4ODUgLTAuMTI2OTUzLC0wLjIwNTkzNyAtMC4xMjY5NTMsLTAuNDA4MjAzIHYgLTIuNTA3ODEzIGMgMCwtMC40MDk1MzEgLTAuMDY1MTQsLTAuNzI2MzUgLTAuMjI2NTYyLC0wLjk0OTIxOCAtMy4zN2UtNCwtNC44N2UtNCAtMC4wMDE2LDQuODVlLTQgLTAuMDAyLDAgLTAuMTU3NTU3LC0wLjIyNjM2MyAtMC40MzAxNTksLTAuMzM3ODkxIC0wLjc2MTcxOCwtMC4zMzc4OTEgLTAuMzc4ODc2LDAgLTAuNzAwODU1LDAuMTQ2NDI4IC0wLjkyMzgyOCwwLjQyMzgyOCAtMy42N2UtNCw0LjQ1ZS00IDMuNjZlLTQsMC4wMDE1IDAsMC4wMDIgLTAuMjIzNTMzLDAuMjcyMjMyIC0wLjMzMDA3OCwwLjY0MjE3NiAtMC4zMzAwNzgsMS4wOTM3NSB2IDIuMjc1MzkxIGMgMCwwLjIwMjI2NiAtMC4wNDczOCwwLjMyOTM1MiAtMC4xMjUsMC40MDgyMDMgLTAuMDc3NjIsMC4wNzg4NSAtMC4yMDA0NTIsMC4xMjY5NTMgLTAuMzk4NDM4LDAuMTI2OTUzIC0wLjE5NDA2NywwIC0wLjMxMjUzMSwtMC4wNDgzNiAtMC4zODg2NzIsLTAuMTI2OTUzIC0wLjA3NjE0LC0wLjA3ODYgLTAuMTIzMDQ3LC0wLjIwNTU2MyAtMC4xMjMwNDcsLTAuNDA4MjAzIHYgLTQgYyAwLC0wLjE5OTEyNCAwLjA0NjcxLC0wLjMyMzU2NSAwLjEyMTA5NCwtMC40MDAzOTEgMC4wNzQzOSwtMC4wNzY4MiAwLjE5MDMzMywtMC4xMjMwNDcgMC4zODA4NTksLTAuMTIzMDQ3IDAuMTkwNTI3LDAgMC4zMDY0NzMsMC4wNDYyMiAwLjM4MDg2LDAuMTIzMDQ3IDAuMDc0MzksMC4wNzY4MyAwLjEyMTA5NCwwLjIwMTI2NyAwLjEyMTA5NCwwLjQwMDM5MSB2IDAuMjEwOTM3IGEgMC4xNDMzMTY4MywwLjE0MzMxNjgzIDAgMCAwIDAuMjY5NTMxLDAuMDcwMzEgYyAwLjE0MzMzNiwtMC4yNjA2MTEgMC4zMzcwMDUsLTAuNDU4OTM2IDAuNTg5ODQzLC0wLjYwMTU2MyBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMiwwIEMgNDMuODg2Mzg5LDc3LjEzODcxNyA0NC4xNzUzODIsNzcuMDYyNSA0NC41MTM2NzIsNzcuMDYyNSBaIgogICAgICAgICBpZD0icGF0aDIzMjkiIC8+CiAgICA8L2c+CiAgICA8ZwogICAgICAgaWQ9InBhdGgyMzA0IgogICAgICAgc3R5bGU9ImZvbnQtc3R5bGU6bm9ybWFsO2ZvbnQtdmFyaWFudDpub3JtYWw7Zm9udC13ZWlnaHQ6Ym9sZDtmb250LXN0cmV0Y2g6bm9ybWFsO2ZvbnQtc2l6ZToxMC41ODMzcHg7bGluZS1oZWlnaHQ6MS4yNTtmb250LWZhbWlseTpOdW5pdG87LWlua3NjYXBlLWZvbnQtc3BlY2lmaWNhdGlvbjonTnVuaXRvLCBCb2xkJztmb250LXZhcmlhbnQtbGlnYXR1cmVzOm5vcm1hbDtmb250LXZhcmlhbnQtY2Fwczpub3JtYWw7Zm9udC12YXJpYW50LW51bWVyaWM6bm9ybWFsO2ZvbnQtdmFyaWFudC1lYXN0LWFzaWFuOm5vcm1hbDt3aGl0ZS1zcGFjZTpwcmU7ZmlsbDojMjI4YzNhO2ZpbGwtb3BhY2l0eToxO3N0cm9rZTojMGI1ZTA2O3N0cm9rZS13aWR0aDowLjI4NjYwNTtzdHJva2UtbGluZWNhcDpyb3VuZDtzdHJva2UtbGluZWpvaW46cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLW9wYWNpdHk6MSIKICAgICAgIHRyYW5zZm9ybT0ibWF0cml4KDEuNDUzNzY2NCwwLDAsMS42MzU5OTI5LDE3LjU1NTgyNiwtNDguNzY2MjYxKSI+CiAgICAgIDxwYXRoCiAgICAgICAgIHN0eWxlPSJjb2xvcjojMDAwMDAwO2ZvbnQtc3R5bGU6bm9ybWFsO2ZvbnQtdmFyaWFudDpub3JtYWw7Zm9udC13ZWlnaHQ6Ym9sZDtmb250LXN0cmV0Y2g6bm9ybWFsO2ZvbnQtc2l6ZToxMC41ODMzcHg7bGluZS1oZWlnaHQ6MS4yNTtmb250LWZhbWlseTpOdW5pdG87LWlua3NjYXBlLWZvbnQtc3BlY2lmaWNhdGlvbjonTnVuaXRvLCBCb2xkJztmb250LXZhcmlhbnQtbGlnYXR1cmVzOm5vcm1hbDtmb250LXZhcmlhbnQtcG9zaXRpb246bm9ybWFsO2ZvbnQtdmFyaWFudC1jYXBzOm5vcm1hbDtmb250LXZhcmlhbnQtbnVtZXJpYzpub3JtYWw7Zm9udC12YXJpYW50LWFsdGVybmF0ZXM6bm9ybWFsO2ZvbnQtdmFyaWFudC1lYXN0LWFzaWFuOm5vcm1hbDtmb250LWZlYXR1cmUtc2V0dGluZ3M6bm9ybWFsO2ZvbnQtdmFyaWF0aW9uLXNldHRpbmdzOm5vcm1hbDt0ZXh0LWluZGVudDowO3RleHQtYWxpZ246c3RhcnQ7dGV4dC1kZWNvcmF0aW9uOm5vbmU7dGV4dC1kZWNvcmF0aW9uLWxpbmU6bm9uZTt0ZXh0LWRlY29yYXRpb24tc3R5bGU6c29saWQ7dGV4dC1kZWNvcmF0aW9uLWNvbG9yOiMwMDAwMDA7bGV0dGVyLXNwYWNpbmc6bm9ybWFsO3dvcmQtc3BhY2luZzpub3JtYWw7dGV4dC10cmFuc2Zvcm06bm9uZTt3cml0aW5nLW1vZGU6bHItdGI7ZGlyZWN0aW9uOmx0cjt0ZXh0LW9yaWVudGF0aW9uOm1peGVkO2RvbWluYW50LWJhc2VsaW5lOmF1dG87YmFzZWxpbmUtc2hpZnQ6YmFzZWxpbmU7dGV4dC1hbmNob3I6c3RhcnQ7d2hpdGUtc3BhY2U6bm9ybWFsO3NoYXBlLXBhZGRpbmc6MDtzaGFwZS1tYXJnaW46MDtpbmxpbmUtc2l6ZTowO2NsaXAtcnVsZTpub256ZXJvO2Rpc3BsYXk6aW5saW5lO292ZXJmbG93OnZpc2libGU7dmlzaWJpbGl0eTp2aXNpYmxlO2lzb2xhdGlvbjphdXRvO21peC1ibGVuZC1tb2RlOm5vcm1hbDtjb2xvci1pbnRlcnBvbGF0aW9uOnNSR0I7Y29sb3ItaW50ZXJwb2xhdGlvbi1maWx0ZXJzOmxpbmVhclJHQjtzb2xpZC1jb2xvcjojMDAwMDAwO3NvbGlkLW9wYWNpdHk6MTt2ZWN0b3ItZWZmZWN0Om5vbmU7ZmlsbDojMjI4YzNhO2ZpbGwtb3BhY2l0eToxO2ZpbGwtcnVsZTpub256ZXJvO3N0cm9rZTpub25lO3N0cm9rZS13aWR0aDowLjI4NjYwNTtzdHJva2UtbGluZWNhcDpyb3VuZDtzdHJva2UtbGluZWpvaW46cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eToxO2NvbG9yLXJlbmRlcmluZzphdXRvO2ltYWdlLXJlbmRlcmluZzphdXRvO3NoYXBlLXJlbmRlcmluZzphdXRvO3RleHQtcmVuZGVyaW5nOmF1dG87ZW5hYmxlLWJhY2tncm91bmQ6YWNjdW11bGF0ZTtzdG9wLWNvbG9yOiMwMDAwMDA7c3RvcC1vcGFjaXR5OjEiCiAgICAgICAgIGQ9Im0gNTMuMjQ1MzMyLDgyLjMwNjMxNSBjIC0wLjUzNjIyMSwwIC0xLjAwMTg4NiwtMC4xMDkzNjEgLTEuMzk2OTk1LC0wLjMyODA4MiAtMC4zOTUxMSwtMC4yMTg3MjEgLTAuNzAyMDI2LC0wLjUyOTE2NSAtMC45MjA3NDcsLTAuOTMxMzMxIC0wLjIxODcyMiwtMC40MDkyMjEgLTAuMzI4MDgzLC0wLjg4ODk5NyAtMC4zMjgwODMsLTEuNDM5MzI4IDAsLTAuNTUwMzMyIDAuMTA5MzYxLC0xLjAyNjU4MSAwLjMyODA4MywtMS40Mjg3NDYgMC4yMTg3MjEsLTAuNDAyMTY1IDAuNTI1NjM3LC0wLjcxMjYwOSAwLjkyMDc0NywtMC45MzEzMyAwLjM5NTEwOSwtMC4yMTg3MjEgMC44NjA3NzQsLTAuMzI4MDgyIDEuMzk2OTk1LC0wLjMyODA4MiAwLjUzNjIyMSwwIDEuMDAxODg2LDAuMTA5MzYxIDEuMzk2OTk2LDAuMzI4MDgyIDAuMzk1MTEsMC4yMTg3MjEgMC43MDIwMjYsMC41MjkxNjUgMC45MjA3NDcsMC45MzEzMyAwLjIxODcyMSwwLjQwMjE2NSAwLjMyODA4MiwwLjg3ODQxNCAwLjMyODA4MiwxLjQyODc0NiAwLDAuNTUwMzMxIC0wLjEwOTM2MSwxLjAzMDEwNyAtMC4zMjgwODIsMS40MzkzMjggLTAuMjE4NzIxLDAuNDAyMTY2IC0wLjUyNTYzNywwLjcxMjYxIC0wLjkyMDc0NywwLjkzMTMzMSAtMC4zOTUxMSwwLjIxODcyMSAtMC44NjA3NzUsMC4zMjgwODIgLTEuMzk2OTk2LDAuMzI4MDgyIHogbSAwLC0xLjAwNTQxMyBjIDAuMzk1MTEsMCAwLjcxMjYwOSwtMC4xNDExMTEgMC45NTI0OTcsLTAuNDIzMzMyIDAuMjM5ODg4LC0wLjI4OTI3NyAwLjM1OTgzMiwtMC43MTI2MDkgMC4zNTk4MzIsLTEuMjY5OTk2IDAsLTAuNTY0NDQzIC0wLjExOTk0NCwtMC45ODQyNDcgLTAuMzU5ODMyLC0xLjI1OTQxMyAtMC4yMzk4ODgsLTAuMjgyMjIxIC0wLjU1NzM4NywtMC40MjMzMzIgLTAuOTUyNDk3LC0wLjQyMzMzMiAtMC4zOTUxMSwwIC0wLjcxMjYwOSwwLjE0MTExMSAtMC45NTI0OTcsMC40MjMzMzIgLTAuMjM5ODg4LDAuMjc1MTY2IC0wLjM1OTgzMiwwLjY5NDk3IC0wLjM1OTgzMiwxLjI1OTQxMyAwLDAuNTU3Mzg3IDAuMTE5OTQ0LDAuOTgwNzE5IDAuMzU5ODMyLDEuMjY5OTk2IDAuMjM5ODg4LDAuMjgyMjIxIDAuNTU3Mzg3LDAuNDIzMzMyIDAuOTUyNDk3LDAuNDIzMzMyIHoiCiAgICAgICAgIGlkPSJwYXRoMjMzMyIgLz4KICAgICAgPHBhdGgKICAgICAgICAgc3R5bGU9ImNvbG9yOiMwMDAwMDA7Zm9udC1zdHlsZTpub3JtYWw7Zm9udC12YXJpYW50Om5vcm1hbDtmb250LXdlaWdodDpib2xkO2ZvbnQtc3RyZXRjaDpub3JtYWw7Zm9udC1zaXplOjEwLjU4MzNweDtsaW5lLWhlaWdodDoxLjI1O2ZvbnQtZmFtaWx5Ok51bml0bzstaW5rc2NhcGUtZm9udC1zcGVjaWZpY2F0aW9uOidOdW5pdG8sIEJvbGQnO2ZvbnQtdmFyaWFudC1saWdhdHVyZXM6bm9ybWFsO2ZvbnQtdmFyaWFudC1wb3NpdGlvbjpub3JtYWw7Zm9udC12YXJpYW50LWNhcHM6bm9ybWFsO2ZvbnQtdmFyaWFudC1udW1lcmljOm5vcm1hbDtmb250LXZhcmlhbnQtYWx0ZXJuYXRlczpub3JtYWw7Zm9udC12YXJpYW50LWVhc3QtYXNpYW46bm9ybWFsO2ZvbnQtZmVhdHVyZS1zZXR0aW5nczpub3JtYWw7Zm9udC12YXJpYXRpb24tc2V0dGluZ3M6bm9ybWFsO3RleHQtaW5kZW50OjA7dGV4dC1hbGlnbjpzdGFydDt0ZXh0LWRlY29yYXRpb246bm9uZTt0ZXh0LWRlY29yYXRpb24tbGluZTpub25lO3RleHQtZGVjb3JhdGlvbi1zdHlsZTpzb2xpZDt0ZXh0LWRlY29yYXRpb24tY29sb3I6IzAwMDAwMDtsZXR0ZXItc3BhY2luZzpub3JtYWw7d29yZC1zcGFjaW5nOm5vcm1hbDt0ZXh0LXRyYW5zZm9ybTpub25lO3dyaXRpbmctbW9kZTpsci10YjtkaXJlY3Rpb246bHRyO3RleHQtb3JpZW50YXRpb246bWl4ZWQ7ZG9taW5hbnQtYmFzZWxpbmU6YXV0bztiYXNlbGluZS1zaGlmdDpiYXNlbGluZTt0ZXh0LWFuY2hvcjpzdGFydDt3aGl0ZS1zcGFjZTpub3JtYWw7c2hhcGUtcGFkZGluZzowO3NoYXBlLW1hcmdpbjowO2lubGluZS1zaXplOjA7Y2xpcC1ydWxlOm5vbnplcm87ZGlzcGxheTppbmxpbmU7b3ZlcmZsb3c6dmlzaWJsZTt2aXNpYmlsaXR5OnZpc2libGU7aXNvbGF0aW9uOmF1dG87bWl4LWJsZW5kLW1vZGU6bm9ybWFsO2NvbG9yLWludGVycG9sYXRpb246c1JHQjtjb2xvci1pbnRlcnBvbGF0aW9uLWZpbHRlcnM6bGluZWFyUkdCO3NvbGlkLWNvbG9yOiMwMDAwMDA7c29saWQtb3BhY2l0eToxO3ZlY3Rvci1lZmZlY3Q6bm9uZTtmaWxsOiMwYjVlMDY7ZmlsbC1vcGFjaXR5OjE7ZmlsbC1ydWxlOm5vbnplcm87c3Ryb2tlOm5vbmU7c3Ryb2tlLWxpbmVjYXA6cm91bmQ7c3Ryb2tlLWxpbmVqb2luOnJvdW5kO3N0cm9rZS1taXRlcmxpbWl0OjQ7c3Ryb2tlLWRhc2hhcnJheTpub25lO3N0cm9rZS1kYXNob2Zmc2V0OjA7c3Ryb2tlLW9wYWNpdHk6MTtjb2xvci1yZW5kZXJpbmc6YXV0bztpbWFnZS1yZW5kZXJpbmc6YXV0bztzaGFwZS1yZW5kZXJpbmc6YXV0bzt0ZXh0LXJlbmRlcmluZzphdXRvO2VuYWJsZS1iYWNrZ3JvdW5kOmFjY3VtdWxhdGU7c3RvcC1jb2xvcjojMDAwMDAwO3N0b3Atb3BhY2l0eToxIgogICAgICAgICBkPSJtIDUzLjI0NjA5NCw3Ni43NzUzOTEgYyAtMC41NTU3MDUsMCAtMS4wNDc2NTUsMC4xMTU2MzEgLTEuNDY2Nzk3LDAuMzQ3NjU2IC0wLjQxNzg2NywwLjIzMTMxOSAtMC43NDc3LDAuNTYzODc4IC0wLjk3ODUxNiwwLjk4ODI4MSAtMC4yMzE4MywwLjQyNjI2NyAtMC4zNDM3NSwwLjkyNjc3NyAtMC4zNDM3NSwxLjQ5NjA5NCAwLDAuNTY5MzE2IDAuMTEyNDQsMS4wNzUwMzkgMC4zNDM3NSwxLjUwNzgxMiAwLjIzMDgxNiwwLjQyNDQwNCAwLjU2MDY0OCwwLjc1Njk2MiAwLjk3ODUxNiwwLjk4ODI4MiAwLjQxOTE0MiwwLjIzMjAyNSAwLjkxMTA5MiwwLjM0NTcwMyAxLjQ2Njc5NywwLjM0NTcwMyAwLjU1NTcwNSwwIDEuMDQ1NzAxLC0wLjExMzY3OCAxLjQ2NDg0NCwtMC4zNDU3MDMgMC40MTc4NjcsLTAuMjMxMzIgMC43NDc3LC0wLjU2Mzg3OCAwLjk3ODUxNSwtMC45ODgyODIgMC4yMzEzMSwtMC40MzI3NzMgMC4zNDU3MDMsLTAuOTM4NDk2IDAuMzQ1NzAzLC0xLjUwNzgxMiAwLC0wLjU2OTMxNyAtMC4xMTM4NzQsLTEuMDY5ODI3IC0wLjM0NTcwMywtMS40OTYwOTQgLTAuMjMwODE1LC0wLjQyNDQwMyAtMC41NjA2NDgsLTAuNzU2OTYyIC0wLjk3ODUxNSwtMC45ODgyODEgLTAuNDE5MTQzLC0wLjIzMjAyNSAtMC45MDkxMzksLTAuMzQ3NjU2IC0xLjQ2NDg0NCwtMC4zNDc2NTYgeiBtIDAsMC4yODcxMDkgYyAwLjUxNjczNiwwIDAuOTU1MDk0LDAuMTA1MTI5IDEuMzI2MTcyLDAuMzEwNTQ3IDAuMzcyMzUyLDAuMjA2MTIzIDAuNjU4NjA3LDAuNDk1MDcyIDAuODY1MjM0LDAuODc1IDAuMjA1NjE0LDAuMzc4MDY0IDAuMzEwNTQ3LDAuODI4MDI3IDAuMzEwNTQ3LDEuMzU5Mzc1IDAsMC41MzEzNDcgLTAuMTA0NDE0LDAuOTg1NDI1IC0wLjMxMDU0NywxLjM3MTA5NCAtMC4yMDY2MjcsMC4zNzk5MjggLTAuNDkyODgyLDAuNjY4ODc2IC0wLjg2NTIzNCwwLjg3NSAtMC4zNzEwNzgsMC4yMDU0MTcgLTAuODA5NDM2LDAuMzA4NTkzIC0xLjMyNjE3MiwwLjMwODU5MyAtMC41MTY3MzcsMCAtMC45NTcwNDgsLTAuMTAzMTc2IC0xLjMyODEyNSwtMC4zMDg1OTMgLTAuMzcyMzUzLC0wLjIwNjEyNCAtMC42NTg2MDcsLTAuNDk1MDcyIC0wLjg2NTIzNSwtMC44NzUgaCAwLjAwMiBjIC0wLjIwNjEzNCwtMC4zODU2NjggLTAuMzEyNSwtMC44Mzk3NDcgLTAuMzEyNSwtMS4zNzEwOTQgMCwtMC41MzEzNDggMC4xMDQ5MzIsLTAuOTgxMzExIDAuMzEwNTQ2LC0xLjM1OTM3NSAwLjIwNjYyOCwtMC4zNzk5MjggMC40OTI4ODIsLTAuNjY4ODc3IDAuODY1MjM1LC0wLjg3NSAwLjM3MTA3NywtMC4yMDU0MTggMC44MTEzODgsLTAuMzEwNTQ3IDEuMzI4MTI1LC0wLjMxMDU0NyB6IG0gMCwwLjcxODc1IGMgLTAuNDI4Mjc4LDAgLTAuNzk1NjUsMC4xNjIyMDcgLTEuMDYwNTQ3LDAuNDcyNjU2IC0zLjYxZS00LDQuMTVlLTQgMy42MWUtNCwwLjAwMTUgMCwwLjAwMiAtMC4yNzEwMjcsMC4zMTE3MjIgLTAuMzk2NDg1LDAuNzY3NTk2IC0wLjM5NjQ4NSwxLjM1MTU2MyAxMGUtNywwLjU3Nzk0NSAwLjEyMzgyMiwxLjAzNzIzOCAwLjM5MjU3OSwxLjM2MTMyOCBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMiwwLjAwMiBjIDAuMjY1MDMyLDAuMzExODAzIDAuNjMzMjcsMC40NzI2NTYgMS4wNjI1LDAuNDcyNjU2IDAuNDI5MjI5LDAgMC43OTU1MTQsLTAuMTYwODUzIDEuMDYwNTQ3LC0wLjQ3MjY1NiBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMiwtMC4wMDIgYyAwLjI2ODc1NiwtMC4zMjQwOSAwLjM5MjU3OCwtMC43ODMzODMgMC4zOTI1NzgsLTEuMzYxMzI4IDAsLTAuNTgzOTY2IC0wLjEyMzUwNSwtMS4wMzk4NDEgLTAuMzk0NTMxLC0xLjM1MTU2MyAtMC4yNjUwMzMsLTAuMzExODAzIC0wLjYzMTMxOCwtMC40NzQ2MDkgLTEuMDYwNTQ3LC0wLjQ3NDYwOSB6IG0gMCwwLjI4NzEwOSBjIDAuMzYwOTksMCAwLjYyNzA1MywwLjEyMDQwOCAwLjg0MTc5NywwLjM3MzA0NyBhIDAuMTQzMzE2ODMsMC4xNDMzMTY4MyAwIDAgMCAwLjAwMiwwIGMgMC4yMDc5OTQsMC4yMzg1ODIgMC4zMjQyMTksMC42MjE4NzQgMC4zMjQyMTgsMS4xNjYwMTYgMCwwLjUzNjgyOCAtMC4xMTUxNTIsMC45MjMyNjkgLTAuMzI2MTcxLDEuMTc3NzM0IC0wLjIxNDc0NCwwLjI1MjY0IC0wLjQ4MDgwNywwLjM3MzA0NyAtMC44NDE3OTcsMC4zNzMwNDcgLTAuMzYwOTkxLDAgLTAuNjI5MDA3LC0wLjEyMDQwNyAtMC44NDM3NSwtMC4zNzMwNDcgLTAuMjExMDIsLTAuMjU0NDY1IC0wLjMyNjE3MiwtMC42NDA5MDYgLTAuMzI2MTcyLC0xLjE3NzczNCAwLC0wLjU0NDE0MiAwLjExNjIyNCwtMC45Mjc0MzQgMC4zMjQyMTksLTEuMTY2MDE2IGEgMC4xNDMzMTY4MywwLjE0MzMxNjgzIDAgMCAwIDAuMDAyLDAgYyAwLjIxNDc0MywtMC4yNTI2MzkgMC40ODI3NTksLTAuMzczMDQ3IDAuODQzNzUsLTAuMzczMDQ3IHoiCiAgICAgICAgIGlkPSJwYXRoMjMzNSIgLz4KICAgIDwvZz4KICAgIDxwYXRoCiAgICAgICBkPSJtIDQzLjI2MzQzNiw4NS45MzA0MzkgcSAtMS4yNzEyMywwIC0xLjI3MTIzLC0xLjQ3NjcyIHYgLTEzLjcwNTc2IHEgMCwtMS40NTM2NCAxLjI3MTIzLC0xLjQ1MzY0IDEuMjkxNzMsMCAxLjI5MTczLDEuNDUzNjQgdiA4LjUxNDE5IGggMC4wNDEgbCAzLjM4MzA5LC00LjAxNDgyIHEgMC40MTAwNywtMC40NjE0NyAwLjcxNzYzLC0wLjcxNTI5IDAuMzA3NTUsLTAuMjUzOCAwLjg0MDY0LC0wLjI1MzggMC41MzMxLDAgMC43OTk2NCwwLjMyMzAyIDAuMjg3MDYsMC4yOTk5NyAwLjI4NzA2LDAuNzM4MzUgMCwwLjQzODQgLTAuMzY5MDcsMC44NzY4IGwgLTMuMDc1NTQsMy42NDU2NiAzLjQyNDEsNC4xNTMyNSBxIDAuMzY5MDcsMC40Mzg0IDAuMzI4MDYsMC44OTk4OCAtMC4wMjA1LDAuNDM4NCAtMC4zMjgwNiwwLjczODM2IC0wLjMwNzU1LDAuMjc2ODggLTAuNzc5MTQsMC4yNzY4OCAtMC41NzQwOSwwIC0wLjkyMjY2LC0wLjI1MzggLTAuMzI4MDUsLTAuMjUzODIgLTAuNzM4MTMsLTAuNzYxNDQgbCAtMy41Njc2MywtNC4xOTk0MiBoIC0wLjA0MSB2IDMuNzM3OTQgcSAwLDEuNDc2NzIgLTEuMjkxNzMsMS40NzY3MiB6IgogICAgICAgc3R5bGU9ImZvbnQtc3R5bGU6bm9ybWFsO2ZvbnQtdmFyaWFudDpub3JtYWw7Zm9udC13ZWlnaHQ6Ym9sZDtmb250LXN0cmV0Y2g6bm9ybWFsO2ZvbnQtc2l6ZToxMC41ODMzcHg7bGluZS1oZWlnaHQ6MS4yNTtmb250LWZhbWlseTpOdW5pdG87LWlua3NjYXBlLWZvbnQtc3BlY2lmaWNhdGlvbjonTnVuaXRvLCBCb2xkJztmb250LXZhcmlhbnQtbGlnYXR1cmVzOm5vcm1hbDtmb250LXZhcmlhbnQtY2Fwczpub3JtYWw7Zm9udC12YXJpYW50LW51bWVyaWM6bm9ybWFsO2ZvbnQtdmFyaWFudC1lYXN0LWFzaWFuOm5vcm1hbDt3aGl0ZS1zcGFjZTpwcmU7ZmlsbDojMTU1NzI0O2ZpbGwtb3BhY2l0eToxO3N0cm9rZTojMDA0OTA3O3N0cm9rZS13aWR0aDowLjY2NjMyMTtzdHJva2UtbWl0ZXJsaW1pdDo0O3N0cm9rZS1kYXNoYXJyYXk6bm9uZTtzdHJva2Utb3BhY2l0eToxIgogICAgICAgaWQ9InBhdGgyMjQtOS03LTUiIC8+CiAgICA8cGF0aAogICAgICAgaWQ9InBhdGgyMjQtMzYtMyIKICAgICAgIHN0eWxlPSJmb250LXN0eWxlOm5vcm1hbDtmb250LXZhcmlhbnQ6bm9ybWFsO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zdHJldGNoOm5vcm1hbDtmb250LXNpemU6MTAuNTgzM3B4O2xpbmUtaGVpZ2h0OjEuMjU7Zm9udC1mYW1pbHk6TnVuaXRvOy1pbmtzY2FwZS1mb250LXNwZWNpZmljYXRpb246J051bml0bywgQm9sZCc7Zm9udC12YXJpYW50LWxpZ2F0dXJlczpub3JtYWw7Zm9udC12YXJpYW50LWNhcHM6bm9ybWFsO2ZvbnQtdmFyaWFudC1udW1lcmljOm5vcm1hbDtmb250LXZhcmlhbnQtZWFzdC1hc2lhbjpub3JtYWw7d2hpdGUtc3BhY2U6cHJlO2ZpbGw6IzRhYjU2MztmaWxsLW9wYWNpdHk6MTtzdHJva2U6IzAwNzIwMDtzdHJva2Utd2lkdGg6MC42NjYzMjE7c3Ryb2tlLWxpbmVjYXA6cm91bmQ7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLW9wYWNpdHk6MSIKICAgICAgIGQ9Im0gNDAuMzc0MzI2LDczLjAwNTAwOSBjIC0wLjc4ODA4LDAuMTM5NTkgLTEuNTI1MjUsMS4wNjU4MSAtMS4wNTA5NiwxLjgzODcxIDAuNDE1OTEsMC42NDk4NSAwLjk5MDk2LDEuMTgwNjIgMS40ODI4NywxLjc3MzIyIGwgNC42NTY2OCw1LjE0NTg2IGMgMC44NDIwMywtMC44Nzg3MiAzLjMwMTM3LC0zLjYwMjM4IDQuOTM0NiwtNS40MDU3MyAwLjQ2MDg5LC0wLjQ4ODg4IDAuNTAxNTcsLTEuMzIxNzcgLTAuMDYwMywtMS43NTU4MSAtMC40NjUwNywtMC41NTE2OSAtMS4zMzA0NywtMC41MzA5NSAtMS43OTM1MSwwLjAwOCAtMS4wMzM2NSwxLjEzMjUxIC0yLjAzMzI0LDIuMjk2ODYgLTMuMDUzNDEsMy40NDE5OCAtMS4zNTc1NywtMS41MzA0NCAtMi43MDAzMywtMy4wNzQ1NiAtNC4wNjczOCwtNC41OTYyNCAtMC4yNzQ2LC0wLjI3MTk4IC0wLjU3MjY4LC0wLjUzNDI4IC0wLjk3MTkxLC0wLjQ2MzU3IHoiCiAgICAgICBzb2RpcG9kaTpub2RldHlwZXM9InNjY2NjY2NjY3NzIiAvPgogIDwvZz4KPC9zdmc+Cg==")
                    .build()
            ))
            ;

        testSiteGeneration(docsite, "externalIcon", true);
    }


    @Test
    public void testHtmlIndex() throws IOException {

        Docsite docsite = new Docsite()
            .title("jExt")
            .index("src/test/resources/README.html")
            ;

        testSiteGeneration(docsite, "htmlIndex", true);

    }


    @Test
    public void testAsciidocIndex() throws IOException {
        Docsite docsite = new Docsite()
            .title("jExt")
            .index("src/test/resources/README.adoc")
            ;
        testSiteGeneration(docsite, "asciidoc", true);
    }


    @Test
    public void testTemplate() throws IOException {

        Docsite docsite = new Docsite()
            .title("jExt")
            .index("src/test/resources/README.html")
            .sections(List.of(
                Section.generated("maven")
                    .source("src/test/resources/plugin.xml")
                    .template("maven-plugin-descriptor")
                    .build()
            ));

        testSiteGeneration(docsite, "template", true);

    }



    private void testSiteGeneration(Docsite docsite, String outputFolderName, boolean useCDN)
    throws IOException {
        testSiteGeneration(docsite, ThemeColors.DEFAULT, null, outputFolderName, useCDN, null);
    }


    private void testSiteGeneration(Docsite docsite, String outputFolderName, boolean useCDN, List<String> languages)
    throws IOException {
        testSiteGeneration(docsite, ThemeColors.DEFAULT, null, outputFolderName, useCDN, languages);
    }


    private void testSiteGeneration(
        Docsite configuration,
        ThemeColors themeColors,
        Path cssFile,
        String outputFolderName,
        boolean useCDN,
        List<String> languages
    ) throws IOException {

        Path outputFolder = Path.of("target/testsites").resolve(outputFolderName);
        if (Files.exists(outputFolder)) {
            Files.walk(outputFolder)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }

        if (!Files.exists(outputFolder)) {
            Files.createDirectories(outputFolder);
        }


        Map<String, Map<String, String>> translations = Map.of(
            "en", Map.of (
                "title.description", "Description in English",
                "changelog", "Changelog"
            ),
            "es", Map.of (
                "title.description", "Descripción en Español",
                "changelog", "Historial"
            )
        );


        new DocsiteEmitter(
            configuration,
            themeColors,
            cssFile,
            useCDN,
            Path.of("."),
            outputFolder,
            new HashMap<>(),
            new ArrayList<>(),
            SiteLanguage.of(languages),
            translations
        ).generateSite();

        Logger.instance().info("file://"+outputFolder.resolve("index.html").toAbsolutePath().toString());
    }
}