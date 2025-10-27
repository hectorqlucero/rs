(ns rs.handlers.admin.ventas.controller
  (:require
   [rs.handlers.admin.ventas.model :refer [get-ventas get-ventas-id]]
   [rs.handlers.admin.ventas.view :refer [ventas-view ventas-form-view]]
   [rs.layout :refer [application error-404]]
   [rs.models.crud :refer [build-form-delete build-form-save]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

(defn ventas
  [request]
  (let [title "Ventas"
        ok (get-session-id request)
        js nil
        rows (get-ventas)
        content (ventas-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))

(defn ventas-add-form
  [_]
  (let [title "New Ventas"
        row nil
        content (ventas-form-view title row)]
    (html content)))

(defn ventas-edit-form
  [_ id]
  (let [title "Edit Ventas"
        row (get-ventas-id id)
        content (ventas-form-view title row)]
    (html content)))

(defn ventas-save
  [{params :params}]
  (let [table "ventas"
  result (build-form-save params table :conn :default)]
    (if result
      {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
      {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"})))

(defn ventas-delete
  [_ id]
  (let [table "ventas"
  result (build-form-delete table id :conn :default)]
    (if result
      {:status 302 :headers {"Location" "/admin/ventas"}}
      (error-404 "Unable to process record!" "/admin/ventas"))))
