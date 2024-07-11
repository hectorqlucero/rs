(ns sk.routes.proutes
  (:require [compojure.core :refer [defroutes GET POST]]
            [sk.handlers.admin.users.controller :as users-controller]
            [sk.handlers.clientes.controller :as clientes-dashboard]
            [sk.handlers.admin.casas.controller :as casas-controller]
            [sk.handlers.admin.clientes.controller :as clientes-controller]
            [sk.handlers.admin.constructoras.controller :as constructoras-controller]
            [sk.handlers.admin.tipo_creditos.controller :as tipo_creditos-controller]
            [sk.handlers.admin.zonas.controller :as zonas-controller]
            [sk.handlers.admin.fraccionamientos.controller :as fraccionamientos-controller]))

(defroutes proutes
  (GET "/admin/users" params users-controller/users)
  (GET "/admin/users/edit/:id" [id] (users-controller/users-edit id))
  (POST "/admin/users/save" params [] (users-controller/users-save params))
  (GET "/admin/users/add" params [] (users-controller/users-add params))
  (GET "/admin/users/delete/:id" [id] (users-controller/users-delete id))

  (GET "/admin/clientes" params clientes-controller/clientes)
  (GET "/admin/clientes/edit/:id" [id] (clientes-controller/clientes-edit id))
  (POST "/admin/clientes/save" params [] (clientes-controller/clientes-save params))
  (GET "/admin/clientes/add" params [] (clientes-controller/clientes-add params))
  (GET "/admin/clientes/delete/:id" [id] (clientes-controller/clientes-delete id))

  (GET "/clientes" params [] (clientes-dashboard/clientes params))
  (GET "/clientes_activos" params [] (clientes-dashboard/clientes-activos params))
  (GET "/renta" params [] (clientes-dashboard/renta params))
  (GET "/renta/casas/:id" [id] (clientes-dashboard/renta-casas id))
  (GET "/clientes/get_casas/:id" [id] (clientes-dashboard/get-casas id))
  (GET "/proceso/renta/:cliente_id/:casa_id" [cliente_id casa_id] (clientes-dashboard/proceso-renta cliente_id casa_id))
  (GET "/proceso/venta/:cliente_id/:casa_id" [cliente_id casa_id] (clientes-dashboard/proceso-venta cliente_id casa_id))
  (GET "/renta/proceso" params [] (clientes-dashboard/renta-proceso params))
  (GET "/renta/proceso/casas/:id" [id] (clientes-dashboard/renta-proceso-casas id))
  (GET "/venta/proceso" params [] (clientes-dashboard/venta-proceso params))
  (GET "/venta/proceso/casas/:id" [id] (clientes-dashboard/venta-proceso-casas id))
  (GET "/regresar/renta/:id/:proceso_id" [id proceso_id] (clientes-dashboard/renta-regresar id proceso_id))
  (GET "/regresar/venta/:id/:proceso_id" [id proceso_id] (clientes-dashboard/venta-regresar id proceso_id))
  (GET "/final/renta/:id/:proceso_id" [id proceso_id] (clientes-dashboard/renta-rentado id proceso_id))
  (GET "/final/venta/:id/:proceso_id" [id proceso_id] (clientes-dashboard/venta-vendido id proceso_id))

  (GET "/admin/constructoras" params constructoras-controller/constructoras)
  (GET "/admin/constructoras/edit/:id" [id] (constructoras-controller/constructoras-edit id))
  (POST "/admin/constructoras/save" params [] (constructoras-controller/constructoras-save params))
  (GET "/admin/constructoras/add" params [] (constructoras-controller/constructoras-add params))
  (GET "/admin/constructoras/delete/:id" [id] (constructoras-controller/constructoras-delete id))

  (GET "/admin/tipo_creditos" params tipo_creditos-controller/tipo_creditos)
  (GET "/admin/tipo_creditos/edit/:id" [id] (tipo_creditos-controller/tipo_creditos-edit id))
  (POST "/admin/tipo_creditos/save" params [] (tipo_creditos-controller/tipo_creditos-save params))
  (GET "/admin/tipo_creditos/add" params [] (tipo_creditos-controller/tipo_creditos-add params))
  (GET "/admin/tipo_creditos/delete/:id" [id] (tipo_creditos-controller/tipo_creditos-delete id))

  (GET "/admin/zonas" params zonas-controller/zonas)
  (GET "/admin/zonas/edit/:id" [id] (zonas-controller/zonas-edit id))
  (POST "/admin/zonas/save" params [] (zonas-controller/zonas-save params))
  (GET "/admin/zonas/add" params [] (zonas-controller/zonas-add params))
  (GET "/admin/zonas/delete/:id" [id] (zonas-controller/zonas-delete id))

  (GET "/admin/fraccionamientos" params fraccionamientos-controller/fraccionamientos)
  (GET "/admin/fraccionamientos/edit/:id" [id] (fraccionamientos-controller/fraccionamientos-edit id))
  (POST "/admin/fraccionamientos/save" params [] (fraccionamientos-controller/fraccionamientos-save params))
  (GET "/admin/fraccionamientos/add" params [] (fraccionamientos-controller/fraccionamientos-add params))
  (GET "/admin/fraccionamientos/delete/:id" [id] (fraccionamientos-controller/fraccionamientos-delete id))

  (GET "/admin/casas" params casas-controller/casas)
  (GET "/admin/casas/edit/:id" [id] (casas-controller/casas-edit id))
  (POST "/admin/casas/save" params [] (casas-controller/casas-save params))
  (GET "/admin/casas/add" params [] (casas-controller/casas-add params))
  (GET "/admin/casas/delete/:id" [id] (casas-controller/casas-delete id)))
