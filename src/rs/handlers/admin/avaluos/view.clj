(ns rs.handlers.admin.avaluos.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-subgrids]]
            [rs.models.util :refer [smart-build-field-config]]))

;; avaluos-view: If you want to show subgrids, pass a :subgrids vector in args.
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

(defn avaluos-view
  [title rows & [args]]
  (let [labels ["Id"
                "propiedad"
                "Perito valuador"
                "Cedula perito"
                "Institucion perito"
                "Fecha avaluo"]
        db-fields [:id
                   :titulo
                   :perito_valuador
                   :cedula_perito
                   :institucion_perito
                   :fecha_avaluo]
        fields (apply array-map (interleave db-fields labels))
        table-id "avaluos_table"
        href "/admin/avaluos"
        args (or args {:new true :edit true :delete true})]
    (if (and (map? args) (contains? args :subgrids))
      (build-grid-with-subgrids title rows table-id fields href args)
      (build-grid title rows table-id fields href args))))

(defn build-avaluos-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})

   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "id_propiedad" :db-type "INTEGER" :comment nil
                                           :table-name "avaluos" :required true :value (get row :id_propiedad)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "perito_valuador" :db-type "TEXT" :comment nil
                                           :table-name "avaluos" :required true :value (get row :perito_valuador)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "cedula_perito" :db-type "TEXT" :comment nil
                                           :table-name "avaluos" :required false :value (get row :cedula_perito)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "institucion_perito" :db-type "TEXT" :comment "INDAABIN, FOVISSSTE, etc."
                                           :table-name "avaluos" :required false :value (get row :institucion_perito)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "fecha_avaluo" :db-type "DATE" :comment nil
                                           :table-name "avaluos" :required true :value (get row :fecha_avaluo)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "vigencia_meses" :db-type "INTEGER" :comment "meses de vigencia del avalúo"
                                           :table-name "avaluos" :required false :value (get row :vigencia_meses)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "valor_avaluo" :db-type "REAL" :comment nil
                                           :table-name "avaluos" :required true :value (get row :valor_avaluo)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "moneda" :db-type "TEXT" :comment nil
                                           :table-name "avaluos" :required false :value (get row :moneda)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "metodo_valuacion" :db-type "TEXT" :comment "fisico, comparativo_mercado, residual, etc."
                                           :table-name "avaluos" :required false :value (get row :metodo_valuacion)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "proposito_avaluo" :db-type "TEXT" :comment "credito_hipotecario, venta, seguro, fiscal"
                                           :table-name "avaluos" :required false :value (get row :proposito_avaluo)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "superficie_terreno_avaluo" :db-type "REAL" :comment nil
                                           :table-name "avaluos" :required false :value (get row :superficie_terreno_avaluo)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "superficie_construccion_avaluo" :db-type "REAL" :comment nil
                                           :table-name "avaluos" :required false :value (get row :superficie_construccion_avaluo)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "estado_conservacion" :db-type "TEXT" :comment "excelente, bueno, regular, malo"
                                           :table-name "avaluos" :required false :value (get row :estado_conservacion)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "observaciones_perito" :db-type "TEXT" :comment nil
                                           :table-name "avaluos" :required false :value (get row :observaciones_perito)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "numero_avaluo" :db-type "TEXT" :comment "número oficial del avalúo"
                                           :table-name "avaluos" :required false :value (get row :numero_avaluo)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "fecha_vencimiento" :db-type "DATE" :comment "calculada automáticamente"
                                           :table-name "avaluos" :required false :value (get row :fecha_vencimiento)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "estado_avaluo" :db-type "TEXT" :comment "vigente, vencido, cancelado"
                                           :table-name "avaluos" :required false :value (get row :estado_avaluo)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "archivo_avaluo" :db-type "TEXT" :comment "referencia al documento"
                                           :table-name "avaluos" :required false :value (get row :archivo_avaluo)}))
   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "fecha_registro" :db-type "DATETIME" :comment nil
                                           :table-name "avaluos" :required false :value (get row :fecha_registro)}))))

(defn avaluos-form-view
  [title row]
  (form "/admin/avaluos/save" (build-avaluos-fields row) (build-modal-buttons) title {:bare true}))
