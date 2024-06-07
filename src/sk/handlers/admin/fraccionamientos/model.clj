(ns sk.handlers.admin.fraccionamientos.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-fraccionamientos-sql
  (str
   "
SELECT fraccionamientos.*,
constructoras.razon_social as constructoras_id_formatted,
tipo_creditos.nombre as tipo_creditos_id_formatted,
zonas.nombre as zonas_id_formatted
FROM fraccionamientos
JOIN constructoras on constructoras.id = fraccionamientos.constructoras_id
JOIN tipo_creditos on tipo_creditos.id = fraccionamientos.tipo_creditos_id
JOIN zonas on zonas.id = fraccionamientos.zonas_id
"))

(defn get-fraccionamientos
  []
  (Query db get-fraccionamientos-sql))

(def get-fraccionamientos-id-sql
  (str
   "
SELECT *
FROM fraccionamientos
WHERE id = ?
"))

(defn get-fraccionamientos-id
  [id]
  (first (Query db [get-fraccionamientos-id-sql id])))

(defn constructoras_id-options
  []
  (let [row {:label "Seleccionar constructora"
             :value ""}
        rows (Query db "select razon_social as label,id as value from constructoras order by razon_social")]
    (list* row rows)))

(defn tipo_creditos_id-options
  []
  (let [row {:label "Seleccionar Tipo de Credito"
             :value ""}
        rows (Query db "select nombre as label,id as value from tipo_creditos order by nombre")]
    (list* row rows)))

(defn zonas_id-options
  []
  (let [row {:label "Seleccionar Zona"
             :value ""}
        rows (Query db "select nombre as label,id as value from zonas order by nombre")]
    (list* row rows)))
(comment
  (zonas_id-options)
  (tipo_creditos_id-options)
  (constructoras_id-options)
  (get-fraccionamientos))
