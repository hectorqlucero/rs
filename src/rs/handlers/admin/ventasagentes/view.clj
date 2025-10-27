(ns rs.handlers.admin.ventasagentes.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn ventas-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "ventas_table" (apply array-map (interleave [:id :created] ["Id" "Created"])) "/admin/ventas" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Id"
                 "propiedad"
                 "agente"
                 "comprador"
                 "fecha venta"
                 "precio venta"
                 "enganche"]
         db-fields [:id
                    :id_propiedad_display
                    :id_agente_display
                    :id_comprador_display
                    :fecha_venta
                    :precio_venta
                    :enganche]
         fields (apply array-map (interleave db-fields labels))
         table-id "ventas_table"
         href "/admin/ventasagentes"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-ventas-fields
  [row]
  ;; Database-driven field generation using smart-build-field-config
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
   (build-field {:id "id_agente" :type "hidden" :name "id_agente" :value (:id_agente row)})

   (build-field (smart-build-field-config {:field-name "id_propiedad" :db-type "INTEGER" :comment nil
                                           :table-name "ventas" :required true :value (get row :id_propiedad)}))
   (build-field (smart-build-field-config {:field-name "id_comprador" :db-type "INTEGER" :comment nil
                                           :table-name "ventas" :required true :value (get row :id_comprador)}))
   (build-field (smart-build-field-config {:field-name "fecha_venta" :db-type "DATE" :comment nil
                                           :table-name "ventas" :required false :value (get row :fecha_venta)}))
   (build-field (smart-build-field-config {:field-name "precio_venta" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required false :value (get row :precio_venta)}))
   (build-field (smart-build-field-config {:field-name "enganche" :db-type "REAL" :comment "pago inicial"
                                           :table-name "ventas" :required false :value (get row :enganche)}))
   (build-field (smart-build-field-config {:field-name "financiamiento_banco" :db-type "TEXT" :comment "banco que otorga crédito"
                                           :table-name "ventas" :required false :value (get row :financiamiento_banco)}))
   (build-field (smart-build-field-config {:field-name "monto_credito" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required false :value (get row :monto_credito)}))
   (build-field (smart-build-field-config {:field-name "plazo_credito_meses" :db-type "INTEGER" :comment nil
                                           :table-name "ventas" :required false :value (get row :plazo_credito_meses)}))
   (build-field (smart-build-field-config {:field-name "tasa_interes" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required false :value (get row :tasa_interes)}))
   (build-field (smart-build-field-config {:field-name "tipo_credito" :db-type "TEXT" :comment "infonavit, fovissste, bancario, particular"
                                           :table-name "ventas" :required false :value (get row :tipo_credito)}))
   (build-field (smart-build-field-config {:field-name "gastos_escrituracion" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required false :value (get row :gastos_escrituracion)}))
   (build-field (smart-build-field-config {:field-name "avaluo_bancario" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required false :value (get row :avaluo_bancario)}))
   (build-field (smart-build-field-config {:field-name "impuesto_adquisicion" :db-type "REAL" :comment "ISAI"
                                           :table-name "ventas" :required false :value (get row :impuesto_adquisicion)}))
   (build-field (smart-build-field-config {:field-name "otros_gastos" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required false :value (get row :otros_gastos)}))
   (build-field (smart-build-field-config {:field-name "notario_publico" :db-type "TEXT" :comment nil
                                           :table-name "ventas" :required false :value (get row :notario_publico)}))
   (build-field (smart-build-field-config {:field-name "numero_notaria" :db-type "TEXT" :comment nil
                                           :table-name "ventas" :required false :value (get row :numero_notaria)}))
   (build-field (smart-build-field-config {:field-name "fecha_escrituracion" :db-type "DATE" :comment nil
                                           :table-name "ventas" :required false :value (get row :fecha_escrituracion)}))
   (build-field (smart-build-field-config {:field-name "numero_escritura" :db-type "TEXT" :comment nil
                                           :table-name "ventas" :required false :value (get row :numero_escritura)}))
   (build-field (smart-build-field-config {:field-name "registro_publico_propiedad" :db-type "TEXT" :comment nil
                                           :table-name "ventas" :required false :value (get row :registro_publico_propiedad)}))
   (build-field (smart-build-field-config {:field-name "folio_mercantil" :db-type "TEXT" :comment nil
                                           :table-name "ventas" :required false :value (get row :folio_mercantil)}))))

(defn build-ventas-form
  [title row]
  (form "/admin/ventasagentes/save" (build-ventas-fields row) (build-modal-buttons) title {:bare true}))
