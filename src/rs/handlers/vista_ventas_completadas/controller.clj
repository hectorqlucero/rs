(ns rs.handlers.vista_ventas_completadas.controller
  (:require
   [rs.handlers.vista_ventas_completadas.model :refer [get-vista_ventas_completadas]]
   [rs.handlers.vista_ventas_completadas.view :refer [vista_ventas_completadas-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]))

(def allowed-rights ["U" "A" "S"])

(defn vista_ventas_completadas
  [request]
  (let [title "Vista_ventas_completadas"
        ok (get-session-id request)
        js nil
        rows (get-vista_ventas_completadas)
        content (vista_ventas_completadas-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))
