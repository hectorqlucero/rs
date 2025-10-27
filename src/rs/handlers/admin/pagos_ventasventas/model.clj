(ns rs.handlers.admin.pagos_ventasventas.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

(def get-pagos_ventas-sql
  (str "SELECT p.id, p.id_venta, p.tipo_pago, p.monto, p.fecha_pago, p.metodo_pago, 
                p.numero_referencia, p.banco_origen, p.observaciones, p.numero_recibo, 
                p.fecha_registro,
                (a.nombre || ' ' || COALESCE(a.apellido_paterno, '')) as agente_registro
         FROM pagos_ventas p
         LEFT JOIN agentes a ON p.agente_registro = a.id
         WHERE p.id_venta = ?
         ORDER BY p.tipo_pago"))

(defn get-pagos_ventas
  [parent-id]
  (Query [get-pagos_ventas-sql parent-id] :conn :default))

(def get-pagos_ventas-id-sql
  (str "SELECT p.id, p.id_venta, p.tipo_pago, p.monto, p.fecha_pago, p.metodo_pago,
                p.numero_referencia, p.banco_origen, p.observaciones, p.numero_recibo,
                p.fecha_registro,
                (a.nombre || ' ' || COALESCE(a.apellido_paterno, '')) as agente_registro
         FROM pagos_ventas p
         LEFT JOIN agentes a ON p.agente_registro = a.id
         WHERE p.id = ?"))

(defn get-pagos_ventas-id
  [id]
  (first (Query [get-pagos_ventas-id-sql (crud-fix-id id)] :conn :default)))
