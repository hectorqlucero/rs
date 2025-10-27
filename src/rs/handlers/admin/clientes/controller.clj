(ns rs.handlers.admin.clientes.controller
  (:require
   [rs.handlers.admin.clientes.model :refer [get-clientes get-clientes-id]]
   [rs.handlers.admin.clientes.view :refer [clientes-view clientes-form-view]]
   [rs.layout :refer [application error-404]]
   [rs.models.crud :refer [build-form-delete build-form-save]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

(defn clientes
  [request]
  (let [title "Clientes"
        ok (get-session-id request)
        js nil
        rows (get-clientes)
        content (clientes-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))

(defn clientes-add-form
  [_]
  (let [title "New Clientes"
        row nil
        content (clientes-form-view title row)]
    (html content)))

(defn clientes-edit-form
  [_ id]
  (let [title "Edit Clientes"
        row (get-clientes-id id)
        content (clientes-form-view title row)]
    (html content)))

(defn clientes-save
  [{params :params}]
  (let [table "clientes"
  result (build-form-save params table :conn :default)]
    (if result
      {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
      {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"})))

(defn clientes-delete
  [_ id]
  (let [table "clientes"
  result (build-form-delete table id :conn :default)]
    (if result
      {:status 302 :headers {"Location" "/admin/clientes"}}
      (error-404 "Unable to process record!" "/admin/clientes"))))
