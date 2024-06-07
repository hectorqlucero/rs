(ns sk.handlers.admin.zonas.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-zonas-sql
  (str
   "
SELECT *
FROM zonas
"))

(defn get-zonas
  []
  (Query db get-zonas-sql))

(def get-zonas-id-sql
  (str
   "
SELECT *
FROM zonas
WHERE id = ?
"))

(defn get-zonas-id
  [id]
  (first (Query db [get-zonas-id-sql id])))
