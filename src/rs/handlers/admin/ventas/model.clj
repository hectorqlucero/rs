(ns rs.handlers.admin.ventas.model
  (:require [rs.models.crud :refer [Query]]))

(def get-ventas-sql
  (str "SELECT v.id, v.fecha_venta, v.precio_venta as precio_final, v.enganche, v.monto_credito,
               v.plazo_credito_meses, v.tasa_interes, v.tipo_credito, v.notario_publico,
               v.fecha_escrituracion, v.numero_escritura, 'activo' as estado_venta,
               p.titulo as id_propiedad,
               (c.nombre || ' ' || COALESCE(c.apellido_paterno, '')) as id_cliente,
               (a.nombre || ' ' || COALESCE(a.apellido_paterno, '')) as id_agente
         FROM ventas v
         LEFT JOIN propiedades p ON v.id_propiedad = p.id
         LEFT JOIN clientes c ON v.id_comprador = c.id
         LEFT JOIN agentes a ON v.id_agente = a.id
         ORDER BY v.fecha_venta DESC"))

(defn get-ventas
  []
  (Query get-ventas-sql :conn :default))

(def get-ventas-id-sql
  (str "SELECT v.*, 
               p.titulo as propiedad,
               (c.nombre || ' ' || COALESCE(c.apellido_paterno, '')) as comprador,
               (a.nombre || ' ' || COALESCE(a.apellido_paterno, '')) as agente
         FROM ventas v
         LEFT JOIN propiedades p ON v.id_propiedad = p.id
         LEFT JOIN clientes c ON v.id_comprador = c.id
         LEFT JOIN agentes a ON v.id_agente = a.id
         WHERE v.id = ?"))

(defn get-ventas-id
  [id]
  (first (Query [get-ventas-id-sql id] :conn :default)))
