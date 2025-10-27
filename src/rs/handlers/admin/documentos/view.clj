(ns rs.handlers.admin.documentos.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-subgrids]]
            [rs.models.util :refer [smart-build-field-config]]))

;; documentos-view: If you want to show subgrids, pass a :subgrids vector in args.
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

(defn documentos-view
  [title rows & [args]]
  (let [labels ["Tipo Documento" "Nombre" "Tabla Referencia" "Estado"]
        db-fields [:tipo_documento :nombre_documento :tabla_referencia :estado_documento]
        fields (apply array-map (interleave db-fields labels))
        table-id "documentos_table"
        href "/admin/documentos"
        args (or args {:new true :edit true :delete true})]
    (if (and (map? args) (contains? args :subgrids))
      (build-grid-with-subgrids title rows table-id fields href args)
      (build-grid title rows table-id fields href args))))

(defn build-documentos-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})

   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "tipo_documento" :db-type "TEXT"
                                           :comment "escritura, avaluo, cedula_catastral, acta_matrimonio, acta_nacimiento, ine, comprobante_ingresos, comprobante_domicilio, contrato_credito"
                                           :table-name "documentos" :required true :value (get row :tipo_documento)}))

   (build-field (smart-build-field-config {:field-name "nombre_documento" :db-type "TEXT" :comment nil
                                           :table-name "documentos" :required true :value (get row :nombre_documento)}))

   (build-field (smart-build-field-config {:field-name "ruta_archivo" :db-type "TEXT" :comment nil
                                           :table-name "documentos" :required false :value (get row :ruta_archivo)}))

   (build-field (smart-build-field-config {:field-name "tabla_referencia" :db-type "TEXT"
                                           :comment "propiedades, clientes, agentes, ventas, alquileres, tramites"
                                           :table-name "documentos" :required true :value (get row :tabla_referencia)}))

   (build-field (smart-build-field-config {:field-name "id_referencia" :db-type "INTEGER" :comment nil
                                           :table-name "documentos" :required true :value (get row :id_referencia)}))

   (build-field (smart-build-field-config {:field-name "fecha_subida" :db-type "DATETIME" :comment nil
                                           :table-name "documentos" :required false :value (get row :fecha_subida)}))

   (build-field (smart-build-field-config {:field-name "estado_documento" :db-type "TEXT"
                                           :comment "pendiente, aprobado, rechazado"
                                           :table-name "documentos" :required false :value (get row :estado_documento)}))

   ;; Foreign key field
   (build-field (smart-build-field-config {:field-name "id_agente" :db-type "INTEGER" :comment nil
                                           :table-name "documentos" :required false :value (get row :id_agente)}))))

(defn documentos-form-view
  [title row]
  (form "/admin/documentos/save" (build-documentos-fields row) (build-modal-buttons) title {:bare true}))
