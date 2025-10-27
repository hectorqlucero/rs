(ns rs.handlers.vista_avaluos_vigentes.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_avaluos_vigentes-view
  [title rows]
  (let [labels ["Id propiedad" "Propiedad" "Direccion" "Municipio" "Estado" "Perito valuador" "Cedula perito" "Institucion perito" "Fecha avaluo" "Vigencia meses" "Valor avaluo" "Moneda" "Metodo valuacion" "Proposito avaluo" "Superficie terreno avaluo" "Superficie construccion avaluo" "Estado conservacion" "Numero avaluo" "Fecha vencimiento" "Dias hasta vencimiento" "Valor catastral" "Precio actual" "Diferencia porcentual catastral" "Diferencia porcentual precio"]
        db-fields [:id_propiedad :propiedad :direccion :municipio :estado :perito_valuador :cedula_perito :institucion_perito :fecha_avaluo :vigencia_meses :valor_avaluo :moneda :metodo_valuacion :proposito_avaluo :superficie_terreno_avaluo :superficie_construccion_avaluo :estado_conservacion :numero_avaluo :fecha_vencimiento :dias_hasta_vencimiento :valor_catastral :precio_actual :diferencia_porcentual_catastral :diferencia_porcentual_precio]
        fields (apply array-map (interleave db-fields labels))
        table-id "vista_avaluos_vigentes_table"]
    (build-dashboard title rows table-id fields)))
