(ns rs.handlers.admin.agentes.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-subgrids]]
            [rs.models.util :refer [smart-build-field-config]]))

;; agentes-view: If you want to show subgrids, pass a :subgrids vector in args.
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

(defn agentes-view
  [title rows & [args]]
  (let [labels ["Nombre" "Apellido Paterno" "Email" "Teléfono" "Comisión %" "Activo"]
        db-fields [:nombre :apellido_paterno :email :telefono :comision_porcentaje :activo]
        fields (apply array-map (interleave db-fields labels))
        table-id "agentes_table"
        href "/admin/agentes"
        args (or args {:new true :edit true :delete true
                       :subgrids [{:title "Propiedades"
                                   :table-name "propiedades"
                                   :foreign-key "id_agente"
                                   :href "/admin/propiedadesagentes"
                                   :icon "bi bi-building"
                                   :label "Propiedades"}
                                  {:title "Ventas"
                                   :table-name "ventas"
                                   :foreign-key "id_agente"
                                   :href "/admin/ventasagentes"
                                   :icon "bi bi-currency-dollar"
                                   :label "Ventas"}
                                  {:title "Alquileres"
                                   :table-name "alquileres"
                                   :foreign-key "id_agente"
                                   :href "/admin/alquileresagentes"
                                   :icon "bi bi-house"
                                   :label "Alquileres"}]})]
    (if (and (map? args) (contains? args :subgrids))
      (build-grid-with-subgrids title rows table-id fields href args)
      (build-grid title rows table-id fields href args))))

(defn build-agentes-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})

   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "nombre" :db-type "TEXT" :comment nil
                                           :table-name "agentes" :required true :value (get row :nombre)}))

   (build-field (smart-build-field-config {:field-name "apellido_paterno" :db-type "TEXT" :comment nil
                                           :table-name "agentes" :required true :value (get row :apellido_paterno)}))

   (build-field (smart-build-field-config {:field-name "apellido_materno" :db-type "TEXT" :comment nil
                                           :table-name "agentes" :required false :value (get row :apellido_materno)}))

   (build-field (smart-build-field-config {:field-name "email" :db-type "TEXT" :comment nil
                                           :table-name "agentes" :required false :value (get row :email)}))

   (build-field (smart-build-field-config {:field-name "telefono" :db-type "TEXT" :comment nil
                                           :table-name "agentes" :required false :value (get row :telefono)}))

   (build-field (smart-build-field-config {:field-name "comision_porcentaje" :db-type "REAL" :comment nil
                                           :table-name "agentes" :required false :value (get row :comision_porcentaje)}))

   (build-field (smart-build-field-config {:field-name "fecha_inicio" :db-type "DATE" :comment nil
                                           :table-name "agentes" :required false :value (get row :fecha_inicio)}))

   (build-field (smart-build-field-config {:field-name "activo" :db-type "TEXT"
                                           :comment "si, no"
                                           :table-name "agentes" :required false :value (get row :activo)}))))

(defn agentes-form-view
  [title row]
  (form "/admin/agentes/save" (build-agentes-fields row) (build-modal-buttons) title {:bare true}))
