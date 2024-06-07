(ns sk.handlers.clientes.controller
  (:require [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]
            [sk.handlers.clientes.model :refer [get-clientes
                                                get-row
                                                get-available-clientes]]
            [sk.handlers.clientes.view :refer [clientes-view
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

(defn clientes-activos [_]
  (let [title "Clientes"
        ok (get-session-id)
        rows (get-available-clientes)
        js nil
        content (clientes-activos-view title rows)]
    (application title ok js content)))

(defn get-casas [clientes_id]
  (let [title "Posibles casas"
        ok (get-session-id)
        js (casas-view-script)
        row (first (get-row clientes_id))
        rows (get-available-clientes)
        content (casas-view title clientes_id row rows)]
    (application title ok js content)))

(comment
  (casas-view "testing" 4 (get-clientes)))
