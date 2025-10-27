(ns rs.handlers.admin.avaluospropiedades.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

;; Generated on 2025-10-26T22:51:26.948-07:00

(def get-avaluos-sql
  (str "SELECT ava.id, ava.id_propiedad, ava.perito_valuador, ava.cedula_perito, ava.institucion_perito, ava.fecha_avaluo, ava.vigencia_meses, ava.valor_avaluo, ava.moneda, ava.metodo_valuacion, ava.proposito_avaluo, ava.superficie_terreno_avaluo, ava.superficie_construccion_avaluo, ava.estado_conservacion, ava.observaciones_perito, ava.numero_avaluo, ava.fecha_vencimiento, ava.estado_avaluo, ava.archivo_avaluo, ava.fecha_registro, pro.titulo AS id_propiedad_display
         FROM avaluos ava
         LEFT JOIN propiedades pro ON ava.id_propiedad = pro.id
         WHERE ava.id_propiedad = ?
         ORDER BY ava.id DESC"))

(defn get-avaluos
  [parent-id]
  (Query [get-avaluos-sql parent-id] :conn :default))

(def get-avaluos-id-sql
  (str "SELECT ava.id, ava.id_propiedad, ava.perito_valuador, ava.cedula_perito, ava.institucion_perito, ava.fecha_avaluo, ava.vigencia_meses, ava.valor_avaluo, ava.moneda, ava.metodo_valuacion, ava.proposito_avaluo, ava.superficie_terreno_avaluo, ava.superficie_construccion_avaluo, ava.estado_conservacion, ava.observaciones_perito, ava.numero_avaluo, ava.fecha_vencimiento, ava.estado_avaluo, ava.archivo_avaluo, ava.fecha_registro, pro.titulo AS id_propiedad_display
         FROM avaluos ava
         LEFT JOIN propiedades pro ON ava.id_propiedad = pro.id
         WHERE ava.id = ?"))

(defn get-avaluos-id
  [id]
  (first (Query [get-avaluos-id-sql (crud-fix-id id)] :conn :default)))
