(ns sk.handlers.admin.fraccionamientos.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.fraccionamientos.view :refer [fraccionamientos-view fraccionamientos-scripts]]))

(defn fraccionamientos [_]
  (let [title "Fraccionamientos"
        ok (get-session-id)
        js (fraccionamientos-scripts)
        content (fraccionamientos-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn fraccionamientos-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "fraccionamientos"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn fraccionamientos-form [id]
  (let [table "fraccionamientos"]
    (build-form-row table id)))

(defn fraccionamientos-save [{params :params}]
  (let [table "fraccionamientos"]
    (build-form-save params table)))

(defn fraccionamientos-delete [{params :params}]
  (let [table "fraccionamientos"]
    (build-form-delete params table)))