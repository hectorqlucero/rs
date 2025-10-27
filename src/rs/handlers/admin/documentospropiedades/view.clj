(ns rs.handlers.admin.documentospropiedades.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn documentos-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "documentos_table" (apply array-map (interleave [:id :created] ["Id" "Created"])) "/admin/documentos" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Id"
         "Tipo documento"
         "Nombre documento"
         "Descripcion"
         "Tabla referencia"
         "Id referencia"
         "Ruta archivo"
         "Nombre archivo"
         "Tamanio kb"
         "Mime type"
         "Fecha subida"
         "Fecha vencimiento"
         "Estado documento"
         "Observaciones"
         "Agente subida"]
         db-fields [:id
                   :tipo_documento
                   :nombre_documento
                   :descripcion
                   :tabla_referencia
                   :id_referencia
                   :ruta_archivo
                   :nombre_archivo
                   :tamanio_kb
                   :mime_type
                   :fecha_subida
                   :fecha_vencimiento
                   :estado_documento
                   :observaciones
                   :agente_subida_display]
         fields (apply array-map (interleave db-fields labels))
         table-id "documentos_table"
         href "/admin/documentospropiedades"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-documentos-fields
  [row]
  ;; Database-driven field generation using smart-build-field-config
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
   (build-field {:id "id_referencia" :type "hidden" :name "id_referencia" :value (:id_referencia row)})

   (build-field (smart-build-field-config {:field-name "tipo_documento" :db-type "TEXT" :comment "escritura, identificacion, comprobante_ingresos, contrato, avaluo, etc."
                                           :table-name "documentos" :required true :value (get row :tipo_documento)}))
   (build-field (smart-build-field-config {:field-name "nombre_documento" :db-type "TEXT" :comment nil
                                           :table-name "documentos" :required true :value (get row :nombre_documento)}))
   (build-field (smart-build-field-config {:field-name "descripcion" :db-type "TEXT" :comment nil
                                           :table-name "documentos" :required false :value (get row :descripcion)}))
   (build-field (smart-build-field-config {:field-name "tabla_referencia" :db-type "TEXT" :comment "propiedades, clientes, ventas, alquileres"
                                           :table-name "documentos" :required false :value (get row :tabla_referencia)}))
   (build-field (smart-build-field-config {:field-name "ruta_archivo" :db-type "TEXT" :comment "path del archivo físico"
                                           :table-name "documentos" :required false :value (get row :ruta_archivo)}))
   (build-field (smart-build-field-config {:field-name "nombre_archivo" :db-type "TEXT" :comment nil
                                           :table-name "documentos" :required false :value (get row :nombre_archivo)}))
   (build-field (smart-build-field-config {:field-name "tamanio_kb" :db-type "INTEGER" :comment nil
                                           :table-name "documentos" :required false :value (get row :tamanio_kb)}))
   (build-field (smart-build-field-config {:field-name "mime_type" :db-type "TEXT" :comment nil
                                           :table-name "documentos" :required false :value (get row :mime_type)}))
   (build-field (smart-build-field-config {:field-name "fecha_subida" :db-type "DATETIME" :comment "CURRENT_TIMESTAMP"
                                           :table-name "documentos" :required false :value (get row :fecha_subida)}))
   (build-field (smart-build-field-config {:field-name "fecha_vencimiento" :db-type "DATE" :comment "para documentos que vencen como identificaciones"
                                           :table-name "documentos" :required false :value (get row :fecha_vencimiento)}))
   (build-field (smart-build-field-config {:field-name "estado_documento" :db-type "TEXT" :comment "activo, vencido, cancelado"
                                           :table-name "documentos" :required false :value (get row :estado_documento)}))
   (build-field (smart-build-field-config {:field-name "agente_subida" :db-type "INTEGER" :comment "referencia a agentes.id"
                                           :table-name "documentos" :required false :value (get row :agente_subida)}))
   (build-field (smart-build-field-config {:field-name "observaciones" :db-type "TEXT" :comment nil
                                           :table-name "documentos" :required false :value (get row :observaciones)}))
   ))

(defn build-documentos-form
  [title row]
  (form "/admin/documentospropiedades/save" (build-documentos-fields row) (build-modal-buttons) title {:bare true}))
