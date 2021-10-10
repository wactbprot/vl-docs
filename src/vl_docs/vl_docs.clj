(ns wactbprot.vl-docs
  ^{:author "Thomas Bock <wactbprot@gmail.com>"
    :doc "Webserver delivers pages, reads and writes to the database."}
  (:require [compojure.route :as route]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.json :as middleware]
            
            [vl-docs.config :as c]
            [vl-docs.db :as db]
            [vl-docs.cus-out :as cus-out]
            [vl-docs.cus-in :as cus-in]
            [vl-docs.page :as page])
  (:gen-class))

(defonce server (atom nil))

(defroutes app-routes
  (GET "/customer/:id" [:as req] (io/cus-out (db/get-doc c/conf (get-in req [:route-params :id]))))
  (POST "/customer/:id" [:as req] (db/put-doc c/conf (io/cus-in  (:body req) (db/get-doc c/conf (get-in req [:route-params :id])))))
  (route/resources "/")
  (route/not-found (page/not-found)))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))

(defn stop [c]
  (when @server (@server :timeout 100)
        (reset! server nil)))

(defn start [{srv :server}] (reset! server (run-server #'app srv)))

(defn -main [& args] (start c/conf))

(comment
  (start c/conf))
