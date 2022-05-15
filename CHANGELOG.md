Changelog
==========================================================================

All notable changes to this project will be documented in this file.


The format is based on [Keep a Changelog][1],
and this project adheres to [Semantic Versioning][2].


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