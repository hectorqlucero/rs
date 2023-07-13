(ns sk.handlers.clientes.view
  (:require [hiccup.page :refer [include-js]]
            [sk.handlers.clientes.model :refer [get-rows get-casas]]
            [hiccup.core :refer [html]]))

(defn get-title [row]
  (str
   "Costo: " (:costo row) ", "
   "Metros Construccion: " (:mtc row) ", "
   "Metros Terreno: " (:mtt row) ", "
   "Modelo: " (:modelo row) ", "
   "Plantas: " (:plantas row) ", "
   "Recamaras: " (:recamaras row) ", "
   "Baños: " (:baños row)))

(defn casas-view [clientes_id]
  (let [rows (get-casas clientes_id)]
    (list
     (html
      [:div.container
       [:table.table.table-bordered.table-hover
        [:thead.table-info
         [:tr
          [:th "CIUDAD"]
          [:th "ZONA"]
          [:th "FRACCIONAMIENTO"]]]

        [:tbody
         (map (fn [row]
                [:tr
                 [:td (:ciudad row)]
                 [:td (:zona row)]
                 [:td [:a.easyui-tooltip {:href "#"
                                          :title (get-title row)
                                          :position "top"} (:nombre row)]]]) rows)]]]))))

(defn my-body [row]
  [:tr
   [:td (:id row)]
   [:td (:nombre row)]
   [:td (:paterno row)]
   [:td (:materno row)]
   [:td (:telefono row)]
   [:td (:celular row)]
   [:td (:email row)]
   [:td (:ingresos_formatted row)]
   [:td (:pc_formatted row)]
   [:td (:tipo_creditos row)]])

(defn clientes-view [title]
  (let [rows (get-rows)]
    (list
     [:table.dg {:data-options "remoteSort:false,fit:true,rownumbers:true,fitColumns:true,toolbar:'#toolbar'" :title title}
      [:thead
       [:tr
        [:th {:data-options "field:'id',sortable:true,width:100"} "ID"]
        [:th {:data-options "field:'nombre',sortable:true,width:100"} "NOMBRE"]
        [:th {:data-options "field:'paterno',sortable:true,width:100"} "PATERNO"]
        [:th {:data-options "field:'materno',sortable:true,width:100"} "MATERNO"]
        [:th {:data-options "field:'telefono',sortable:true,width:100"} "TELEFONO"]
        [:th {:data-options "field:'celular',sortable:true,width:100"} "CELULAR"]
        [:th {:data-options "field:'email',sortable:true,width:100"} "EMAIL"]
        [:th {:data-options "field:'ingresos_formatted',sortable:true,width:100"} "INGRESOS"]
        [:th {:data-options "field:'pc_formatted',sortable:true,width:100"} "PC"]
        [:th {:data-options "field:'tipo_creditos',sortable:true,width:100"} "TIPO CREDITO"]]]
      [:tbody (map my-body rows)]]
     [:div#toolbar
      [:a {:href "/clientes/reporte"
           :class "easyui-linkbutton"
           :data-options "iconCls:'icon-print',plain: true"} "Reporte"]
      [:a {:href "/clientes/pdf"
           :class "easyui-linkbutton"
           :data-options "iconCls:'icon-save',plain: true"} "PDF"]
      [:a {:href "/clientes/csv"
           :class "easyui-linkbutton"
           :data-options "iconCls:'icon-large-smartart',plain: true"} "CSV"]])))

(defn clientes-scripts []
  (include-js "js/clientesView.js"))

(comment
  (get-casas 1))
