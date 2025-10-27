(ns rs.handlers.vista_avaluos_vigentes.controller
  (:require
   [rs.handlers.vista_avaluos_vigentes.model :refer [get-vista_avaluos_vigentes]]
   [rs.handlers.vista_avaluos_vigentes.view :refer [vista_avaluos_vigentes-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]))

(def allowed-rights ["U" "A" "S"])

(defn vista_avaluos_vigentes
  [request]
  (let [title "Vista_avaluos_vigentes"
        ok (get-session-id request)
        js nil
        rows (get-vista_avaluos_vigentes)
        content (vista_avaluos_vigentes-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))
