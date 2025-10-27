(ns rs.handlers.admin.documentospropiedades.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

;; Generated on 2025-10-26T22:49:38.092-07:00

(def get-documentos-sql
  (str "SELECT doc.id, doc.tipo_documento, doc.nombre_documento, doc.descripcion, doc.tabla_referencia, doc.id_referencia, doc.ruta_archivo, doc.nombre_archivo, doc.tamanio_kb, doc.mime_type, doc.fecha_subida, doc.fecha_vencimiento, doc.estado_documento, doc.agente_subida, doc.observaciones, age.nombre AS agente_subida_display
         FROM documentos doc
         LEFT JOIN agentes age ON doc.agente_subida = age.id
         WHERE doc.id_referencia = ?
         ORDER BY doc.id DESC"))

(defn get-documentos
  [parent-id]
  (Query [get-documentos-sql parent-id] :conn :default))

(def get-documentos-id-sql
  (str "SELECT doc.id, doc.tipo_documento, doc.nombre_documento, doc.descripcion, doc.tabla_referencia, doc.id_referencia, doc.ruta_archivo, doc.nombre_archivo, doc.tamanio_kb, doc.mime_type, doc.fecha_subida, doc.fecha_vencimiento, doc.estado_documento, doc.agente_subida, doc.observaciones, age.nombre AS agente_subida_display
         FROM documentos doc
         LEFT JOIN agentes age ON doc.agente_subida = age.id
         WHERE doc.id = ?"))

(defn get-documentos-id
  [id]
  (first (Query [get-documentos-id-sql (crud-fix-id id)] :conn :default)))
