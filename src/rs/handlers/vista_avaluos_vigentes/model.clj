(ns rs.handlers.vista_avaluos_vigentes.model
  (:require [rs.models.crud :refer [Query]]))

(def get-vista_avaluos_vigentes-sql
  (str "SELECT * FROM vista_avaluos_vigentes"))

(defn get-vista_avaluos_vigentes
  []
  (Query get-vista_avaluos_vigentes-sql :conn :default))
