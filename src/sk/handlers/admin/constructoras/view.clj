(ns sk.handlers.admin.constructoras.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn constructoras-view
  [title rows]
  (let [labels ["RAZON SOCIAL" "RFC" "TELEFONO" "CELULAR" "FAX" "EMAIL"]
        db-fields [:razon_social :rfc :telefono :celular :fax :email]
        fields (zipmap db-fields labels)
        table-id "constructoras_table"
        href "/admin/constructoras"]
    (build-grid title rows table-id fields href)))

(defn build-constructoras-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-field {:label "RAZON SOCIAL"
                 :type "text"
                 :id "razon_social"
                 :name "razon_social"
                 :placeholder "razon_social aqui..."
                 :required false
                 :error " "
                 :value (:razon_social row)})
   (build-field {:label "RFC"
                 :type "text"
                 :id "rfc"
                 :name "rfc"
                 :placeholder "rfc aqui..."
                 :required false
                 :error " "
                 :value (:rfc row)})
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
   (build-field {:label "FAX"
                 :type "text"
                 :id "fax"
                 :name "fax"
                 :placeholder "fax aqui..."
                 :required false
                 :error " "
                 :value (:fax row)})
   (build-field {:label "EMAIL"
                 :type "text"
                 :id "email"
                 :name "email"
                 :placeholder "email aqui..."
                 :required false
                 :error " "
                 :value (:email row)})))

(defn build-constructoras-form
  [title row]
  (let [fields (build-constructoras-fields row)
        href "/admin/constructoras/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-constructoras-modal
  [title row]
  (build-modal title row (build-constructoras-form title row)))

(defn constructoras-edit-view
  [title row rows]
  (list
   (constructoras-view "constructoras Manteniento" rows)
   (build-constructoras-modal title row)))

(defn constructoras-add-view
  [title row rows]
  (list
   (constructoras-view "constructoras Mantenimiento" rows)
   (build-constructoras-modal title row)))

(defn constructoras-modal-script
  []
  (modal-script))
