(ns vl-docs.cus
  (:require [vl-docs.config :as c]
            [vl-docs.components :as compo]
            [vl-docs.page :as page]
            [vl-docs.utils :as u]
            [clojure.string :as string]))

(defn table-row [m]
  (let [id (:id m)]
    [:tr
     [:td [:a.uk-link-toggle {:href (str "./" id)} (get-in m [:value :Customer :Sign])]]
     [:td [:a.uk-link-toggle {:href (str "./" id)} (get-in m [:value :Customer :Name])]]
     [:td [:a.uk-link-toggle {:href (str "http://a73434:5984/_utils/#database/vl_db/" id)} [:span {:uk-icon "database"}]]]]))

(defn table [data]
  [:table.uk-table.uk-table-striped.uk-table-hover
   [:thead
    [:tr
     [:th.uk-table-shrink "Kürzel"]
     [:th.uk-table-expand "Name"]
     [:th ""]]]
   (into [:tbody] (map table-row data))])


(defn menu [data]
  (page/index
   (into (compo/article)
         [(compo/form-heading "Kundendokumente")
          (table data)])))
