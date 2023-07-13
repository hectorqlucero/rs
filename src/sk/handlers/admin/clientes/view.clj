(ns sk.handlers.admin.clientes.view
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
     :prompt "Nombre del cliente..."
     :data-options "label:'Nombre:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "paterno"
     :name "paterno"
     :class "easyui-textbox"
     :prompt "Apellido paterno del cliente..."
     :data-options "label:'Paterno:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "materno"
     :name "materno"
     :class "easyui-textbox"
     :prompt "Apellido materno del cliente..."
     :data-options "label:'Materno:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "telefono"
     :name "telefono"
     :class "easyui-textbox"
     :prompt "Telefono del cliente..."
     :data-options "label:'Telefono:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "celular"
     :name "celular"
     :class "easyui-textbox"
     :prompt "Celular del cliente..."
     :data-options "label:'Celular:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "email"
     :name "email"
     :class "easyui-textbox"
     :prompt "Email del cliente..."
     :data-options "label:'Email:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "ingresos"
     :name "ingresos"
     :class "easyui-numberbox"
     :prompt "Ingresos del cliente..."
     :data-options "label:'Ingresos:',
        labelPosition:'top',
        min:0,
        precision:2,
        required:true,
        width:'100%'"})
   (build-field
    {:id "pc"
     :name "pc"
     :class "easyui-numberbox"
     :prompt "Precalificación del cliente..."
     :data-options "label:'Pre Calificación:',
        labelPosition:'top',
        min:0,
        precision:2,
        required:true,
        width:'100%'"})
   (build-field
    {:id "tipo_creditos_id"
     :name "tipo_creditos_id"
     :class "easyui-combobox"
     :prompt "Tipo de credito del cliente..."
     :data-options "label:'Tipo Credito:',
        labelPosition:'top',
        url:'/table_ref/get_tipo_creditos',
        method:'GET',
        required:true,
        width:'100%'"})
   (build-field
    {:id "tipo"
     :name "tipo"
     :class "easyui-combobox"
     :data-options "label:'Renta/Venta',
        labelPosition:'top',
        url:'/table_ref/get_tipos',
        method:'GET',
        required:true,
        width:'100%'"})))

(defn clientes-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/clientes"
    (list
     [:th {:data-options "field:'nombre',sortable:true,width:100"} "NOMBRE"]
     [:th {:data-options "field:'paterno',sortable:true,width:100"} "PATERNO"]
     [:th {:data-options "field:'materno',sortable:true,width:100"} "MATERNO"]
     [:th {:data-options "field:'telefono',sortable:true,width:100"} "TELEFONO"]
     [:th {:data-options "field:'celular',sortable:true,width:100"} "CELULAR"]
     [:th {:data-options "field:'email',sortable:true,width:100"} "EMAIL"]
     [:th {:data-options "field:'tipo',sortable:true,width:100"} "TIPO"]
     [:th {:data-options "field:'ingresos_formatted',sortable:true,width:100"} "INGRESOS"]
     [:th {:data-options "field:'pc_formatted',sortable:true,width:100"} "PC"]
     [:th {:data-options "field:'tipo_creditos_id',sortable:true,width:100"
           :formatter "get_tipo_credito"} "TIPO CREDITO"]))
   (build-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn clientes-scripts []
  (list
   (include-js "/js/grid.js")
   [:script
    "
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
