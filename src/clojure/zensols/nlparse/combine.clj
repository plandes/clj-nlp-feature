(ns zensols.nlparse.combine
  (:require [clojure.tools.logging :as log]))

(def ^{:dynamic true :private true}
  combined-features nil)

(defmacro with-combined-features
  {:style/indent 0}
  [& forms]
  `(binding [combined-features (atom {})]
     (do ~@forms)))

(defn combine-feature-vals
  "Apply feature creation functions only where they don't already exist
  in [[combined-features]]."
  [fmap]
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
  `(->> (merge ~@(->> (map (fn [form]
                             (->> form first
                                  (ns-resolve (ns-name *ns*))
                                  var-get
                                  (#(hash-map % (read-string
                                                 (str "#" (pr-str form)))))))
                           forms)
                      doall))
        combine-feature-vals))
