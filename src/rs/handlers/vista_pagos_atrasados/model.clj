(ns rs.handlers.vista_pagos_atrasados.model
  (:require [rs.models.crud :refer [Query]]))

(def get-vista_pagos_atrasados-sql
  (str "SELECT * FROM vista_pagos_atrasados"))

(defn get-vista_pagos_atrasados
  []
  (Query get-vista_pagos_atrasados-sql :conn :default))
