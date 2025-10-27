(ns rs.handlers.vista_propiedades_disponibles.controller
  (:require
   [rs.handlers.vista_propiedades_disponibles.model :refer [get-vista_propiedades_disponibles]]
   [rs.handlers.vista_propiedades_disponibles.view :refer [vista_propiedades_disponibles-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]))

(def allowed-rights ["U" "A" "S"])

(defn vista_propiedades_disponibles
  [request]
  (let [title "Vista_propiedades_disponibles"
        ok (get-session-id request)
        js nil
        rows (get-vista_propiedades_disponibles)
        content (vista_propiedades_disponibles-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))
