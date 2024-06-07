(ns sk.handlers.admin.tipo_creditos.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn tipo_creditos-view
  [title rows]
  (let [labels ["NOMBRE"]
        db-fields [:nombre]
        fields (zipmap db-fields labels)
        table-id "tipo_creditos_table"
        args {:new true :delete false :edit true}
        href "/admin/tipo_creditos"]
    (build-grid title rows table-id fields href args)))

(defn build-tipo_creditos-fields
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
                 :value (:nombre row)})))

(defn build-tipo_creditos-form
  [title row]
  (let [fields (build-tipo_creditos-fields row)
        href "/admin/tipo_creditos/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-tipo_creditos-modal
  [title row]
  (build-modal title row (build-tipo_creditos-form title row)))

(defn tipo_creditos-edit-view
  [title row rows]
  (list
   (tipo_creditos-view "tipo de creditos Manteniento" rows)
   (build-tipo_creditos-modal title row)))

(defn tipo_creditos-add-view
  [title row rows]
  (list
   (tipo_creditos-view "tipo de creditos Mantenimiento" rows)
   (build-tipo_creditos-modal title row)))

(defn tipo_creditos-modal-script
  []
  (modal-script))
