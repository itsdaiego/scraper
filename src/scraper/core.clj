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

(defn get-articles-titles
  [dom]
  (map html/text (html/select dom [:article.content :h2])))

(defn print-articles-titles
  [titles]
  (doseq [title titles] (println title)))


(def email (environ/env :email))
(def pass (environ/env :pass))

(def conn {:host "smtp.gmail.com"
           :ssl true
           :user email
           :pass pass})

(defn send-email
  [titles]
  (postal/send-message conn {:from email
                    :to email
                    :subject "A message, from the past"
                    :body "Hi there, me!"}))

(defn -main
  ""
  [& args]
  (send-email(print-articles-titles
    (get-articles-titles
      (get-chess-dom)))))
