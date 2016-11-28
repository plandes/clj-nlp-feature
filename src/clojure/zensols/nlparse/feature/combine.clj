(ns zensols.nlparse.feature.combine
  (:require [clojure.tools.logging :as log]))

(def ^{:dynamic true :private true}
  combined-features
  "Stores the intermediate results of the combined features to avoid
  re-computation."
  nil)

(defmacro with-combined-features
  "Create a lexical context of combined features.  Wrap this around all of your
  feature genertion function calls.  To be used with [[combine-features]]."
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
                          (let [feats (or (and cfeats (get cfeats fkey))
                                          ((eval form)))]
                            {fkey feats})))
                  (apply merge))]
    (when combined-features
      (log/tracef "combined: %s" (pr-str @combined-features))
      (swap! combined-features merge fmap))
    (->> fmap vals (apply merge))))

(defmacro combine-features
  "Combine features from the forms for which features don't already exist in
  **features**, which is a map with functions as keys and features already
  created with that function as the value of the respective function."
  [& forms]
  (log/tracef "macro combine features for %s" (pr-str forms))
  `(->> (merge ~@(->> (map (fn [form]
                             (->> form first
                                  (ns-resolve (ns-name *ns*))
                                  var-get
                                  (#(hash-map % (read-string
                                                 (str "#" (pr-str form)))))))
                           forms)
                      doall))
        combine-feature-values))
