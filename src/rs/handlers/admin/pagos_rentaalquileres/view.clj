(ns rs.handlers.admin.pagos_rentaalquileres.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn pagos_renta-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "pagos_renta_table" (apply array-map (interleave [:id :created] ["Id" "Created"])) "/admin/pagos_renta" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Id"
         "Mes correspondiente"
         "Monto"
         "Fecha pago"
         "Metodo pago"
         "Estado pago"
         "Recargo mora"
         "Dias atraso"
         "Numero recibo"
         "Observaciones pago"
         "Agente registro"
         "Id alquiler"]
         db-fields [:id
                   :mes_correspondiente
                   :monto
                   :fecha_pago
                   :metodo_pago
                   :estado_pago
                   :recargo_mora
                   :dias_atraso
                   :numero_recibo
                   :observaciones_pago
                   :agente_registro_display
                   :id_alquiler_display]
         fields (apply array-map (interleave db-fields labels))
         table-id "pagos_renta_table"
         href "/admin/pagos_rentaalquileres"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-pagos_renta-fields
  [row]
  ;; Database-driven field generation using smart-build-field-config
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
   (build-field {:id "id_alquiler" :type "hidden" :name "id_alquiler" :value (:id_alquiler row)})

   (build-field (smart-build-field-config {:field-name "mes_correspondiente" :db-type "TEXT" :comment nil
                                           :table-name "pagos_renta" :required false :value (get row :mes_correspondiente)}))
   (build-field (smart-build-field-config {:field-name "monto" :db-type "REAL" :comment nil
                                           :table-name "pagos_renta" :required false :value (get row :monto)}))
   (build-field (smart-build-field-config {:field-name "fecha_pago" :db-type "DATE" :comment nil
                                           :table-name "pagos_renta" :required false :value (get row :fecha_pago)}))
   (build-field (smart-build-field-config {:field-name "metodo_pago" :db-type "TEXT" :comment "efectivo, cheque, transferencia, credito"
                                           :table-name "pagos_renta" :required false :value (get row :metodo_pago)}))
   (build-field (smart-build-field-config {:field-name "estado_pago" :db-type "TEXT" :comment "pendiente, pagado, atrasado"
                                           :table-name "pagos_renta" :required false :value (get row :estado_pago)}))
   (build-field (smart-build-field-config {:field-name "recargo_mora" :db-type "REAL" :comment "0"
                                           :table-name "pagos_renta" :required false :value (get row :recargo_mora)}))
   (build-field (smart-build-field-config {:field-name "dias_atraso" :db-type "INTEGER" :comment "0"
                                           :table-name "pagos_renta" :required false :value (get row :dias_atraso)}))
   (build-field (smart-build-field-config {:field-name "numero_recibo" :db-type "TEXT" :comment nil
                                           :table-name "pagos_renta" :required false :value (get row :numero_recibo)}))
   (build-field (smart-build-field-config {:field-name "observaciones_pago" :db-type "TEXT" :comment nil
                                           :table-name "pagos_renta" :required false :value (get row :observaciones_pago)}))
   (build-field (smart-build-field-config {:field-name "agente_registro" :db-type "INTEGER" :comment nil
                                           :table-name "pagos_renta" :required false :value (get row :agente_registro)}))
   ))

(defn build-pagos_renta-form
  [title row]
  (form "/admin/pagos_rentaalquileres/save" (build-pagos_renta-fields row) (build-modal-buttons) title {:bare true}))
