(ns sk.handlers.admin.constructoras.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.constructoras.model :refer [get-constructoras get-constructoras-id]]
            [sk.handlers.admin.constructoras.view :refer [constructoras-view constructoras-edit-view constructoras-add-view constructoras-modal-script]]))

(defn constructoras [_]
  (let [title "Constructoras"
        ok (get-session-id)
        js nil
        rows (get-constructoras)
        content (constructoras-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn constructoras-edit
  [id]
  (let [title "Modificar constructoras"
        ok (get-session-id)
        js (constructoras-modal-script)
        row (get-constructoras-id  id)
        rows (get-constructoras)
        content (constructoras-edit-view title row rows)]
    (application title ok js content)))

(defn constructoras-save
  [{params :params}]
  (let [table "constructoras"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/constructoras")
      (error-404 "No se pudo procesar el record!" "/admin/constructoras"))))

(defn constructoras-add
  [_]
  (let [title "Crear nueva constructora"
        ok (get-session-id)
        js (constructoras-modal-script)
        row nil
        rows (get-constructoras)
        content (constructoras-add-view title row rows)]
    (application title ok js content)))

(defn constructoras-delete
  [id]
  (let [table "constructoras"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/constructoras")
      (error-404 "No se pudo procesar el record!" "/admin/constructoras"))))
