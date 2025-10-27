(ns rs.handlers.vista_clientes_completa.view
  (:require [rs.models.grid :refer [build-dashboard]]))

(defn vista_clientes_completa-view
  [title rows]
  (let [labels ["Nombre" "Apellido paterno" "Apellido materno" "Nombre completo" "Rfc" "Curp" "Fecha nacimiento" "Edad" "Estado civil" "Nacionalidad" "Identificacion oficial" "Numero identificacion" "Telefono" "Email" "Direccion completa" "Ocupacion" "Empresa trabajo" "Telefono trabajo" "Ingresos mensuales" "Otros ingresos" "Ingresos totales" "Scoring crediticio" "Calificacion crediticia" "Banco principal" "Tiene credito infonavit" "Numero infonavit" "Referencia personal nombre" "Referencia personal telefono" "Referencia comercial nombre" "Referencia comercial telefono" "Fecha registro" "Activo" "Total propiedades" "Contratos renta" "Total compras"]
        db-fields [:nombre :apellido_paterno :apellido_materno :nombre_completo :rfc :curp :fecha_nacimiento :edad :estado_civil :nacionalidad :identificacion_oficial :numero_identificacion :telefono :email :direccion_completa :ocupacion :empresa_trabajo :telefono_trabajo :ingresos_mensuales :otros_ingresos :ingresos_totales :scoring_crediticio :calificacion_crediticia :banco_principal :tiene_credito_infonavit :numero_infonavit :referencia_personal_nombre :referencia_personal_telefono :referencia_comercial_nombre :referencia_comercial_telefono :fecha_registro :activo :total_propiedades :contratos_renta :total_compras]
        fields (apply array-map (interleave db-fields labels))
        table-id "vista_clientes_completa_table"]
    (build-dashboard title rows table-id fields)))
