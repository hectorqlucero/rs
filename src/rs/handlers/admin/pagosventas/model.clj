(ns rs.handlers.admin.pagosventas.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

;; Generated on 2025-10-26T19:31:31.694-07:00

(def get-pagos-sql
  (str "SELECT pag.id, pag.id_venta, pag.descripcion, ven.descripcion AS ven_descripcion
         FROM pagos pag
         LEFT JOIN ventas ven ON pag.id_venta = ven.id
         WHERE pag.id_venta = ?
         ORDER BY pag.id DESC"))

(defn get-pagos
  [parent-id]
  (Query [get-pagos-sql parent-id] :conn :default))

(def get-pagos-id-sql
  (str "SELECT pag.id, pag.id_venta, pag.descripcion, ven.descripcion AS ven_descripcion
         FROM pagos pag
         LEFT JOIN ventas ven ON pag.id_venta = ven.id
         WHERE pag.id = ?"))

(defn get-pagos-id
  [id]
  (first (Query [get-pagos-id-sql (crud-fix-id id)] :conn :default)))
