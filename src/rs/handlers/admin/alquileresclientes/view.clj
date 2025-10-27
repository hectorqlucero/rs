(ns rs.handlers.admin.alquileresclientes.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new build-grid]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn alquileres-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "alquileres_table" (apply array-map (interleave [:propiedad :agente :renta_mensual :fecha_inicio :estado_alquiler] ["Propiedad" "Agente" "Renta Mensual" "Fecha Inicio" "Estado"])) "/admin/alquileres" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Propiedad" "Agente" "Renta Mensual" "Fecha Inicio" "Estado"]
         db-fields [:propiedad :agente :renta_mensual :fecha_inicio :estado_alquiler]
         fields (apply array-map (interleave db-fields labels))
         table-id "alquileres_table"
         href "/admin/alquileresclientes"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-alquileres-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
   (build-field {:id "id_inquilino" :type "hidden" :name "id_inquilino" :value (:id_inquilino row)})

   ;; Foreign key fields - automatically populated with smart options
   (build-field (smart-build-field-config {:field-name "id_propiedad" :db-type "INTEGER" :comment nil
                                           :table-name "alquileres" :required true :value (get row :id_propiedad)}))

   (build-field (smart-build-field-config {:field-name "id_agente" :db-type "INTEGER" :comment nil
                                           :table-name "alquileres" :required false :value (get row :id_agente)}))

   ;; Smart fields based on database schema
   (build-field (smart-build-field-config {:field-name "fecha_inicio" :db-type "DATE" :comment nil
                                           :table-name "alquileres" :required true :value (get row :fecha_inicio)}))

   (build-field (smart-build-field-config {:field-name "fecha_fin" :db-type "DATE" :comment nil
                                           :table-name "alquileres" :required false :value (get row :fecha_fin)}))

   (build-field (smart-build-field-config {:field-name "monto_mensual" :db-type "REAL" :comment nil
                                           :table-name "alquileres" :required true :value (get row :monto_mensual)}))

   (build-field (smart-build-field-config {:field-name "deposito" :db-type "REAL" :comment nil
                                           :table-name "alquileres" :required false :value (get row :deposito)}))

   (build-field (smart-build-field-config {:field-name "estado_alquiler" :db-type "TEXT"
                                           :comment "activo, cancelado, vencido"
                                           :table-name "alquileres" :required false :value (get row :estado_alquiler)}))))

(defn build-alquileres-form
  [title row]
  (form "/admin/alquileresclientes/save" (build-alquileres-fields row) (build-modal-buttons) title {:bare true}))
