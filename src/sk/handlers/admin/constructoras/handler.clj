(ns sk.handlers.admin.constructoras.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.constructoras.view :refer [constructoras-view constructoras-scripts]]))

(defn constructoras [_]
  (let [title "Constructoras"
        ok (get-session-id)
        js (constructoras-scripts)
        content (constructoras-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn constructoras-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "constructoras"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn constructoras-form [id]
  (let [table "constructoras"]
    (build-form-row table id)))

(defn constructoras-save [{params :params}]
  (let [table "constructoras"]
    (build-form-save params table)))

(defn constructoras-delete [{params :params}]
  (let [table "constructoras"]
    (build-form-delete params table)))