(ns rs.routes.proutes
  (:require
   [compojure.core :refer [defroutes GET POST]]
   [rs.handlers.admin.tramites.controller :as tramites-controller]
   [rs.handlers.vista_ventas_completadas.controller :as vista_ventas_completadas-dashboard]
   [rs.handlers.vista_tramites_pendientes.controller :as vista_tramites_pendientes-dashboard]
   [rs.handlers.vista_pagos_atrasados.controller :as vista_pagos_atrasados-dashboard]
   [rs.handlers.vista_documentos_por_vencer.controller :as vista_documentos_por_vencer-dashboard]
   [rs.handlers.vista_contratos_renta_activos.controller :as vista_contratos_renta_activos-dashboard]
   [rs.handlers.vista_clientes_completa.controller :as vista_clientes_completa-dashboard]
   [rs.handlers.vista_avaluos_vigentes.controller :as vista_avaluos_vigentes-dashboard]
   [rs.handlers.admin.avaluos.controller :as avaluos-controller]
   [rs.handlers.admin.alquileresagentes.controller :as alquileresagentes-controller]
   [rs.handlers.admin.ventasagentes.controller :as ventasagentes-controller]
   [rs.handlers.admin.alquileresclientes.controller :as alquileresclientes-controller]
   [rs.handlers.admin.ventasclientes.controller :as ventasclientes-controller]
   [rs.handlers.admin.propiedadesagentes.controller :as propiedadesagentes-controller]
   [rs.handlers.admin.pagos_rentaalquileres.controller :as pagos_rentaalquileres-controller]
   [rs.handlers.admin.pagos_ventasventas.controller :as pagos_ventasventas-controller]
   [rs.handlers.reports.vista_avaluos_vigentes.controller :as vista_avaluos_vigentes-report]
   [rs.handlers.reports.vista_documentos_por_vencer.controller :as vista_documentos_por_vencer-report]
   [rs.handlers.admin.avaluospropiedades.controller :as avaluospropiedades-controller]
   [rs.handlers.admin.documentospropiedades.controller :as documentospropiedades-controller]
   [rs.handlers.admin.documentos.controller :as documentos-controller]
   [rs.handlers.vista_agentes_performance.controller :as vista_agentes_performance-dashboard]
   [rs.handlers.vista_propiedades_disponibles.controller :as vista_propiedades_disponibles-dashboard]
   [rs.handlers.admin.alquileres.controller :as alquileres-controller]
   [rs.handlers.admin.ventas.controller :as ventas-controller]
   [rs.handlers.admin.agentes.controller :as agentes-controller]
   [rs.handlers.admin.clientes.controller :as clientes-controller]
   [rs.handlers.admin.propiedades.controller :as propiedades-controller]
   [rs.handlers.admin.users.controller :as users-controller]
   [rs.handlers.reports.users.controller :as users-report]
   [rs.handlers.users.controller :as users-dashboard]))

(defroutes proutes
  ;; Subgrid routes for documentospropiedades (parent: propiedades)
  (GET "/admin/documentospropiedades" params [] (documentospropiedades-controller/documentospropiedades-grid params))
  (GET "/admin/documentospropiedades/add-form/:parent_id" [parent_id :as request] (documentospropiedades-controller/documentospropiedades-add-form request parent_id))
  (GET "/admin/documentospropiedades/edit-form/:id" [id :as request] (documentospropiedades-controller/documentospropiedades-edit-form request id))
  (POST "/admin/documentospropiedades/save" params [] (documentospropiedades-controller/documentospropiedades-save params))
  (GET "/admin/documentospropiedades/delete/:id" [id :as request] (documentospropiedades-controller/documentospropiedades-delete request id))
  (GET "/admin/tramites" params [] (tramites-controller/tramites params))
  (GET "/admin/tramites/add-form" params [] (tramites-controller/tramites-add-form params))
  (GET "/admin/tramites/edit-form/:id" [id :as request] (tramites-controller/tramites-edit-form request id))
  (POST "/admin/tramites/save" params [] (tramites-controller/tramites-save params))
  (GET "/admin/tramites/delete/:id" [id :as request] (tramites-controller/tramites-delete request id))
  (GET "/admin/documentos" params [] (documentos-controller/documentos params))
  (GET "/admin/documentos/add-form" params [] (documentos-controller/documentos-add-form params))
  (GET "/admin/documentos/edit-form/:id" [id :as request] (documentos-controller/documentos-edit-form request id))
  (POST "/admin/documentos/save" params [] (documentos-controller/documentos-save params))
  (GET "/admin/documentos/delete/:id" [id :as request] (documentos-controller/documentos-delete request id))
  (GET "/vista_ventas_completadas" params [] (vista_ventas_completadas-dashboard/vista_ventas_completadas params))
  (GET "/vista_tramites_pendientes" params [] (vista_tramites_pendientes-dashboard/vista_tramites_pendientes params))
  (GET "/vista_pagos_atrasados" params [] (vista_pagos_atrasados-dashboard/vista_pagos_atrasados params))
  (GET "/vista_documentos_por_vencer" params [] (vista_documentos_por_vencer-dashboard/vista_documentos_por_vencer params))
  (GET "/vista_contratos_renta_activos" params [] (vista_contratos_renta_activos-dashboard/vista_contratos_renta_activos params))
  (GET "/vista_clientes_completa" params [] (vista_clientes_completa-dashboard/vista_clientes_completa params))
  (GET "/vista_avaluos_vigentes" params [] (vista_avaluos_vigentes-dashboard/vista_avaluos_vigentes params))
  ;; Subgrid routes for alquileresagentes (parent: agentes)
  (GET "/admin/alquileresagentes" params [] (alquileresagentes-controller/alquileresagentes-grid params))
  (GET "/admin/alquileresagentes/add-form/:parent_id" [parent_id :as request] (alquileresagentes-controller/alquileresagentes-add-form request parent_id))
  (GET "/admin/alquileresagentes/edit-form/:id" [id :as request] (alquileresagentes-controller/alquileresagentes-edit-form request id))
  (POST "/admin/alquileresagentes/save" params [] (alquileresagentes-controller/alquileresagentes-save params))
  (GET "/admin/alquileresagentes/delete/:id" [id :as request] (alquileresagentes-controller/alquileresagentes-delete request id))

  ;; Subgrid routes for ventasagentes (parent: agentes)
  (GET "/admin/ventasagentes" request [] (ventasagentes-controller/ventasagentes-grid request))
  (GET "/admin/ventasagentes/add-form/:parent_id" [parent_id :as request] (ventasagentes-controller/ventasagentes-add-form request parent_id))
  (GET "/admin/ventasagentes/edit-form/:id" [id :as request] (ventasagentes-controller/ventasagentes-edit-form request id))
  (POST "/admin/ventasagentes/save" params [] (ventasagentes-controller/ventasagentes-save params))
  (GET "/admin/ventasagentes/delete/:id" [id :as request] (ventasagentes-controller/ventasagentes-delete request id))

  ;; Subgrid routes for alquileresclientes (parent: clientes)
  (GET "/admin/alquileresclientes" request [] (alquileresclientes-controller/alquileresclientes-grid request))
  (GET "/admin/alquileresclientes/add-form/:parent_id" [parent_id :as request] (alquileresclientes-controller/alquileresclientes-add-form request parent_id))
  (GET "/admin/alquileresclientes/edit-form/:id" [id :as request] (alquileresclientes-controller/alquileresclientes-edit-form request id))
  (POST "/admin/alquileresclientes/save" params [] (alquileresclientes-controller/alquileresclientes-save params))
  (GET "/admin/alquileresclientes/delete/:id" [id :as request] (alquileresclientes-controller/alquileresclientes-delete request id))

  ;; Subgrid routes for ventasclientes (parent: clientes)
  (GET "/admin/ventasclientes" request [] (ventasclientes-controller/ventasclientes-grid request))
  (GET "/admin/ventasclientes/add-form/:parent_id" [parent_id :as request] (ventasclientes-controller/ventasclientes-add-form request parent_id))
  (GET "/admin/ventasclientes/edit-form/:id" [id :as request] (ventasclientes-controller/ventasclientes-edit-form request id))
  (POST "/admin/ventasclientes/save" params [] (ventasclientes-controller/ventasclientes-save params))
  (GET "/admin/ventasclientes/delete/:id" [id :as request] (ventasclientes-controller/ventasclientes-delete request id))

  ;; Subgrid routes for propiedadesagentes (parent: agentes)
  (GET "/admin/propiedadesagentes" request [] (propiedadesagentes-controller/propiedadesagentes-grid request))
  (GET "/admin/propiedadesagentes/add-form/:parent_id" [parent_id :as request] (propiedadesagentes-controller/propiedadesagentes-add-form request parent_id))
  (GET "/admin/propiedadesagentes/edit-form/:id" [id :as request] (propiedadesagentes-controller/propiedadesagentes-edit-form request id))
  (POST "/admin/propiedadesagentes/save" params [] (propiedadesagentes-controller/propiedadesagentes-save params))
  (GET "/admin/propiedadesagentes/delete/:id" [id :as request] (propiedadesagentes-controller/propiedadesagentes-delete request id))

  ;; Subgrid routes for pagos_rentaalquileres (parent: alquileres)
  (GET "/admin/pagos_rentaalquileres" request [] (pagos_rentaalquileres-controller/pagos_rentaalquileres-grid request))
  (GET "/admin/pagos_rentaalquileres/add-form/:parent_id" [parent_id :as request] (pagos_rentaalquileres-controller/pagos_rentaalquileres-add-form request parent_id))
  (GET "/admin/pagos_rentaalquileres/edit-form/:id" [id :as request] (pagos_rentaalquileres-controller/pagos_rentaalquileres-edit-form request id))
  (POST "/admin/pagos_rentaalquileres/save" params [] (pagos_rentaalquileres-controller/pagos_rentaalquileres-save params))
  (GET "/admin/pagos_rentaalquileres/delete/:id" [id :as request] (pagos_rentaalquileres-controller/pagos_rentaalquileres-delete request id))

  ;; Subgrid routes for pagos_ventasventas (parent: ventas)
  (GET "/admin/pagos_ventasventas" request [] (pagos_ventasventas-controller/pagos_ventasventas-grid request))
  (GET "/admin/pagos_ventasventas/add-form/:parent_id" [parent_id :as request] (pagos_ventasventas-controller/pagos_ventasventas-add-form request parent_id))
  (GET "/admin/pagos_ventasventas/edit-form/:id" [id :as request] (pagos_ventasventas-controller/pagos_ventasventas-edit-form request id))
  (POST "/admin/pagos_ventasventas/save" params [] (pagos_ventasventas-controller/pagos_ventasventas-save params))
  (GET "/admin/pagos_ventasventas/delete/:id" [id :as request] (pagos_ventasventas-controller/pagos_ventasventas-delete request id))

  ;; Report routes
  (GET "/reports/vista_avaluos_vigentes" params [] (vista_avaluos_vigentes-report/vista_avaluos_vigentes params))
  (GET "/reports/vista_documentos_por_vencer" params [] (vista_documentos_por_vencer-report/vista_documentos_por_vencer params))

  ;; Subgrid routes for avaluospropiedades (parent: propiedades)
  (GET "/admin/avaluospropiedades" request [] (avaluospropiedades-controller/avaluospropiedades-grid request))
  (GET "/admin/avaluospropiedades/add-form/:parent_id" [parent_id :as request] (avaluospropiedades-controller/avaluospropiedades-add-form request parent_id))
  (GET "/admin/avaluospropiedades/edit-form/:id" [id :as request] (avaluospropiedades-controller/avaluospropiedades-edit-form request id))
  (POST "/admin/avaluospropiedades/save" params [] (avaluospropiedades-controller/avaluospropiedades-save params))
  (GET "/admin/avaluospropiedades/delete/:id" [id :as request] (avaluospropiedades-controller/avaluospropiedades-delete request id))

  ;; Subgrid routes for documentospropiedades (parent: propiedades)
  (GET "/admin/documentospropiedades" request [] (documentospropiedades-controller/documentospropiedades-grid request))
  (GET "/admin/documentospropiedades/add-form/:parent_id" [parent_id :as request] (documentospropiedades-controller/documentospropiedades-add-form request parent_id))
  (GET "/admin/documentospropiedades/edit-form/:id" [id :as request] (documentospropiedades-controller/documentospropiedades-edit-form request id))
  (POST "/admin/documentospropiedades/save" params [] (documentospropiedades-controller/documentospropiedades-save params))
  (GET "/admin/documentospropiedades/delete/:id" [id :as request] (documentospropiedades-controller/documentospropiedades-delete request id))

  ;; Admin grid routes for documentos
  (GET "/admin/documentos" params [] (documentos-controller/documentos params))
  (GET "/admin/documentos/add-form" params [] (documentos-controller/documentos-add-form params))
  (GET "/admin/documentos/edit-form/:id" [id :as request] (documentos-controller/documentos-edit-form request id))
  (POST "/admin/documentos/save" params [] (documentos-controller/documentos-save params))
  (GET "/admin/documentos/delete/:id" [id :as request] (documentos-controller/documentos-delete request id))

  ;; Dashboard routes
  (GET "/vista_agentes_performance" params [] (vista_agentes_performance-dashboard/vista_agentes_performance params))
  (GET "/vista_propiedades_disponibles" params [] (vista_propiedades_disponibles-dashboard/vista_propiedades_disponibles params))

  ;; Admin grid routes for avaluos
  (GET "/admin/avaluos" params [] (avaluos-controller/avaluos params))
  (GET "/admin/avaluos/add-form" params [] (avaluos-controller/avaluos-add-form params))
  (GET "/admin/avaluos/edit-form/:id" [id :as request] (avaluos-controller/avaluos-edit-form request id))
  (POST "/admin/avaluos/save" params [] (avaluos-controller/avaluos-save params))
  (GET "/admin/avaluos/delete/:id" [id :as request] (avaluos-controller/avaluos-delete request id))

  ;; Admin grid routes for alquileres
  (GET "/admin/alquileres" params [] (alquileres-controller/alquileres params))
  (GET "/admin/alquileres/add-form" params [] (alquileres-controller/alquileres-add-form params))
  (GET "/admin/alquileres/edit-form/:id" [id :as request] (alquileres-controller/alquileres-edit-form request id))
  (POST "/admin/alquileres/save" params [] (alquileres-controller/alquileres-save params))
  (GET "/admin/alquileres/delete/:id" [id :as request] (alquileres-controller/alquileres-delete request id))

  ;; Admin grid routes for ventas
  (GET "/admin/ventas" params [] (ventas-controller/ventas params))
  (GET "/admin/ventas/add-form" params [] (ventas-controller/ventas-add-form params))
  (GET "/admin/ventas/edit-form/:id" [id :as request] (ventas-controller/ventas-edit-form request id))
  (POST "/admin/ventas/save" params [] (ventas-controller/ventas-save params))
  (GET "/admin/ventas/delete/:id" [id :as request] (ventas-controller/ventas-delete request id))

  ;; Admin grid routes for agentes
  (GET "/admin/agentes" params [] (agentes-controller/agentes params))
  (GET "/admin/agentes/add-form" params [] (agentes-controller/agentes-add-form params))
  (GET "/admin/agentes/edit-form/:id" [id :as request] (agentes-controller/agentes-edit-form request id))
  (POST "/admin/agentes/save" params [] (agentes-controller/agentes-save params))
  (GET "/admin/agentes/delete/:id" [id :as request] (agentes-controller/agentes-delete request id))

  ;; Admin grid routes for clientes
  (GET "/admin/clientes" params [] (clientes-controller/clientes params))
  (GET "/admin/clientes/add-form" params [] (clientes-controller/clientes-add-form params))
  (GET "/admin/clientes/edit-form/:id" [id :as request] (clientes-controller/clientes-edit-form request id))
  (POST "/admin/clientes/save" params [] (clientes-controller/clientes-save params))
  (GET "/admin/clientes/delete/:id" [id :as request] (clientes-controller/clientes-delete request id))

  ;; Admin grid routes for propiedades
  (GET "/admin/propiedades" params [] (propiedades-controller/propiedades params))
  (GET "/admin/propiedades/add-form" params [] (propiedades-controller/propiedades-add-form params))
  (GET "/admin/propiedades/edit-form/:id" [id :as request] (propiedades-controller/propiedades-edit-form request id))
  (POST "/admin/propiedades/save" params [] (propiedades-controller/propiedades-save params))
  (GET "/admin/propiedades/delete/:id" [id :as request] (propiedades-controller/propiedades-delete request id))

  ;; User management routes
  (GET "/reports/users" params [] (users-report/users params))
  (GET "/admin/users" params [] (users-controller/users params))
  (GET "/admin/users/add-form" params [] (users-controller/users-add-form params))
  (GET "/admin/users/edit-form/:id" [id :as request] (users-controller/users-edit-form request id))
  (POST "/admin/users/save" params [] (users-controller/users-save params))
  (GET "/admin/users/delete/:id" [id :as request] (users-controller/users-delete request id))

  ;; Dashboard user routes
  (GET "/users" params [] (users-dashboard/users params)))