Changelog
==========================================================================

All notable changes to this project will be documented in this file.


The format is based on [Keep a Changelog][1],
and this project adheres to [Semantic Versioning][2].

[1.5.2]
--------------------------------------------------------------------------
**Release date:** 2022-09-14
### Modified
- Bump `snakeyaml` to version 1.32
### Fixed
- [Localization not working for multi-module projects](https://github.com/luiinge/docsite-maven-plugin/issues/4)


[1.5.1]
--------------------------------------------------------------------------
**Release date:** 2022-07-17
### Fixed
- Fixed issue regarding the expanded menu with many sections

[1.5.0]
--------------------------------------------------------------------------
**Release date:** 2022-07-10
### Added
- Asciidoc support
### Modified
- Automatic switch between expanded menu and collapsed menu
according client width



[1.4.0]
--------------------------------------------------------------------------
**Release date:** 2022-06-26
### Added
- Render [Mermaid](https://mermaid-js.github.io/) diagrams embedded in Markdown
### Modified
- Rework of menus for better responsiveness
- Language selection now uses names instead of flags
- `<h1>` tags removed from content fragments
### Fixed
- Bumped version of `maven-shared-utils` to `3.3.4` [CVE-2022-29599](https://nvd.nist.gov/vuln/detail/CVE-2022-29599)


[1.3.0]
--------------------------------------------------------------------------
**Release date:** 2022-05-08
### Added
- Multi-localization capabilities
### Modified
- Minor tweaks in the default stylesheet.



[1.2.2]
--------------------------------------------------------------------------
**Release date:** 2022-04-10
### Fixed
- The Maven build no longer fails when the `generate` goal is invoked without configuration
and a README file is not present
- Links were not generated for Markdown headers that included code marks
- Fixed several problems when using the `aggregate` goal
- Fixed bug that prevented using the `themeColors` property in the pom configuration
- Fixed goal table in the built-in Maven plugin descriptor template
- Index pages included an unexpected '- index' in the title

[1.2.1]
--------------------------------------------------------------------------
**Release date:** 2022-03-26
### Fixed
- Fixed header navigation layout that caused overflow in small screens

[1.2.0]
--------------------------------------------------------------------------
**Release date:** 2022-03-25
### Added
- New goal `aggregate` for multi-module projects
- Optional *Company information* section within the header
- New section type `copy`, similar to `embedded` but opening a new tab instead of a `<iframe>`
embedded page
- Icons now can be specified as data values using the expression `data:/image/...`

### Fixed
- Markdown tables are now correctly rendered
- Fixed header navigation layout that caused overflow in small screens



[1.1.0]
--------------------------------------------------------------------------
**Release date:** 2022-01-06

### Added
- Added `metadata` and `scripts` elements to the site configuration
(useful for adding analytic scripts)
- Added `favicon` to the site configuration
- Added a *jump to content* button when navigating with the `Tab` key
- HTML `blockquote` tags now have differentiated style

[1.0.0] 
--------------------------------------------------------------------------
**Release date:** 2021-12-17

- Initial release.  


[1]: <https://keepachangelog.com>
[2]: <https://semver.org/>