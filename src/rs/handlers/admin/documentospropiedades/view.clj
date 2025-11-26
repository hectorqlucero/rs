(ns rs.handlers.admin.documentospropiedades.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new build-grid]]))

(defn documentos-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "documentos_table" (apply array-map (interleave [:tipo_documento :nombre_documento :estado_documento] ["Tipo" "Nombre" "Estado"])) "/admin/documentos" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Tipo" "Nombre" "Estado"]
         db-fields [:tipo_documento :nombre_documento :estado_documento]
         fields (apply array-map (interleave db-fields labels))
         table-id "documentos_table"
         href "/admin/documentospropiedades"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-documentos-fields
  [row]
  (list
    (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
    (build-field {:id "id_propiedad" :type "hidden" :name "id_propiedad" :value (:id_propiedad row)})
    (build-field {:label "Tipo" :type "text" :id "tipo_documento" :name "tipo_documento" :placeholder "Tipo here..." :required false :value (get row :tipo_documento)})
(build-field {:label "Nombre" :type "text" :id "nombre_documento" :name "nombre_documento" :placeholder "Nombre here..." :required false :value (get row :nombre_documento)})
(build-field {:label "Estado" :type "text" :id "estado_documento" :name "estado_documento" :placeholder "Estado here..." :required false :value (get row :estado_documento)})

  ))

(defn build-documentos-form
  [title row]
  (form "/admin/documentospropiedades/save" (build-documentos-fields row) (build-modal-buttons) title {:bare true}))
