(ns rs.handlers.vista_agentes_performance.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_agentes_performance-view
  [title rows]
  (let [labels ["Agente" "Total Ventas" "Monto Ventas" "Contratos Renta" "Monto Rentas" "Comisiones Totales" "Comisiones Pagadas" "Comisiones Pendientes" "Propiedades"]
        db-fields [:agente :total_ventas :monto_total_ventas :total_contratos_renta :monto_mensual_rentas :total_comisiones :comisiones_pagadas :comisiones_pendientes :propiedades_en_cartera]
        fields (apply array-map (interleave db-fields labels))
        table-id "vista_agentes_performance_table"]
    (build-dashboard title rows table-id fields)))
