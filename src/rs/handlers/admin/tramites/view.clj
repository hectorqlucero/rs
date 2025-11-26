(ns rs.handlers.admin.tramites.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-subgrids]]))

;; tramites-view: If you want to show subgrids, pass a :subgrids vector in args.
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

(defn tramites-view
  [title rows & [args]]
  (let [labels ["Tipo" "Descripción" "Venta" "Alquiler" "Estado" "Dependencia"]
        db-fields [:tipo_tramite :descripcion :id_venta :id_alquiler :estado_tramite :dependencia]
        fields (apply array-map (interleave db-fields labels))
        table-id "tramites_table"
        href "/admin/tramites"
        args (or args {:new true :edit true :delete true})]
    (if (and (map? args) (contains? args :subgrids))
      (build-grid-with-subgrids title rows table-id fields href args)
      (build-grid title rows table-id fields href args))))

(defn build-tramites-fields
  [row]
  (list
    (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
    (build-field {:label "Tipo" :type "text" :id "tipo_tramite" :name "tipo_tramite" :placeholder "Tipo here..." :required false :value (get row :tipo_tramite)})
(build-field {:label "Descripción" :type "text" :id "descripcion" :name "descripcion" :placeholder "Descripción here..." :required false :value (get row :descripcion)})
(build-field {:label "Venta" :type "text" :id "id_venta" :name "id_venta" :placeholder "Venta here..." :required false :value (get row :id_venta)})
(build-field {:label "Alquiler" :type "text" :id "id_alquiler" :name "id_alquiler" :placeholder "Alquiler here..." :required false :value (get row :id_alquiler)})
(build-field {:label "Estado" :type "text" :id "estado_tramite" :name "estado_tramite" :placeholder "Estado here..." :required false :value (get row :estado_tramite)})
(build-field {:label "Dependencia" :type "text" :id "dependencia" :name "dependencia" :placeholder "Dependencia here..." :required false :value (get row :dependencia)})

  ))

(defn tramites-form-view
  [title row]
  (form "/admin/tramites/save" (build-tramites-fields row) (build-modal-buttons) title {:bare true}))
