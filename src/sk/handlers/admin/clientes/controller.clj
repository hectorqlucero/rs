(ns sk.handlers.admin.clientes.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.clientes.model :refer [get-clientes get-clientes-id]]
            [sk.handlers.admin.clientes.view :refer [clientes-view clientes-edit-view clientes-add-view clientes-modal-script]]))

(defn clientes [_]
  (let [title "Clientes"
        ok (get-session-id)
        js nil
        rows (get-clientes)
        content (clientes-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn clientes-edit
  [id]
  (let [title "Modificar clientes"
        ok (get-session-id)
        js (clientes-modal-script)
        row (get-clientes-id  id)
        rows (get-clientes)
        content (clientes-edit-view title row rows)]
    (application title ok js content)))

(defn clientes-save
  [{params :params}]
  (let [table "clientes"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/clientes")
      (error-404 "No se pudo procesar el record!" "/admin/clientes"))))

(defn clientes-add
  [_]
  (let [title "Crear nuevo clientes"
        ok (get-session-id)
        js (clientes-modal-script)
        row nil
        rows (get-clientes)
        content (clientes-add-view title row rows)]
    (application title ok js content)))

(defn clientes-delete
  [id]
  (let [table "clientes"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/clientes")
      (error-404 "No se pudo procesar el record!" "/admin/clientes"))))
