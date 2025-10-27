(ns rs.handlers.vista_propiedades_disponibles.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_propiedades_disponibles-view
  [title rows]
  (let [labels ["Título" "Tipo" "Precio" "Municipio" "Días Publicado"]
        db-fields [:titulo :tipo :precio :municipio :dias_publicado]
        fields (apply array-map (interleave db-fields labels))
        table-id "vista_propiedades_disponibles_table"]
    (build-dashboard title rows table-id fields)))
