(ns sk.handlers.admin.zonas.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.zonas.view :refer [zonas-view zonas-scripts]]))

(defn zonas [_]
  (let [title "Zonas"
        ok (get-session-id)
        js (zonas-scripts)
        content (zonas-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn zonas-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "zonas"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn zonas-form [id]
  (let [table "zonas"]
    (build-form-row table id)))

(defn zonas-save [{params :params}]
  (let [table "zonas"]
    (build-form-save params table)))

(defn zonas-delete [{params :params}]
  (let [table "zonas"]
    (build-form-delete params table)))