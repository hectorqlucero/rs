(ns rs.handlers.admin.documentos.model
  (:require [rs.models.crud :refer [Query]]))

(def get-documentos-sql
  (str "SELECT * FROM documentos"))

(defn get-documentos
  []
  (Query get-documentos-sql :conn :default))

(defn get-documentos-id
  [id]
  (first (Query (str "SELECT * FROM documentos WHERE id=" id) :conn :default)))
