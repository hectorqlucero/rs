(ns sk.handlers.clientes.model
  (:require [sk.models.crud :refer [Query db]]))

;; Start get-rows
(def get-rows-sql
  "
  SELECT
  c.id,
  c.nombre,
  c.paterno,
  c.materno,
  c.telefono,
  c.celular,
  c.email,
  c.ingresos,
  CONCAT('$',format(c.ingresos,2)) as ingresos_formatted,
  c.pc,
  CONCAT('$',format(c.pc,2)) as pc_formatted,
  c.tipo_creditos_id,
  t.nombre as tipo_creditos
  FROM clientes c
  JOIN tipo_creditos t on t.id = c.tipo_creditos_id
  ORDER BY
  c.nombre,c.paterno,c.materno
  ")
(defn get-rows []
  (Query db [get-rows-sql]))
;; End get-rows

;; Start get-casas
(defn get-casas-sql [clientes_id crow]
  "Note: the 4 hardcoded comes from the tipo_creditos_id = 4 = Todos. Prevented users to delete tipo_creditos_table to allow this..."
  (str
   "
    SELECT
    f.id,
    s.razon_social,
    f.nombre,
    f.estado,
    f.ciudad,
    z.nombre as zona,
    c.modelo,
    c.recamaras,
    c.baños,
    c.plantas,
    c.mtc,
    c.mtt,
    CONCAT('$',FORMAT(c.costo,'es_MX')) as costo,
    c.comentarios
    FROM fraccionamientos f
    JOIN constructoras s on s.id = f.constructoras_id
    JOIN casas c on c.fraccionamientos_id = f.id
    JOIN zonas z on z.id = f.zonas_id
    WHERE
    (f.tipo_creditos_id = " (:tipo_creditos_id crow) "
    OR f.tipo_creditos_id = 4)
    AND c.costo <= " (:pc crow) "
    AND c.tipo = \"" (:tipo crow) \" "
    ORDER BY
    f.estado,
    f.ciudad,
    z.nombre
    "))

(defn get-clientes-row [clientes_id]
  (first (Query db ["select id,tipo_creditos_id,tipo,pc from clientes where id = ?" clientes_id])))

(defn get-casas [clientes_id]
  (Query db [(get-casas-sql clientes_id (get-clientes-row clientes_id))]))
;; End get-casas

(comment
  (get-clientes-row 8)
  (get-casas-sql 8 (get-clientes-row 8))
  (get-casas 8)
  (get-rows))
