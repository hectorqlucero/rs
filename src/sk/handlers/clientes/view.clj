(ns sk.handlers.clientes.view
  (:require [sk.models.grid :refer [build-dashboard
                                    build-modal
                                    modal-script]]
            [sk.handlers.clientes.model :refer [casas-renta
                                                casas-venta
                                                clientes-venta
                                                casas-renta-proceso
                                                casas-venta-proceso]]))

(defn build-table-head
  []
  (list
   [:thead
    [:tr
     [:th {:data-sortable "true"
           :data-field ":nombre_completo"} "NOMBRE"]
     [:th {:data-sortable "true"
           :data-field ":celular"} "CELULAR"]
     [:th {:data-sortable "true"
           :data-field ":email"} "EMAIL"]
     [:th {:data-sortable "true"
           :data-field ":ingresos_formatted"} "INGRESOS"]
     [:th {:data-sortable "true"
           :data-field ":pc_formatted"} "PC"]
     [:th {:data-sortable "true"
           :data-field ":tipo_creditos_id_formatted"} "TIPO DE CREDITO"]
     [:th {:data-sortable "true"
           :data-field ":tipo_formatted"} "TIPO"]
     [:th.text-nowrap.text-center {:data-sortable "false"
                                   :style "width:15%;"} "CASAS"]]]))

(defn renta-view
  [title rows]
  (list
   [:div.table-responsive
    [:table.table.table-sm {:id "renta_table"
                            :data-locale "es-MX"
                            :data-toggle "table"
                            :data-show-columns "true"
                            :data-show-print "true"
                            :data-search "true"
                            :data-pagination "true"
                            :data-key-events "true"}
     [:caption title]
     (build-table-head)
     (map (fn [row]
            [:tr
             [:td (:nombre_completo row)]
             [:td (:celular row)]
             [:td (:email row)]
             [:td (:ingresos_formatted row)]
             [:td (:pc_formatted row)]
             [:td (:tipo_creditos_id_formatted row)]
             [:td (:tipo_formatted row)]
             [:td.text-nowrap.text-center {:style "width:15%;"}
              [:a.btn.btn-primary {:role "button"
                                   :style "margin:1px;"
                                   :href (str "/renta/casas/" (:id row))} "Casas"]]]) rows)]]))

(defn renta-proceso-view
  [title rows]
  (list
   [:div.table-responsive
    [:table.table.table-sm {:id "renta_table"
                            :data-locale "es-MX"
                            :data-toggle "table"
                            :data-show-columns "true"
                            :data-show-print "true"
                            :data-search "true"
                            :data-pagination "true"
                            :data-key-events "true"}
     [:caption title]
     (build-table-head)
     (map (fn [row]
            [:tr
             [:td (:nombre_completo row)]
             [:td (:celular row)]
             [:td (:email row)]
             [:td (:ingresos_formatted row)]
             [:td (:pc_formatted row)]
             [:td (:tipo_creditos_id_formatted row)]
             [:td (:tipo_formatted row)]
             [:td.text-nowrap.text-center {:style "width:15%;"}
              [:a.btn.btn-primary {:role "button"
                                   :style "margin:1px;"
                                   :href (str "/renta/proceso/casas/" (:cliente_id row))} "Casas"]]]) rows)]]))

(defn venta-proceso-view
  [title rows]
  (list
   [:div.table-responsive
    [:table.table.table-sm {:id "renta_table"
                            :data-locale "es-MX"
                            :data-toggle "table"
                            :data-show-columns "true"
                            :data-show-print "true"
                            :data-search "true"
                            :data-pagination "true"
                            :data-key-events "true"}
     [:caption title]
     (build-table-head)
     (map (fn [row]
            [:tr
             [:td (:nombre_completo row)]
             [:td (:celular row)]
             [:td (:email row)]
             [:td (:ingresos_formatted row)]
             [:td (:pc_formatted row)]
             [:td (:tipo_creditos_id_formatted row)]
             [:td (:tipo_formatted row)]
             [:td.text-nowrap.text-center {:style "width:15%;"}
              [:a.btn.btn-primary {:role "button"
                                   :style "margin:1px;"
                                   :href (str "/venta/proceso/casas/" (:cliente_id row))} "Casas"]]]) rows)]]))

(defn clientes-view
  [title rows]
  (list
   [:div.table-responsive
    [:table.table.table-sm {:id "clientes_table"
                            :data-locale "es-MX"
                            :data-toggle "table"
                            :data-show-columns "true"
                            :data-show-toggle "true"
                            :data-show-print "true"
                            :data-search "true"
                            :data-pagination "true"
                            :data-key-events "true"}
     [:caption title]
     [:thead
      [:tr
       [:th {:data-sortable "true"
             :data-field ":nombre_completo"} "NOMBRE"]
       [:th {:data-sortable "true"
             :data-field ":celular"} "CELULAR"]
       [:th {:data-sortable "true"
             :data-field ":email"} "EMAIL"]
       [:th {:data-sortable "true"
             :data-field ":ingresos_formatted"} "INGRESOS"]
       [:th {:data-sortable "true"
             :data-field ":pc_formatted"} "PC"]
       [:th {:data-sortable "true"
             :data-field ":tipo_creditos_id_formatted"} "TIPO DE CREDITO"]
       [:th {:data-sortable "true"
             :data-field ":tipo_formatted"} "TIPO"]]]
     [:tbody
      (map (fn [row]
             [:tr
              [:td (:nombre_completo row)]
              [:td (:celular row)]
              [:td (:email row)]
              [:td (:ingresos_formatted row)]
              [:td (:pc_formatted row)]
              [:td (:tipo_creditos_id_formatted row)]
              [:td (:tipo_formatted row)]]) rows)]]]))

(defn clientes-activos-view
  [title rows]
  (list
   [:div.table-responsive
    [:table.table.table-sm {:id "clientes_table"
                            :data-locale "es-MX"
                            :data-toggle "table"
                            :data-show-columns "true"
                            :data-show-toggle "true"
                            :data-show-print "true"
                            :data-search "true"
                            :data-pagination "true"
                            :data-key-events "true"}
     [:caption title]
     [:thead
      [:tr
       [:th {:data-sortable "true"
             :data-field ":nombre_completo"} "NOMBRE"]
       [:th {:data-sortable "true"
             :data-field ":celular"} "CELULAR"]
       [:th {:data-sortable "true"
             :data-field ":email"} "EMAIL"]
       [:th {:data-sortable "true"
             :data-field ":ingresos_formatted"} "INGRESOS"]
       [:th {:data-sortable "true"
             :data-field ":pc_formatted"} "PC"]
       [:th {:data-sortable "true"
             :data-field ":tipo_creditos"} "TIPO DE CREDITO"]
       [:th {:data-sortable "true"
             :data-field ":tipo_creditos_id_formatted"} "TIPO"]
       [:th.text-nowrap.text-center {:data-sortable "false"
                                     :style "width:15%;"} "CASAS"]]]
     [:tbody
      (map (fn [row]
             [:tr
              [:td (:nombre_completo row)]
              [:td (:celular row)]
              [:td (:email row)]
              [:td (:ingresos_formatted row)]
              [:td (:pc_formatted row)]
              [:td (:tipo_creditos_id_formatted row)]
              [:td (:tipo row)]
              [:td.text-nowrap.text-center {:style "width:15%;"}
               [:a.btn.btn-primary {:role "button"
                                    :style "margin:1px;"
                                    :href (str "/clientes/get_casas/" (:id row))} "Casas"]]]) rows)]]]))

(defn renta-proceso-button [row]
  (let [clientes-id (:cliente_id row)
        casas-id (:id row)]
    [:div.col.col-auto [:a.btn.btn-secondary {:role "button"
                                              :href (str "/proceso/renta/" clientes-id "/" casas-id)} " Agregar a Proceso"]]))

(defn venta-proceso-button [row]
  (let [clientes-id (:cliente_id row)
        casas-id (:casa_id row)]
    [:div.col.col-auto [:a.btn.btn-secondary {:role "button"
                                              :href (str "/proceso/venta/" clientes-id "/" casas-id)} "Agregar a Proceso"]]))

(defn final-renta-button
  [row]
  (let [clientes-id (:cliente_id row)
        casas-id (:casa_id row)]
    [:div.col.col-auto
     [:a.btn.btn-success {:role "button"
                          :href (str "/final/renta/" casas-id)} "Rentada"]
     [:a.btn.btn-danger {:role "button"
                         :href (str "/regresar/renta/" casas-id)} "Regresar"]]))

(defn final-venta-button
  [row]
  (let [clientes-id (:cliente_id row)
        casas-id (:casa_id row)]
    [:div.col.col-auto
     [:a.btn.btn-success {:role "button"
                          :href (str "/final/venta/" casas-id)} "Vendida"]
     [:a.btn.btn-danger {:role "button"
                         :href (str "/regresar/venta/" casas-id)} "Regresar"]]))

(defn build-house-body
  [hbuttons row]
  (list
   [:div.card-body
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Ciudad:"]]
     [:div.col.col-auto.text-nowrap (:ciudad row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Constructora:"]]
     [:div.col.col-auto.text-nowrap (:razon_social row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Fraccionamiento:"]]
     [:div.col.col-auto.text-nowrap (:nombre row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Zona:"]]
     [:div.col.col-auto.text-nowrap (:zona row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Costo:"]]
     [:div.col.col-auto.text-nowrap (:costo row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Metros Construidos:"]]
     [:div.col.col-auto.text-nowrap (:mtc row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Metros del Terreno:"]]
     [:div.col.col-auto.text-nowrap (:mtt row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Modelo:"]]
     [:div.col.col-auto.text-nowrap (:modelo row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Plantas:"]]
     [:div.col.col-auto.text-nowrap (:plantas row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Recamaras:"]]
     [:div.col.col-auto.text-nowrap (:recamaras row)]]
    [:div.row
     [:div.col.col-auto [:span.fw-bold.text-nowrap "Baños:"]]
     [:div.col.col-auto.text-nowrap (:baños row)]]
    [:div.row (cond
                (= hbuttons 1) (renta-proceso-button row)
                (= hbuttons 2) (venta-proceso-button row)
                (= hbuttons 3) (final-renta-button row)
                (= hbuttons 4) (final-venta-button row))]]))

(defn build-house
  [title crows hbuttons]
  (list
   [:div.card
    [:div.card-header title]
    (map (partial build-house-body hbuttons) crows)
    [:div.card-footer
     [:button.btn.btn-primary {:type "button"
                               :data-bs-dismiss "modal"
                               :aria-label "Close"
                               :value "Regresar"} "Regresar"]]]))

(defn casas-view
  [title clientes-id row rows]
  (let [crows (casas-venta clientes-id)
        title (str (:nombre row) " " (:paterno row) " " (:materno row) " - " title)
        hbuttons 2]
    (list
     (clientes-activos-view title rows)
     (build-modal title {} (build-house title crows hbuttons)))))

(defn renta-casas-view
  [title clientes-id row rows]
  (let [crows (casas-renta clientes-id)
        title (str (:nombre row) " " (:paterno row) " " (:materno row) " - " title)
        hbuttons 1]
    (list
     (renta-view title rows)
     (build-modal title {} (build-house title crows hbuttons)))))

(defn renta-proceso-casas-view
  [title clientes-id row rows]
  (let [crows (casas-renta-proceso clientes-id)
        title (str (:nombre row) " " (:paterno row) " " (:materno row) " - " title)
        hbuttons 3]
    (list
     (renta-proceso-view title rows)
     (build-modal title {} (build-house title crows hbuttons)))))

(defn venta-proceso-casas-view
  [title clientes-id row rows]
  (let [crows (casas-venta-proceso clientes-id)
        title (str (:nombre row) " " (:paterno row) " " (:materno row) " - " title)
        hbuttons 4]
    (list
     (venta-proceso-view title rows)
     (build-modal title {} (build-house title crows hbuttons)))))

(defn casas-view-script
  []
  (modal-script))
