(ns vl-docs.cus-out
  (:require [vl-docs.config :as c]
            [vl-docs.page :as p]
            [vl-docs.utils :as u]
            [clojure.string :as string]))

(defn cmd-map [cmd path] {:data-cmd cmd :data-path path})
(defn opt-map [opt path label]  {:label label :data-path  path :options opt})
(defn inp-map [path label] {:label label :data-path  path})

(defn cmd-replace-address [path]
  (into (p/grid)
        [(-> (cmd-map :invoice-alt-address path)
             (p/button "Rechnung"))

         (-> (cmd-map  :shipping-alt-address path)
             (p/button "Versand"))

         (-> (cmd-map :main-alt-address path)
             (p/button "Haupt"))]))

(defn cmd-replace-contact [path]
  (into (p/grid)
        [(-> (cmd-map :invoice-alt-contact path)
             (p/button "Rechnung"))
         (-> (cmd-map :shipping-alt-contact path)
             (p/button "Versand"))
         (-> (cmd-map :main-alt-contact path)
             (p/button "Haupt"))]))

(defn category [data base layout]
  (-> (opt-map ["EU-Ausland" "Inland" "Ausland"] (str base "Category") "Kategorie")
      (u/info data)
      (p/form-select layout)))

(defn gender [data base layout]
  (-> (opt-map ["" "female" "male" "other"] (str base "Gender")  "Geschlecht")
      (u/info data)
      (p/form-select layout)))

(defn lang [data base layout]
  (-> (opt-map ["de" "en"] (str base "Lang")  "Sprache")
      (u/info data)
      (p/form-select layout)))


(defn company-type [data base layout]
  (-> (opt-map ["" "Firma" "DAkkS" "NMI" "NMI/DI" "PTB" "Sonstige"] (str base "Type") "Typ")
      (u/info data)
      (p/form-select layout)))

(defn contact [data base]
  [(-> {:label "Name"
        :data-path  (str base "Name")}
       (u/info data)
       (p/form-text-input {:width :one-quarter}))

   (-> {:label "Email"
        :data-path  (str base "Email")}
       (u/info data)
       (p/form-text-input {:width :one-quarter}))

   (-> {:label "Telefon"
        :data-path  (str base "Phone")}
       (u/info data)
       (p/form-text-input {:width :one-quarter}))

   (gender data base {:width :one-quarter})])


(defn main [data]
  (let [base "Customer."]
  [(-> {:label "Kürzel"
        :data-path  (str base "Sign")}
       (u/info data)
       (p/form-text-input {:width :one-quarter}))

   (-> {:label "Debitor"
        :data-path  (str base "DebitorenNr")
        :data-type "int"}
       (u/info data)
       (p/form-text-input {:width :one-quarter}))

   (company-type data base {:width :one-quarter})

   (-> {:label "Sprache"
        :data-path  (str base "Lang")
        :options ["de" "en"]}
       (u/info data)
       (p/form-select {:width :one-quarter}))

   (-> {:label "Kommentar"
        :data-path  (str base "Comment")}
       (u/info data)
       (p/form-text-input {:width :full}))]))

(defn address-name [data base layout prop-name] 
  [(-> {:label "Adresszeile 1"
        :data-path  (str base prop-name)}
       (u/info data)
       (p/form-text-input layout))])

(defn address-tail [data base]
  [(-> {:label "Adresszeile 2"
        :data-path (str base "AddName")}
       (u/info data)
       (p/form-text-input {:width :half}))
   
   (-> {:label "Adresszeile 3"
        :data-path (str base "AddAddName")}
       (u/info data)
       (p/form-text-input {:width :half}))

   (-> {:label "Straße Nr."
        :data-path (str base "Address.Street")}
       (u/info data)
       (p/form-text-input {:width :half}))

   (-> {:label "PLZ"
        :data-path (str base "Address.Zipcode")}
       (u/info data)
       (p/form-text-input {:width :one-quarter}))

   (-> {:label "Bezirk/Distrikt"
        :data-path (str base "Address.District")}
       (u/info data)
       (p/form-text-input {:width :one-quarter}))

   (-> {:label "Ort"
        :data-path (str base "Address.Town")}
       (u/info data)
       (p/form-text-input {:width :half}))

   (-> {:label "Landeskürzel"
        :data-path (str base "Address.Land")}
       (u/info data)
       (p/form-text-input {:width :one-quarter}))
   
   (category data (str base "Address.") {:width :one-quarter})])

(defn main-contact [data] (contact data "Customer.Contact."))

(defn shipping-contact [data] (contact data "Customer.Shipping."))

(defn invoice-contact [data] (contact data "Customer.Invoice."))

(defn main-address [data]
  (let [base "Customer."]
    (into (address-name data base {:width :full} "Name")
          (address-tail data base))))

(defn sub-address [data base]
  (into (address-name data base {:width :full} "CustomerName")
        (address-tail data base)))

(defn out [data]
  (p/index
   (into (p/acc-frame)
         [(p/acc-sheet "Adresse und Kontakt"
                       (into (p/article)
                             [(p/form-heading "Allgemein")
                              (into (p/form) (main data))
                              (p/form-heading "Adresse")
                              (into (p/form) (main-address data))
                              (p/form-heading "Kontakt")
                              (into (p/form) (main-contact data))]) {:open true})
          (p/acc-sheet "Versand"
                       (into (p/article)
                             [(p/form-heading "Versandadresse")
                              (into (p/form) (sub-address data "Customer.Shipping."))
                              (p/form-heading "Versandkontakt")
                              (into (p/form) (shipping-contact data))]))
          (p/acc-sheet "Rechnung"
                       (into (p/article)
                             [(p/form-heading "Rechnungsadresse")
                              (into (p/form) (sub-address data "Customer.Invoice"))
                              (p/form-heading "Rechnungskontakt")
                              (into (p/form) (invoice-contact data))]))
          (p/acc-sheet "Auswahl Adressen"
                       (into (p/article)
                             [(p/form-heading "Alternative 1")
                              (cmd-replace-address "Customer.AltAddress.0.")
                              (into (p/form) (sub-address data "Customer.AltAddress.0."))
                              (p/form-heading "Alternative 2")
                              (cmd-replace-address "Customer.AltAddress.1.")
                              (into (p/form) (sub-address data "Customer.AltAddress.1."))
                              (p/form-heading "Alternative 3")
                              (cmd-replace-address "Customer.AltAddress.2.")
                              (into (p/form) (sub-address data "Customer.AltAddress.2.")) ]))
          (p/acc-sheet "Auswahl Kontakte"
                       (into (p/article)
                             [(p/form-heading "Alternative 1")
                              (cmd-replace-contact "Customer.AltContact.0.")
                              (into (p/form) (contact data "Customer.AltContact.0."))
                              (p/form-heading "Alternative 1")
                              (cmd-replace-contact "Customer.AltContact.1.")
                              (into (p/form) (contact data "Customer.AltContact.1."))
                              (p/form-heading "Alternative 1")
                              (cmd-replace-contact "Customer.AltContact.2.")
                              (into (p/form) (contact data "Customer.AltContact.2.")) ]))])))


