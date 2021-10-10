(ns vl-docs.config)

(def defaults {:server {:port 8012}
               :db-prot "http"
               :db-host (or (System/getenv "DB_HOST") "localhost")
               :db-port 5984
               :db-name "doc_test"
               :db-usr (System/getenv "CAL_USR")
               :db-pwd (System/getenv "CAL_PWD")})

(defn db-base-url [{db-port :db-port db-host :db-host db-prot :db-prot}]
  (str db-prot "://" db-host ":" db-port))

(defn db-url [{db-name :db-name :as c}]
  (str (db-base-url c) "/" db-name))

(def conf 
  (assoc defaults
         :db-base-url (db-base-url defaults)
         :db-url (db-url defaults)
         :db-opt {:headers {"Content-Type" "application/json"}
                  :timeout 100
                  :basic-auth [(:db-usr defaults) (:db-pwd defaults)]}))
