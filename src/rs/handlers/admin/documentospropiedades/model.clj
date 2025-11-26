(ns rs.handlers.admin.documentospropiedades.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

(def get-documentos-sql
  (str "SELECT id, id_propiedad, tipo_documento, nombre_documento, estado_documento
         FROM documentos t
         WHERE t.id_propiedad = ?
         ORDER BY t.tipo_documento"))

(defn get-documentos
  [parent-id]
  (Query [get-documentos-sql parent-id] :conn :default))

(def get-documentos-id-sql
  (str "SELECT id, id_propiedad, tipo_documento, nombre_documento, estado_documento
         FROM documentos
         WHERE id = ?"))

(defn get-documentos-id
  [id]
  (first (Query [get-documentos-id-sql (crud-fix-id id)] :conn :default)))
