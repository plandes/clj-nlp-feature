# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).


## [Unreleased]
### Changed
- Better top level documentation.
- Move to MIT license.

## [0.0.10] - 2018-03-22
### Changed
- Updated map backed dictionary to (non)-snapshot version.


## [0.0.9] - 2017-11-02
### Added
- In memory implementation of dictionary (MapBackedDictionary implementation).
- Feature to bail from WordNet dictionary lookups on tokens that are too long.

### Changed
- Dictionary creation configuration options.


## [0.0.8] - 2017-10-27
### Changed
- Lazy sequence in combine fix.
- Tighter control on Wordnet resources.
- Actioncli deps

## [0.0.7] - 2017-10-17
### Changed
- Guard locale unicode to locale data structure creation.
- Dependencies and stricter log dep exclusions.

## [0.0.6] - 2017-04-27
### Added
- Travis build

### Changed
- Documentation (meta used by other packages with deps on this package).


## [0.0.5] - 2017-02-02
### Added
- Changelog
- Moved to lein-git-version 1.2.7


## [0.0.4] - 2016-12-14
## Added
- More logging


## [0.0.3] - 2016-10-28
### Changed
- Moving combined feature creation to feature repo.
- Default arguments to feature creation functions.


## [0.0.2] - 2016-10-23
First major release.

## Added
- Unicode features


[Unreleased]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.10...HEAD
[0.0.10]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.9...v0.0.10
[0.0.9]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.8...v0.0.9
[0.0.8]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.7...v0.0.8
[0.0.7]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.6...v0.0.7
[0.0.6]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.5...v0.0.6
[0.0.5]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.4...v0.0.5
[0.0.4]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.3...v0.0.4
[0.0.3]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.2...v0.0.3
[0.0.2]: https://github.com/plandes/clj-nlp-feature/compare/v0.0.1...v0.0.2
