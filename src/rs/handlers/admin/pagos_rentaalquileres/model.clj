(ns rs.handlers.admin.pagos_rentaalquileres.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

;; Generated on 2025-10-26T22:57:42.736-07:00

(def get-pagos_renta-sql
  (str "SELECT pr.id, pr.id_alquiler, pr.mes_correspondiente, pr.monto, pr.fecha_pago, pr.metodo_pago, pr.estado_pago, pr.recargo_mora, pr.dias_atraso, pr.numero_recibo, pr.observaciones_pago, pr.agente_registro, age.nombre AS agente_registro_display, alq.id_propiedad AS id_alquiler_display
         FROM pagos_renta pr
         LEFT JOIN agentes age ON pr.agente_registro = age.id LEFT JOIN alquileres alq ON pr.id_alquiler = alq.id
         WHERE pr.id_alquiler = ?
         ORDER BY pr.id DESC"))

(defn get-pagos_renta
  [parent-id]
  (Query [get-pagos_renta-sql parent-id] :conn :default))

(def get-pagos_renta-id-sql
  (str "SELECT pr.id, pr.id_alquiler, pr.mes_correspondiente, pr.monto, pr.fecha_pago, pr.metodo_pago, pr.estado_pago, pr.recargo_mora, pr.dias_atraso, pr.numero_recibo, pr.observaciones_pago, pr.agente_registro, age.nombre AS agente_registro_display, alq.id_propiedad AS id_alquiler_display
         FROM pagos_renta pr
         LEFT JOIN agentes age ON pr.agente_registro = age.id LEFT JOIN alquileres alq ON pr.id_alquiler = alq.id
         WHERE pr.id = ?"))

(defn get-pagos_renta-id
  [id]
  (first (Query [get-pagos_renta-id-sql (crud-fix-id id)] :conn :default)))
