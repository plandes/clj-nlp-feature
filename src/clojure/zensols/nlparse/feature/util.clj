(ns ^{:doc "Utility functions for feature creation"
      :author "Paul Landes"}
    zensols.nlparse.feature.util
  (:require [clojure.tools.logging :as log]
            [clojure.string :as s])
  (:require [zensols.nlparse.wordnet :as wn]
            [zensols.nlparse.wordlist :as wl]))

(def none-label
  "Value used for missing features."
  "<none>")

(def beginning-of-sentence-label
  "Beginning of sentence marker."
  "<bos>")

(def end-of-sentence-label
  "End of sentence marker."
  "<eos>")

;; util
(defn upper-case? [text]
 (= (count text)
    (count (take-while #(Character/isUpperCase %) text))))

(defn lc-null
  "Return the lower case version of a string or nil if nil given."
  [str]
  (if str (s/lower-case str)))

(defn or-none
  "Return **str** if non-`nil` or otherwise the sepcial [[none-label]]."
  [str]
  (or str none-label))

(defn or-0
  "Call and return the value given by **val-fn** iff **check** is non-`nil`,
  otherwise return 0."
  ([val]
   (or val 0))
  ([check val-fn]
   (if check (val-fn check) 0)))

(defn ratio-true
  "Return the ratio of **items** whose evaluation of **true-fn** is `true`."
  [items true-fn]
  (/ (->> items (map true-fn)
          (filter true?) count)
     (count items)))
