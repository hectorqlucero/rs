(ns rs.handlers.admin.propiedades.model
  (:require [rs.models.crud :refer [Query]]))

(def get-propiedades-sql
  (str "SELECT * FROM propiedades"))

(defn get-propiedades
  []
  (Query get-propiedades-sql :conn :default))

(defn get-propiedades-id
  [id]
  (first (Query (str "SELECT * FROM propiedades WHERE id=" id) :conn :default)))
