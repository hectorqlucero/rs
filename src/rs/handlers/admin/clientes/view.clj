(ns rs.handlers.admin.clientes.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-subgrids]]
            [rs.models.util :refer [smart-build-field-config]]))

;; clientes-view: If you want to show subgrids, pass a :subgrids vector in args.
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

(defn clientes-view
  [title rows & [args]]
  (let [labels ["Nombre" "Apellido Paterno" "Teléfono" "Email" "RFC"]
        db-fields [:nombre :apellido_paterno :telefono :email :rfc]
        fields (apply array-map (interleave db-fields labels))
        table-id "clientes_table"
        href "/admin/clientes"
        args (or args {:new true :edit true :delete true})]
    (if (and (map? args) (contains? args :subgrids))
      (build-grid-with-subgrids title rows table-id fields href args)
      (build-grid title rows table-id fields href args))))

(defn build-clientes-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})

   ;; === INFORMACIÓN BÁSICA ===
   (build-field (smart-build-field-config {:field-name "nombre" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required true :value (get row :nombre)}))

   (build-field (smart-build-field-config {:field-name "apellido_paterno" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required true :value (get row :apellido_paterno)}))

   (build-field (smart-build-field-config {:field-name "apellido_materno" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :apellido_materno)}))

   ;; === IDENTIFICACIÓN MEXICANA ===
   (build-field (smart-build-field-config {:field-name "rfc" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :rfc)}))

   (build-field (smart-build-field-config {:field-name "curp" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :curp)}))

   (build-field (smart-build-field-config {:field-name "fecha_nacimiento" :db-type "DATE" :comment nil
                                           :table-name "clientes" :required false :value (get row :fecha_nacimiento)}))

   (build-field (smart-build-field-config {:field-name "estado_civil" :db-type "TEXT"
                                           :comment "soltero, casado, divorciado, viudo, union_libre"
                                           :table-name "clientes" :required false :value (get row :estado_civil)}))

   (build-field (smart-build-field-config {:field-name "identificacion_oficial" :db-type "TEXT"
                                           :comment "INE, pasaporte, cedula"
                                           :table-name "clientes" :required false :value (get row :identificacion_oficial)}))

   (build-field (smart-build-field-config {:field-name "numero_identificacion" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :numero_identificacion)}))

   ;; === CONTACTO ===
   (build-field (smart-build-field-config {:field-name "telefono" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :telefono)}))

   (build-field (smart-build-field-config {:field-name "email" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :email)}))

   ;; === DIRECCIÓN MEXICANA ===
   (build-field (smart-build-field-config {:field-name "calle" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :calle)}))

   (build-field (smart-build-field-config {:field-name "numero_exterior" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :numero_exterior)}))

   (build-field (smart-build-field-config {:field-name "numero_interior" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :numero_interior)}))

   (build-field (smart-build-field-config {:field-name "colonia" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :colonia)}))

   (build-field (smart-build-field-config {:field-name "municipio" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :municipio)}))

   (build-field (smart-build-field-config {:field-name "estado" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :estado)}))

   (build-field (smart-build-field-config {:field-name "codigo_postal" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :codigo_postal)}))

   ;; === INFORMACIÓN FINANCIERA ===
   (build-field (smart-build-field-config {:field-name "ocupacion" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :ocupacion)}))

   (build-field (smart-build-field-config {:field-name "empresa_trabajo" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :empresa_trabajo)}))

   (build-field (smart-build-field-config {:field-name "telefono_trabajo" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :telefono_trabajo)}))

   (build-field (smart-build-field-config {:field-name "ingresos_mensuales" :db-type "REAL" :comment nil
                                           :table-name "clientes" :required false :value (get row :ingresos_mensuales)}))

   (build-field (smart-build-field-config {:field-name "scoring_crediticio" :db-type "INTEGER" :comment nil
                                           :table-name "clientes" :required false :value (get row :scoring_crediticio)}))

   ;; === INFONAVIT ===
   (build-field (smart-build-field-config {:field-name "tiene_credito_infonavit" :db-type "TEXT"
                                           :comment "si, no, usado"
                                           :table-name "clientes" :required false :value (get row :tiene_credito_infonavit)}))

   (build-field (smart-build-field-config {:field-name "numero_infonavit" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :numero_infonavit)}))

   ;; === OBSERVACIONES ===
   (build-field (smart-build-field-config {:field-name "observaciones" :db-type "TEXT" :comment nil
                                           :table-name "clientes" :required false :value (get row :observaciones)}))))

(defn clientes-form-view
  [title row]
  (form "/admin/clientes/save" (build-clientes-fields row) (build-modal-buttons) title {:bare true}))
