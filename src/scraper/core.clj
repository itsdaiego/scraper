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
  (html/select dom [:article.content :h2.title-featured :a]))

(defn parse-content
  [content]
  (hash-map content :content))
>>>>>>> 3945bcc... get titles content

(defn -main
  ""
  [& args]
  (let [titles (parse-content(get-articles-titles(get-chess-dom)))]
    (println titles)))
