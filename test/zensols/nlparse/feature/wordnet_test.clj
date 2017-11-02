(ns zensols.nlparse.feature.wordnet-test
  (:require [clojure.test :refer :all])
  (:require [clojure.string :as s]
            [zensols.nlparse.wordnet :refer :all :as w]))

(deftest repeat-test
  (testing "repeating chars"
    (let [iws (lookup-word "cat")]
      (is (= 2 (count iws)))
      (is (= "cat" (.getLemma (first iws)))))))

(deftest create-test
  (testing "dict type"
    (with-dictionary dict
      (is (instance? net.sf.extjwnl.dictionary.MapBackedDictionary dict)))
    (with-context (create-dictionary-context :default)
      (with-dictionary dict
        (is (instance? net.sf.extjwnl.dictionary.FileBackedDictionary dict))))))

