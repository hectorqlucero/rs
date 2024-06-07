(ns sk.handlers.admin.tipo_creditos.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.tipo_creditos.model :refer [get-tipo_creditos get-tipo_creditos-id]]
            [sk.handlers.admin.tipo_creditos.view :refer [tipo_creditos-view tipo_creditos-edit-view tipo_creditos-add-view tipo_creditos-modal-script]]))

(defn tipo_creditos [_]
  (let [title "Tipo de creditos"
        ok (get-session-id)
        js nil
        rows (get-tipo_creditos)
        content (tipo_creditos-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn tipo_creditos-edit
  [id]
  (let [title "Modificar tipo de credito"
        ok (get-session-id)
        js (tipo_creditos-modal-script)
        row (get-tipo_creditos-id  id)
        rows (get-tipo_creditos)
        content (tipo_creditos-edit-view title row rows)]
    (application title ok js content)))

(defn tipo_creditos-save
  [{params :params}]
  (let [table "tipo de creditos"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/tipo_creditos")
      (error-404 "No se pudo procesar el record!" "/admin/tipo_creditos"))))

(defn tipo_creditos-add
  [_]
  (let [title "Crear nuevo tipo de credito"
        ok (get-session-id)
        js (tipo_creditos-modal-script)
        row nil
        rows (get-tipo_creditos)
        content (tipo_creditos-add-view title row rows)]
    (application title ok js content)))

(defn tipo_creditos-delete
  [id]
  (let [table "tipo de creditos"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/tipo_creditos")
      (error-404 "No se pudo procesar el record!" "/admin/tipo_creditos"))))
