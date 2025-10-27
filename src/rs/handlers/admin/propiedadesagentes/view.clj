(ns rs.handlers.admin.propiedadesagentes.view
  (:require [rs.models.form :refer [form build-field build-modal-buttons]]
            [rs.models.grid :refer [build-grid build-grid-with-custom-new build-grid]]
            [rs.models.util :refer [smart-build-field-config]]))

(defn propiedades-view
  ([title rows]
   ;; If no parent-id, fallback to normal grid
   (build-grid title rows "propiedades_table" (apply array-map (interleave [:titulo :tipo :precio :estado_propiedad :municipio] ["Título" "Tipo" "Precio" "Estado" "Municipio"])) "/admin/propiedades" {:new true :edit true :delete true}))
  ([title rows parent-id]
   (let [labels ["Título" "Tipo" "Precio" "Estado" "Municipio"]
         db-fields [:titulo :tipo :precio :estado_propiedad :municipio]
         fields (apply array-map (interleave db-fields labels))
         table-id "propiedades_table"
         href "/admin/propiedadesagentes"
         args {:new true :edit true :delete true}
         new-record-href (if parent-id
                           (str href "/add-form/" parent-id)
                           (str href "/add-form"))]
     (if parent-id
       (build-grid-with-custom-new title rows table-id fields href args new-record-href)
       (build-grid title rows table-id fields href args)))))

(defn build-propiedades-fields
  [row]
  (list
   (build-field {:id "id" :type "hidden" :name "id" :value (:id row)})
   (build-field {:id "id_agente" :type "hidden" :name "id_agente" :value (:id_agente row)})

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

   (build-field (smart-build-field-config {:field-name "municipio" :db-type "TEXT" :comment nil
                                           :table-name "propiedades" :required false :value (get row :municipio)}))

   (build-field (smart-build-field-config {:field-name "estado_propiedad" :db-type "TEXT"
                                           :comment "disponible, rentado, vendido, retirado"
                                           :table-name "propiedades" :required false :value (get row :estado_propiedad)}))))

(defn build-propiedades-form
  [title row]
  (form "/admin/propiedadesagentes/save" (build-propiedades-fields row) (build-modal-buttons) title {:bare true}))
