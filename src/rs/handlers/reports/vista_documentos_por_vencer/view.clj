(ns rs.handlers.reports.vista_documentos_por_vencer.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_documentos_por_vencer-view
  [title rows]
  (let [table-id "vista_documentos_por_vencer_table"
        labels ["Tipo Documento" "Nombre Documento" "Referencia" "Agente Responsable" "Fecha Vencimiento" "Días para Vencer" "Observaciones"]
        db-fields [:tipo_documento :nombre_documento :referencia_nombre :agente_responsable :fecha_vencimiento :dias_hasta_vencimiento :observaciones]
        fields (apply array-map (interleave db-fields labels))]
    (build-dashboard title rows table-id fields)))
