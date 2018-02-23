(ns scraper.core
  (:require [net.cgrand.enlive-html :as html]
            [org.httpkit.client :as http]
            [postal.core :as postal]
            [environ.core :as environ])
  (:gen-class))

(use 'clojure.tools.trace)

(defn get-chess-dom
  []
  (html/html-snippet
    (:body @(http/get "https://www.chess.com/articles" {:insecure? true}))))

(defn get-articles-title
  [dom]
  (map html/text (html/select dom [:article :h2])))

(defn print-articles-title
  [titles]
  (do 
    (println "Most recent articles")
    (doseq [title titles] (println title))
    (lazy-seq titles)
    ))


(defn get-newest-title
  [titles]
  (first titles))

(def email (environ/env :email))
(def pass (environ/env :pass))

(def conn {:host "smtp.gmail.com"
           :ssl true
           :user email
           :pass pass})

(defn send-email
  [featured-title]
  (println "Featured title:" featured-title)
  (postal/send-message conn {:from email
                             :to email
                             :subject "New chess.com featured article"
                             :body featured-title}))

(defn -main
  ""
  [& args]
  (-> (get-chess-dom)
      (get-articles-title)
      (print-articles-title)
      (get-newest-title)
      (send-email)))
