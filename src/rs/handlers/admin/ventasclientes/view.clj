(ns rs.handlers.admin.ventasclientes.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new build-grid]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn ventas-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "ventas_table" (apply array-map (interleave [:descripcion :fecha] ["Descripcion" "Fecha"])) "/admin/ventas" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Descripcion" "Fecha" "Estado"]
         db-fields [:descripcion :fecha :estado]
         fields (apply array-map (interleave db-fields labels))
         table-id "ventas_table"
         href "/admin/ventasclientes"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-ventas-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
   (build-field {:id "id_cliente" :type "hidden" :name "id_cliente" :value (:id_cliente row)})

   ;; Smart fields based on database schema
   (build-field (smart-build-field-config {:field-name "descripcion" :db-type "TEXT" :comment nil
                                           :table-name "ventas" :required true :value (get row :descripcion)}))

   (build-field (smart-build-field-config {:field-name "fecha_inicio" :db-type "DATE" :comment nil
                                           :table-name "ventas" :required false :value (get row :fecha_inicio)}))

   (build-field (smart-build-field-config {:field-name "estado" :db-type "TEXT"
                                           :comment "activo, inactivo"
                                           :table-name "ventas" :required false :value (get row :estado)}))))

(defn build-ventas-form
  [title row]
  (form "/admin/ventasclientes/save" (build-ventas-fields row) (build-modal-buttons) title {:bare true}))
