(ns rs.handlers.admin.tramites.controller
  (:require
   [rs.handlers.admin.tramites.model :refer [get-tramites get-tramites-id]]
   [rs.handlers.admin.tramites.view :refer [tramites-view tramites-form-view]]
   [rs.layout :refer [application error-404]]
   [rs.models.crud :refer [build-form-delete build-form-save]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

(defn tramites
  [request]
  (let [title "Tramites"
        ok (get-session-id request)
        js nil
        rows (get-tramites)
        content (tramites-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))

(defn tramites-add-form
  [_]
  (let [title "New Tramites"
        row nil
        content (tramites-form-view title row)]
    (html content)))

(defn tramites-edit-form
  [_ id]
  (let [title "Edit Tramites"
        row (get-tramites-id id)
        content (tramites-form-view title row)]
    (html content)))

(defn tramites-save
  [{params :params}]
  (let [table "tramites"
  result (build-form-save params table :conn :default)]
    (if result
      {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
      {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"})))

(defn tramites-delete
  [_ id]
  (let [table "tramites"
  result (build-form-delete table id :conn :default)]
    (if result
      {:status 302 :headers {"Location" "/admin/tramites"}}
      (error-404 "Unable to process record!" "/admin/tramites"))))
