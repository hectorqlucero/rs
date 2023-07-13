(ns sk.routes.proutes
  (:require
   [compojure.core :refer [defroutes GET POST]]
   [sk.handlers.admin.menus.handler :as menus]
   [sk.handlers.admin.users.handler :as users]
   [sk.handlers.admin.pincludes.handler :as pincludes]
   [sk.handlers.admin.proutes.handler :as proutes]
   [sk.handlers.admin.rincludes.handler :as rincludes]
   [sk.handlers.admin.routes.handler :as routes]
   [sk.handlers.admin.tipo_creditos.handler :as tipo_creditos]
   [sk.handlers.admin.zonas.handler :as zonas]
   [sk.handlers.admin.constructoras.handler :as constructoras]
   [sk.handlers.admin.fraccionamientos.handler :as fraccionamientos]
   [sk.handlers.admin.casas.handler :as casas]
   [sk.handlers.admin.clientes.handler :as clientes]
   [sk.handlers.clientes.handler :as clientes-output]))

(defroutes proutes
  (GET "/admin/menus" req [] (menus/menus req))
  (POST "/admin/menus" req [] (menus/menus-grid req))
  (GET "/admin/menus/edit/:id" [id] (menus/menus-form id))
  (POST "/admin/menus/save" req [] (menus/menus-save req))
  (POST "/admin/menus/delete" req [] (menus/menus-delete req))
  (GET "/admin/users" req [] (users/users req))
  (POST "/admin/users" req [] (users/users-grid req))
  (GET "/admin/users/edit/:id" [id] (users/users-form id))
  (POST "/admin/users/save" req [] (users/users-save req))
  (POST "/admin/users/delete" req [] (users/users-delete req))
  (GET "/admin/pincludes" req [] (pincludes/pincludes req))
  (POST "/admin/pincludes" req [] (pincludes/pincludes-grid req))
  (GET "/admin/pincludes/edit/:id" [id] (pincludes/pincludes-form id))
  (POST "/admin/pincludes/save" req [] (pincludes/pincludes-save req))
  (POST "/admin/pincludes/delete" req [] (pincludes/pincludes-delete req))
  (GET "/admin/proutes" req [] (proutes/proutes req))
  (POST "/admin/proutes" req [] (proutes/proutes-grid req))
  (GET "/admin/proutes/edit/:id" [id] (proutes/proutes-form id))
  (POST "/admin/proutes/save" req [] (proutes/proutes-save req))
  (POST "/admin/proutes/delete" req [] (proutes/proutes-delete req))
  (GET "/admin/rincludes" req [] (rincludes/rincludes req))
  (POST "/admin/rincludes" req [] (rincludes/rincludes-grid req))
  (GET "/admin/rincludes/edit/:id" [id] (rincludes/rincludes-form id))
  (POST "/admin/rincludes/save" req [] (rincludes/rincludes-save req))
  (POST "/admin/rincludes/delete" req [] (rincludes/rincludes-delete req))
  (GET "/admin/routes" req [] (routes/routes req))
  (POST "/admin/routes" req [] (routes/routes-grid req))
  (GET "/admin/routes/edit/:id" [id] (routes/routes-form id))
  (POST "/admin/routes/save" req [] (routes/routes-save req))
  (POST "/admin/routes/delete" req [] (routes/routes-delete req))
  (GET "/admin/tipo_creditos" req [] (tipo_creditos/tipo_creditos req))
  (POST "/admin/tipo_creditos" req [] (tipo_creditos/tipo_creditos-grid req))
  (GET "/admin/tipo_creditos/edit/:id" [id] (tipo_creditos/tipo_creditos-form id))
  (POST "/admin/tipo_creditos/save" req [] (tipo_creditos/tipo_creditos-save req))
  (POST "/admin/tipo_creditos/delete" req [] (tipo_creditos/tipo_creditos-delete req))
  (GET "/admin/zonas" req [] (zonas/zonas req))
  (POST "/admin/zonas" req [] (zonas/zonas-grid req))
  (GET "/admin/zonas/edit/:id" [id] (zonas/zonas-form id))
  (POST "/admin/zonas/save" req [] (zonas/zonas-save req))
  (POST "/admin/zonas/delete" req [] (zonas/zonas-delete req))
  (GET "/admin/constructoras" req [] (constructoras/constructoras req))
  (POST "/admin/constructoras" req [] (constructoras/constructoras-grid req))
  (GET "/admin/constructoras/edit/:id" [id] (constructoras/constructoras-form id))
  (POST "/admin/constructoras/save" req [] (constructoras/constructoras-save req))
  (POST "/admin/constructoras/delete" req [] (constructoras/constructoras-delete req))
  (GET "/admin/fraccionamientos" req [] (fraccionamientos/fraccionamientos req))
  (POST "/admin/fraccionamientos" req [] (fraccionamientos/fraccionamientos-grid req))
  (GET "/admin/fraccionamientos/edit/:id" [id] (fraccionamientos/fraccionamientos-form id))
  (POST "/admin/fraccionamientos/save" req [] (fraccionamientos/fraccionamientos-save req))
  (POST "/admin/fraccionamientos/delete" req [] (fraccionamientos/fraccionamientos-delete req))
  (GET "/admin/casas" req [] (casas/casas req))
  (POST "/admin/casas" req [] (casas/casas-grid req))
  (GET "/admin/casas/edit/:id" [id] (casas/casas-form id))
  (POST "/admin/casas/save" req [] (casas/casas-save req))
  (POST "/admin/casas/delete" req [] (casas/casas-delete req))
  (GET "/admin/clientes" req [] (clientes/clientes req))
  (POST "/admin/clientes" req [] (clientes/clientes-grid req))
  (GET "/admin/clientes/edit/:id" [id] (clientes/clientes-form id))
  (POST "/admin/clientes/save" req [] (clientes/clientes-save req))
  (POST "/admin/clientes/delete" req [] (clientes/clientes-delete req))
  (GET "/clientes" req [] (clientes-output/clientes req))
  (GET "/clientes/reporte" req [] (clientes-output/clientes-reporte req))
  (GET "/clientes/pdf" req [] (clientes-output/clientes-pdf req))
  (GET "/clientes/csv" req [] (clientes-output/clientes-csv req))
  (GET "/clientes/get_casas/:clientes_id" [clientes_id] (clientes-output/get-casas clientes_id)))
