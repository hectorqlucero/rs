(ns rs.handlers.vista_documentos_por_vencer.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_documentos_por_vencer-view
  [title rows]
  (let [labels ["Tipo documento" "Nombre documento" "Tabla referencia" "Id referencia" "Fecha vencimiento" "Dias hasta vencimiento" "Referencia nombre" "Agente responsable" "Observaciones"]
        db-fields [:tipo_documento :nombre_documento :tabla_referencia :id_referencia :fecha_vencimiento :dias_hasta_vencimiento :referencia_nombre :agente_responsable :observaciones]
        fields (apply array-map (interleave db-fields labels))
        table-id "vista_documentos_por_vencer_table"]
    (build-dashboard title rows table-id fields)))
