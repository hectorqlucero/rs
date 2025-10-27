(ns rs.handlers.vista_ventas_completadas.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_ventas_completadas-view
  [title rows]
  (let [labels ["Propiedad" "Direccion" "Municipio" "Estado" "Comprador" "Telefono comprador" "Rfc comprador" "Propietario" "Agente" "Fecha venta" "Precio venta" "Enganche" "Monto credito" "Tipo credito" "Financiamiento banco" "Gastos escrituracion" "Impuesto adquisicion" "Otros gastos" "Total gastos" "Notario publico" "Numero escritura" "Fecha escrituracion" "Comision agente" "Comision pagada" "Estado escrituracion"]
        db-fields [:propiedad :direccion :municipio :estado :comprador :telefono_comprador :rfc_comprador :propietario :agente :fecha_venta :precio_venta :enganche :monto_credito :tipo_credito :financiamiento_banco :gastos_escrituracion :impuesto_adquisicion :otros_gastos :total_gastos :notario_publico :numero_escritura :fecha_escrituracion :comision_agente :comision_pagada :estado_escrituracion]
        fields (apply array-map (interleave db-fields labels))
        table-id "vista_ventas_completadas_table"]
    (build-dashboard title rows table-id fields)))
