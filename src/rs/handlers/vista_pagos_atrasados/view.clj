(ns rs.handlers.vista_pagos_atrasados.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_pagos_atrasados-view
  [title rows]
  (let [labels ["Id alquiler" "Propiedad" "Direccion" "Inquilino" "Telefono" "Rfc" "Mes correspondiente" "Monto" "Fecha pago" "Metodo pago" "Estado pago" "Dias atraso" "Recargo mora" "Total adeudo" "Agente" "Telefono agente" "Nombre fiador" "Telefono fiador" "Tipo garantia"]
        db-fields [:id_alquiler :propiedad :direccion :inquilino :telefono :rfc :mes_correspondiente :monto :fecha_pago :metodo_pago :estado_pago :dias_atraso :recargo_mora :total_adeudo :agente :telefono_agente :nombre_fiador :telefono_fiador :tipo_garantia]
        fields (apply array-map (interleave db-fields labels))
        table-id "vista_pagos_atrasados_table"]
    (build-dashboard title rows table-id fields)))
