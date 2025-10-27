(ns rs.handlers.vista_tramites_pendientes.controller
  (:require
   [rs.handlers.vista_tramites_pendientes.model :refer [get-vista_tramites_pendientes]]
   [rs.handlers.vista_tramites_pendientes.view :refer [vista_tramites_pendientes-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]))

(def allowed-rights ["U" "A" "S"])

(defn vista_tramites_pendientes
  [request]
  (let [title "Vista_tramites_pendientes"
        ok (get-session-id request)
        js nil
        rows (get-vista_tramites_pendientes)
        content (vista_tramites_pendientes-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))
