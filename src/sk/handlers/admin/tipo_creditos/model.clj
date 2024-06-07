(ns sk.handlers.admin.tipo_creditos.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-tipo_creditos-sql
  (str
   "
SELECT *
FROM tipo_creditos
"))

(defn get-tipo_creditos
  []
  (Query db get-tipo_creditos-sql))

(def get-tipo_creditos-id-sql
  (str
   "
SELECT *
FROM tipo_creditos
WHERE id = ?
"))

(defn get-tipo_creditos-id
  [id]
  (first (Query db [get-tipo_creditos-id-sql id])))
