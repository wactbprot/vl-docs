(ns vl-docs.cus-post
  (:require [vl-docs.config :as c]
            [vl-docs.utils :as u]
            [clojure.string :as string]))

(defn alt-address [{cus :Customer :as doc} p]
  (if (and (contains? (set p) :AltAddress) (not (contains? cus :AltAddress)))
    (assoc-in doc [:Customer :AltAddress] [])
    doc))

(defn alt-contact [{cus :Customer :as doc} p]
  (if (and (contains? (set p) :AltContact) (not (contains? cus :AltContact)))
    (assoc-in doc [:Customer :AltContact] [])
    doc)) 

(defn customer [doc p]
  (-> doc
      (alt-address p)
      (alt-contact p)))

;;........................................................................
;; alt customer to shipping
;;........................................................................
(defn shipping-alt-contact [doc p]
  (assoc-in doc [:Customer :Shipping] (get-in doc p)))

;;........................................................................
;; alt customer to invoice 
;;........................................................................
(defn invoice-alt-contact [doc p]
  (assoc-in doc [:Customer :Invoice] (get-in doc p)))

;;........................................................................
;; alt customer to main
;;........................................................................
(defn main-alt-contact [doc p]
  (assoc-in doc [:Customer :Contact] (get-in doc p)))

;;........................................................................
;; alt address to shipping address
;;........................................................................
(defn shipping-alt-address [{{ship :Shipping} :Customer :as doc} p]
  (assoc-in doc [:Customer :Shipping] (merge ship (get-in doc p))))

;;........................................................................
;; alt address to invoice address
;;........................................................................
(defn invoice-alt-address [{{invo :Invoice} :Customer :as doc} p]
  (assoc-in doc [:Customer :Invoice] (merge invo (get-in doc p))))

;;........................................................................
;; alt address to main address
;;........................................................................
(defn main-names [{n :CustomerName  an :AddName aan :AddAddName}]
  (cond-> {}
    (seq n) (assoc :Name n)
    (seq an) (assoc :AddName an)
    (seq aan) (assoc :AddAddName aan)))

(defn main-address [addr]
  (-> addr
      (dissoc :CustomerName)
      (dissoc :AddName)
      (dissoc :AddAddName)))

(defn main-alt-address [{cus :Customer :as doc} p]
  (let [addr (get-in doc p)
        cus (merge cus (main-names addr))
        cus (assoc cus :Address (merge (:Address cus) (main-address addr)))]
    (assoc doc :Customer cus)))

;;........................................................................
;; command dispach
;;........................................................................
(defn command [doc p cmd]
  (condp = (keyword cmd)
    :invoice-alt-address (invoice-alt-address doc p)
    :shipping-alt-address (shipping-alt-address doc p)
    :main-alt-address (main-alt-address doc p)
    :invoice-alt-contact (invoice-alt-contact doc p)
    :shipping-alt-contact (shipping-alt-contact doc p)
    :main-alt-contact (main-alt-contact doc p)))


;;........................................................................
;; data from page doc from db
;;........................................................................
(defn receive [{p :path v :value t :type cmd :cmd} doc]
  (let [p (u/path p)]
    (cond
      (seq cmd) (command doc p cmd)
      (seq v) (assoc-in (customer doc p) p (if t (u/value t v) v))
      :else {:error "unkown request"})))
