(ns rs.handlers.vista_documentos_por_vencer.model
  (:require [rs.models.crud :refer [Query]]))

(def get-vista_documentos_por_vencer-sql
  (str "SELECT * FROM vista_documentos_por_vencer"))

(defn get-vista_documentos_por_vencer
  []
  (Query get-vista_documentos_por_vencer-sql :conn :default))
