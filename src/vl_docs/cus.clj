(ns vl-docs.cus
  (:require [vl-docs.config :as c]
            [vl-docs.components :as compo]
            [vl-docs.page :as page]
            [vl-docs.utils :as u]
            [clojure.string :as string]))

(defn table-row [m]
  (let [id    (-> m :id)
        eaddr (-> m :value :Customer :EAkteObjectAddress)]
    [:tr
     [:td [:a.uk-link-toggle {:href (str "./" id)} (-> m :value :Customer :Sign)]]
     [:td [:a.uk-link-toggle {:href (str "./" id)} (-> m :value :Customer :Name)]]
     [:td [:a.uk-link-toggle {:href (str "./" id)} (-> m :value :Customer :Contact :Name)]]
     [:td [:a.uk-link-toggle {:href (str "./" id)} (-> m :value :Customer :Comment)]]
     [:td (when eaddr
            [:a.uk-link-toggle {:href (str "https://eakte.ptb.de/fsc/mx/" eaddr)} [:span {:uk-icon "folder"}]])]
     [:td [:a.uk-link-toggle {:href (str "http://localhost:5984/_utils/#database/vl_db/" id)} [:span {:uk-icon "database"}]]]]))

(defn table [data]
  [:table.uk-table.uk-table-striped.uk-table-hover
   [:thead
    [:tr
     [:th.uk-table-shrink "Kürzel"]
     [:th "Name"]
     [:th "Kontakt"]
     [:th.uk-table-expand "Kommentar"]
     [:th "EAkte"]
     [:th "DB"]]]
   (into [:tbody] (map table-row data))])


(defn menu [data]
  (page/index
   (into (compo/article)
         [(compo/form-heading "Kundendokumente")
          (table data)])))
