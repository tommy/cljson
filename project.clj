(defproject cljson "0.1.0-SNAPSHOT"
  :description "Little web tool to convert between JSON and EDN."
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2755"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.omcljs/om "0.8.8"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [org.clojure/data.json "0.2.5"]
            [lein-s3-static-deploy "0.1.0"]]

  :aws {:access-key ~(-> "secret.edn" slurp read-string :access)
        :secret-key ~(-> "secret.edn" slurp read-string :secret)
        :s3-static-deploy {:bucket "cljson.com"
                           :local-root "public"}}

  :source-paths ["src" "target/classes"]

  :clean-targets ["public/build/cljson" "public/build/cljson.js"]

  :cljsbuild {
    :builds [{:id "cljson"
              :source-paths ["src"]
              :compiler {
                :output-to "public/build/cljson.js"
                :output-dir "public/build"
                :optimizations :advanced
                :cache-analysis true
                }}]})
