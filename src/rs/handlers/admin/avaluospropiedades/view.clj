(ns rs.handlers.admin.avaluospropiedades.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn avaluos-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "avaluos_table" (apply array-map (interleave [:id :created] ["Id" "Created"])) "/admin/avaluos" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Id"
         "Perito valuador"
         "Cedula perito"
         "Institucion perito"
         "Fecha avaluo"
         "Vigencia meses"
         "Valor avaluo"
         "Moneda"
         "Metodo valuacion"
         "Proposito avaluo"
         "Superficie terreno avaluo"
         "Superficie construccion avaluo"
         "Estado conservacion"
         "Observaciones perito"
         "Numero avaluo"
         "Fecha vencimiento"
         "Estado avaluo"
         "Archivo avaluo"
         "Fecha registro"
         "Id propiedad"]
         db-fields [:id
                   :perito_valuador
                   :cedula_perito
                   :institucion_perito
                   :fecha_avaluo
                   :vigencia_meses
                   :valor_avaluo
                   :moneda
                   :metodo_valuacion
                   :proposito_avaluo
                   :superficie_terreno_avaluo
                   :superficie_construccion_avaluo
                   :estado_conservacion
                   :observaciones_perito
                   :numero_avaluo
                   :fecha_vencimiento
                   :estado_avaluo
                   :archivo_avaluo
                   :fecha_registro
                   :id_propiedad_display]
         fields (apply array-map (interleave db-fields labels))
         table-id "avaluos_table"
         href "/admin/avaluospropiedades"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-avaluos-fields
  [row]
  ;; Database-driven field generation using smart-build-field-config
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
   (build-field {:id "id_propiedad" :type "hidden" :name "id_propiedad" :value (:id_propiedad row)})

   (build-field (smart-build-field-config {:field-name "perito_valuador" :db-type "TEXT" :comment nil
                                           :table-name "avaluos" :required true :value (get row :perito_valuador)}))
   (build-field (smart-build-field-config {:field-name "cedula_perito" :db-type "TEXT" :comment nil
                                           :table-name "avaluos" :required false :value (get row :cedula_perito)}))
   (build-field (smart-build-field-config {:field-name "institucion_perito" :db-type "TEXT" :comment "INDAABIN, FOVISSSTE, etc."
                                           :table-name "avaluos" :required false :value (get row :institucion_perito)}))
   (build-field (smart-build-field-config {:field-name "fecha_avaluo" :db-type "DATE" :comment nil
                                           :table-name "avaluos" :required true :value (get row :fecha_avaluo)}))
   (build-field (smart-build-field-config {:field-name "vigencia_meses" :db-type "INTEGER" :comment "meses de vigencia del avalúo"
                                           :table-name "avaluos" :required false :value (get row :vigencia_meses)}))
   (build-field (smart-build-field-config {:field-name "valor_avaluo" :db-type "REAL" :comment nil
                                           :table-name "avaluos" :required true :value (get row :valor_avaluo)}))
   (build-field (smart-build-field-config {:field-name "moneda" :db-type "TEXT" :comment "'MXN'"
                                           :table-name "avaluos" :required false :value (get row :moneda)}))
   (build-field (smart-build-field-config {:field-name "metodo_valuacion" :db-type "TEXT" :comment "fisico, comparativo_mercado, residual, etc."
                                           :table-name "avaluos" :required false :value (get row :metodo_valuacion)}))
   (build-field (smart-build-field-config {:field-name "proposito_avaluo" :db-type "TEXT" :comment "credito_hipotecario, venta, seguro, fiscal"
                                           :table-name "avaluos" :required false :value (get row :proposito_avaluo)}))
   (build-field (smart-build-field-config {:field-name "superficie_terreno_avaluo" :db-type "REAL" :comment nil
                                           :table-name "avaluos" :required false :value (get row :superficie_terreno_avaluo)}))
   (build-field (smart-build-field-config {:field-name "superficie_construccion_avaluo" :db-type "REAL" :comment nil
                                           :table-name "avaluos" :required false :value (get row :superficie_construccion_avaluo)}))
   (build-field (smart-build-field-config {:field-name "estado_conservacion" :db-type "TEXT" :comment "excelente, bueno, regular, malo"
                                           :table-name "avaluos" :required false :value (get row :estado_conservacion)}))
   (build-field (smart-build-field-config {:field-name "observaciones_perito" :db-type "TEXT" :comment nil
                                           :table-name "avaluos" :required false :value (get row :observaciones_perito)}))
   (build-field (smart-build-field-config {:field-name "numero_avaluo" :db-type "TEXT" :comment "número oficial del avalúo"
                                           :table-name "avaluos" :required false :value (get row :numero_avaluo)}))
   (build-field (smart-build-field-config {:field-name "fecha_vencimiento" :db-type "DATE" :comment "calculada automáticamente"
                                           :table-name "avaluos" :required false :value (get row :fecha_vencimiento)}))
   (build-field (smart-build-field-config {:field-name "estado_avaluo" :db-type "TEXT" :comment "vigente, vencido, cancelado"
                                           :table-name "avaluos" :required false :value (get row :estado_avaluo)}))
   (build-field (smart-build-field-config {:field-name "archivo_avaluo" :db-type "TEXT" :comment "referencia al documento"
                                           :table-name "avaluos" :required false :value (get row :archivo_avaluo)}))
   (build-field (smart-build-field-config {:field-name "fecha_registro" :db-type "DATETIME" :comment "CURRENT_TIMESTAMP"
                                           :table-name "avaluos" :required false :value (get row :fecha_registro)}))
   ))

(defn build-avaluos-form
  [title row]
  (form "/admin/avaluospropiedades/save" (build-avaluos-fields row) (build-modal-buttons) title {:bare true}))
