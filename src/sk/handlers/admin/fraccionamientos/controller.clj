(ns sk.handlers.admin.fraccionamientos.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.fraccionamientos.model :refer [get-fraccionamientos get-fraccionamientos-id]]
            [sk.handlers.admin.fraccionamientos.view :refer [fraccionamientos-view fraccionamientos-edit-view fraccionamientos-add-view fraccionamientos-modal-script]]))

(defn fraccionamientos [_]
  (let [title "Fraccionamientos"
        ok (get-session-id)
        js nil
        rows (get-fraccionamientos)
        content (fraccionamientos-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn fraccionamientos-edit
  [id]
  (let [title "Modificar fraccionamiento"
        ok (get-session-id)
        js (fraccionamientos-modal-script)
        row (get-fraccionamientos-id  id)
        rows (get-fraccionamientos)
        content (fraccionamientos-edit-view title row rows)]
    (application title ok js content)))

(defn fraccionamientos-save
  [{params :params}]
  (let [table "fraccionamientos"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/fraccionamientos")
      (error-404 "No se pudo procesar el record!" "/admin/fraccionamientos"))))

(defn fraccionamientos-add
  [_]
  (let [title "Crear nuevo fraccionamiento"
        ok (get-session-id)
        js (fraccionamientos-modal-script)
        row nil
        rows (get-fraccionamientos)
        content (fraccionamientos-add-view title row rows)]
    (application title ok js content)))

(defn fraccionamientos-delete
  [id]
  (let [table "fraccionamientos"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/fraccionamientos")
      (error-404 "No se pudo procesar el record!" "/admin/fraccionamientos"))))
