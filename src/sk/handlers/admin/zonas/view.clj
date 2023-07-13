(ns sk.handlers.admin.zonas.view
  (:require
   [hiccup.page :refer [include-js]]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [sk.models.util :refer
    [build-dialog build-dialog-buttons build-field build-table build-toolbar]]))

(defn dialog-fields []
  (list
   (build-field
    {:id "id"
     :name "id"
     :type "hidden"})
   (build-field
    {:id "nombre"
     :name "nombre"
     :class "easyui-textbox"
     :prompt "Nombre de la zona ej. Zona Norte"
     :data-options "label:'Zona:',
        labelPosition:'top',
        required:true,
        width:'100%'"})))

(defn zonas-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/zonas"
    (list
     [:th {:data-options "field:'id',sortable:true,width:100"} "ID"]
     [:th {:data-options "field:'nombre',sortable:true,width:100"} "NOMBRE"]))
   (build-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn zonas-scripts []
  (include-js "/js/grid.js"))