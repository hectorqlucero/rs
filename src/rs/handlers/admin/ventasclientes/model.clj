(ns rs.handlers.admin.ventasclientes.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

;; Generated on 2025-10-26T19:31:14.539-07:00

(def get-ventas-sql
  (str "SELECT ven.id, ven.id_cliente, ven.descripcion, cli.descripcion AS cli_descripcion
         FROM ventas ven
         LEFT JOIN clientes cli ON ven.id_cliente = cli.id
         WHERE ven.id_cliente = ?
         ORDER BY ven.id DESC"))

(defn get-ventas
  [parent-id]
  (Query [get-ventas-sql parent-id] :conn :default))

(def get-ventas-id-sql
  (str "SELECT ven.id, ven.id_cliente, ven.descripcion, cli.descripcion AS cli_descripcion
         FROM ventas ven
         LEFT JOIN clientes cli ON ven.id_cliente = cli.id
         WHERE ven.id = ?"))

(defn get-ventas-id
  [id]
  (first (Query [get-ventas-id-sql (crud-fix-id id)] :conn :default)))
