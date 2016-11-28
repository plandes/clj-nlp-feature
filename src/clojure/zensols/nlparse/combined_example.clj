(ns zensols.nlparse.combined-example
  (:require [clojure.string :as s]
            [clojure.tools.logging :as log])
  (:require [zensols.nlparse.feature.combine :refer (with-combined-features) :as c]
            [zensols.nlparse.feature.char :as cf]
            [zensols.nlparse.feature.word :as w]))

(defn- tokenize [utterance]
  (->> (s/split utterance #"\s+")
       (map #(hash-map :text %))))

(defn calc-feature-1 [tokens]
  (c/combine-features (cf/capital-features tokens)
                      (cf/unicode-features tokens 1)))

;; different namespace...
(defn calc-feature-2 [tokens]
  (c/combine-features (cf/capital-features tokens)    
                      (w/dictionary-features tokens)))

(with-combined-features
  (let [tokens (->> "My name is Paul" tokenize)
        f1-features (calc-feature-1 tokens)
        f2-features (calc-feature-2 tokens)]
    (clojure.pprint/pprint {:f1 f1-features :f2 f2-features})))
