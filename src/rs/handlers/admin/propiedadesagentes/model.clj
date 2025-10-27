(ns rs.handlers.admin.propiedadesagentes.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

(def get-propiedades-sql
  (str "SELECT id, id_agente, titulo, tipo, precio, estado_propiedad, municipio
         FROM propiedades t
         WHERE t.id_agente = ?
         ORDER BY t.titulo"))

(defn get-propiedades
  [parent-id]
  (Query [get-propiedades-sql parent-id] :conn :default))

(def get-propiedades-id-sql
  (str "SELECT id, id_agente, titulo, tipo, precio, estado_propiedad, municipio
         FROM propiedades
         WHERE id = ?"))

(defn get-propiedades-id
  [id]
  (first (Query [get-propiedades-id-sql (crud-fix-id id)] :conn :default)))
