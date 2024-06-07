(ns sk.handlers.admin.fraccionamientos.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.handlers.admin.fraccionamientos.model :refer [constructoras_id-options
                                                              tipo_creditos_id-options
                                                              zonas_id-options]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn fraccionamientos-view
  [title rows]
  (let [labels ["NOMBRE" "ESTADO" "CIUDAD" "CONSTRUCTORA" "ZONA" "TIPO DE CREDITO"]
        db-fields [:nombre :estado :ciudad :constructoras_id_formatted :zonas_id_formatted :tipo_creditos_id_formatted]
        fields (zipmap db-fields labels)
        table-id "fraccionamientos_table"
        href "/admin/fraccionamientos"]
    (build-grid title rows table-id fields href)))

(defn build-fraccionamientos-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-field {:label "NOMBRE"
                 :type "text"
                 :id "nombre"
                 :name "nombre"
                 :placeholder "nombre aqui..."
                 :required false
                 :error " "
                 :value (:nombre row)})
   (build-field {:label "ESTADO"
                 :type "text"
                 :id "estado"
                 :name "estado"
                 :placeholder "Abreviacion de estado. Ej: BC, BCS. Maximo 5 caracteres..."
                 :required false
                 :error " "
                 :value (:estado row)})
   (build-field {:label "CIUDAD"
                 :type "text"
                 :id "ciudad"
                 :name "ciudad"
                 :placeholder "ciudad aqui..."
                 :required false
                 :error " "
                 :value (:ciudad row)})
   (build-select {:label "CONSTRUCTORAS"
                  :id "constructoras_id"
                  :name "constructoras_id"
                  :value (:constructoras_id row)
                  :options (constructoras_id-options)})
   (build-select {:label "ZONAS"
                  :id "zonas_id"
                  :name "zonas_id"
                  :value (:zonas_id row)
                  :options (zonas_id-options)})
   (build-select {:label "TIPO De CREDITO"
                  :id "tipo_creditos_id"
                  :name "tipo_creditos_id"
                  :value (:tipo_creditos_id row)
                  :options (tipo_creditos_id-options)})))

(defn build-fraccionamientos-form
  [title row]
  (let [fields (build-fraccionamientos-fields row)
        href "/admin/fraccionamientos/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-fraccionamientos-modal
  [title row]
  (build-modal title row (build-fraccionamientos-form title row)))

(defn fraccionamientos-edit-view
  [title row rows]
  (list
   (fraccionamientos-view "fraccionamientos Manteniento" rows)
   (build-fraccionamientos-modal title row)))

(defn fraccionamientos-add-view
  [title row rows]
  (list
   (fraccionamientos-view "fraccionamientos Mantenimiento" rows)
   (build-fraccionamientos-modal title row)))

(defn fraccionamientos-modal-script
  []
  (modal-script))
