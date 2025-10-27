(ns rs.handlers.admin.alquileres.controller
  (:require
   [rs.handlers.admin.alquileres.model :refer [get-alquileres get-alquileres-id]]
   [rs.handlers.admin.alquileres.view :refer [alquileres-view alquileres-form-view]]
   [rs.layout :refer [application error-404]]
   [rs.models.crud :refer [build-form-delete build-form-save]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

(defn alquileres
  [request]
  (let [title "Alquileres"
        ok (get-session-id request)
        js nil
        rows (get-alquileres)
        content (alquileres-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))

(defn alquileres-add-form
  [_]
  (let [title "New Alquileres"
        row nil
        content (alquileres-form-view title row)]
    (html content)))

(defn alquileres-edit-form
  [_ id]
  (let [title "Edit Alquileres"
        row (get-alquileres-id id)
        content (alquileres-form-view title row)]
    (html content)))

(defn alquileres-save
  [{params :params}]
  (let [table "alquileres"
  result (build-form-save params table :conn :default)]
    (if result
      {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
      {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"})))

(defn alquileres-delete
  [_ id]
  (let [table "alquileres"
  result (build-form-delete table id :conn :default)]
    (if result
      {:status 302 :headers {"Location" "/admin/alquileres"}}
      (error-404 "Unable to process record!" "/admin/alquileres"))))
