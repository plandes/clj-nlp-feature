(ns ^{:doc "Wraps the [Extended Java WordNet Library](http://extjwnl.sourceforge.net/javadocs/)."
      :author "Paul Landes"}
    zensols.nlparse.wordnet
  (:import [net.sf.extjwnl.dictionary Dictionary]
           [net.sf.extjwnl.data.relationship RelationshipFinder]
           [net.sf.extjwnl.data POS PointerType])
  (:require [clojure.string :as str]))

;; ;RelationshipList list = RelationshipFinder.findRelationships(start.getSenses().get(0), end.getSenses().get(0), PointerType.SIMILAR_TO
;; (with-dictionary dict
;;   (let [w1 (first (lookup-word "dog"))
;;         w2 (first (lookup-word "canine"))]
;;     (RelationshipFinder/findRelationships
;;      (first (.getSenses w1))
;;      (first (.getSenses w2)) PointerType/SIMILAR_TO)))

(def ^:private word-pattern (re-pattern "^\\w+$"))

;;; wordnet
(def pos-tag-any
  "Special POS tag to indicate any POS tag (see [[has-pos-tag?]])"
  "any")

(def pos-adjective
  "The adjective wordnet API POS tag."
  POS/ADJECTIVE)

(def pos-adverb
  "The adverb wordnet API POS tag."
  POS/ADVERB)

(def pos-noun
  "The noun wordnet API POS tag."
  POS/NOUN)

(def pos-verb
  "The verb wordnet API POS tag."
  POS/VERB)

(def pos-tags
  "All wordnet tags, which
  include [[pos-verb]], [[pos-noun]], [[pos-adverb]], [[pos-adjective]]."
  (POS/getAllPOS))

(defn create-dictionary-context
  "Create a dictionray context to change the behavior of WordNet lookups.

  The type of dictionary is determined by **resource**, which can be:

  * **:map** *MapBackedDictionary*, which initializes with the dictionary.
  This is faster but takes more memory.
  * **:file** *FileBackedDictionary*, which hits a file (resource in our case)
  for each lookup.  This is slower but takes less memory.
  * **:default** Same as **:map** and default with 0 arg invocation.

  Note that if a token (word or lemma) is not valid per [[valid-token?]] then
  any of the lookup function (i.e. [[lookup-word]]) will return an empty list,
  `nil`, or `false` depending on the function.  See the [keys](#keys) section
  for more inforamtion.


  ## Keys

  * **:max-length** the maximum length or token that prevents a lookup"
  ([] (create-dictionary-context :default))
  ([resource & {:keys [max-length]}]
   (->> (cond (= :map resource)
              (->> "/net/sf/extjwnl/data/wordnet/wn31/map/res_properties.xml"
                   Dictionary/getResourceInstance)
              (= :file resource)
              (->> "/net/sf/extjwnl/data/wordnet/wn31/res_properties.xml"
                   Dictionary/getResourceInstance)
              (= :default resource)
              (Dictionary/getDefaultResourceInstance)
              (string? resource)
              (Dictionary/getResourceInstance resource)
              true (-> (format "Unknown resource: %s" resource)
                       (ex-info {:resource resource})
                       throw))
        (array-map :max-length max-length :instance))))

(def ^:dynamic *dictionary-context*
  "Frameowrk specific variable--use [[with-context]] instead of modifying this
  variable."
  (create-dictionary-context))

(defmacro with-context
  "Modify the default WordNet dictionary functionality by using a context
  created with [[create-dictionary-contxt]]."
  {:style/indent 1}
  [context & forms]
  `(binding [*dictionary-context* ~context]
     ~@forms))

(defmacro with-dictionary
  "Use a dictionary and set the symbol **dict-sym**.

  Example usage:
  ```
  (with-dictionary dict
    (.lookupAllIndexWords dict))
  ```"
  {:style/indent 1}
  [dict-sym & forms]
  `(let [~dict-sym (->> *dictionary-context* :instance)]
     ~@forms))

(defn valid-token?
  "Return whether or not **token** is not valid for lookup."
  [token]
  (let [{:keys [max-length]} *dictionary-context*]
    (or (nil? max-length) (< (count token) max-length))))

(defn lookup-word
  "Lookup a word (lemmatized) in wordnet.

  * **pos-tag** type of POS/VERB and one of the pos-noun,verb etc"
  ([lemma]
   (if-not (valid-token? lemma)
     []
     (with-dictionary dict
       (->> (.lookupAllIndexWords dict lemma)
            .getIndexWordCollection
            (into [])))))
  ([lemma pos-tag]
   (if-not (valid-token? lemma)
     []
     (with-dictionary dict
       [(.lookupIndexWord dict pos-tag lemma)]))))

(defn synonyms
  "Get all synonyms for a lemma and return as word objects.
  This uses [[lookup-word]] to get the words, then returns synset words.

  See [[lookup-word]] for **args** documentation."
  [& args]
  (with-dictionary dict
    (->> (apply lookup-word args)
         (mapcat #(.getSenses %))
         (mapcat #(.getWords %)))))

(defn verb-frame-flags
  "If **synset** is a verb type synset return its verb frame flags.  Otherwise
  return nil."
  [synset]
  (if (and synset (instance? net.sf.extjwnl.data.VerbSynset synset))
    (.getVerbFrameFlags synset)))

(defn adjective-cluster?
  [synset]
  (and synset
       (instance? net.sf.extjwnl.data.AdjectiveSynset synset)
       (.isAdjectiveCluster synset)))

(defn looks-like-word? [lemma]
  (and lemma (not (nil? (re-find word-pattern lemma)))))

(defn in-dictionary?
  "Return whether or not a lemmatized word is in WordNet."
  [lemma]
  (and (looks-like-word? lemma)
       (not (empty? (lookup-word lemma)))))

(defn pos-tag-set
  "Return all the POS tags found for a lemmatized word."
  [lemma-or-indexed-word]
  (let [iws (if (string? lemma-or-indexed-word)
              (lookup-word lemma-or-indexed-word)
              lemma-or-indexed-word)]
    (into #{} (map #(-> % .getPOS .getLabel) iws))))

(defn has-pos-tag?
  "Return whether or not the lemmatized word has exists for a POS tag.

  * **lemma** the lemma (or word) to look up in wordnet
  * **pos-tag-name** the name of the pos tag or [[pos-tag-any]]"
  [lemma pos-tag-name]
  (let [iws (lookup-word lemma)]
    (if (= pos-tag-any pos-tag-name)
      (not (empty? iws))
      (contains? (pos-tag-set iws) pos-tag-name))))

(defn wordnet-pos-labels
  "Return all word used POS tags."
  []
  (map #(.getLabel %) (POS/values)))

(defn lookup-word-by-sense
  "Lookup a word by verbnet *old* sense key.

  For example `buy%2:40:00::` maps to
  [buy/bought](http://wordnet-rdf.princeton.edu/wn31/buy-v)."
  [sense-key]
  (with-dictionary dict
    (.getWordBySenseKey dict sense-key)))

(defn present-tense-verb?
  "Return whether **word** looks like a present tense word."
  [word]
  (let [lword (str/lower-case word)]
    (if-not (valid-token? word)
      false
      (with-dictionary dict
        (let [iword (.lookupIndexWord dict POS/VERB lword)]
          (if-not iword
            false
            (= lword (.getKey iword))))))))

(defn to-present-tense-verb
  "Return the present tense of **verb**."
  [word]
  (with-dictionary dict
    (if (valid-token? word)
      (let [iw (.lookupIndexWord dict POS/VERB word)]
        (if iw (.getLemma iw))))))
