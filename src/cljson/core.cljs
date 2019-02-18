(ns ^:figwheel-hooks cljson.core
  (:require
    [cljs.reader :refer [read-string]]
    [cljs.pprint :as pprint]
    [reagent.core :as r]))

(enable-console-print!)

;;;;;;;;;;;;;;;;;;;;
;; parsing functions

(defn parse-json
  [s]
  (try
    (-> s
        JSON.parse
        (js->clj :keywordize-keys true))
    (catch :default e nil)))

(defn parse-edn
  [s]
  (try
    (read-string s)
    (catch :default e nil)))

;;;;;;;;;;;;;;;;;;;;;
;; printing functions

(def to-json
  (comp #(JSON.stringify % nil 2)
        clj->js))

(def to-edn #(with-out-str (pprint/pprint %)))


;;;

(def edn-opts
  {:placeholder "EDN"
   :text (r/atom "")
   :parse-fn parse-edn
   :print-fn to-edn})

(def json-opts
  {:placeholder "JSON"
   :text (r/atom "")
   :parse-fn parse-json
   :print-fn to-json})

(defn input-component
  [opts other]
  (println "re-rendering " (:placeholder opts))
  [:textarea {:style {:border "none"
                      :height "80%"
                      :width "calc(50% - 1em)"
                      :marginRight "1em"
                      :fontFamily "Menlo, Monospace"}
              :placeholder (:placeholder opts)
              :value @(:text opts)
              :onChange #(do (reset! (:text opts) (-> % .-target .-value))
                             (when ((:parse-fn opts) @(:text opts))
                               (reset! (:text other) ((:print-fn other) ((:parse-fn opts) @(:text opts))))))}])

(defn application-component
  []
  (println "re-rendering application")
  [:div {:style {:maxWidth "50em"
                 :margin "auto"
                 :padding "1em 1em"
                 :textAlign "center"
                 :height "100%"}}
   [:div {:style {:textAlign "left"
                  :fontSize "larger"
                  :margin "0.25em 0"}}
    "Clojure/EDN \u27fa JSON"]
   [input-component edn-opts json-opts]
   [input-component json-opts edn-opts]
   [:div {:style {:textAlign "right"
                  :margin "0.25em 0.75em 0 0"
                  :fontSize "smaller"}}
    "by "
    [:a {:href "http://twitter.com/papungha"}
     "tommy"]]])

(defn ^:export main
  []
  (r/render
    [application-component]
    (.getElementById js/document "app")))

(main)
