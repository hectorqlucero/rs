(ns rs.handlers.users.controller
  (:require
   [rs.handlers.users.model :refer [get-users]]
   [rs.handlers.users.view :refer [users-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id]]))

(defn users
  [request]
  (let [title "Dashboard"
        ok (get-session-id request)
        js nil
        rows (get-users)
        content (users-view title rows)]
    (application request title ok js content)))
