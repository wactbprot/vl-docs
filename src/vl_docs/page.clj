(ns vl-docs.page
  ^{:author "Thomas Bock <wactbprot@gmail.com>"
    :doc "Gives the frame for all the specific content"}
  (:require [hiccup.form :as hf]
            [hiccup.page :as hp]
            [clojure.string :as string]))

(defn not-found []
  (hp/html5
   [:h1 "404 Error!"]
   [:b "Page not found!"]
   [:p [:a {:href ".."} "Return to main page"]]))

;;........................................................................
;; nav
;;........................................................................
(defn nav []
  [:div.uk-section-primary.uk-preserve-color

    [:div {:uk-sticky "animation: uk-animation-slide-top; sel-target: .uk-navbar-container; cls-active: uk-navbar-sticky; cls-inactive: uk-navbar-transparent uk-light; top: 200"}

  [:nav.uk-navbar-container
   {:uk-navbar ""}
   [:div.uk-navbar-center
    [:ul.uk-navbar-nav
     [:li [:a {:target "_blank"
               :href "https://gitlab1.ptb.de/vaclab/repliclj"} "gitlab"]]
     [:li [:a {:target "_blank"
               :href "http://a75438:5601/app/discover#/view/6fde0090-06ff-11ec-a0ed-9fa5b8b37aed"} "elasticsearch"]]
     [:li [:a { :target "_blank"
               :href "https://docs.couchdb.org/en/main/replication/index.html"} "repli docu"]]]]]]])

;;........................................................................
;; body
;;........................................................................
(defn body [content]
  [:body#body
   (nav)
   [:div.uk-container.uk-padding-small.uk-margin-smal.uk-text-smalll content]
   (hp/include-js "/js/jquery.js")
   (hp/include-js "/js/doc.js")
   (hp/include-js "/js/uikit.js")
   (hp/include-js "/js/uikit-icons.js")])

;;........................................................................
;; head
;;........................................................................
(defn head []
  [:head [:title "doc"]
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   (hp/include-css "/css/uikit.css")])

;;........................................................................
;; index
;;........................................................................
(defn index [content] (hp/html5 (head) (body content)))
