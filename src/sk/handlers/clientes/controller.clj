(ns sk.handlers.clientes.controller
  (:require [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]
            [sk.handlers.clientes.model :refer [get-clientes
                                                clientes-renta
                                                clientes-venta
                                                get-row]]
            [sk.handlers.clientes.view :refer [clientes-view
                                               renta-view
                                               renta-casas-view
                                               clientes-activos-view
                                               casas-view
                                               casas-view-script]]))

(defn clientes [_]
  (let [title "Clientes"
        ok (get-session-id)
        js nil
        rows (get-clientes)
        content (clientes-view title rows)]
    (application title ok js content)))

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

(comment
  (casas-view "testing" 4 (get-clientes)))
