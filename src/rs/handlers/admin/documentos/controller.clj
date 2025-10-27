(ns rs.handlers.admin.documentos.controller
  (:require
   [rs.handlers.admin.documentos.model :refer [get-documentos get-documentos-id]]
   [rs.handlers.admin.documentos.view :refer [documentos-view documentos-form-view]]
   [rs.layout :refer [application error-404]]
   [rs.models.crud :refer [build-form-delete build-form-save]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

(defn documentos
  [request]
  (let [title "Documentos"
        ok (get-session-id request)
        js nil
        rows (get-documentos)
        content (documentos-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))

(defn documentos-add-form
  [_]
  (let [title "New Documentos"
        row nil
        content (documentos-form-view title row)]
    (html content)))

(defn documentos-edit-form
  [_ id]
  (let [title "Edit Documentos"
        row (get-documentos-id id)
        content (documentos-form-view title row)]
    (html content)))

(defn documentos-save
  [{params :params}]
  (let [table "documentos"
  result (build-form-save params table :conn :default)]
    (if result
      {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
      {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"})))

(defn documentos-delete
  [_ id]
  (let [table "documentos"
  result (build-form-delete table id :conn :default)]
    (if result
      {:status 302 :headers {"Location" "/admin/documentos"}}
      (error-404 "Unable to process record!" "/admin/documentos"))))
