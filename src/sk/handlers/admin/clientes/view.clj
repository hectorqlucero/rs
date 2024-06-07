(ns sk.handlers.admin.clientes.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.handlers.admin.clientes.model :refer [tipo_creditos_id-options]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn clientes-view
  [title rows]
  (let [labels ["NOMBRE" "CELULAR" "EMAIL" "INGRESOS" "PC" "TIPO CREDITO"]
        db-fields [:nombre_formatted :celular :email :ingresos_formatted :pc_formatted :tipo_creditos_id_formatted]
        fields (zipmap db-fields labels)
        table-id "clientes_table"
        href "/admin/clientes"]
    (build-grid title rows table-id fields href)))

(defn build-clientes-fields
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
   (build-field {:label "PATERNO"
                 :type "text"
                 :id "paterno"
                 :name "paterno"
                 :placeholder "paterno aqui..."
                 :required false
                 :error " "
                 :value (:paterno row)})
   (build-field {:label "MATERNO"
                 :type "text"
                 :id "materno"
                 :name "materno"
                 :placeholder "materno aqui..."
                 :required false
                 :error " "
                 :value (:materno row)})
   (build-field {:label "TELEFONO"
                 :type "text"
                 :id "telefono"
                 :name "telefono"
                 :placeholder "telefono aqui..."
                 :required false
                 :error " "
                 :value (:telefono row)})
   (build-field {:label "CELULAR"
                 :type "text"
                 :id "celular"
                 :name "celular"
                 :placeholder "celular aqui..."
                 :required false
                 :error " "
                 :value (:celular row)})
   (build-field {:label "EMAIL"
                 :type "text"
                 :id "email"
                 :name "email"
                 :placeholder "email aqui..."
                 :required false
                 :error " "
                 :value (:email row)})
   (build-field {:label "INGRESOS"
                 :type "text"
                 :id "ingresos"
                 :name "ingresos"
                 :placeholder "ingresos aqui..."
                 :required false
                 :error " "
                 :value (:ingresos row)})
   (build-field {:label "PC"
                 :type "text"
                 :id "pc"
                 :name "pc"
                 :placeholder "pc aqui..."
                 :required false
                 :error " "
                 :value (:pc row)})
   (build-select {:label "Tipo de Credito"
                  :id "tipo_creditos_id"
                  :name "tipo_creditos_id"
                  :value (:tipo_creditos_id row)
                  :options (tipo_creditos_id-options)})
   (build-radio {:label "TIPO"
                 :name "tipo"
                 :value (:tipo row)
                 :options [{:id "tipoV"
                            :label "Venta"
                            :value "V"}
                           {:id "tipoR"
                            :label "Renta"
                            :value "R"}]})))

(defn build-clientes-form
  [title row]
  (let [fields (build-clientes-fields row)
        href "/admin/clientes/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-clientes-modal
  [title row]
  (build-modal title row (build-clientes-form title row)))

(defn clientes-edit-view
  [title row rows]
  (list
   (clientes-view "clientes Manteniento" rows)
   (build-clientes-modal title row)))

(defn clientes-add-view
  [title row rows]
  (list
   (clientes-view "clientes Mantenimiento" rows)
   (build-clientes-modal title row)))

(defn clientes-modal-script
  []
  (modal-script))
