(ns rs.handlers.vista_agentes_performance.model
  (:require [rs.models.crud :refer [Query]]))

(def get-vista_agentes_performance-sql
  (str "SELECT * FROM vista_agentes_performance"))

(defn get-vista_agentes_performance
  []
  (Query get-vista_agentes_performance-sql :conn :default))
