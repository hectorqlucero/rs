(ns sk.handlers.admin.zonas.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.zonas.model :refer [get-zonas get-zonas-id]]
            [sk.handlers.admin.zonas.view :refer [zonas-view zonas-edit-view zonas-add-view zonas-modal-script]]))

(defn zonas [_]
  (let [title "Zonas"
        ok (get-session-id)
        js nil
        rows (get-zonas)
        content (zonas-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn zonas-edit
  [id]
  (let [title "Modificar zona"
        ok (get-session-id)
        js (zonas-modal-script)
        row (get-zonas-id  id)
        rows (get-zonas)
        content (zonas-edit-view title row rows)]
    (application title ok js content)))

(defn zonas-save
  [{params :params}]
  (let [table "zonas"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/zonas")
      (error-404 "No se pudo procesar el record!" "/admin/zonas"))))

(defn zonas-add
  [_]
  (let [title "Crear nueva zona"
        ok (get-session-id)
        js (zonas-modal-script)
        row nil
        rows (get-zonas)
        content (zonas-add-view title row rows)]
    (application title ok js content)))

(defn zonas-delete
  [id]
  (let [table "zonas"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/zonas")
      (error-404 "No se pudo procesar el record!" "/admin/zonas"))))
