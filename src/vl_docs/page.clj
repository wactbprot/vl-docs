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
  [:nav.uk-navbar-container
   {:uk-navbar ""}
   [:div.uk-navbar-center
    [:ul.uk-navbar-nav
     [:li [:a {:target "_blank"
               :href "https://gitlab1.ptb.de/vaclab/vl-docs"} "gitlab"]]
     [:li [:a { :target "_blank"
               :href "http://a75438:5984/_utils/#database/vl_db/_all_docs"} "Datenbank"]]
     [:li [:a { :target "_blank"
               :href "http://localhost:8882/pla"} "vle"]]
     [:li [:a { :target "_blank"
               :href "/customer/"} "Kunden"]]]]])

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
  [:head [:title "vl-docs"]
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   (hp/include-css "/css/uikit.css")])

;;........................................................................
;; index
;;........................................................................
(defn index [content] (hp/html5 (head) (body content)))
