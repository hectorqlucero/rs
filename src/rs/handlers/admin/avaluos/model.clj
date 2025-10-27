(ns rs.handlers.admin.avaluos.model
  (:require [rs.models.crud :refer [Query]]))

(def get-avaluos-sql
  (str "
       SELECT a.*,p.titulo 
       FROM avaluos a
       JOIN propiedades p on p.id = a.id_propiedad
       "))

(defn get-avaluos
  []
  (Query get-avaluos-sql :conn :default))

(defn get-avaluos-id
  [id]
  (first (Query (str "SELECT * FROM avaluos WHERE id=" id) :conn :default)))
