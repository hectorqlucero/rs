(ns sk.handlers.admin.casas.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.handlers.admin.casas.model :refer [fraccionamientos_id-options]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn casas-view
  [title rows]
  (let [labels ["FRACCIONAMIENTO" "MODELO" "TIPO" "RECAMARAS" "BAÑOS" "PLANTAS" "COSTO"]
        db-fields [:fraccionamientos_id_formatted :modelo :tipo :recamaras :baños :plantas :costo_formatted]
        fields (zipmap db-fields labels)
        table-id "casas_table"
        href "/admin/casas"]
    (build-grid title rows table-id fields href)))

(defn build-casas-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-select {:label "FRACCIONAMIENTO"
                  :id "fraccionamientos_id"
                  :name "fraccionamientos_id"
                  :value (:fraccionamientos_id row)
                  :options (fraccionamientos_id-options)})
   (build-radio {:label "TIPO"
                 :name "tipo"
                 :value (:tipo row)
                 :options [{:id "tipoV"
                            :label "Venta"
                            :value "V"}
                           {:id "tipoR"
                            :label "Renta"
                            :value "R"}]})
   (build-field {:label "MODELO"
                 :type "text"
                 :id "modelo"
                 :name "modelo"
                 :placeholder "modelo aqui..."
                 :required false
                 :error " "
                 :value (:modelo row)})
   (build-field {:label "RECAMARAS"
                 :type "number"
                 :id "recamaras"
                 :name "recamaras"
                 :min "1"
                 :max "9"
                 :step "1"
                 :placeholder "recamaras aqui..."
                 :required false
                 :error " "
                 :value (:recamaras row)})
   (build-field {:label "BAÑOS"
                 :type "number"
                 :id "baños"
                 :name "baños"
                 :min "1"
                 :max "6"
                 :step ".5"
                 :placeholder "baños aqui..."
                 :required false
                 :error " "
                 :value (:baños row)})
   (build-field {:label "PLANTAS"
                 :type "number"
                 :id "plantas"
                 :name "plantas"
                 :min "1"
                 :max "20"
                 :step "1"
                 :placeholder "plantas aqui..."
                 :required false
                 :error " "
                 :value (:plantas row)})
   (build-field {:label "COSTO"
                 :type "text"
                 :id "costo"
                 :name "costo"
                 :placeholder "costo aqui..."
                 :required false
                 :error " "
                 :value (:costo row)})
   (build-field {:label "MTC"
                 :type "text"
                 :id "mtc"
                 :name "mtc"
                 :placeholder "mtc aqui..."
                 :required false
                 :error " "
                 :value (:mtc row)})
   (build-field {:label "MTT"
                 :type "text"
                 :id "mtt"
                 :name "mtt"
                 :placeholder "mtt aqui..."
                 :required false
                 :error " "
                 :value (:mtt row)})
   (build-textarea {:label "COMENTARIOS"
                    :id "comentarios"
                    :name "comentarios"
                    :rows "3"
                    :placeholder "comentarios aqui..."
                    :required false
                    :error " "
                    :value (:comentarios row)})))

(defn build-casas-form
  [title row]
  (let [fields (build-casas-fields row)
        href "/admin/casas/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-casas-modal
  [title row]
  (build-modal title row (build-casas-form title row)))

(defn casas-edit-view
  [title row rows]
  (list
   (casas-view "casas Mantenimiento" rows)
   (build-casas-modal title row)))

(defn casas-add-view
  [title row rows]
  (list
   (casas-view "casas Mantenimiento" rows)
   (build-casas-modal title row)))

(defn casas-modal-script
  []
  (modal-script))
