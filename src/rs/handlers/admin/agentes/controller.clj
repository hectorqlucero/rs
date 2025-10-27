(ns rs.handlers.admin.agentes.controller
  (:require
   [rs.handlers.admin.agentes.model :refer [get-agentes get-agentes-id]]
   [rs.handlers.admin.agentes.view :refer [agentes-view agentes-form-view]]
   [rs.layout :refer [application error-404]]
   [rs.models.crud :refer [build-form-delete build-form-save]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

(defn agentes
  [request]
  (let [title "Agentes"
        ok (get-session-id request)
        js nil
        rows (get-agentes)
        content (agentes-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))

(defn agentes-add-form
  [_]
  (let [title "New Agentes"
        row nil
        content (agentes-form-view title row)]
    (html content)))

(defn agentes-edit-form
  [_ id]
  (let [title "Edit Agentes"
        row (get-agentes-id id)
        content (agentes-form-view title row)]
    (html content)))

(defn agentes-save
  [{params :params}]
  (let [table "agentes"
  result (build-form-save params table :conn :default)]
    (if result
      {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
      {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"})))

(defn agentes-delete
  [_ id]
  (let [table "agentes"
  result (build-form-delete table id :conn :default)]
    (if result
      {:status 302 :headers {"Location" "/admin/agentes"}}
      (error-404 "Unable to process record!" "/admin/agentes"))))
