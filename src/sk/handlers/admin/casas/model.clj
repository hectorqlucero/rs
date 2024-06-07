(ns sk.handlers.admin.casas.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-casas-sql
  (str
   "
SELECT 
casas.*,
FORMAT(casas.baños,1) as baños_formatted,
CONCAT('$',' ',FORMAT(casas.costo,2)) as costo_formatted,
FORMAT(casas.mtc,0) as mtc_formatted,
FORMAT(casas.mtt,0) as mtt_formatted,
fraccionamientos.nombre as fraccionamientos_id_formatted
FROM casas
JOIN fraccionamientos on fraccionamientos.id = casas.fraccionamientos_id
"))

(defn get-casas
  []
  (Query db get-casas-sql))

(def get-casas-id-sql
  (str
   "
SELECT *
FROM casas
WHERE id = ?
"))

(defn get-casas-id
  [id]
  (first (Query db [get-casas-id-sql id])))

(defn fraccionamientos_id-options
  []
  (let [row {:label "Seleccionar Fraccionamiento"
             :value ""}
        rows (Query db "select nombre as label,id as value from fraccionamientos ORDER BY nombre")]
    (list* row rows)))

(comment
  (fraccionamientos_id-options)
  (get-casas))
