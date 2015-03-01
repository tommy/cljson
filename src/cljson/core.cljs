(ns cljson.core
  (:require [om.core :as om]
            [om.dom :as dom]
            [cljs.core.async :refer [put! chan <!]]
            [cljs.reader :refer [read-string]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)
(println "Loaded core.")

;;;;;;;;;;;;;;;;;;;;
;; parsing functions

(defn json->
  [s]
  (try
    (-> s
        JSON.parse
        (js->clj :keywordize-keys true))
    (catch :default e nil)))

(defn edn->
  [s]
  (try
    (read-string s)
    (catch :default e nil)))

;;;;;;;;;;;;;;;;;;;;;
;; printing functions

(def ->json
  (comp #(JSON.stringify % nil 2)
        clj->js))

(def ->edn pr-str)

;;;;;;;;;;;;;
;; components

(defn text-change
  [e owner to-chan]
  (let [v (.. e -target -value)]
    (om/set-state! owner :text v)
    (put! to-chan v)))

(defn input-component
  [placeholder]
  (fn [data owner]
    (reify
      om/IInitState
      (init-state [_]
        {:text ""})

      om/IWillMount
      (will-mount [_]
        (go (loop []
              (when-let [change (<! (:from data))]
                (when-let [parsed ((:read-fn data) change)]
                  (println placeholder " Got a change: " parsed)
                  (om/set-state! owner :text ((:write-fn data) parsed)))
                (recur)))))

      om/IRenderState
      (render-state [this {:keys [text]}]
        (dom/textarea #js {:style #js {:border "none"
                                       :height "80%"
                                       :width "calc(50% - 1em)"
                                       :marginRight "1em"
                                       :fontFamily "Menlo, Monospace"}
                           :placeholder placeholder
                           :value text
                           :onChange #(text-change % owner (:to data))})))))

(def app-component
  (fn [data owner]
    (reify
      om/IInitState
      (init-state [_]
        {:json-changes (chan)
         :edn-changes (chan)})

      om/IRenderState
      (render-state [this {:keys [json-changes edn-changes]}]
        (dom/div #js {:style #js {:maxWidth "50em"
                                  :margin "auto"
                                  :padding "1em 1em"
                                  :textAlign "center"
                                  :height "100%"}}
                 (dom/div #js {:style #js {:textAlign "left"
                                           :fontSize "larger"
                                           :margin "0.25em 0"}}
                         "Clojure/EDN \u27fa JSON")
                 (om/build (input-component "EDN")
                           {:from json-changes :to edn-changes
                            :write-fn ->edn :read-fn json->})
                 (om/build (input-component "JSON")
                           {:from edn-changes :to json-changes
                            :write-fn ->json :read-fn edn->})
                 (dom/div #js {:style #js {:textAlign "right"
                                           :margin "0.25em 0.75em 0 0"
                                           :fontSize "smaller"}}
                          "by "
                          (dom/a #js {:href "http://twitter.com/papungha"}
                                 "tommy")))))))


(def app-state (atom {}))

(om/root
  app-component
  app-state
  {:target (. js/document (getElementById "app"))})
