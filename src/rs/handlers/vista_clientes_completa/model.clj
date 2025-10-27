(ns rs.handlers.vista_clientes_completa.model
  (:require [rs.models.crud :refer [Query]]))

(def get-vista_clientes_completa-sql
  (str "SELECT * FROM vista_clientes_completa"))

(defn get-vista_clientes_completa
  []
  (Query get-vista_clientes_completa-sql :conn :default))
