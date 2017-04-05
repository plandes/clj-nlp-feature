(defproject com.zensols.nlp/feature "0.1.0-SNAPSHOT"
  :description "A utility library for language feature creation."
  :url "https://github.com/plandes/clj-nlp-parse"
  :license {:name "Apache License version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0"
            :distribution :repo}
  :plugins [[lein-codox "0.10.0"]
            [org.clojars.cvillecsteele/lein-git-version "1.2.7"]]
  :codox {:metadata {:doc/format :markdown}
          :project {:name "NLP Feature Creation"}
          :output-path "target/doc/codox"}
  :git-version {:root-ns "zensols.nlparse.version"
                :path "src/clojure/zensols/nlparse/version"
                :version-cmd "git describe --match v*.* --abbrev=4 --dirty=-dirty"}
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :javac-options ["-Xlint:unchecked"]
  :jar-exclusions [#".gitignore"]
  :dependencies [[org.clojure/clojure "1.8.0"]

                 ;; command line
                 [com.zensols.tools/actioncli "0.0.15"]

                 ;; language name to locale
                 [com.neovisionaries/nv-i18n "1.11"]

                 ;; resource data parsing
                 [org.clojure/data.csv "0.1.2"]

                 ;; feature stats
                 [net.mikera/core.matrix.stats "0.7.0"]

                 ;; NLP
                 ;; wordnet
                 [net.sf.extjwnl/extjwnl "1.9"]
                 [net.sf.extjwnl/extjwnl-data-wn31 "1.2"]]
  :profiles {:appassem {:aot :all}
             :snapshot {:git-version {:version-cmd "echo -snapshot"}}
             :dev
             {:jvm-opts
              ["-Dlog4j.configurationFile=test-resources/log4j2.xml" "-Xms4g" "-Xmx12g" "-XX:+UseConcMarkSweepGC"]
              :exclusions [org.slf4j/slf4j-log4j12]
              :dependencies [[org.apache.logging.log4j/log4j-core "2.7"]
                             [org.apache.logging.log4j/log4j-slf4j-impl "2.7"]
                             [com.zensols/clj-append "1.0.5"]]}})
