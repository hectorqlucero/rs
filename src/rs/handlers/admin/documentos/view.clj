(ns rs.handlers.admin.documentos.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-subgrids]]))

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
  (let [labels ["Tipo" "Nombre" "Propiedad" "Cliente" "Venta" "Alquiler" "Estado"]
        db-fields [:tipo_documento :nombre_documento :id_propiedad :id_cliente :id_venta :id_alquiler :estado_documento]
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
    (build-field {:label "Tipo" :type "text" :id "tipo_documento" :name "tipo_documento" :placeholder "Tipo here..." :required false :value (get row :tipo_documento)})
(build-field {:label "Nombre" :type "text" :id "nombre_documento" :name "nombre_documento" :placeholder "Nombre here..." :required false :value (get row :nombre_documento)})
(build-field {:label "Propiedad" :type "text" :id "id_propiedad" :name "id_propiedad" :placeholder "Propiedad here..." :required false :value (get row :id_propiedad)})
(build-field {:label "Cliente" :type "text" :id "id_cliente" :name "id_cliente" :placeholder "Cliente here..." :required false :value (get row :id_cliente)})
(build-field {:label "Venta" :type "text" :id "id_venta" :name "id_venta" :placeholder "Venta here..." :required false :value (get row :id_venta)})
(build-field {:label "Alquiler" :type "text" :id "id_alquiler" :name "id_alquiler" :placeholder "Alquiler here..." :required false :value (get row :id_alquiler)})
(build-field {:label "Estado" :type "text" :id "estado_documento" :name "estado_documento" :placeholder "Estado here..." :required false :value (get row :estado_documento)})

  ))

(defn documentos-form-view
  [title row]
  (form "/admin/documentos/save" (build-documentos-fields row) (build-modal-buttons) title {:bare true}))
