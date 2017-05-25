(ns ^{:doc "Utility functions for feature creation"
      :author "Paul Landes"}
    zensols.nlparse.feature.util
  (:require [clojure.tools.logging :as log]
            [clojure.string :as s]))

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

(defn ratio-0-if-empty
  "Return a ratio or 0 if **b** is 0 to guard against divide by zero."
  [a b]
  (if (= 0 b)
    0
    (/ a b)))

(defn ratio-neg-if-empty
  "Return a ratio or -1 if **b** is 0 to guard against divide by zero."
  [a b]
  (if (= 0 b)
    -1
    (/ a b)))

(defn ratio-true
  "Return the ratio of **items** whose evaluation of **true-fn** is `true`.
  If **items** is empty return 0."
  [items true-fn]
  (let [cnt (count items)]
    (if (= cnt 0)
      0
      (/ (->> items (map true-fn)
              (filter true?) count)
         cnt))))

(defn or-empty-0
  "If sequence **seq** is non-empty, apply **afn** to seq.  Otherwise return 0."
  [seq afn]
  (if (or (not seq) (empty? seq) (nil? (first seq)))
    0
    (afn seq)))
