(ns rs.handlers.admin.propiedades.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-subgrids]]
            [rs.models.util :refer [smart-build-field-config]]))

;; propiedades-view: If you want to show subgrids, pass a :subgrids vector in args.
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

(defn propiedades-view
  [title rows & [args]]
  (let [labels ["Título" "Tipo" "Modalidad" "Precio" "Municipio" "Estado Propiedad"]
        db-fields [:titulo :tipo :modalidad :precio :municipio :estado_propiedad]
        fields (apply array-map (interleave db-fields labels))
        table-id "propiedades_table"
        href "/admin/propiedades"
        args (or args {:new true :edit true :delete true
                       :subgrids [{:title "Documentos"
                                   :table-name "documentos"
                                   :foreign-key "id_referencia"
                                   :href "/admin/documentospropiedades"
                                   :icon "bi bi-file-earmark"
                                   :label "Documentos"}
                                  {:title "Avaluos"
                                   :table-name "avaluos"
                                   :foreign-key "id_propiedad"
                                   :href "/admin/avaluospropiedades"
                                   :icon "bi bi-building"
                                   :label "Avaluos"}]})]
    (if (and (map? args) (contains? args :subgrids))
      (build-grid-with-subgrids title rows table-id fields href args)
      (build-grid title rows table-id fields href args))))

(defn build-propiedades-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})

   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "titulo" :db-type "TEXT" :comment nil
                                           :table-name "propiedades" :required true :value (get row :titulo)}))

   (build-field (smart-build-field-config {:field-name "tipo" :db-type "TEXT"
                                           :comment "casa, departamento, terreno, local_comercial, oficina, bodega"
                                           :table-name "propiedades" :required true :value (get row :tipo)}))

   (build-field (smart-build-field-config {:field-name "modalidad" :db-type "TEXT"
                                           :comment "venta, renta, venta_renta"
                                           :table-name "propiedades" :required true :value (get row :modalidad)}))

   (build-field (smart-build-field-config {:field-name "precio" :db-type "REAL" :comment nil
                                           :table-name "propiedades" :required false :value (get row :precio)}))

   (build-field (smart-build-field-config {:field-name "descripcion" :db-type "TEXT" :comment nil
                                           :table-name "propiedades" :required false :value (get row :descripcion)}))

   (build-field (smart-build-field-config {:field-name "superficie_terreno" :db-type "REAL" :comment nil
                                           :table-name "propiedades" :required false :value (get row :superficie_terreno)}))

   (build-field (smart-build-field-config {:field-name "superficie_construccion" :db-type "REAL" :comment nil
                                           :table-name "propiedades" :required false :value (get row :superficie_construccion)}))

   (build-field (smart-build-field-config {:field-name "recamaras" :db-type "INTEGER" :comment nil
                                           :table-name "propiedades" :required false :value (get row :recamaras)}))

   (build-field (smart-build-field-config {:field-name "banos" :db-type "INTEGER" :comment nil
                                           :table-name "propiedades" :required false :value (get row :banos)}))

   (build-field (smart-build-field-config {:field-name "colonia" :db-type "TEXT" :comment nil
                                           :table-name "propiedades" :required false :value (get row :colonia)}))

   (build-field (smart-build-field-config {:field-name "municipio" :db-type "TEXT" :comment nil
                                           :table-name "propiedades" :required false :value (get row :municipio)}))

   (build-field (smart-build-field-config {:field-name "estado" :db-type "TEXT" :comment nil
                                           :table-name "propiedades" :required false :value (get row :estado)}))

   (build-field (smart-build-field-config {:field-name "codigo_postal" :db-type "TEXT" :comment nil
                                           :table-name "propiedades" :required false :value (get row :codigo_postal)}))

   (build-field (smart-build-field-config {:field-name "uso_suelo" :db-type "TEXT"
                                           :comment "habitacional, comercial, mixto"
                                           :table-name "propiedades" :required false :value (get row :uso_suelo)}))

   (build-field (smart-build-field-config {:field-name "regimen_propiedad" :db-type "TEXT"
                                           :comment "propiedad privada, ejidal, comunal"
                                           :table-name "propiedades" :required false :value (get row :regimen_propiedad)}))

   (build-field (smart-build-field-config {:field-name "estado_propiedad" :db-type "TEXT"
                                           :comment "disponible, rentado, vendido, retirado"
                                           :table-name "propiedades" :required false :value (get row :estado_propiedad)}))

   ;; Foreign key fields - automatically populated with options
   (build-field (smart-build-field-config {:field-name "id_propietario" :db-type "INTEGER" :comment nil
                                           :table-name "propiedades" :required false :value (get row :id_propietario)}))

   (build-field (smart-build-field-config {:field-name "id_agente" :db-type "INTEGER" :comment nil
                                           :table-name "propiedades" :required false :value (get row :id_agente)}))))

(defn propiedades-form-view
  [title row]
  (form "/admin/propiedades/save" (build-propiedades-fields row) (build-modal-buttons) title {:bare true}))
