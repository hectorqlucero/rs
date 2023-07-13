(ns sk.handlers.admin.constructoras.model
  (:require [sk.models.crud :refer [Query db]]))

(defn get-rows [tabla]
  (Query db [(str "select * from " tabla)]))

(comment
  (get-rows "constructoras"))