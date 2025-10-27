(ns rs.handlers.admin.ventas.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-subgrids]]
            [rs.models.util :refer [smart-build-field-config]]))

;; ventas-view: If you want to show subgrids, pass a :subgrids vector in args.
;; Example:
;;   (let [args {:new true
;;               :edit true
;;               :delete true
;;               :subgrids [{:title "Phones"
;;                           :table-name "phones"
;;                           :foreign-key "user_id"
;;                           :href "/admin/phonesusers"
;;                           :icon "bi bi-telephone"
;;                           :label "Phones"}]}]
;;     (build-grid-with-subgrids title rows table-id fields href args))
;; Otherwise, just use build-grid as usual.

(defn ventas-view
  [title rows & [args]]
  (let [labels ["Propiedad" "Cliente" "Agente" "Precio Final" "Fecha Venta" "Estado"]
        db-fields [:id_propiedad :id_cliente :id_agente :precio_final :fecha_venta :estado_venta]
        fields (apply array-map (interleave db-fields labels))
        table-id "ventas_table"
        href "/admin/ventas"
        args (or args {:new true :edit true :delete true
                       :subgrids [{:title "Pagos Ventas"
                                   :table-name "pagos_ventas"
                                   :foreign-key "id_venta"
                                   :href "/admin/pagos_ventasventas"
                                   :icon "bi bi-cash-coin"
                                   :label "Pagos"}]})]
    (if (and (map? args) (contains? args :subgrids))
      (build-grid-with-subgrids title rows table-id fields href args)
      (build-grid title rows table-id fields href args))))

(defn build-ventas-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})

   ;; Foreign key fields - automatically populated with options
   (build-field (smart-build-field-config {:field-name "id_propiedad" :db-type "INTEGER" :comment nil
                                           :table-name "ventas" :required true :value (get row :id_propiedad)}))

   (build-field (smart-build-field-config {:field-name "id_comprador" :db-type "INTEGER" :comment nil
                                           :table-name "ventas" :required true :value (get row :id_comprador)}))

   (build-field (smart-build-field-config {:field-name "id_agente" :db-type "INTEGER" :comment nil
                                           :table-name "ventas" :required false :value (get row :id_agente)}))

   ;; Smart fields based on database schema
   (build-field (smart-build-field-config {:field-name "fecha_venta" :db-type "DATE" :comment nil
                                           :table-name "ventas" :required true :value (get row :fecha_venta)}))

   (build-field (smart-build-field-config {:field-name "precio_venta" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required true :value (get row :precio_venta)}))

   (build-field (smart-build-field-config {:field-name "tipo_credito" :db-type "TEXT"
                                           :comment "infonavit, fovissste, bancario, particular"
                                           :table-name "ventas" :required false :value (get row :tipo_credito)}))

   (build-field (smart-build-field-config {:field-name "monto_credito" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required false :value (get row :monto_credito)}))

   (build-field (smart-build-field-config {:field-name "enganche" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required false :value (get row :enganche)}))

   (build-field (smart-build-field-config {:field-name "plazo_meses" :db-type "INTEGER" :comment nil
                                           :table-name "ventas" :required false :value (get row :plazo_meses)}))

   (build-field (smart-build-field-config {:field-name "tasa_interes" :db-type "REAL" :comment nil
                                           :table-name "ventas" :required false :value (get row :tasa_interes)}))

   (build-field (smart-build-field-config {:field-name "escriturado" :db-type "TEXT"
                                           :comment "si, no, proceso"
                                           :table-name "ventas" :required false :value (get row :escriturado)}))

   (build-field (smart-build-field-config {:field-name "observaciones" :db-type "TEXT" :comment nil
                                           :table-name "ventas" :required false :value (get row :observaciones)}))))

(defn ventas-form-view
  [title row]
  (form "/admin/ventas/save" (build-ventas-fields row) (build-modal-buttons) title {:bare true}))
