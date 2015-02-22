(ns cljson.connect
  (:require [clojure.browser.repl :as repl]))

(.log js/console "Testing.")
(repl/connect "http://localhost:9000/repl")
