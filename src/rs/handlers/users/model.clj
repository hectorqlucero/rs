(ns rs.handlers.users.model
  (:require
   [rs.models.crud :refer [db Query]]))

(defn get-users
  []
  (Query db "select * from users_view"))

(comment
  (get-users))
