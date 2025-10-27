(ns rs.handlers.vista_contratos_renta_activos.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_contratos_renta_activos-view
  [title rows]
  (let [labels ["Propiedad" "Direccion" "Colonia" "Municipio" "Estado propiedad" "Inquilino" "Telefono inquilino" "Email inquilino" "Rfc inquilino" "Fecha inicio" "Fecha fin" "Monto mensual" "Deposito garantia" "Incremento anual" "Dia pago" "Incluye mantenimiento" "Incluye servicios" "Permite mascotas" "Numero ocupantes" "Uso permitido" "Agente" "Telefono agente" "Dias restantes contrato" "Ultimo pago" "Estado ultimo pago" "Dias atraso" "Recargo mora" "Nombre fiador" "Telefono fiador" "Tipo garantia" "Monto garantia" "Notario publico" "Numero notaria"]
        db-fields [:propiedad :direccion :colonia :municipio :estado_propiedad :inquilino :telefono_inquilino :email_inquilino :rfc_inquilino :fecha_inicio :fecha_fin :monto_mensual :deposito_garantia :incremento_anual :dia_pago :incluye_mantenimiento :incluye_servicios :permite_mascotas :numero_ocupantes :uso_permitido :agente :telefono_agente :dias_restantes_contrato :ultimo_pago :estado_ultimo_pago :dias_atraso :recargo_mora :nombre_fiador :telefono_fiador :tipo_garantia :monto_garantia :notario_publico :numero_notaria]
        fields (apply array-map (interleave db-fields labels))
        table-id "vista_contratos_renta_activos_table"]
    (build-dashboard title rows table-id fields)))
