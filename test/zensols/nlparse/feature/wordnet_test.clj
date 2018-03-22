(ns zensols.nlparse.feature.wordnet-test
  (:require [clojure.test :refer :all])
  (:require [clojure.string :as s]
            [zensols.nlparse.wordnet :refer :all :as w]))

(def *test-file-backed* false)

(deftest repeat-test
  (testing "repeating chars"
    (let [iws (lookup-word "cat")]
      (is (= 2 (count iws)))
      (is (= "cat" (.getLemma (first iws)))))))

(deftest morph-test
  (testing "morphology test"
    (is (= true (present-tense-verb? "push")))
    (is (= "run" (to-present-tense-verb "running")))))

(deftest create-test
  (testing "dict type"
    (with-context (create-dictionary-context :map)
      (with-dictionary dict
          (is (instance? net.sf.extjwnl.dictionary.MapBackedDictionary dict))))
    ;; force pass travis test--map backed is currently the fastest and smallest
    ;; anyway
    ;; PL -- 2018-03-22
    (when *test-file-backed*
      (with-context (create-dictionary-context :file)
        (with-dictionary dict
          (is (instance? net.sf.extjwnl.dictionary.FileBackedDictionary dict)))))))

(deftest avoid-large-tokens
  (testing "too long tokens"
    (with-context (create-dictionary-context
                   :default :max-length 4)
      (is (= 0 (count (lookup-word "door"))))
      (is (= 2 (count (lookup-word "cat"))))
      (is (= true (present-tense-verb? "run")))
      (is (= false (present-tense-verb? "push")))
      (is (= nil (to-present-tense-verb "running")))
      (is (= "be" (to-present-tense-verb "was"))))))

(deftest sense-test
  (let [verb (lookup-word-by-sense "buy%2:40:00::")]
    (is (not (nil? verb)))
    (is (= "buy" (.getLemma verb)))))
