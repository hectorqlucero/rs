(ns rs.handlers.vista_contratos_renta_activos.controller
  (:require
   [rs.handlers.vista_contratos_renta_activos.model :refer [get-vista_contratos_renta_activos]]
   [rs.handlers.vista_contratos_renta_activos.view :refer [vista_contratos_renta_activos-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]))

(def allowed-rights ["U" "A" "S"])

(defn vista_contratos_renta_activos
  [request]
  (let [title "Vista_contratos_renta_activos"
        ok (get-session-id request)
        js nil
        rows (get-vista_contratos_renta_activos)
        content (vista_contratos_renta_activos-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))
