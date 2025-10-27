(ns rs.handlers.admin.alquileres.model
  (:require [rs.models.crud :refer [Query]]))

(def get-alquileres-sql
  "SELECT a.*,
          p.titulo AS propiedad,
          c.nombre AS cliente,
          ag.nombre AS agente
   FROM alquileres a
   JOIN propiedades p ON a.id_propiedad = p.id
   JOIN clientes c ON a.id_inquilino = c.id
   JOIN agentes ag ON a.id_agente = ag.id")

(defn get-alquileres
  []
  (Query get-alquileres-sql :conn :default))

(defn get-alquileres-id
  [id]
  (first (Query ["SELECT * FROM alquileres WHERE id=?" id] :conn :default)))

(comment
  (get-alquileres)
  (get-alquileres-id 1))