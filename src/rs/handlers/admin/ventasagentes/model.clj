(ns rs.handlers.admin.ventasagentes.model
  (:require [rs.models.crud :refer [Query crud-fix-id]]))

;; Generated on 2025-10-26T22:54:18.966-07:00

(def get-ventas-sql
  (str "SELECT ven.id, ven.id_propiedad, ven.id_comprador, ven.id_agente, ven.fecha_venta, ven.precio_venta, ven.enganche, ven.financiamiento_banco, ven.monto_credito, ven.plazo_credito_meses, ven.tasa_interes, ven.tipo_credito, ven.gastos_escrituracion, ven.avaluo_bancario, ven.impuesto_adquisicion, ven.otros_gastos, ven.notario_publico, ven.numero_notaria, ven.fecha_escrituracion, ven.numero_escritura, ven.registro_publico_propiedad, ven.folio_mercantil, age.nombre AS id_agente_display, cli.nombre AS id_comprador_display, pro.titulo AS id_propiedad_display
         FROM ventas ven
         LEFT JOIN agentes age ON ven.id_agente = age.id LEFT JOIN clientes cli ON ven.id_comprador = cli.id LEFT JOIN propiedades pro ON ven.id_propiedad = pro.id
         WHERE ven.id_agente = ?
         ORDER BY ven.id DESC"))

(defn get-ventas
  [parent-id]
  (Query [get-ventas-sql parent-id] :conn :default))

(def get-ventas-id-sql
  (str "SELECT ven.id, ven.id_propiedad, ven.id_comprador, ven.id_agente, ven.fecha_venta, ven.precio_venta, ven.enganche, ven.financiamiento_banco, ven.monto_credito, ven.plazo_credito_meses, ven.tasa_interes, ven.tipo_credito, ven.gastos_escrituracion, ven.avaluo_bancario, ven.impuesto_adquisicion, ven.otros_gastos, ven.notario_publico, ven.numero_notaria, ven.fecha_escrituracion, ven.numero_escritura, ven.registro_publico_propiedad, ven.folio_mercantil, age.nombre AS id_agente_display, cli.nombre AS id_comprador_display, pro.titulo AS id_propiedad_display
         FROM ventas ven
         LEFT JOIN agentes age ON ven.id_agente = age.id LEFT JOIN clientes cli ON ven.id_comprador = cli.id LEFT JOIN propiedades pro ON ven.id_propiedad = pro.id
         WHERE ven.id = ?"))

(defn get-ventas-id
  [id]
  (first (Query [get-ventas-id-sql (crud-fix-id id)] :conn :default)))
