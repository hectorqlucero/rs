(ns rs.handlers.admin.alquileresagentes.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn alquileres-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "alquileres_table" (apply array-map (interleave [:id :created] ["Id" "Created"])) "/admin/alquileres" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Id"
                 "propiedad"
                 "agente"
                 "inquilino"
                 "Fecha inicio"
                 "Fecha fin"
                 "Monto mensual"
                 "Dia pago"
                 "estado"]
         db-fields [:id
                    :id_propiedad_display
                    :id_agente_display
                    :id_inquilino_display
                    :fecha_inicio
                    :fecha_fin
                    :monto_mensual
                    :dia_pago
                    :estado_alquiler]
         fields (apply array-map (interleave db-fields labels))
         table-id "alquileres_table"
         href "/admin/alquileresagentes"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-alquileres-fields
  [row]
  ;; Database-driven field generation using smart-build-field-config
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
   (build-field {:id "id_agente" :type "hidden" :name "id_agente" :value (:id_agente row)})

   (build-field (smart-build-field-config {:field-name "id_propiedad" :db-type "INTEGER" :comment nil
                                           :table-name "alquileres" :required true :value (get row :id_propiedad)}))
   (build-field (smart-build-field-config {:field-name "id_inquilino" :db-type "INTEGER" :comment nil
                                           :table-name "alquileres" :required true :value (get row :id_inquilino)}))
   (build-field (smart-build-field-config {:field-name "fecha_inicio" :db-type "DATE" :comment nil
                                           :table-name "alquileres" :required false :value (get row :fecha_inicio)}))
   (build-field (smart-build-field-config {:field-name "fecha_fin" :db-type "DATE" :comment nil
                                           :table-name "alquileres" :required false :value (get row :fecha_fin)}))
   (build-field (smart-build-field-config {:field-name "monto_mensual" :db-type "REAL" :comment nil
                                           :table-name "alquileres" :required false :value (get row :monto_mensual)}))
   (build-field (smart-build-field-config {:field-name "deposito_garantia" :db-type "REAL" :comment "generalmente 2 meses de renta"
                                           :table-name "alquileres" :required false :value (get row :deposito_garantia)}))
   (build-field (smart-build-field-config {:field-name "primer_mes" :db-type "REAL" :comment "pago del primer mes"
                                           :table-name "alquileres" :required false :value (get row :primer_mes)}))
   (build-field (smart-build-field-config {:field-name "ultimo_mes" :db-type "REAL" :comment "pago del último mes por adelantado"
                                           :table-name "alquileres" :required false :value (get row :ultimo_mes)}))
   (build-field (smart-build-field-config {:field-name "incremento_anual" :db-type "REAL" :comment "% de incremento anual"
                                           :table-name "alquileres" :required false :value (get row :incremento_anual)}))
   (build-field (smart-build-field-config {:field-name "dia_pago" :db-type "INTEGER" :comment "día del mes para pago"
                                           :table-name "alquileres" :required false :value (get row :dia_pago)}))
   (build-field (smart-build-field-config {:field-name "incluye_mantenimiento" :db-type "TEXT" :comment "'no'"
                                           :table-name "alquileres" :required false :value (get row :incluye_mantenimiento)}))
   (build-field (smart-build-field-config {:field-name "incluye_servicios" :db-type "TEXT" :comment "qué servicios incluye"
                                           :table-name "alquileres" :required false :value (get row :incluye_servicios)}))
   (build-field (smart-build-field-config {:field-name "permite_mascotas" :db-type "TEXT" :comment "'no'"
                                           :table-name "alquileres" :required false :value (get row :permite_mascotas)}))
   (build-field (smart-build-field-config {:field-name "numero_ocupantes" :db-type "INTEGER" :comment nil
                                           :table-name "alquileres" :required false :value (get row :numero_ocupantes)}))
   (build-field (smart-build-field-config {:field-name "uso_permitido" :db-type "TEXT" :comment "habitacional, comercial, oficina"
                                           :table-name "alquileres" :required false :value (get row :uso_permitido)}))
   (build-field (smart-build-field-config {:field-name "clausulas_especiales" :db-type "TEXT" :comment nil
                                           :table-name "alquileres" :required false :value (get row :clausulas_especiales)}))
   (build-field (smart-build-field-config {:field-name "notario_publico" :db-type "TEXT" :comment "nombre del notario que ratificó"
                                           :table-name "alquileres" :required false :value (get row :notario_publico)}))
   (build-field (smart-build-field-config {:field-name "numero_notaria" :db-type "TEXT" :comment nil
                                           :table-name "alquileres" :required false :value (get row :numero_notaria)}))
   (build-field (smart-build-field-config {:field-name "fecha_ratificacion" :db-type "DATE" :comment nil
                                           :table-name "alquileres" :required false :value (get row :fecha_ratificacion)}))
   (build-field (smart-build-field-config {:field-name "estado_alquiler" :db-type "TEXT" :comment "activo, cancelado, vencido"
                                           :table-name "alquileres" :required false :value (get row :estado_alquiler)}))))

(defn build-alquileres-form
  [title row]
  (form "/admin/alquileresagentes/save" (build-alquileres-fields row) (build-modal-buttons) title {:bare true}))
