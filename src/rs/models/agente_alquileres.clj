(ns rs.models.agente-alquileres
  (:require [rs.models.crud :refer [Query]]
            [clojure.string :as str]))

;; Generated on 2025-10-26T19:07:08.483-07:00

;; Get records for parent
(defn get-alquileres-for-agente [agente-id]
  (Query [(str "SELECT alq.id, alq.agente_id, alq.cliente_id, alq.propiedad_id, alq.fecha_inicio, alq.fecha_fin, alq.monto, alq.descripcion, age.descripcion AS age_descripcion, cli.descripcion AS cli_descripcion, pro.descripcion AS pro_descripcion"
               " FROM alquileres alq"
               " LEFT JOIN agentes age ON alq.agente_id = age.id LEFT JOIN clientes cli ON alq.cliente_id = cli.id LEFT JOIN propiedads pro ON alq.propiedad_id = pro.id"
               " WHERE alq.agente_id = ?") agente-id] :conn :default))

;; Field configuration
(def field-config
  [{:name "id", :type "number", :label "Id"}
   {:name "agente_id", :type "select", :label "Agente id", :options-fn "get-agentes-options"}
   {:name "cliente_id", :type "select", :label "Cliente id", :options-fn "get-clientes-options"}
   {:name "propiedad_id", :type "select", :label "Propiedad id", :options-fn "get-propiedads-options"}
   {:name "fecha_inicio", :type "date", :label "Fecha inicio"}
   {:name "fecha_fin", :type "date", :label "Fecha fin"}
   {:name "monto", :type "number", :step "0.01", :label "Monto"}
   {:name "descripcion", :type "textarea", :label "Descripcion"}])
