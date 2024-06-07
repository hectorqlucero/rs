(ns sk.handlers.admin.constructoras.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-constructoras-sql
  (str
   "
SELECT *
FROM constructoras
"))

(defn get-constructoras
  []
  (Query db get-constructoras-sql))

(def get-constructoras-id-sql
  (str
   "
SELECT *
FROM constructoras
WHERE id = ?
"))

(defn get-constructoras-id
  [id]
  (first (Query db [get-constructoras-id-sql id])))
