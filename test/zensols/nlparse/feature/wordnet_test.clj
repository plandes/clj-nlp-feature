(ns zensols.nlparse.feature.wordnet-test
  (:require [clojure.test :refer :all])
  (:require [clojure.string :as s]
            [zensols.nlparse.wordnet :refer :all]))

(deftest repeat-test
  (testing "repeating chars"
    (let [iws (lookup-word "cat")]
      (is (= 2 (count iws)))
      (is (= "cat" (.getLemma (first iws)))))))
