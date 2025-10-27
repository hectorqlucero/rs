(ns rs.handlers.admin.alquileresclientes.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

(def get-alquileres-sql
  (str "SELECT a.id, a.id_inquilino as id_cliente, a.id_propiedad, 
               a.monto_mensual as renta_mensual, a.fecha_inicio, a.estado_alquiler,
               p.titulo as propiedad,
               (ag.nombre || ' ' || COALESCE(ag.apellido_paterno, '')) as agente
         FROM alquileres a
         LEFT JOIN propiedades p ON a.id_propiedad = p.id
         LEFT JOIN agentes ag ON a.id_agente = ag.id
         WHERE a.id_inquilino = ?
         ORDER BY a.fecha_inicio DESC"))

(defn get-alquileres
  [parent-id]
  (Query [get-alquileres-sql parent-id] :conn :default))

(def get-alquileres-id-sql
  (str "SELECT a.*, 
               p.titulo as propiedad,
               (ag.nombre || ' ' || COALESCE(ag.apellido_paterno, '')) as agente
         FROM alquileres a
         LEFT JOIN propiedades p ON a.id_propiedad = p.id
         LEFT JOIN agentes ag ON a.id_agente = ag.id
         WHERE a.id = ?"))

(defn get-alquileres-id
  [id]
  (first (Query [get-alquileres-id-sql (crud-fix-id id)] :conn :default)))
