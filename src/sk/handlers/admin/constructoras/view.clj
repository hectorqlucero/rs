(ns sk.handlers.admin.constructoras.view
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
    {:id "razon_social"
     :name "razon_social"
     :class "easyui-textbox"
     :prompt "Razon Social del negocio..."
     :data-options "label:'Razon Social:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "rfc"
     :name "rfc"
     :class "easyui-textbox"
     :prompt "RFC del negocio"
     :data-options "label:'RFC:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "telefono"
     :name "telefono"
     :class "easyui-textbox"
     :prompt "Telefono del negocio..."
     :data-options "label:'Telefono:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "celular"
     :name "celular"
     :class "easyui-textbox"
     :prompt "Celular del negocio..."
     :data-options "label:'Celular:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "fax"
     :name "fax"
     :class "easyui-textbox"
     :prompt "Fax del negocio..."
     :data-options "label:'Fax:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "email"
     :name "email"
     :class "easyui-textbox"
     :prompt "Email del negocio..."
     :data-options "label:'Email:',
        labelPosition:'top',
        required:false,
        width:'100%'"})))

(defn constructoras-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/constructoras"
    (list
     [:th {:data-options "field:'id',sortable:true,width:100"} "ID"]
     [:th {:data-options "field:'razon_social',sortable:true,width:100"} "RAZON_SOCIAL"]
     [:th {:data-options "field:'rfc',sortable:true,width:100"} "RFC"]
     [:th {:data-options "field:'telefono',sortable:true,width:100"} "TELEFONO"]
     [:th {:data-options "field:'celular',sortable:true,width:100"} "CELULAR"]
     [:th {:data-options "field:'fax',sortable:true,width:100"} "FAX"]
     [:th {:data-options "field:'email',sortable:true,width:100"} "EMAIL"]))
   (build-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn constructoras-scripts []
  (include-js "/js/grid.js"))