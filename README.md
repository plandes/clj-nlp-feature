# Natural Language Feature Creation

[![Travis CI Build Status][travis-badge]][travis-link]

  [travis-link]: https://travis-ci.org/plandes/clj-nlp-feature
  [travis-badge]: https://travis-ci.org/plandes/clj-nlp-feature.svg?branch=master

This library provides simple character and token based feature creation
functions.  For additiona feature libraries and examples of how to use this
library see the [NLP parse library](https://github.com/plandes/clj-nlp-parse).

Features (creation):
* [WordNet]
  * [Dictionary features](https://plandes.github.io/clj-nlp-feature/codox/zensols.nlparse.feature.word.html#var-dictionary-features)
* [Token statistics]:
  * Average character length
  * Mention count
  * Sentence count
  * Stopword count
  * Interrogative indication
* [Character statistics]:
  * Capital tokens
  * Punctuation
  * Unicode
  * Repeating characters
  * Latin vs. Non-latin character sets
* [Feature utilities]
  * End/begin of sentence
  * Ratio functions

<!-- markdown-toc start - Don't edit this section. Run M-x markdown-toc-refresh-toc -->
## Table of Contents

- [Obtaining](#obtaining)
- [Documentation](#documentation)
- [Usage](#usage)
- [Citation](#citation)
- [Building](#building)
- [Changelog](#changelog)
- [Citation](#citation-1)
- [References](#references)
- [License](#license)

<!-- markdown-toc end -->


## Obtaining

In your `project.clj` file, add:

[![Clojars Project](https://clojars.org/com.zensols.nlp/feature/latest-version.svg)](https://clojars.org/com.zensols.nlp/feature/)

## Documentation


API documentation:
* [Clojure](https://plandes.github.io/clj-nlp-feature/codox/index.html)
* [Java](https://plandes.github.io/clj-nlp-feature/apidocs/index.html)


## Usage

The following illustrates how to create character and token based features:
```clojure
(:require [zensols.nlparse.feature.char :as cf]
          [zensols.nlparse.feature.word :as w])

(defn- tokenize [utterance]
  (->> (s/split utterance #"\s+")
       (map #(hash-map :text %))))

(defn calc-feature-1 [tokens]
  (log/debugf "calculating features for <%s>" (pr-str tokens))
  (merge (cf/capital-features tokens)
         (cf/unicode-features tokens 1)))

;; in a different namespace to calculate features for a different model...
(defn calc-feature-2 [tokens]
  (log/debugf "calculating features for <%s>" (pr-str tokens))
  (merge (cf/capital-features tokens)    
         (w/dictionary-features tokens)))

(let [tokens (->> "My name is Paul" tokenize)
      f1-features (calc-feature-1 tokens)
      f2-features (calc-feature-2 tokens)]
  (clojure.pprint/pprint {:f1 f1-features
                          :f2 f2-features}))
```

In this example, we're creating features for two different models in the
`calc-features-*` functions.  This is common where there are some common
features between models.  However, we're recalculating the capital case
features in `cf/capital-features`.  We have to do this in case where our
feature generation is in different namespaces or even different libraries/jars.

Fortunately, this library provides a way to avoid recreating these features as
shown below:
```clojure
(defn calc-feature-1 [tokens]
  (log/debugf "calculating features for <%s>" (pr-str tokens))
  (c/combine-features (cf/capital-features tokens)
                      (cf/unicode-features tokens 1)))

;; in a different namespace to calculate features for a different model...
(defn calc-feature-2 [tokens]
  (log/debugf "calculating features for <%s>" (pr-str tokens))
  (c/combine-features (cf/capital-features tokens)    
                      (w/dictionary-features tokens)))

(let [tokens (->> "My name is Paul" tokenize)
      f1-features (calc-feature-1 tokens)
      f2-features (calc-feature-2 tokens)]
  (clojure.pprint/pprint {:f1 f1-features
                          :f2 f2-features}))
```
We replace `merge` with `c/combine-features`, which adds these features to an
atom with a map.  For those features that are already created, namely
`cf/capital-features`, the function is not invoked a second time and uses the
value in the map in the atom.


## Citation

There are two utilities for looking up words:
* [WordNet]: wraps [this library](http://extjwnl.sourceforge.net)
* Word lists: English word lists taken from [this repo](https://github.com/dwyl/english-words)

Usage of these libraries are available as features with the
`dictionary-features` function found [here](https://plandes.github.io/clj-nlp-feature/codox/zensols.nlparse.feature.word.html#var-dictionary-features).

All other [word lists](ftp://ftp.gnu.org/gnu/aspell/dict/0index.html) come from
the [GNU Aspell](http://aspell.net) dictionaries.


## Building

To build from source, do the folling:

- Install [Leiningen](http://leiningen.org) (this is just a script)
- Install [GNU make](https://www.gnu.org/software/make/)
- Install [Git](https://git-scm.com)
- Download the source: `git clone https://github.com/clj-nlp-feature && cd clj-nlp-feature`
- Download the make include files:
```bash
mkdir ../clj-zenbuild && wget -O - https://api.github.com/repos/plandes/clj-zenbuild/tarball | tar zxfv - -C ../clj-zenbuild --strip-components 1
```
- Compile: `make compile` do compile or `make install` to install in your local
  maven repo.


## Changelog

An extensive changelog is available [here](CHANGELOG.md).


## Citation

If you use this software in your research, please cite with the following
BibTeX:

```jflex
@misc{plandes-clj-nlp-feature,
  author = {Paul Landes},
  title = {Natural Language Feature Creation},
  year = {2018},
  publisher = {GitHub},
  journal = {GitHub repository},
  howpublished = {\url{https://github.com/plandes/clj-nlp-feature}}
}
```


## References

```jflex
@Book{wordnet1998,
  title = {WordNet: An Electronic Lexical Database},
  author = {Christiane Fellbaum},
  year = {1998},
  publisher = {Bradford Books},
}
```


## License

Copyright © 2016, 2017, 2018 Paul Landes

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


<!-- links -->
[WordNet]: https://wordnet.princeton.edu
[Token statistics]: https://plandes.github.io/clj-nlp-feature/codox/zensols.nlparse.feature.word.html
[Character statistics]: https://plandes.github.io/clj-nlp-feature/codox/zensols.nlparse.feature.char.html
[Feature utilities]: https://plandes.github.io/clj-nlp-feature/codox/zensols.nlparse.feature.util.html
