(ns sk.handlers.admin.tipo_creditos.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.tipo_creditos.view :refer [tipo_creditos-view tipo_creditos-scripts]]))

(defn tipo_creditos [_]
  (let [title "Tipo_creditos"
        ok (get-session-id)
        js (tipo_creditos-scripts)
        content (tipo_creditos-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn tipo_creditos-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "tipo_creditos"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn tipo_creditos-form [id]
  (let [table "tipo_creditos"]
    (build-form-row table id)))

(defn tipo_creditos-save [{params :params}]
  (let [table "tipo_creditos"]
    (build-form-save params table)))

(defn tipo_creditos-delete [{params :params}]
  nil)
