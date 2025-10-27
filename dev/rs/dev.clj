(ns rs.dev
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [rs.models.crud :refer [config]]
            [rs.core :as core]))

(defn -main []
  (jetty/run-jetty (wrap-reload #'core/app) {:port (:port config)}))
