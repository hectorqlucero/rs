(ns rs.handlers.admin.avaluos.controller
  (:require
   [rs.handlers.admin.avaluos.model :refer [get-avaluos get-avaluos-id]]
   [rs.handlers.admin.avaluos.view :refer [avaluos-view avaluos-form-view]]
   [rs.layout :refer [application error-404]]
   [rs.models.crud :refer [build-form-delete build-form-save]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(def allowed-rights ["U" "A" "S"])

(defn avaluos
  [request]
  (let [title "Avaluos"
        ok (get-session-id request)
        js nil
        rows (get-avaluos)
        content (avaluos-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))

(defn avaluos-add-form
  [_]
  (let [title "New Avaluos"
        row nil
        content (avaluos-form-view title row)]
    (html content)))

(defn avaluos-edit-form
  [_ id]
  (let [title "Edit Avaluos"
        row (get-avaluos-id id)
        content (avaluos-form-view title row)]
    (html content)))

(defn avaluos-save
  [{params :params}]
  (let [table "avaluos"
  result (build-form-save params table :conn :default)]
    (if result
      {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
      {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"})))

(defn avaluos-delete
  [_ id]
  (let [table "avaluos"
  result (build-form-delete table id :conn :default)]
    (if result
      {:status 302 :headers {"Location" "/admin/avaluos"}}
      (error-404 "Unable to process record!" "/admin/avaluos"))))
