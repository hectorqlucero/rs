(ns rs.handlers.vista_tramites_pendientes.model
  (:require [rs.models.crud :refer [Query]]))

(def get-vista_tramites_pendientes-sql
  (str "SELECT * FROM vista_tramites_pendientes"))

(defn get-vista_tramites_pendientes
  []
  (Query get-vista_tramites_pendientes-sql :conn :default))
