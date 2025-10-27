(ns rs.handlers.admin.alquileres.view
  (:require
   [rs.models.form :refer [build-field build-modal-buttons form]]
   [rs.models.grid :refer [build-grid build-grid-with-subgrids]]
   [rs.models.util :refer [smart-build-field-config]]))

;; alquileres-view: If you want to show subgrids, pass a :subgrids vector in args.
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

(defn alquileres-view
  [title rows & [args]]
  (let [labels ["Propiedad" "Cliente" "Agente" "Renta Mensual" "Fecha Inicio" "Estado"]
        db-fields [:propiedad :cliente :agente :monto_mensual :fecha_inicio :estado_alquiler]
        fields (apply array-map (interleave db-fields labels))
        table-id "alquileres_table"
        href "/admin/alquileres"
        args (or args {:new true :edit true :delete true
                       :subgrids [{:title "Pagos Renta"
                                   :table-name "pagos_renta"
                                   :foreign-key "id_alquiler"
                                   :href "/admin/pagos_rentaalquileres"
                                   :icon "bi bi-cash-coin"
                                   :label "Pagos"}]})]
    (if (and (map? args) (contains? args :subgrids))
      (build-grid-with-subgrids title rows table-id fields href args)
      (build-grid title rows table-id fields href args))))

(defn build-alquileres-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})

   ;; Foreign key fields - automatically populated with smart options
   (build-field (smart-build-field-config {:field-name "id_propiedad" :db-type "INTEGER" :comment nil
                                           :table-name "alquileres" :required true :value (get row :id_propiedad)}))

   (build-field (smart-build-field-config {:field-name "id_inquilino" :db-type "INTEGER" :comment nil
                                           :table-name "alquileres" :required true :value (get row :id_inquilino)}))

   (build-field (smart-build-field-config {:field-name "id_agente" :db-type "INTEGER" :comment nil
                                           :table-name "alquileres" :required false :value (get row :id_agente)}))

   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "fecha_inicio" :db-type "DATE" :comment nil
                                           :table-name "alquileres" :required true :value (get row :fecha_inicio)}))

   (build-field (smart-build-field-config {:field-name "fecha_fin" :db-type "DATE" :comment nil
                                           :table-name "alquileres" :required false :value (get row :fecha_fin)}))

   (build-field (smart-build-field-config {:field-name "monto_mensual" :db-type "REAL" :comment nil
                                           :table-name "alquileres" :required true :value (get row :monto_mensual)}))

   (build-field (smart-build-field-config {:field-name "deposito" :db-type "REAL" :comment nil
                                           :table-name "alquileres" :required false :value (get row :deposito)}))

   (build-field (smart-build-field-config {:field-name "dia_pago" :db-type "INTEGER" :comment nil
                                           :table-name "alquileres" :required false :value (get row :dia_pago)}))

   (build-field (smart-build-field-config {:field-name "uso_permitido" :db-type "TEXT"
                                           :comment "habitacional, comercial, oficina"
                                           :table-name "alquileres" :required false :value (get row :uso_permitido)}))

   (build-field (smart-build-field-config {:field-name "incluye_servicios" :db-type "TEXT"
                                           :comment "si, no, parcial"
                                           :table-name "alquileres" :required false :value (get row :incluye_servicios)}))

   (build-field (smart-build-field-config {:field-name "estado_alquiler" :db-type "TEXT"
                                           :comment "activo, cancelado, vencido"
                                           :table-name "alquileres" :required false :value (get row :estado_alquiler)}))

   (build-field (smart-build-field-config {:field-name "observaciones" :db-type "TEXT" :comment nil
                                           :table-name "alquileres" :required false :value (get row :observaciones)}))))

(defn alquileres-form-view
  [title row]
  (form "/admin/alquileres/save" (build-alquileres-fields row) (build-modal-buttons) title {:bare true}))
