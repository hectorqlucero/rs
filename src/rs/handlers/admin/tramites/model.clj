(ns rs.handlers.admin.tramites.model
  (:require [rs.models.crud :refer [Query]]))

(def get-tramites-sql
  (str "SELECT * FROM tramites"))

(defn get-tramites
  []
  (Query get-tramites-sql :conn :default))

(defn get-tramites-id
  [id]
  (first (Query (str "SELECT * FROM tramites WHERE id=" id) :conn :default)))
