(ns sk.handlers.admin.fraccionamientos.view
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
     :prompt "Nombre del fraccionamiento..."
     :data-options "label:'Nombre:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "estado"
     :name "estado"
     :class "easyui-textbox"
     :prompt "Estado donde se encuentra el fraccionamiento..."
     :data-options "label:'Estado:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "ciudad"
     :name "ciudad"
     :class "easyui-textbox"
     :prompt "Ciudad donde esta el fraccionamiento..."
     :data-options "label:'Ciudad:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "constructoras_id"
     :name "constructoras_id"
     :class "easyui-combobox"
     :data-options "label:'Constructora:',
        labelPosition:'top',
        url:'/table_ref/get_constructoras',
        method:'GET',
        required:true,
        width:'100%'"})
   (build-field
    {:id "zonas_id"
     :name "zonas_id"
     :class "easyui-combobox"
     :data-options "label:'Zona:',
        labelPosition:'top',
        url:'/table_ref/get_zonas',
        method:'GET',
        required:true,
        width:'100%'"})
   (build-field
    {:id "tipo_creditos_id"
     :name "tipo_creditos_id"
     :class "easyui-combobox"
     :data-options "label:'Tipo Credito:',
        labelPosition:'top',
        url:'/table_ref/get_tipo_creditos',
        method:'GET',
        required:true,
        width:'100%'"})))

(defn fraccionamientos-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/fraccionamientos"
    (list
     [:th {:data-options "field:'id',sortable:true,width:100"} "ID"]
     [:th {:data-options "field:'nombre',sortable:true,width:100"} "NOMBRE"]
     [:th {:data-options "field:'estado',sortable:true,width:100"} "ESTADO"]
     [:th {:data-options "field:'ciudad',sortable:true,width:100"} "CIUDAD"]
     [:th {:data-options "field:'constructoras_id',sortable:true,width:100"
           :formatter "get_constructora"} "CONSTRUCTORAS_ID"]
     [:th {:data-options "field:'zonas_id',sortable:true,width:100"
           :formatter "get_zona"} "ZONAS_ID"]
     [:th {:data-options "field:'tipo_creditos_id',sortable:true,width:100"
           :formatter "get_tipo_credito"} "TIPO_CREDITOS_ID"]))
   (build-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn fraccionamientos-scripts []
  (list
   (include-js "/js/grid.js")
   [:script
    "
   function get_constructora(val,row,index) {
    var result = null;
    var scriptUrl = '/table_ref/get-item/constructoras/razon_social/id/' + val;
    $.ajax({
      url:scriptUrl,
      type:'get',
      dataType:'html',
      async:false,
      success:function(data) {
        result = data;
      }
    });
    return result
   }
     
   function get_zona(val,row,index) {
    var result = null;
    var scriptUrl = '/table_ref/get-item/zonas/nombre/id/' + val;
    $.ajax({
      url:scriptUrl,
      type:'get',
      dataType:'html',
      async:false,
      success:function(data) {
        result = data;
      }
    });
    return result
   }
   
   function get_tipo_credito(val,row,index) {
    var result = null;
    var scriptUrl = '/table_ref/get-item/tipo_creditos/nombre/id/' + val;
    $.ajax({
      url:scriptUrl,
      type:'get',
      dataType:'html',
      async:false,
      success:function(data) {
        result = data;
      }
    });
    return result
   }
   "]))