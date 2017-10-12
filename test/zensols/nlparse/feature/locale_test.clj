(ns zensols.nlparse.feature.locale-test
  (:require [clojure.test :refer :all])
  (:require [clojure.string :as s]
            [zensols.nlparse.locale :refer :all]))

(deftest locale-test
  (testing "repeating chars"
    (is (= '({:name "latin", :range [32 591]})
           (unicode-for-char \a)))))

