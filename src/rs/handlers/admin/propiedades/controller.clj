(ns rs.handlers.admin.propiedades.controller
  (:require
   [rs.handlers.admin.propiedades.model :refer [get-propiedades get-propiedades-id]]
   [rs.handlers.admin.propiedades.view :refer [propiedades-view propiedades-form-view]]
   [rs.layout :refer [application error-404]]
   [rs.models.crud :refer [build-form-delete build-form-save]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

(defn propiedades
  [request]
  (let [title "Propiedades"
        ok (get-session-id request)
        js nil
        rows (get-propiedades)
        content (propiedades-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))

(defn propiedades-add-form
  [_]
  (let [title "New Propiedades"
        row nil
        content (propiedades-form-view title row)]
    (html content)))

(defn propiedades-edit-form
  [_ id]
  (let [title "Edit Propiedades"
        row (get-propiedades-id id)
        content (propiedades-form-view title row)]
    (html content)))

(defn propiedades-save
  [{params :params}]
  (let [table "propiedades"
  result (build-form-save params table :conn :default)]
    (if result
      {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
      {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"})))

(defn propiedades-delete
  [_ id]
  (let [table "propiedades"
  result (build-form-delete table id :conn :default)]
    (if result
      {:status 302 :headers {"Location" "/admin/propiedades"}}
      (error-404 "Unable to process record!" "/admin/propiedades"))))
