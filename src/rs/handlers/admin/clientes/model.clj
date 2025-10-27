(ns rs.handlers.admin.clientes.model
  (:require [rs.models.crud :refer [Query]]))

(def get-clientes-sql
  (str "SELECT * FROM clientes"))

(defn get-clientes
  []
  (Query get-clientes-sql :conn :default))

(defn get-clientes-id
  [id]
  (first (Query (str "SELECT * FROM clientes WHERE id=" id) :conn :default)))
