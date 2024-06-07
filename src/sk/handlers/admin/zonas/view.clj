(ns sk.handlers.admin.zonas.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn zonas-view
  [title rows]
  (let [labels ["NOMBRE"]
        db-fields [:nombre]
        fields (zipmap db-fields labels)
        table-id "zonas_table"
        href "/admin/zonas"]
    (build-grid title rows table-id fields href)))

(defn build-zonas-fields
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

(defn build-zonas-form
  [title row]
  (let [fields (build-zonas-fields row)
        href "/admin/zonas/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-zonas-modal
  [title row]
  (build-modal title row (build-zonas-form title row)))

(defn zonas-edit-view
  [title row rows]
  (list
   (zonas-view "zonas Manteniento" rows)
   (build-zonas-modal title row)))

(defn zonas-add-view
  [title row rows]
  (list
   (zonas-view "zonas Mantenimiento" rows)
   (build-zonas-modal title row)))

(defn zonas-modal-script
  []
  (modal-script))
