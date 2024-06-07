(ns sk.handlers.admin.casas.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.casas.model :refer [get-casas get-casas-id]]
            [sk.handlers.admin.casas.view :refer [casas-view casas-edit-view casas-add-view casas-modal-script]]))

(defn casas [_]
  (let [title "Casas"
        ok (get-session-id)
        js nil
        rows (get-casas)
        content (casas-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn casas-edit
  [id]
  (let [title "Modificar casa"
        ok (get-session-id)
        js (casas-modal-script)
        row (get-casas-id  id)
        rows (get-casas)
        content (casas-edit-view title row rows)]
    (application title ok js content)))

(defn casas-save
  [{params :params}]
  (let [table "casas"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/casas")
      (error-404 "No se pudo procesar el record!" "/admin/casas"))))

(defn casas-add
  [_]
  (let [title "Crear nueva casa"
        ok (get-session-id)
        js (casas-modal-script)
        row nil
        rows (get-casas)
        content (casas-add-view title row rows)]
    (application title ok js content)))

(defn casas-delete
  [id]
  (let [table "casas"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/casas")
      (error-404 "No se pudo procesar el record!" "/admin/casas"))))
