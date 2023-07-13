(ns sk.handlers.admin.clientes.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.clientes.view :refer [clientes-view clientes-scripts]]))

(defn clientes [_]
  (let [title "Clientes"
        ok (get-session-id)
        js (clientes-scripts)
        content (clientes-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn clientes-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "clientes"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn clientes-form [id]
  (let [table "clientes"]
    (build-form-row table id)))

(defn clientes-save [{params :params}]
  (let [table "clientes"]
    (build-form-save params table)))

(defn clientes-delete [{params :params}]
  (let [table "clientes"]
    (build-form-delete params table)))
