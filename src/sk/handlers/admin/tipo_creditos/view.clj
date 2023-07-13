(ns sk.handlers.admin.tipo_creditos.view
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
     :prompt "El tipo de credito ej. Infonavit..."
     :data-options "label:'Credito:',
        labelPosition:'top',
        required:true,
        width:'100%'"})))

(defn special-toolbar []
  [:div#toolbar
   [:a {:href         "javascript:void(0)"
        :class        "easyui-linkbutton"
        :data-options "iconCls: 'icon-add',plain: true"
        :onclick      "newItem()"} "Nuevo"]
   [:a {:href         "javascript:void(0)"
        :class        "easyui-linkbutton"
        :data-options "iconCls: 'icon-edit',plain: true"
        :onclick      "editItem({})"} "Editar"]])

(defn tipo_creditos-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/tipo_creditos"
    (list
     [:th {:data-options "field:'id',sortable:true,width:100"} "ID"]
     [:th {:data-options "field:'nombre',sortable:true,width:100"} "NOMBRE"]))
   (special-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn tipo_creditos-scripts []
  (include-js "/js/grid.js"))
