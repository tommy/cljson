(defproject cljson "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2755"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.omcljs/om "0.8.8"]]

  :plugins [[lein-cljsbuild "1.0.4"]]

  :source-paths ["src" "target/classes"]

  :clean-targets ["out/cljson" "out/cljson.js"]

  :cljsbuild {
    :builds [{:id "cljson"
              :source-paths ["src"]
              :compiler {
                :output-to "out/cljson.js"
                :output-dir "out"
                :optimizations :none
                :cache-analysis true
                :source-map true}}]})
