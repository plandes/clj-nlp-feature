Natural Language Feature Creation
=================================

This library provides simple character and token based feature creation
functions.  For more rich feature library and examples of how to use this
library see the
[NLP parse Clojure library](https://github.com/plandes/clj-nlp-parse).

Obtaining
---------
In your `project.clj` file, add:

[![Clojars Project](https://clojars.org/com.zensols.nlp/feature/latest-version.svg)](https://clojars.org/com.zensols.nlp/feature/)

Documentation
-------------
Additional [documentation](https://plandes.github.io/clj-nlp-feature/codox/index.html).

Usage
-----
There are two utilities for looking up words:
* WordNet: wraps [this library](http://extjwnl.sourceforge.net)
* Word lists: English word lists taken from [this repo](https://github.com/dwyl/english-words)

Usage of these libraries are available as features with the
`dictionary-features` function found [here](https://plandes.github.io/clj-nlp-feature/codox/zensols.nlparse.feature.html#var-dictionary-features).

All other [word lists](ftp://ftp.gnu.org/gnu/aspell/dict/0index.html) come from
the [GNU Aspell](http://aspell.net) dictionaries.

License
--------
Copyright © 2016 Paul Landes

Apache License version 2.0

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
