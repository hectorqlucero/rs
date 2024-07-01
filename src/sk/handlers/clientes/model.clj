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

;; Start min renta-venta-proceso
(defn casas-renta-min-proceso []
  (-> (Query db "select costo from casas where costo > 0 and tipo = 'R' and status = 'P' order by costo limit 1")
      first
      :costo))

(defn casas-venta-min-proceso []
  (-> (Query db "select costo from casas where costo > 0 and tipo = 'V' and status = 'P' order by costo limit 1")
      first
      :costo))
;; End min renta-venta-proceso

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

(defn clientes-renta-proceso
  []
  (let [min-renta (casas-renta-min-proceso)]
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
  and casas.status = ?
  ")

(defn casas-renta
  [cliente-id]
  (let [row (-> (Query db ["select pc from clientes where id = ?" cliente-id])
                first)
        pc (:pc row)
        status "A"
        result (Query db [casas-renta-sql pc status])
        rows (map #(assoc % :cliente_id cliente-id) result)]
    rows))
;; End casas-renta

;; Start casas-renta-proceso
(defn casas-renta-proceso
  [cliente-id]
  (let [row (-> (Query db ["select pc from clientes where id = ?" cliente-id])
                first)
        pc (:pc row)
        status "P"]
    (Query db [casas-renta-sql pc status])))
;; End casas-renta-proceso

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

(defn clientes-venta-proceso
  []
  (let [min-venta (casas-venta-min-proceso)]
    (Query db [clientes-venta-sql min-venta])))
;; End clientes-venta

;; Start casas-venta
(defn casas-venta-sql [crow status]
  (str
   "
    SELECT
    f.id,
    c.id as casa_id,
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
    AND c.status = \"" status \" "
    ORDER BY
    f.estado,
    f.ciudad,
    z.nombre
  "))

(defn casas-venta
  [cliente-id]
  (let [status "A"
        crow (get-row cliente-id)
        result (Query db (casas-venta-sql crow status))
        rows (map #(assoc % :cliente_id cliente-id) result)]
    rows))
;; End casas-venta

;; Start clientes-proceso
(def clientes-proceso-sql
  "
  select
  proceso.cliente_id,
  proceso.casa_id,
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
  from proceso
  join clientes on clientes.id = proceso.cliente_id
  join casas on casas.id = proceso.casa_id
  join tipo_creditos on tipo_creditos.id = clientes.tipo_creditos_id
  where casas.tipo = ?
  order by nombre_completo
  ")

(defn clientes-renta-proceso
  []
  (Query db [clientes-proceso-sql "R"]))

(defn clientes-venta-proceso
  []
  (Query db [clientes-proceso-sql "V"]))
;; End clientes proceso

;; Start casas-proceso
(def casas-proceso-sql
  "
  select
  proceso.id,
  casas.fraccionamientos_id,
  casas.id as casa_id,
  constructoras.razon_social,
  fraccionamientos.nombre,
  fraccionamientos.estado,
  fraccionamientos.ciudad,
  zonas.nombre as zona,
  casas.modelo,
  casas.recamaras,
  casas.baños,
  casas.plantas,
  casas.mtc,
  casas.mtt,
  CONCAT('$',FORMAT(casas.costo,'es_MX')) as costo,
  casas.comentarios
  FROM proceso
  join casas on casas.id = proceso.casa_id
  join fraccionamientos on fraccionamientos.id = casas.fraccionamientos_id
  join constructoras on constructoras.id = fraccionamientos.constructoras_id
  join zonas on zonas.id = fraccionamientos.zonas_id
  WHERE
  proceso.cliente_id = ?
  and casas.tipo = ?
  ")

(defn casas-renta-proceso
  [cliente-id]
  (Query db [casas-proceso-sql cliente-id "R"]))

(defn casas-venta-proceso
  [cliente-id]
  (Query db [casas-proceso-sql cliente-id "V"]))
;; End casas-proceso

(comment
  (casas-venta-proceso 17)
  (casas-venta-min-proceso)
  (clientes-venta-proceso)

  (casas-renta-proceso 26)
  (casas-renta-min-proceso)
  (clientes-renta-proceso)

  (get-row 17)
  (casas-venta 17)
  (casas-venta-min)
  (clientes-venta)

  (casas-renta 26)
  (casas-renta-min)
  (clientes-renta)

  (get-clientes))
