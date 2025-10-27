(ns rs.handlers.admin.tramites.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn tramites-view
  [title rows & [args]]
  (let [labels ["Tipo" "Descripción" "Dependencia" "Fecha Inicio" "Estado" "Responsable" "Agente"]
        db-fields [:tipo_tramite :descripcion :dependencia :fecha_inicio :estado_tramite :responsable :agente_registro]
        fields (apply array-map (interleave db-fields labels))
        table-id "tramites_table"
        href "/admin/tramites"
        args (or args {:new true :edit true :delete true})]
    (build-grid title rows table-id fields href args)))

(defn build-tramites-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})

   ;; Smart fields based on database schema and migration comments
   (build-field (smart-build-field-config {:field-name "tabla_referencia" :db-type "TEXT" :comment nil
                                           :table-name "tramites" :required true :value (get row :tabla_referencia)}))

   (build-field (smart-build-field-config {:field-name "id_referencia" :db-type "INTEGER" :comment nil
                                           :table-name "tramites" :required true :value (get row :id_referencia)}))

   (build-field (smart-build-field-config {:field-name "tipo_tramite" :db-type "TEXT" :comment nil
                                           :table-name "tramites" :required true :value (get row :tipo_tramite)}))

   (build-field (smart-build-field-config {:field-name "descripcion" :db-type "TEXT" :comment nil
                                           :table-name "tramites" :required false :value (get row :descripcion)}))

   (build-field (smart-build-field-config {:field-name "dependencia" :db-type "TEXT" :comment nil
                                           :table-name "tramites" :required false :value (get row :dependencia)}))

   (build-field (smart-build-field-config {:field-name "numero_expediente" :db-type "TEXT" :comment nil
                                           :table-name "tramites" :required false :value (get row :numero_expediente)}))

   (build-field (smart-build-field-config {:field-name "fecha_inicio" :db-type "DATE" :comment nil
                                           :table-name "tramites" :required false :value (get row :fecha_inicio)}))

   (build-field (smart-build-field-config {:field-name "fecha_estimada_fin" :db-type "DATE" :comment nil
                                           :table-name "tramites" :required false :value (get row :fecha_estimada_fin)}))

   (build-field (smart-build-field-config {:field-name "fecha_real_fin" :db-type "DATE" :comment nil
                                           :table-name "tramites" :required false :value (get row :fecha_real_fin)}))

   (build-field (smart-build-field-config {:field-name "estado_tramite" :db-type "TEXT"
                                           :comment "en_proceso, completado, cancelado, pausado"
                                           :table-name "tramites" :required false :value (get row :estado_tramite)}))

   (build-field (smart-build-field-config {:field-name "costo_tramite" :db-type "REAL" :comment nil
                                           :table-name "tramites" :required false :value (get row :costo_tramite)}))

   (build-field (smart-build-field-config {:field-name "responsable" :db-type "TEXT" :comment nil
                                           :table-name "tramites" :required false :value (get row :responsable)}))

   (build-field (smart-build-field-config {:field-name "observaciones" :db-type "TEXT" :comment nil
                                           :table-name "tramites" :required false :value (get row :observaciones)}))

   ;; Foreign key field - automatically populated with agente options
   (build-field (smart-build-field-config {:field-name "agente_registro" :db-type "INTEGER" :comment nil
                                           :table-name "tramites" :required true :value (get row :agente_registro)}))))

(defn tramites-form-view
  [title row]
  (form "/admin/tramites/save" (build-tramites-fields row) (build-modal-buttons) title {:bare true}))