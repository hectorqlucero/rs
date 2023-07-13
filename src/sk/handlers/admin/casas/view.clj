(ns sk.handlers.admin.casas.view
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
    {:id "fraccionamientos_id"
     :name "fraccionamientos_id"
     :class "easyui-combobox"
     :data-options "label:'Fraccionamiento:',
        labelPosition:'top',
        url:'/table_ref/get_fraccionamientos',
        method:'GET',
        required:true,
        width:'100%'"})
   (build-field
    {:id "modelo"
     :name "modelo"
     :class "easyui-textbox"
     :prompt "El modelo de la casa"
     :data-options "label:'Modelo:',
        labelPosition:'top',
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
        width:'100%'"})
   (build-field
    {:id "recamaras"
     :name "recamaras"
     :class "easyui-numberbox"
     :prompt "Numero de recamaras de la casa..."
     :data-options "label:'Recamaras:',
        labelPosition:'top',
        min:0,
        precision:0,
        required:true,
        width:'100%'"})
   (build-field
    {:id "baños"
     :name "baños"
     :class "easyui-numberbox"
     :prompt "Numero de baños de la casa..."
     :data-options "label:'Baños:',
        labelPosition:'top',
        min:0,
        precision:1,
        required:true,
        width:'100%'"})
   (build-field
    {:id "plantas"
     :name "plantas"
     :class "easyui-numberbox"
     :prompt "Numero de plantas de la casa..."
     :data-options "label:'Plantas:',
        labelPosition:'top',
        min:1,
        precision:0,
        required:true,
        width:'100%'"})
   (build-field
    {:id "costo"
     :name "costo"
     :class "easyui-numberbox"
     :prompt "Costo de la casa..."
     :data-options "label:'Costo:',
        labelPosition:'top',
        min:0,
        precision:2,
        required:true,
        width:'100%'"})
   (build-field
    {:id "mtc"
     :name "mtc"
     :class "easyui-numberbox"
     :prompt "Metros de construccion de la casa..."
     :data-options "label:'Metros de Construcción:',
        labelPosition:'top',
        min:0,
        precision:0,
        required:true,
        width:'100%'"})
   (build-field
    {:id "mtt"
     :name "mtt"
     :class "easyui-numberbox"
     :prompt "Metros del terreno de la casa..."
     :data-options "label:'Metros del Terreno:',
        labelPosition:'top',
        min:0,
        precision:0,
        required:true,
        width:'100%'"})
   (build-field
    {:id "comentarios"
     :name "comentarios"
     :class "easyui-textbox"
     :prompt "Comentarios adicionales de la propiedad..."
     :data-options "label:'Comentarios:',
        labelPosition:'top',
        required:false,
        multiline:true,
        height:120,
        width:'100%'"})))

(defn casas-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/casas"
    (list
     [:th {:data-options "field:'fraccionamientos_id',sortable:true,width:100"
           :formatter "get_fraccionamiento"} "FRACC"]
     [:th {:data-options "field:'modelo',sortable:true,width:100"} "MODELO"]
     [:th {:data-options "field:'tipo',sortable:true,width:100"} "TIPO"]
     [:th {:data-options "field:'recamaras',sortable:true,width:100"} "RECAMARAS"]
     [:th {:data-options "field:'baños',sortable:true,width:100"} "BAÑOS"]
     [:th {:data-options "field:'plantas',sortable:true,width:100"} "PLANTAS"]
     [:th {:data-options "field:'costo_formatted',sortable:true,width:100"} "COSTO"]
     [:th {:data-options "field:'mtc',sortable:true,width:100"} "MC"]
     [:th {:data-options "field:'mtt',sortable:true,width:100"} "MT"]))
   (build-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn casas-scripts []
  (list
   (include-js "/js/grid.js")
   [:script
    "
   function get_fraccionamiento(val,row,index) {
    var result = null;
    var scriptUrl = '/table_ref/get-item/fraccionamientos/nombre/id/' + val;
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
