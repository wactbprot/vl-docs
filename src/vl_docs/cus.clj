(ns vl-docs.cus
  (:require [vl-docs.config :as c]
            [vl-docs.components :as compo]
            [vl-docs.page :as page]
            [vl-docs.utils :as u]
            [clojure.string :as string]))

(defn table-row [m]
  [:tr
   [:td (:id m)]
   [:td (get-in m [:value :Customer :Sign])]
   [:td (get-in m [:value :Customer :Name])]])

(defn table [data]
  [:table.uk-table.uk-table-striped
    [:thead
        [:tr
         [:th "id"]
         [:th "sign"]
         [:th "name"]]]
    (into [:tbody] (map table-row data))])


(defn menu [data]
  (page/index
  
         (table data)))
