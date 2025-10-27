(ns rs.handlers.vista_clientes_completa.controller
  (:require
   [rs.handlers.vista_clientes_completa.model :refer [get-vista_clientes_completa]]
   [rs.handlers.vista_clientes_completa.view :refer [vista_clientes_completa-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]))

(def allowed-rights ["U" "A" "S"])

(defn vista_clientes_completa
  [request]
  (let [title "Vista_clientes_completa"
        ok (get-session-id request)
        js nil
        rows (get-vista_clientes_completa)
        content (vista_clientes_completa-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))
