(ns scraper.core
  (:require [net.cgrand.enlive-html :as html]
            [org.httpkit.client :as http])
  (:gen-class))

(use 'clojure.tools.trace)

(defn get-fide-dom
  []
  (html/html-snippet
    (:body @(http/get "https://www.chess.com/articles" {:insecure? true}))))

(defn -main
  ""
  [& args]
  (let [content (get-fide-dom)]
    (println ["result" content])))
