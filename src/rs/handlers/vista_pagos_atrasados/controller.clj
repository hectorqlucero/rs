(ns rs.handlers.vista_pagos_atrasados.controller
  (:require
   [rs.handlers.vista_pagos_atrasados.model :refer [get-vista_pagos_atrasados]]
   [rs.handlers.vista_pagos_atrasados.view :refer [vista_pagos_atrasados-view]]
   [rs.layout :refer [application]]
   [rs.models.util :refer [get-session-id user-level]]))

(def allowed-rights ["U" "A" "S"])

(defn vista_pagos_atrasados
  [request]
  (let [title "Vista_pagos_atrasados"
        ok (get-session-id request)
        js nil
        rows (get-vista_pagos_atrasados)
        content (vista_pagos_atrasados-view title rows)
        user-r (user-level request)]
    (if (some #(= user-r %) allowed-rights)
      (application request title ok js content)
      (application request title ok nil (str "Not authorized to access this item! (level(s) " allowed-rights ")")))))
