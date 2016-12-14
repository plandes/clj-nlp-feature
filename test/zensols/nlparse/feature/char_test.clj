(ns zensols.nlparse.feature.char-test
  (:require [clojure.test :refer :all])
  (:require [clojure.string :as s]
            [zensols.nlparse.feature.char :refer :all]))

(deftest repeat-test
  (testing "repeating chars"
    (is (= {:lrs-len 2 :lrs-unique-chars 1 :lrs-occurs-1 3 :lrs-occurs-ratio-1 1 :lrs-length-1 1}
           (lrs-features "aaa" 1)))
    (is (= {:lrs-len 4 :lrs-unique-chars 2 :lrs-occurs-1 1 :lrs-occurs-ratio-1 1/6 :lrs-length-1 1}
           (lrs-features "ababab" 1)))
    (is (= {:lrs-len 14 :lrs-unique-chars 7 :lrs-occurs-1 6 :lrs-occurs-ratio-1 6/53 :lrs-length-1 1}
           (lrs-features "abcabc aabb aaaaaa abcabcabcabc abcdefgabcdefgabcdefg" 1)))))

(deftest dist-test
  (testing "char distribution test"
    (is (= {:char-dist-unique 1 :char-dist-unique-ratio 1/3 :char-dist-count 3 :char-dist-variance -1 :char-dist-mean 3}
           (char-dist-features "aaa")))
    (is (= {:char-dist-unique 2 :char-dist-unique-ratio 1/3 :char-dist-count 6 :char-dist-variance 0.0 :char-dist-mean 3}
           (char-dist-features "ababab")))))
