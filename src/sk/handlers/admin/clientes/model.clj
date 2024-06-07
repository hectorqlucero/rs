(ns sk.handlers.admin.clientes.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-clientes-sql
  (str
   "
SELECT clientes.*,
CONCAT(IFNULL(clientes.nombre,''),' ',IFNULL(clientes.paterno,''),' ',IFNULL(clientes.materno,'')) as nombre_formatted,
tipo_creditos.nombre as tipo_creditos_id_formatted,
CONCAT('$',FORMAT(clientes.ingresos,2)) as ingresos_formatted,
CONCAT('$',FORMAT(clientes.pc,2)) as pc_formatted
FROM clientes
JOIN tipo_creditos on tipo_creditos.id = clientes.tipo_creditos_id
ORDER BY nombre,paterno,materno
"))

(defn get-clientes
  []
  (Query db get-clientes-sql))

(def get-clientes-id-sql
  (str
   "
SELECT *
FROM clientes
WHERE id = ?
"))

(defn get-clientes-id
  [id]
  (first (Query db [get-clientes-id-sql id])))

(defn tipo_creditos_id-options
  []
  (let [row {:label "Seleccionar tipo de credito"
             :value ""}
        rows (Query db "select nombre as label, id as value from tipo_creditos order by nombre")]
    (list* row rows)))

(comment
  (tipo_creditos_id-options)
  (get-clientes))
