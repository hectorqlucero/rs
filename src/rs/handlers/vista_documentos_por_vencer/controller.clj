(ns rs.handlers.vista_documentos_por_vencer.controller
  (:require
   [rs.handlers.vista_documentos_por_vencer.model :refer [get-vista_documentos_por_vencer]]
   [rs.handlers.vista_documentos_por_vencer.view :refer [vista_documentos_por_vencer-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]))

(def allowed-rights ["U" "A" "S"])

(defn vista_documentos_por_vencer
  [request]
  (let [title "Vista_documentos_por_vencer"
        ok (get-session-id request)
        js nil
        rows (get-vista_documentos_por_vencer)
        content (vista_documentos_por_vencer-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))
