(ns sk.handlers.clientes.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.crud :refer [db crud-fix-id Insert Update Delete]]
            [sk.models.util :refer [get-session-id]]
            [sk.handlers.clientes.model :refer [get-clientes
                                                clientes-renta
                                                clientes-renta-proceso
                                                casas-renta-proceso
                                                casas-venta-proceso
                                                clientes-venta-proceso
                                                clientes-venta
                                                get-row]]
            [sk.handlers.clientes.view :refer [clientes-view
                                               renta-view
                                               renta-proceso-view
                                               renta-casas-view
                                               venta-proceso-view
                                               clientes-activos-view
                                               casas-view
                                               renta-proceso-casas-view
                                               venta-proceso-casas-view
                                               casas-view-script]]))

(defn clientes [_]
  (let [title "Clientes"
        ok (get-session-id)
        js nil
        rows (get-clientes)
        content (clientes-view title rows)]
    (application title ok js content)))

;; Start renta
(defn renta [_]
  (let [title "Clientes - Renta"
        ok (get-session-id)
        rows (clientes-renta)
        js nil
        content (renta-view title rows)]
    (application title ok js content)))

(defn renta-casas [clientes_id]
  (let [title "Casas en Renta"
        ok (get-session-id)
        js (casas-view-script)
        row (get-row clientes_id)
        rows (clientes-renta)
        content (renta-casas-view title clientes_id row rows)]
    (application title ok js content)))
;; End renta

;; Start venta
(defn clientes-activos [_]
  (let [title "Clientes"
        ok (get-session-id)
        rows (clientes-venta)
        js nil
        content (clientes-activos-view title rows)]
    (application title ok js content)))

(defn get-casas [clientes_id]
  (let [title "Posibles casas"
        ok (get-session-id)
        js (casas-view-script)
        row (get-row clientes_id)
        rows (clientes-venta)
        content (casas-view title clientes_id row rows)]
    (application title ok js content)))
;; End venta

(defn proceso-renta
  [cliente-id casa-id]
  (let [id (crud-fix-id casa-id)
        crow {:status "P"}
        cresult (Update db :casas crow ["id = ?" id])
        prow {:cliente_id cliente-id
              :casa_id casa-id}
        result (Insert db :proceso prow)]
    (if (and
         (seq cresult)
         (seq result))
      (error-404 "Casa transferida a proceso correctamente!" "/renta")
      (error-404 "No se pudo transferir a proceso!" "/renta"))))

(defn proceso-venta
  [cliente-id casa-id]
  (let [id (crud-fix-id casa-id)
        crow {:status "P"}
        cresult (Update db :casas crow ["id = ?" id])
        prow {:cliente_id cliente-id
              :casa_id casa-id}
        result (Insert db :proceso prow)]
    (if (and
         (seq cresult)
         (seq result))
      (error-404 "Casa transferida a proceso correctamente!" "/clientes_activos")
      (error-404 "No se pudo transferir a proceso!" "/clientes_activos"))))

(defn renta-proceso
  [_]
  (let [title "Clientes - Renta - En proceso"
        ok (get-session-id)
        rows (clientes-renta-proceso)
        js nil
        content (renta-proceso-view title rows)]
    (application title ok js content)))

(defn renta-proceso-casas
  [cliente-id]
  (let [title "Clientes - Renta - En proceso"
        ok (get-session-id)
        js (casas-view-script)
        row (get-row cliente-id)
        rows (clientes-renta-proceso)
        content (renta-proceso-casas-view title cliente-id row rows)]
    (application title ok js content)))

(defn venta-proceso
  [_]
  (let [title "Clientes - Venta - En proceso"
        ok (get-session-id)
        rows (clientes-venta-proceso)
        js nil
        content (venta-proceso-view title rows)]
    (application title ok js content)))

(defn venta-proceso-casas
  [cliente-id]
  (let [title "Clientes - Renta - En proceso"
        ok (get-session-id)
        js (casas-view-script)
        row (get-row cliente-id)
        rows (clientes-venta-proceso)
        content (venta-proceso-casas-view title cliente-id row rows)]
    (application title ok js content)))

(defn renta-regresar
  [casa-id proceso-id]
  (let [id (crud-fix-id casa-id)
        crow {:status "A"}
        result (Update db :casas crow ["id = ?" id])
        result1 (Delete db :proceso ["id = ?" proceso-id])]
    (if (and
         seq result
         seq result1)
      (error-404 "Casa se regreso correctamente!" "/renta")
      (error-404 "Hubo errores, la casa no se pudo regresar" "/renta"))))

(defn venta-regresar
  [casa-id proceso-id]
  (let [id (crud-fix-id casa-id)
        crow {:status "A"}
        result (Update db :casas crow ["id = ?" id])
        result1 (Delete db :proceso ["id = ?" proceso-id])]
    (if (and
         seq result
         seq result1)
      (error-404 "Casa se regreso correctamente!" "/venta")
      (error-404 "Hubo errores, la casa no se pudo regresar" "/venta"))))

(comment
  (casas-view "testing" 4 (get-clientes)))
