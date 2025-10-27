(ns rs.menu)

(def reports-items
  [;; Real Estate Reports
   ["/reports/vista_documentos_por_vencer" "Documentos por Vencer"]
   ["/reports/vista_avaluos_vigentes" "Avalúos Vigentes"]
   ;; System Reports  
   ["/reports/users" "Users"]])

(def admin-items
  [;; Core Real Estate Management
   ["/admin/propiedades" "Propiedades"]
   ["/admin/clientes" "Clientes"]
   ["/admin/agentes" "Agentes"]
   ["/admin/ventas" "Ventas"]
   ["/admin/alquileres" "Alquileres"]
   ;; Supporting Entities
   ["/admin/documentos" "Documentos"]
   ["/admin/avaluos" "Avaluos"]
   ;; System Administration
   ["/admin/users" "Users" "S"]]) ; Only system users

(def dashboard-items
  [["/vista_propiedades_disponibles" "Propiedades Disponibles"]
   ["/vista_agentes_performance" "Performance Agentes"]
   ["/vista_avaluos_vigentes" "Avaluos Vigentes"]
   ["/vista_clientes_completa" "CLientes"]
   ["/vista_contratos_renta_activos" "Contratos Renta Activos"]
   ["/vista_documentos_por_vencer" "Documentos Por Vencer"]
   ["/vista_pagos_atrasados" "Pagos Atrasados"]
   ["/vista_tramites_pendientes" "Tramites Pendientes"]
   ["/vista_ventas_completadas" "Ventas Competadas"]])

(def menu-config
  {:nav-links [["/" "Home"]]
   :dropdowns {:dashboards {:id "navdrop0"
                            :data-id "dashboards"
                            :label "Dashboards"
                            :items dashboard-items}
               :reports {:id "navdrop1"
                         :data-id "reports"
                         :label "Reportes"
                         :items reports-items}
               :admin {:id "navdrop2"
                       :data-id "admin"
                       :label "Administración"
                       :items admin-items}}})
