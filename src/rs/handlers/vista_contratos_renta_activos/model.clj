(ns rs.handlers.vista_contratos_renta_activos.model
  (:require [rs.models.crud :refer [Query]]))

(def get-vista_contratos_renta_activos-sql
  (str "SELECT * FROM vista_contratos_renta_activos"))

(defn get-vista_contratos_renta_activos
  []
  (Query get-vista_contratos_renta_activos-sql :conn :default))
