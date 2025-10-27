(ns rs.handlers.admin.users.controller
  (:require
   [rs.handlers.admin.users.model :refer [get-user get-users]]
   [rs.handlers.admin.users.view :refer [users-view]]
   [rs.layout :refer [application error-404]]
   [rs.models.crud :refer [build-form-delete build-form-save]]
   [rs.models.util :refer [get-session-id user-level]]
   [hiccup.core :refer [html]]))

(defn users
  [request]
  (let [title "Users"
        ok (get-session-id request)
        js nil
        rows (get-users)
        content (users-view title rows)]
    (if (= (user-level request) "S")
      (application request title ok js content)
      (application request title ok nil "Not authorized to access this item! (level 'S')"))))

(defn users-add-form
  [_]
  (let [title "New User"
        row nil]
    (html (rs.handlers.admin.users.view/users-add-form title row))))

(defn users-edit-form
  [_ id]
  (let [title "Edit User"
        row (get-user id)]
    (html (rs.handlers.admin.users.view/users-edit-form title row))))


(defn users-save
  [request]
  (let [table "users"
        form-fields (or (:params request) (:form-params request) request)]
    (try
      (if (build-form-save form-fields table :conn :default)
        {:status 200 :headers {"Content-Type" "application/json"} :body "{\"ok\":true}"}
        {:status 500 :headers {"Content-Type" "application/json"} :body "{\"ok\":false}"})
      (catch Exception e
        {:status 500
         :headers {"Content-Type" "application/json"}
         :body (str "{\"ok\":false,\"error\":" (pr-str (.getMessage e)) "}")}))))

(defn users-delete
  [_ id]
  (let [table "users"
        result (build-form-delete table id :conn :default)]
    (if result
      {:status 302 :headers {"Location" "/admin/users"}}
      (error-404 "Unable to process record!" "/admin/users"))))
