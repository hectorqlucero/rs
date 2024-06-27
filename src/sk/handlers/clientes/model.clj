(ns sk.handlers.clientes.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

;; Start get-clientes
(def get-clientes-sql
  (str
   "
SELECT clientes.*,
CONCAT(IFNULL(clientes.nombre,''),' ',IFNULL(clientes.paterno,''),' ',IFNULL(clientes.materno,'')) as nombre_completo,
CASE
   WHEN clientes.tipo = 'V' THEN 'Venta'
   WHEN clientes.tipo = 'R' THEN 'Renta'
END as tipo_formatted,
CONCAT('$','',FORMAT(clientes.ingresos,2)) as ingresos_formatted,
CONCAT('$','',FORMAT(clientes.pc,2)) as pc_formatted,
tipo_creditos.nombre as tipo_creditos_id_formatted
FROM clientes
JOIN tipo_creditos on tipo_creditos.id = clientes.tipo_creditos_id
ORDER BY clientes.nombre,clientes.paterno,clientes.materno
"))

(defn get-clientes
  []
  (Query db get-clientes-sql))
;; End get-clientes

;; Start get-row
(def get-row-sql
  "
  SELECT
  c.id,
  c.nombre,
  c.paterno,
  c.materno,
  CONCAT(IFNULL(c.nombre,''),' ',IFNULL(c.paterno,''),' ',IFNULL(c.materno,'')) as nombre_completo,
  c.telefono,
  c.celular,
  c.email,
  c.ingresos,
  CONCAT('$',format(c.ingresos,2)) as ingresos_formatted,
  c.pc,
  CONCAT('$',format(c.pc,2)) as pc_formatted,
  c.tipo_creditos_id,
  t.nombre as tipo_creditos,
  c.tipo,
  CASE
  WHEN c.tipo = 'V' THEN 'Venta'
  WHEN c.tipo = 'R' THEN 'Renta'
  END as tipo_formatted
  FROM clientes c
  JOIN tipo_creditos t on t.id = c.tipo_creditos_id
  WHERE c.id = ?
  ORDER BY
  c.nombre,c.paterno,c.materno
  ")

(defn get-row [cliente-id]
  (-> (Query db [get-row-sql cliente-id])
      first))
;; End get-row

;; Start min renta-venta
(defn casas-renta-min []
  (-> (Query db "select costo from casas where costo > 0 and tipo = 'R' and status = 'A' order by costo limit 1")
      first
      :costo))

(defn casas-venta-min []
  (-> (Query db "select costo from casas where costo > 0 and tipo = 'V' and status = 'A' order by costo limit 1")
      first
      :costo))
;; End min renta-venta

;; Start clientes-renta
(def clientes-renta-sql
  "
  select
  clientes.id,
  CONCAT(IFNULL(clientes.nombre,''),' ',IFNULL(clientes.paterno,''),' ',IFNULL(clientes.materno,'')) as nombre_completo,
  clientes.celular,
  clientes.email,
  clientes.ingresos,
  CONCAT('$',format(clientes.ingresos,2)) as ingresos_formatted,
  clientes.pc,
  CONCAT('$',format(clientes.pc,2)) as pc_formatted,
  clientes.tipo_creditos_id,
  tipo_creditos.nombre as tipo_creditos_id_formatted,
  clientes.tipo,
  CASE
  WHEN clientes.tipo = 'V' THEN 'Venta'
  WHEN clientes.tipo = 'R' THEN 'Renta'
  END as tipo_formatted
  from clientes
  join tipo_creditos on tipo_creditos.id = clientes.tipo_creditos_id
  where tipo = 'R'
  and clientes.pc >= ?
  ")

(defn clientes-renta
  []
  (let [min-renta (casas-renta-min)]
    (Query db [clientes-renta-sql min-renta])))
;; End clientes-renta

;; Start casas-renta
(def casas-renta-sql
  "
  select
  casas.id,
  fraccionamientos.ciudad,
  constructoras.razon_social,
  fraccionamientos.nombre,
  zonas.nombre as zona,
  CONCAT('$',format(casas.costo,2)) as costo,
  casas.mtc,
  casas.mtt,
  casas.plantas,
  casas.recamaras,
  casas.baños
  from casas
  join fraccionamientos on fraccionamientos.id = casas.fraccionamientos_id
  join constructoras on constructoras.id = fraccionamientos.constructoras_id
  join zonas on zonas.id = fraccionamientos.zonas_id
  where
  casas.costo <= ?
  and casas.tipo = 'R'
  and casas.status = 'A'
  ")

(defn casas-renta
  [cliente-id]
  (let [row (-> (Query db ["select pc from clientes where id = ?" cliente-id])
                first)
        pc (:pc row)]
    (Query db [casas-renta-sql pc])))
;; End casas-renta

;; Start clientes-venta
(def clientes-venta-sql
  "
  select
  clientes.id,
  CONCAT(IFNULL(clientes.nombre,''),' ',IFNULL(clientes.paterno,''),' ',IFNULL(clientes.materno,'')) as nombre_completo,
  clientes.celular,
  clientes.email,
  clientes.ingresos,
  CONCAT('$',format(clientes.ingresos,2)) as ingresos_formatted,
  clientes.pc,
  CONCAT('$',format(clientes.pc,2)) as pc_formatted,
  clientes.tipo_creditos_id,
  tipo_creditos.nombre as tipo_creditos_id_formatted,
  clientes.tipo,
  CASE
  WHEN clientes.tipo = 'V' THEN 'Venta'
  WHEN clientes.tipo = 'R' THEN 'Renta'
  END as tipo_formatted
  from clientes
  join tipo_creditos on tipo_creditos.id = clientes.tipo_creditos_id
  where tipo = 'V'
  and clientes.pc >= ?
  ")

(defn clientes-venta
  []
  (let [min-venta (casas-venta-min)]
    (Query db [clientes-venta-sql min-venta])))
;; End clientes-venta

;; Start casas-venta
(defn casas-venta-sql [crow]
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
    AND c.status = 'A'
    ORDER BY
    f.estado,
    f.ciudad,
    z.nombre
  "))

(defn casas-venta
  [cliente-id]
  (let [crow (get-row cliente-id)]
    (Query db (casas-venta-sql crow))))
;; End casas-venta

(comment
  (get-row 17)
  (casas-venta 17)
  (casas-venta-min)
  (clientes-venta)

  (casas-renta 26)
  (casas-renta-min)
  (clientes-renta)

  (get-clientes))
