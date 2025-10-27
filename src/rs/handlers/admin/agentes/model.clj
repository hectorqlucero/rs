(ns rs.handlers.admin.agentes.model
  (:require [rs.models.crud :refer [Query]]))

(def get-agentes-sql
  (str "SELECT * FROM agentes"))

(defn get-agentes
  []
  (Query get-agentes-sql :conn :default))

(defn get-agentes-id
  [id]
  (first (Query (str "SELECT * FROM agentes WHERE id=" id) :conn :default)))
