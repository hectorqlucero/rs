(ns rs.handlers.admin.pagos_ventasventas.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new build-grid]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn pagos_ventas-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "pagos_ventas_table" (apply array-map (interleave [:tipo_pago :monto :fecha_pago :metodo_pago :numero_recibo] ["Tipo Pago" "Monto" "Fecha" "Método" "Estado"])) "/admin/pagos_ventas" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Tipo Pago" "Monto" "Fecha" "Método" "Banco" "Recibo" "Agente"]
         db-fields [:tipo_pago :monto :fecha_pago :metodo_pago :banco_origen :numero_recibo :agente_registro]
         fields (apply array-map (interleave db-fields labels))
         table-id "pagos_ventas_table"
         href "/admin/pagos_ventasventas"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-pagos_ventas-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
   (build-field {:id "id_venta" :type "hidden" :name "id_venta" :value (:id_venta row)})

   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "tipo_pago" :db-type "TEXT"
                                           :comment "enganche, abono, liquidacion, escrituracion"
                                           :table-name "pagos_ventas" :required true :value (get row :tipo_pago)}))

   (build-field (smart-build-field-config {:field-name "monto" :db-type "REAL" :comment nil
                                           :table-name "pagos_ventas" :required true :value (get row :monto)}))

   (build-field (smart-build-field-config {:field-name "fecha_pago" :db-type "DATE" :comment nil
                                           :table-name "pagos_ventas" :required false :value (get row :fecha_pago)}))

   (build-field (smart-build-field-config {:field-name "metodo_pago" :db-type "TEXT"
                                           :comment "efectivo, cheque, transferencia, credito"
                                           :table-name "pagos_ventas" :required false :value (get row :metodo_pago)}))

   (build-field (smart-build-field-config {:field-name "numero_referencia" :db-type "TEXT" :comment nil
                                           :table-name "pagos_ventas" :required false :value (get row :numero_referencia)}))

   (build-field (smart-build-field-config {:field-name "banco_origen" :db-type "TEXT" :comment nil
                                           :table-name "pagos_ventas" :required false :value (get row :banco_origen)}))

   (build-field (smart-build-field-config {:field-name "observaciones" :db-type "TEXT" :comment nil
                                           :table-name "pagos_ventas" :required false :value (get row :observaciones)}))

   (build-field (smart-build-field-config {:field-name "numero_recibo" :db-type "TEXT" :comment nil
                                           :table-name "pagos_ventas" :required false :value (get row :numero_recibo)}))

   ;; Foreign key field - automatically populated with agente options
   (build-field (smart-build-field-config {:field-name "agente_registro" :db-type "INTEGER" :comment nil
                                           :table-name "pagos_ventas" :required true :value (get row :agente_registro)}))))

(defn build-pagos_ventas-form
  [title row]
  (form "/admin/pagos_ventasventas/save" (build-pagos_ventas-fields row) (build-modal-buttons) title {:bare true}))
