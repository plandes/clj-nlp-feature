(ns zensols.nlparse.feature.combine
  (:require [clojure.tools.logging :as log]))

(def ^{:dynamic true :private true}
  combined-features
  "Stores the intermediate results of the combined features to avoid
  re-computation."
  nil)

(defmacro with-combined-features
  "Create a lexical context of combined features.  Wrap this around all of your
  feature generation function calls.  To be used with [[combine-features]]."
  {:style/indent 0}
  [& forms]
  `(binding [combined-features (atom {})]
     (do ~@forms)))

(defn combine-feature-values
  "Apply feature creation functions only where they don't already exist
  in [[combined-features]]."
  [fmap]
  (log/tracef "combine feature values for %s" (pr-str fmap))
  (let [cfeats (if combined-features @combined-features)
        fmap (->> fmap
                  (map (fn [[fkey form]]
                         (log/tracef "%s => %s" fkey (get cfeats fkey))
                         (let [feats (or (and cfeats (get cfeats fkey))
                                         (doall ((eval form))))]
                           {fkey feats})))
                  (apply merge))]
    (when combined-features
      (swap! combined-features merge fmap)
      (log/debugf "combined: %s" (pr-str @combined-features)))
    (->> fmap vals (apply merge))))

(defmacro combine-features
  "Combine features from the forms for which features don't already exist in
  **features**, which is a map with functions as keys and features already
  created with that function as the value of the respective function.

  Limitations: each form must be a function call and not a macro or any other
  special form."
  [& forms]
  (log/debugf "macro combine features for %s" (pr-str forms))
  ;; var-get was used below where `.toString` is currently but in the call to
  ;; `combine-feature-values` yielded differn functions (memory locations) as
  ;; if they were re-evaled for every call--maybe this is some Clojure language
  ;; speed up method at play; remains a mystery
  `(->> ~@(->> forms
               (map (fn [form]
                      (->> form first
                           (ns-resolve (ns-name *ns*))
                           .toString
                           (#(hash-map % (read-string
                                          (str "#" (pr-str form))))))))
               (apply merge)
               (#(do (log/tracef "fmap macro: %s" (pr-str %)) %))
               list)
        combine-feature-values))
