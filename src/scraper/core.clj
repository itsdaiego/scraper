(ns scraper.core
  (:require [net.cgrand.enlive-html :as html]
            [org.httpkit.client :as http])
  (:gen-class))

(use 'clojure.tools.trace)

(defn get-chess-dom
  []
  (html/html-snippet
    (:body @(http/get "https://www.chess.com/articles" {:insecure? true}))))

(defn get-articles-titles
  [dom]
  (map html/text (html/select dom [:article.content :h2])))

(defn print-articles-titles
  [titles]
  (doseq [title titles] (println title)))

(defn -main
  ""
  [& args]
  (print-articles-titles
    (get-articles-titles
      (get-chess-dom))))

