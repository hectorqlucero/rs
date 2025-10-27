(ns rs.handlers.vista_agentes_performance.controller
  (:require
   [rs.handlers.vista_agentes_performance.model :refer [get-vista_agentes_performance]]
   [rs.handlers.vista_agentes_performance.view :refer [vista_agentes_performance-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]))

(def allowed-rights ["U" "A" "S"])

(defn vista_agentes_performance
  [request]
  (let [title "Vista_agentes_performance"
        ok (get-session-id request)
        js nil
        rows (get-vista_agentes_performance)
        content (vista_agentes_performance-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))
