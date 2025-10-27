(ns rs.handlers.admin.alquileresagentes.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

;; Generated on 2025-10-26T22:39:52.835-07:00

(def get-alquileres-sql
  (str "SELECT alq.id, alq.id_propiedad, alq.id_inquilino, alq.id_agente, alq.fecha_inicio, alq.fecha_fin, alq.monto_mensual, alq.deposito_garantia, alq.primer_mes, alq.ultimo_mes, alq.incremento_anual, alq.dia_pago, alq.incluye_mantenimiento, alq.incluye_servicios, alq.permite_mascotas, alq.numero_ocupantes, alq.uso_permitido, alq.clausulas_especiales, alq.notario_publico, alq.numero_notaria, alq.fecha_ratificacion, alq.estado_alquiler, age.nombre AS id_agente_display, cli.nombre AS id_inquilino_display, pro.titulo AS id_propiedad_display
         FROM alquileres alq
         LEFT JOIN agentes age ON alq.id_agente = age.id LEFT JOIN clientes cli ON alq.id_inquilino = cli.id LEFT JOIN propiedades pro ON alq.id_propiedad = pro.id
         WHERE alq.id_agente = ?
         ORDER BY alq.id DESC"))

(defn get-alquileres
  [parent-id]
  (Query [get-alquileres-sql parent-id] :conn :default))

(def get-alquileres-id-sql
  (str "SELECT alq.id, alq.id_propiedad, alq.id_inquilino, alq.id_agente, alq.fecha_inicio, alq.fecha_fin, alq.monto_mensual, alq.deposito_garantia, alq.primer_mes, alq.ultimo_mes, alq.incremento_anual, alq.dia_pago, alq.incluye_mantenimiento, alq.incluye_servicios, alq.permite_mascotas, alq.numero_ocupantes, alq.uso_permitido, alq.clausulas_especiales, alq.notario_publico, alq.numero_notaria, alq.fecha_ratificacion, alq.estado_alquiler, age.nombre AS id_agente_display, cli.nombre AS id_inquilino_display, pro.titulo AS id_propiedad_display
         FROM alquileres alq
         LEFT JOIN agentes age ON alq.id_agente = age.id LEFT JOIN clientes cli ON alq.id_inquilino = cli.id LEFT JOIN propiedades pro ON alq.id_propiedad = pro.id
         WHERE alq.id = ?"))

(defn get-alquileres-id
  [id]
  (first (Query [get-alquileres-id-sql (crud-fix-id id)] :conn :default)))
