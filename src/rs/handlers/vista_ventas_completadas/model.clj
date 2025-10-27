(ns rs.handlers.vista_ventas_completadas.model
  (:require [rs.models.crud :refer [Query]]))

(def get-vista_ventas_completadas-sql
  (str "SELECT * FROM vista_ventas_completadas"))

(defn get-vista_ventas_completadas
  []
  (Query get-vista_ventas_completadas-sql :conn :default))
