(ns vl-docs.cus-get
  (:require [vl-docs.config :as c]
            [vl-docs.components :as compo]
            [vl-docs.page :as page]
            [vl-docs.utils :as u]
            [clojure.string :as string]))

(defn cmd-map [cmd path] {:data-cmd cmd :data-path path})
(defn opt-map [opt path label]  {:label label :data-path  path :options opt})
(defn inp-map [path label] {:label label :data-path  path})

(defn cmd-replace-address [path]
  (into (compo/grid)
        [(-> (cmd-map :invoice-alt-address path)
             (compo/button "Rechnung"))

         (-> (cmd-map  :shipping-alt-address path)
             (compo/button "Versand"))

         (-> (cmd-map :main-alt-address path)
             (compo/button "Haupt"))]))

(defn cmd-replace-contact [path]
  (into (compo/grid)
        [(-> (cmd-map :invoice-alt-contact path)
             (compo/button "Rechnung"))
         (-> (cmd-map :shipping-alt-contact path)
             (compo/button "Versand"))
         (-> (cmd-map :main-alt-contact path)
             (compo/button "Haupt"))]))

(defn category [data base layout]
  (-> (opt-map ["EU-Ausland" "Inland" "Ausland"] (str base "Category") "Kategorie")
      (u/info data)
      (compo/form-select layout)))

(defn gender [data base layout]
  (-> (opt-map ["" "female" "male" "other"] (str base "Gender")  "Geschlecht")
      (u/info data)
      (compo/form-select layout)))

(defn lang [data base layout]
  (-> (opt-map ["de" "en"] (str base "Lang")  "Sprache")
      (u/info data)
      (compo/form-select layout)))


(defn company-type [data base layout]
  (-> (opt-map ["" "Firma" "DAkkS" "NMI" "NMI/DI" "PTB" "Sonstige"] (str base "Type") "Typ")
      (u/info data)
      (compo/form-select layout)))

(defn contact [data base]
  [(-> {:label "Name"
        :data-path  (str base "Name")}
       (u/info data)
       (compo/form-text-input {:width :one-quarter}))

   (-> {:label "Email"
        :data-path  (str base "Email")}
       (u/info data)
       (compo/form-text-input {:width :one-quarter}))

   (-> {:label "Telefon"
        :data-path  (str base "Phone")}
       (u/info data)
       (compo/form-text-input {:width :one-quarter}))

   (gender data base {:width :one-quarter})])


(defn main [data]
  (let [base "Customer."]
  [(-> {:label "Kürzel"
        :data-path  (str base "Sign")}
       (u/info data)
       (compo/form-text-input {:width :one-quarter}))

   (-> {:label "Debitor"
        :data-path  (str base "DebitorenNr")
        :data-type "int"}
       (u/info data)
       (compo/form-text-input {:width :one-quarter}))

   (company-type data base {:width :one-quarter})

   (-> {:label "Sprache"
        :data-path  (str base "Lang")
        :options ["de" "en"]}
       (u/info data)
       (compo/form-select {:width :one-quarter}))

   (-> {:label "Kommentar"
        :data-path  (str base "Comment")}
       (u/info data)
       (compo/form-text-input {:width :full}))]))

(defn address-name [data base layout main-name]
  [(-> {:label "Adresszeile 1"
        :data-path  (str base main-name)}
       (u/info data)
       (compo/form-text-input layout))

   (-> {:label "Adresszeile 2"
        :data-path (str base "AddName")}
       (u/info data)
       (compo/form-text-input {:width :half}))

   (-> {:label "Adresszeile 3"
        :data-path (str base "AddAddName")}
       (u/info data)
       (compo/form-text-input {:width :half}))])


(defn address-tail [data base]
  [(-> {:label "Straße Nr."
        :data-path (str base "Street")}
       (u/info data)
       (compo/form-text-input {:width :half}))

   (-> {:label "PLZ"
        :data-path (str base "Zipcode")}
       (u/info data)
       (compo/form-text-input {:width :one-quarter}))

   (-> {:label "Bezirk/Distrikt"
        :data-path (str base "District")}
       (u/info data)
       (compo/form-text-input {:width :one-quarter}))

   (-> {:label "Ort"
        :data-path (str base "Town")}
       (u/info data)
       (compo/form-text-input {:width :half}))

   (-> {:label "Landeskürzel"
        :data-path (str base "Land")}
       (u/info data)
       (compo/form-text-input {:width :one-quarter}))

   (category data (str base "") {:width :one-quarter})])

(defn main-contact [data] (contact data "Customer.Contact."))

(defn shipping-contact [data] (contact data "Customer.Shipping."))

(defn invoice-contact [data] (contact data "Customer.Invoice."))

(defn main-address [data]
  (let [base "Customer."]
    (into (address-name data base {:width :full} "Name")
          (address-tail data (str base "Address.")))))

(defn sub-address [data base]
  (into (address-name data base {:width :full} "CustomerName")
            (address-tail data base)))

(defn emit [data]
  (page/index
   (into (compo/acc-frame)
         [(compo/acc-sheet "Adresse und Kontakt"
                       (into (compo/article)
                             [(compo/form-heading "Allgemein")
                              (into (compo/form) (main data))
                              (compo/form-heading "Adresse")
                              (into (compo/form) (main-address data))
                              (compo/form-heading "Kontakt")
                              (into (compo/form) (main-contact data))]){:open true})
          (compo/acc-sheet "Versand"
                       (into (compo/article)
                             [(compo/form-heading "Versandadresse")
                              (into (compo/form) (sub-address data "Customer.Shipping."))
                              (compo/form-heading "Versandkontakt")
                              (into (compo/form) (shipping-contact data))]))
          (compo/acc-sheet "Rechnung"
                       (into (compo/article)
                             [(compo/form-heading "Rechnungsadresse")
                              (into (compo/form) (sub-address data "Customer.Invoice"))
                              (compo/form-heading "Rechnungskontakt")
                              (into (compo/form) (invoice-contact data))]))
          (compo/acc-sheet "Auswahl Adressen"
                       (into (compo/article)
                             [(compo/form-heading "Alternative 1")
                              (cmd-replace-address "Customer.AltAddress.0.")
                              (into (compo/form) (sub-address data "Customer.AltAddress.0."))
                              (compo/form-heading "Alternative 2")
                              (cmd-replace-address "Customer.AltAddress.1.")
                              (into (compo/form) (sub-address data "Customer.AltAddress.1."))
                              (compo/form-heading "Alternative 3")
                              (cmd-replace-address "Customer.AltAddress.2.")
                              (into (compo/form) (sub-address data "Customer.AltAddress.2.")) ]))
          (compo/acc-sheet "Auswahl Kontakte"
                       (into (compo/article)
                             [(compo/form-heading "Alternative 1")
                              (cmd-replace-contact "Customer.AltContact.0.")
                              (into (compo/form) (contact data "Customer.AltContact.0."))
                              (compo/form-heading "Alternative 2")
                              (cmd-replace-contact "Customer.AltContact.1.")
                              (into (compo/form) (contact data "Customer.AltContact.1."))
                              (compo/form-heading "Alternative 3")
                              (cmd-replace-contact "Customer.AltContact.2.")
                              (into (compo/form) (contact data "Customer.AltContact.2.")) ]))])))
