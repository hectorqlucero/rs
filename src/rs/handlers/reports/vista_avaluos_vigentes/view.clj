(ns rs.handlers.reports.vista_avaluos_vigentes.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_avaluos_vigentes-view
  [title rows]
  (let [table-id "vista_avaluos_vigentes_table"
        labels ["Propiedad" "Municipio" "Estado" "Perito" "Fecha Avalúo" "Valor Avalúo" "Días Vencimiento" "Precio Actual" "Dif. % Catastral"]
        db-fields [:propiedad :municipio :estado :perito_valuador :fecha_avaluo :valor_avaluo :dias_hasta_vencimiento :precio_actual :diferencia_porcentual_catastral]
        fields (apply array-map (interleave db-fields labels))]
    (build-dashboard title rows table-id fields)))
