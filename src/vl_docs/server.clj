(ns vl-docs.server
  ^{:author "Thomas Bock <wactbprot@gmail.com>"
    :doc "Webserver delivers pages, reads and writes to the database."}
  (:require [compojure.route :as route]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.json :as middleware]            
            [vl-docs.config :as c]
            [vl-docs.db :as db]
            [vl-docs.cus :as cus]
            [vl-docs.cus-get :as cus-get]
            [vl-docs.cus-post :as cus-post]
            [vl-docs.utils :as u]
            [vl-docs.page :as page])
  (:gen-class))

(defonce server (atom nil))

(defroutes app-routes
  (GET "/customer/" [:as req] (cus/menu (db/get-view (assoc c/conf :db-view (:db-customer-view c/conf)))))
  
  (GET "/customer/:id" [:as req] (->> req
                                      (u/req->id)
                                      (db/get-doc c/conf)
                                      (cus-get/emit)))
  
  (POST "/customer/:id" [:as req] (->> req
                                       (u/req->id)
                                       (db/get-doc c/conf )
                                       (cus-post/receive (:body req))
                                       (db/put-doc c/conf)))
  
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
