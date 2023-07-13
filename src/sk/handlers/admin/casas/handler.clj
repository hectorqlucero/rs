(ns sk.handlers.admin.casas.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.casas.view :refer [casas-view casas-scripts]]))

(defn casas [_]
  (let [title "Casas"
        ok (get-session-id)
        js (casas-scripts)
        content (casas-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn casas-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "casas"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn casas-form [id]
  (let [table "casas"]
    (build-form-row table id)))

(defn casas-save [{params :params}]
  (let [table "casas"]
    (build-form-save params table)))

(defn casas-delete [{params :params}]
  (let [table "casas"]
    (build-form-delete params table)))