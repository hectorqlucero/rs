(ns rs.handlers.reports.users.controller
  (:require
   [rs.handlers.reports.users.model :refer [get-users]]
   [rs.handlers.reports.users.view :refer [users-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id]]))

(defn users [params]
  (let [title "Users Report"
        ok (get-session-id params)
        js nil
        rows (get-users)
        content (users-view title rows)]
    (application params title ok js content)))

