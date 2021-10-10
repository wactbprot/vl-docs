(ns vl-docs.utils
  (:require [clojure.string :as string]))


(defn str->path-element [s]
  (if (re-matches #"[0-9]*" s) (Integer/parseInt s) (keyword s))) 

(defn path
  "Converts string path to vector.
  Example:
  ```clojure
  (def t {:w [{:m 1}]})
  (get-in t (path \"w.0.m\"))
  ```"
  [s]
  (mapv str->path-element (string/split s #"\.")))

(defn info [m data] (assoc m :value (get-in data (path (:data-path m)))))

(defn value [t v]
  (condp = (keyword t)
    :int (BigInteger. v)
    :float (Float/parseFloat v)
    v))
