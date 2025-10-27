(ns rs.handlers.vista_tramites_pendientes.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_tramites_pendientes-view
  [title rows]
  (let [labels ["Tipo tramite" "Descripcion" "Dependencia" "Numero expediente" "Fecha inicio" "Fecha estimada fin" "Estado tramite" "Responsable" "Costo tramite" "Dias transcurridos" "Dias restantes" "Referencia descripcion" "Cliente nombre" "Agente registro"]
        db-fields [:tipo_tramite :descripcion :dependencia :numero_expediente :fecha_inicio :fecha_estimada_fin :estado_tramite :responsable :costo_tramite :dias_transcurridos :dias_restantes :referencia_descripcion :cliente_nombre :agente_registro]
        fields (apply array-map (interleave db-fields labels))
        table-id "vista_tramites_pendientes_table"]
    (build-dashboard title rows table-id fields)))
