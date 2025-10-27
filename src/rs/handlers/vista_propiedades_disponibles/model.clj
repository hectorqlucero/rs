(ns rs.handlers.vista_propiedades_disponibles.model
  (:require [rs.models.crud :refer [Query]]))

(def get-vista_propiedades_disponibles-sql
  (str "SELECT * FROM vista_propiedades_disponibles"))

(defn get-vista_propiedades_disponibles
  []
  (Query get-vista_propiedades_disponibles-sql :conn :default))

(comment
  (get-vista_propiedades_disponibles))