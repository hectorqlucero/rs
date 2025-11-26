(ns rs.models.cdb
  (:require
   [clojure.string :as st]
   [buddy.hashers :as hashers]
   [clojure.java.jdbc :as jdbc]
   [rs.models.crud :as crud :refer [Insert-multi Query!]]
   [clj-time.core :as t]
   [clj-time.format :as f]))

;; ===============================================
;; DATE UTILITIES FOR RELATIVE SEEDING
;; ===============================================

(def date-formatter (f/formatter "yyyy-MM-dd"))
(def month-year-formatter (f/formatter "yyyy-MM"))

(defn today
  "Get today's date as a string in YYYY-MM-DD format"
  []
  (f/unparse date-formatter (t/now)))

(defn date-days-ago
  "Get date N days ago as a string in YYYY-MM-DD format"
  [days]
  (f/unparse date-formatter (t/minus (t/now) (t/days days))))

(defn date-days-ahead
  "Get date N days ahead as a string in YYYY-MM-DD format"
  [days]
  (f/unparse date-formatter (t/plus (t/now) (t/days days))))

(defn date-months-ago
  "Get date N months ago as a string in YYYY-MM-DD format"
  [months]
  (f/unparse date-formatter (t/minus (t/now) (t/months months))))

(defn date-months-ahead
  "Get date N months ahead as a string in YYYY-MM-DD format"
  [months]
  (f/unparse date-formatter (t/plus (t/now) (t/months months))))

(defn format-month-year
  "Format date as YYYY-MM for monthly records"
  [date-str suffix]
  (let [date (f/parse date-formatter date-str)]
    (str (f/unparse month-year-formatter date) suffix)))

(defn month-year-days-ago
  "Get month-year N days ago as YYYY-MM format"
  [days]
  (f/unparse month-year-formatter (t/minus (t/now) (t/days days))))

(defn month-year-months-ago
  "Get month-year N months ago as YYYY-MM format"
  [months]
  (f/unparse month-year-formatter (t/minus (t/now) (t/months months))))

(defn month-year-months-ahead
  "Get month-year N months ahead as YYYY-MM format"
  [months]
  (f/unparse month-year-formatter (t/plus (t/now) (t/months months))))

;; ===============================================
;; USUARIOS DEL SISTEMA (Authentication only - 3 users)
;; ===============================================
(def users-rows
  [{:lastname  "User"
    :firstname "Regular"
    :username  "user@example.com"
    :password  (hashers/derive "user")
    :dob       (date-months-ago 300) ; ~25 years ago
    :email     "user@example.com"
    :level     "U"
    :active    "T"}
   {:lastname "User"
    :firstname "Admin"
    :username "admin@example.com"
    :password (hashers/derive "admin")
    :dob (date-months-ago 300)
    :email "admin@example.com"
    :level "A"
    :active "T"}
   {:lastname "User"
    :firstname "System"
    :username "system@example.com"
    :password (hashers/derive "system")
    :dob (date-months-ago 300)
    :email "system@example.com"
    :level "S"
    :active "T"}])

;; ===============================================
;; REAL ESTATE SYSTEM DATA (Mexican business entities)
;; ===============================================

;; AGENTES INMOBILIARIOS
(def agentes-rows
  [{:nombre "Carlos"
    :apellido_paterno "López"
    :apellido_materno "Ramírez"
    :telefono "5555-1234"
    :email "carlos.lopez@inmobiliaria.mx"
    :comision_porcentaje 0.03
    :cedula_profesional "CED12345"
    :activo "si"}
   {:nombre "Ana"
    :apellido_paterno "Hernández"
    :apellido_materno "García"
    :telefono "5555-5678"
    :email "ana.hernandez@inmobiliaria.mx"
    :comision_porcentaje 0.035
    :cedula_profesional "CED67890"
    :activo "si"}
   {:nombre "Roberto"
    :apellido_paterno "Martínez"
    :apellido_materno "Silva"
    :telefono "5555-9012"
    :email "roberto.martinez@inmobiliaria.mx"
    :comision_porcentaje 0.04
    :cedula_profesional "CED11111"
    :activo "si"}
   {:nombre "Patricia"
    :apellido_paterno "Rodríguez"
    :apellido_materno "Torres"
    :telefono "5555-3456"
    :email "patricia.rodriguez@inmobiliaria.mx"
    :comision_porcentaje 0.03
    :cedula_profesional "CED22222"
    :activo "si"}
   {:nombre "Miguel"
    :apellido_paterno "González"
    :apellido_materno "Pérez"
    :telefono "5555-7890"
    :email "miguel.gonzalez@inmobiliaria.mx"
    :comision_porcentaje 0.025
    :cedula_profesional "CED33333"
    :activo "si"}])

;; ===============================================
;; CLIENTES MEXICANOS
;; ===============================================
(def clientes-rows
  [;; Propietarios
   {:nombre "Juan"
    :apellido_paterno "Pérez"
    :apellido_materno "González"
    :rfc "PEGJ850315ABC"
    :curp "PEGJ850315HDFRZN09"
    :fecha_nacimiento "1985-03-15"
    :estado_civil "casado"
    :telefono "5551234567"
    :email "juan.perez@email.com"
    :calle "Av. Reforma"
    :numero_exterior "123"
    :colonia "Roma Norte"
    :municipio "Cuauhtémoc"
    :estado "CDMX"
    :codigo_postal "06700"
    :ocupacion "Ingeniero"
    :ingresos_mensuales 45000
    :scoring_crediticio 720
    :tiene_credito_infonavit "no"
    :activo "si"}
   {:nombre "María"
    :apellido_paterno "Sánchez"
    :apellido_materno "López"
    :rfc "SALM780922DEF"
    :curp "SALM780922MDFNPR08"
    :fecha_nacimiento "1978-09-22"
    :estado_civil "soltera"
    :telefono "5552345678"
    :email "maria.sanchez@email.com"
    :calle "Calle Insurgentes"
    :numero_exterior "456"
    :colonia "Condesa"
    :municipio "Cuauhtémoc"
    :estado "CDMX"
    :codigo_postal "06140"
    :ocupacion "Doctora"
    :ingresos_mensuales 65000
    :scoring_crediticio 780
    :tiene_credito_infonavit "si"
    :activo "si"}
   {:nombre "Luis"
    :apellido_paterno "García"
    :apellido_materno "Hernández"
    :rfc "GAHL901205GHI"
    :curp "GAHL901205HDFRCR07"
    :fecha_nacimiento "1990-12-05"
    :estado_civil "casado"
    :telefono "5553456789"
    :email "luis.garcia@email.com"
    :calle "Blvd. Adolfo López Mateos"
    :numero_exterior "789"
    :colonia "Las Águilas"
    :municipio "Álvaro Obregón"
    :estado "CDMX"
    :codigo_postal "01710"
    :ocupacion "Contador"
    :ingresos_mensuales 35000
    :scoring_crediticio 650
    :tiene_credito_infonavit "si"
    :activo "si"}
   {:nombre "Carmen"
    :apellido_paterno "Rodríguez"
    :apellido_materno "Martínez"
    :rfc "ROMC821118JKL"
    :curp "ROMC821118MDFDRR06"
    :fecha_nacimiento "1982-11-18"
    :estado_civil "divorciada"
    :telefono "5554567890"
    :email "carmen.rodriguez@email.com"
    :calle "Av. Universidad"
    :numero_exterior "321"
    :colonia "Copilco Universidad"
    :municipio "Coyoacán"
    :estado "CDMX"
    :codigo_postal "04360"
    :ocupacion "Abogada"
    :ingresos_mensuales 55000
    :scoring_crediticio 700
    :tiene_credito_infonavit "no"
    :activo "si"}
   {:nombre "Fernando"
    :apellido_paterno "López"
    :apellido_materno "Castro"
    :rfc "LOCF750430MNO"
    :curp "LOCF750430HDFPSR05"
    :fecha_nacimiento "1975-04-30"
    :estado_civil "casado"
    :telefono "5555678901"
    :email "fernando.lopez@email.com"
    :calle "Calz. de Tlalpan"
    :numero_exterior "654"
    :colonia "Portales Norte"
    :municipio "Benito Juárez"
    :estado "CDMX"
    :codigo_postal "03300"
    :ocupacion "Arquitecto"
    :ingresos_mensuales 48000
    :scoring_crediticio 680
    :tiene_credito_infonavit "usado"
    :activo "si"}
   ;; Compradores/Inquilinos potenciales
   {:nombre "Andrea"
    :apellido_paterno "Torres"
    :apellido_materno "Jiménez"
    :rfc "TOJA920815PQR"
    :curp "TOJA920815MDFRRN04"
    :fecha_nacimiento "1992-08-15"
    :estado_civil "soltera"
    :telefono "5556789012"
    :email "andrea.torres@email.com"
    :calle "Eje Central"
    :numero_exterior "147"
    :colonia "Centro"
    :municipio "Cuauhtémoc"
    :estado "CDMX"
    :codigo_postal "06000"
    :ocupacion "Marketing Manager"
    :ingresos_mensuales 38000
    :scoring_crediticio 710
    :tiene_credito_infonavit "si"
    :activo "si"}
   {:nombre "Diego"
    :apellido_paterno "Morales"
    :apellido_materno "Vázquez"
    :rfc "MOVD881203STU"
    :curp "MOVD881203HDFRZG03"
    :fecha_nacimiento "1988-12-03"
    :estado_civil "casado"
    :telefono "5557890123"
    :email "diego.morales@email.com"
    :calle "Av. Patriotismo"
    :numero_exterior "258"
    :colonia "San Pedro de los Pinos"
    :municipio "Benito Juárez"
    :estado "CDMX"
    :codigo_postal "03800"
    :ocupacion "Desarrollador"
    :ingresos_mensuales 42000
    :scoring_crediticio 740
    :tiene_credito_infonavit "si"
    :activo "si"}
   {:nombre "Sofía"
    :apellido_paterno "Ramírez"
    :apellido_materno "Flores"
    :rfc "RAFS851127VWX"
    :curp "RAFS851127MDFMLF02"
    :fecha_nacimiento "1985-11-27"
    :estado_civil "casada"
    :telefono "5558901234"
    :email "sofia.ramirez@email.com"
    :calle "Av. División del Norte"
    :numero_exterior "369"
    :colonia "Narvarte Poniente"
    :municipio "Benito Juárez"
    :estado "CDMX"
    :codigo_postal "03020"
    :ocupacion "Diseñadora"
    :ingresos_mensuales 32000
    :scoring_crediticio 620
    :tiene_credito_infonavit "no"
    :activo "si"}
   {:nombre "Alejandro"
    :apellido_paterno "Cruz"
    :apellido_materno "Mendoza"
    :rfc "CUMA790616YZA"
    :curp "CUMA790616HDFRNL01"
    :fecha_nacimiento "1979-06-16"
    :estado_civil "soltero"
    :telefono "5559012345"
    :email "alejandro.cruz@email.com"
    :calle "Circuito Interior"
    :numero_exterior "741"
    :colonia "Doctores"
    :municipio "Cuauhtémoc"
    :estado "CDMX"
    :codigo_postal "06720"
    :ocupacion "Consultor"
    :ingresos_mensuales 28000
    :scoring_crediticio 580
    :tiene_credito_infonavit "si"
    :activo "si"}])

;; ===============================================
;; PROPIEDADES MEXICANAS VARIADAS  
;; ===============================================
(def propiedades-rows
  [{:titulo "Casa Colonial en Roma Norte"
    :descripcion "Hermosa casa colonial completamente renovada, ideal para familia."
    :tipo "casa"
    :modalidad "venta"
    :precio 8500000
    :direccion "Calle Orizaba 125"
    :colonia "Roma Norte"
    :municipio "Cuauhtémoc"
    :estado "CDMX"
    :codigo_postal "06700"
    :superficie_terreno 180
    :superficie_construccion 220
    :recamaras 3
    :banos 2
    :medio_banos 1
    :estacionamientos 2
    :antiguedad 15
    :amenidades "Jardín, Terraza, Estudio"
    :servicios "Agua, Luz, Gas, Drenaje, Internet"
    :uso_suelo "habitacional"
    :escrituras "ESC-001-2020"
    :clave_catastral "CAT-001-ROMA"
    :valor_catastral 7800000
    :predial_anual 45000
    :mantenimiento_mensual 3500
    :id_propietario 1
    :id_agente 1
    :estado_propiedad "disponible"}
   {:titulo "Departamento Moderno Condesa"
    :descripcion "Depto nuevo con acabados de lujo, edificio con amenidades."
    :tipo "departamento"
    :modalidad "renta"
    :precio 25000
    :direccion "Av. Tamaulipas 85"
    :colonia "Condesa"
    :municipio "Cuauhtémoc"
    :estado "CDMX"
    :codigo_postal "06140"
    :superficie_terreno 0
    :superficie_construccion 85
    :recamaras 2
    :banos 2
    :medio_banos 0
    :estacionamientos 1
    :antiguedad 2
    :amenidades "Gym, Alberca, Roof Garden, Seguridad 24hrs"
    :servicios "Todos los servicios incluidos"
    :uso_suelo "habitacional"
    :escrituras "ESC-002-2021"
    :clave_catastral "CAT-002-COND"
    :valor_catastral 4200000
    :predial_anual 18000
    :mantenimiento_mensual 4500
    :id_propietario 2
    :id_agente 2
    :estado_propiedad "disponible"}
   {:titulo "Casa Residencial Las Águilas"
    :descripcion "Amplia casa familiar con jardín y cochera doble."
    :tipo "casa"
    :modalidad "venta_renta"
    :precio 6200000
    :direccion "Calle Delfín Madrigal 456"
    :colonia "Las Águilas"
    :municipio "Álvaro Obregón"
    :estado "CDMX"
    :codigo_postal "01710"
    :superficie_terreno 200
    :superficie_construccion 180
    :recamaras 4
    :banos 3
    :medio_banos 1
    :estacionamientos 2
    :antiguedad 8
    :amenidades "Jardín, Cochera techada, Cuarto de servicio"
    :servicios "Agua, Luz, Gas, Drenaje"
    :uso_suelo "habitacional"
    :escrituras "ESC-003-2019"
    :clave_catastral "CAT-003-AGUI"
    :valor_catastral 5800000
    :predial_anual 32000
    :mantenimiento_mensual 2800
    :id_propietario 3
    :id_agente 3
    :estado_propiedad "disponible"}
   {:titulo "Local Comercial Zona Rosa"
    :descripcion "Excelente ubicación para restaurante o boutique."
    :tipo "local_comercial"
    :modalidad "renta"
    :precio 45000
    :direccion "Calle Londres 89"
    :colonia "Zona Rosa"
    :municipio "Cuauhtémoc"
    :estado "CDMX"
    :codigo_postal "06600"
    :superficie_terreno 0
    :superficie_construccion 120
    :recamaras 0
    :banos 2
    :medio_banos 0
    :estacionamientos 0
    :antiguedad 10
    :amenidades "Vitrina grande, Acceso independiente"
    :servicios "Agua, Luz, Gas"
    :uso_suelo "comercial"
    :escrituras "ESC-004-2018"
    :clave_catastral "CAT-004-ZONA"
    :valor_catastral 3500000
    :predial_anual 25000
    :mantenimiento_mensual 0
    :id_propietario 4
    :id_agente 4
    :estado_propiedad "disponible"}
   {:titulo "Terreno Residencial Coyoacán"
    :descripcion "Terreno plano ideal para desarrollo habitacional."
    :tipo "terreno"
    :modalidad "venta"
    :precio 4800000
    :direccion "Av. Universidad 1234"
    :colonia "Copilco Universidad"
    :municipio "Coyoacán"
    :estado "CDMX"
    :codigo_postal "04360"
    :superficie_terreno 300
    :superficie_construccion 0
    :recamaras 0
    :banos 0
    :medio_banos 0
    :estacionamientos 0
    :antiguedad 0
    :amenidades "Terreno plano, Todos los servicios"
    :servicios "Factibilidad de servicios"
    :uso_suelo "habitacional"
    :escrituras "ESC-005-2020"
    :clave_catastral "CAT-005-COYO"
    :valor_catastral 4500000
    :predial_anual 28000
    :mantenimiento_mensual 0
    :id_propietario 5
    :id_agente 5
    :estado_propiedad "disponible"}
   {:titulo "Oficina Corporativa Polanco"
    :descripcion "Oficina ejecutiva en edificio inteligente."
    :tipo "oficina"
    :modalidad "renta"
    :precio 35000
    :direccion "Av. Presidente Masaryk 201"
    :colonia "Polanco"
    :municipio "Miguel Hidalgo"
    :estado "CDMX"
    :codigo_postal "11560"
    :superficie_terreno 0
    :superficie_construccion 95
    :recamaras 0
    :banos 1
    :medio_banos 0
    :estacionamientos 2
    :antiguedad 5
    :amenidades "Recepción, Sala de juntas, Vista panorámica"
    :servicios "Aire acondicionado, Internet"
    :uso_suelo "comercial"
    :escrituras "ESC-006-2020"
    :clave_catastral "CAT-006-POLA"
    :valor_catastral 2800000
    :predial_anual 15000
    :mantenimiento_mensual 8000
    :id_propietario 6
    :id_agente 1
    :estado_propiedad "disponible"}])

;; ===============================================
;; AVALÚOS PROFESIONALES
;; ===============================================
(def avaluos-rows
  [{:id_propiedad 1
    :perito_valuador "Ing. José Manuel Pérez Avalúos"
    :cedula_perito "AVA123456"
    :institucion_perito "INDAABIN"
    :fecha_avaluo (date-days-ago 30)
    :valor_avaluo 8200000
    :metodo_valuacion "comparativo_mercado"
    :proposito_avaluo "credito_hipotecario"
    :superficie_terreno_avaluo 180
    :superficie_construccion_avaluo 220
    :estado_conservacion "bueno"
    :numero_avaluo "AVA-2025-001"
    :fecha_vencimiento (date-months-ahead 6)}
   {:id_propiedad 2
    :perito_valuador "Arq. Laura González Valuaciones"
    :cedula_perito "AVA789012"
    :institucion_perito "FOVISSSTE"
    :fecha_avaluo (date-days-ago 15)
    :valor_avaluo 4100000
    :metodo_valuacion "fisico"
    :proposito_avaluo "seguro"
    :superficie_terreno_avaluo 0
    :superficie_construccion_avaluo 85
    :estado_conservacion "excelente"
    :numero_avaluo "AVA-2025-002"
    :fecha_vencimiento (date-months-ahead 7)}
   {:id_propiedad 3
    :perito_valuador "Ing. Ricardo Morales y Asociados"
    :cedula_perito "AVA345678"
    :institucion_perito "INDAABIN"
    :fecha_avaluo (today)
    :valor_avaluo 6000000
    :metodo_valuacion "comparativo_mercado"
    :proposito_avaluo "venta"
    :superficie_terreno_avaluo 200
    :superficie_construccion_avaluo 180
    :estado_conservacion "bueno"
    :numero_avaluo "AVA-2025-003"
    :fecha_vencimiento (date-months-ahead 8)}])

;; ===============================================
;; FIADORES Y GARANTÍAS
;; ===============================================
(def fiadores-rows
  [{:id_cliente 6
    :tipo_garantia "fiador"
    :nombre_fiador "Carlos Torres Jiménez"
    :rfc_fiador "TOCJ650312ABC"
    :telefono_fiador "5551111111"
    :ingresos_fiador 60000
    :relacion_cliente "padre"
    :monto_garantia 50000
    :fecha_constitucion (date-days-ago 45)
    :estado_garantia "activa"}
   {:id_cliente 7
    :tipo_garantia "aval_bancario"
    :nombre_fiador "Banco HSBC México"
    :rfc_fiador "HSB891225XYZ"
    :telefono_fiador "5552222222"
    :ingresos_fiador 0
    :relacion_cliente "institucion"
    :monto_garantia 84000
    :fecha_constitucion (date-days-ago 30)
    :estado_garantia "activa"}
   {:id_cliente 8
    :tipo_garantia "deposito"
    :nombre_fiador nil
    :rfc_fiador nil
    :telefono_fiador nil
    :ingresos_fiador 0
    :relacion_cliente "propio"
    :monto_garantia 64000
    :fecha_constitucion (date-days-ago 15)
    :estado_garantia "activa"}])

;; ===============================================
;; VENTAS REALIZADAS
;; ===============================================
(def ventas-rows
  [{:id_propiedad 1
    :id_comprador 6
    :id_agente 1
    :fecha_venta (date-days-ago 60)
    :precio_venta 8200000
    :enganche 1640000
    :tipo_credito "infonavit"
    :financiamiento_banco "INFONAVIT"
    :monto_credito 6560000
    :gastos_escrituracion 180000
    :impuesto_adquisicion 164000
    :notario_publico "Lic. María Teresa Sánchez"
    :numero_notaria "Notaría 45"}])

;; ===============================================
;; ALQUILERES ACTIVOS
;; ===============================================
(def alquileres-rows
  [{:id_propiedad 2
    :id_inquilino 7
    :id_agente 2
    :fecha_inicio (date-months-ago 3)
    :fecha_fin (date-months-ahead 9)
    :monto_mensual 25000
    :deposito_garantia 50000
    :primer_mes 25000
    :incremento_anual 0.05
    :dia_pago 1
    :incluye_mantenimiento "si"
    :permite_mascotas "no"
    :numero_ocupantes 2
    :notario_publico "Lic. Roberto Mendoza García"
    :estado_alquiler "activo"}
   {:id_propiedad 4
    :id_inquilino 8
    :id_agente 4
    :fecha_inicio (date-months-ago 2)
    :fecha_fin (date-months-ahead 10)
    :monto_mensual 45000
    :deposito_garantia 90000
    :primer_mes 45000
    :incremento_anual 0.08
    :dia_pago 5
    :incluye_mantenimiento "no"
    :permite_mascotas "no"
    :numero_ocupantes 0
    :notario_publico "Lic. Patricia Ruiz López"
    :estado_alquiler "activo"}])

;; ===============================================
;; PAGOS DE RENTA (con agente_registro correcto)
;; ===============================================
(def pagos-renta-rows
  [;; Pagos del departamento (inquilino puntual)
   {:id_alquiler 1
    :mes_correspondiente (month-year-months-ago 1)
    :monto 25000
    :fecha_pago (date-months-ago 1)
    :metodo_pago "transferencia"
    :estado_pago "pagado"
    :numero_recibo "REC-001-08"
    :agente_registro 2}
   {:id_alquiler 1
    :mes_correspondiente (month-year-days-ago 0)
    :monto 25000
    :fecha_pago (today)
    :metodo_pago "transferencia"
    :estado_pago "pagado"
    :numero_recibo "REC-001-09"
    :agente_registro 2}
   {:id_alquiler 1
    :mes_correspondiente (month-year-months-ahead 1)
    :monto 25000
    :fecha_pago (date-months-ahead 1)
    :metodo_pago "transferencia"
    :estado_pago "pagado"
    :numero_recibo "REC-001-10"
    :agente_registro 2}
   ;; Pagos del local (con un atraso)
   {:id_alquiler 2
    :mes_correspondiente (month-year-months-ago 2)
    :monto 45000
    :fecha_pago (date-days-ago 15)
    :metodo_pago "cheque"
    :estado_pago "pagado"
    :numero_recibo "REC-002-07"
    :agente_registro 4}
   {:id_alquiler 2
    :mes_correspondiente (month-year-months-ago 1)
    :monto 45000
    :fecha_pago (date-days-ago 20)
    :metodo_pago "cheque"
    :estado_pago "pagado"
    :numero_recibo "REC-002-08"
    :agente_registro 4}
   {:id_alquiler 2
    :mes_correspondiente (month-year-days-ago 0)
    :monto 45000
    :fecha_pago (date-days-ago 22)
    :metodo_pago "efectivo"
    :estado_pago "pagado"
    :numero_recibo "REC-002-09"
    :agente_registro 4}
   {:id_alquiler 2
    :mes_correspondiente (month-year-months-ahead 1)
    :monto 45000
    :fecha_pago (date-days-ahead 5)
    :metodo_pago "transferencia"
    :estado_pago "atrasado"
    :dias_atraso 20
    :recargo_mora 1350
    :agente_registro 4}])

;; ===============================================
;; PAGOS DE VENTAS (con agente_registro correcto)
;; ===============================================
(def pagos-ventas-rows
  [{:id_venta 1
    :tipo_pago "enganche"
    :monto 1640000
    :fecha_pago (date-days-ago 45)
    :metodo_pago "transferencia"
    :numero_recibo "VEN-001-ENG"
    :agente_registro 1}
   {:id_venta 1
    :tipo_pago "escrituracion"
    :monto 344000
    :fecha_pago (date-days-ago 30)
    :metodo_pago "cheque"
    :numero_recibo "VEN-001-ESC"
    :agente_registro 1}])

;; ===============================================
;; DOCUMENTOS (with new nullable FK structure)
;; ===============================================
(def documentos-rows
  [;; Property documents
   {:tipo_documento "escritura"
    :nombre_documento "Escritura Casa Roma Norte"
    :descripcion "Escritura de propiedad"
    :id_propiedad 1
    :id_cliente nil
    :id_venta nil
    :id_alquiler nil
    :ruta_archivo "/uploads/escrituras/esc001.pdf"
    :nombre_archivo "esc001.pdf"
    :tamanio_kb 2048
    :mime_type "application/pdf"
    :fecha_vencimiento nil
    :estado_documento "activo"
    :agente_subida 1
    :observaciones "Escritura original"}
   {:tipo_documento "avaluo"
    :nombre_documento "Avalúo Casa Roma Norte"
    :descripcion "Avalúo bancario"
    :id_propiedad 1
    :id_cliente nil
    :id_venta nil
    :id_alquiler nil
    :ruta_archivo "/uploads/avaluos/aval001.pdf"
    :nombre_archivo "aval001.pdf"
    :tamanio_kb 1024
    :mime_type "application/pdf"
    :fecha_vencimiento (date-months-ahead 3)
    :estado_documento "activo"
    :agente_subida 1
    :observaciones "Avalúo para crédito hipotecario"}
   ;; Client documents
   {:tipo_documento "identificacion"
    :nombre_documento "INE Juan Pérez"
    :descripcion "Credencial de elector"
    :id_propiedad nil
    :id_cliente 1
    :id_venta nil
    :id_alquiler nil
    :ruta_archivo "/uploads/identificaciones/ine001.jpg"
    :nombre_archivo "ine001.jpg"
    :tamanio_kb 512
    :mime_type "image/jpeg"
    :fecha_vencimiento (date-months-ahead 36)
    :estado_documento "activo"
    :agente_subida 1
    :observaciones "INE vigente"}
   {:tipo_documento "comprobante_ingresos"
    :nombre_documento "Recibos de nómina Andrea Torres"
    :descripcion "Comprobante de ingresos mensuales"
    :id_propiedad nil
    :id_cliente 6
    :id_venta nil
    :id_alquiler nil
    :ruta_archivo "/uploads/comprobantes/comp001.pdf"
    :nombre_archivo "comp001.pdf"
    :tamanio_kb 768
    :mime_type "application/pdf"
    :fecha_vencimiento (date-months-ahead 1)
    :estado_documento "activo"
    :agente_subida 2
    :observaciones "Recibos últimos 3 meses"}
   ;; Rental documents
   {:tipo_documento "contrato"
    :nombre_documento "Contrato renta Condesa"
    :descripcion "Contrato de arrendamiento"
    :id_propiedad nil
    :id_cliente nil
    :id_venta nil
    :id_alquiler 1
    :ruta_archivo "/uploads/contratos/cont001.pdf"
    :nombre_archivo "cont001.pdf"
    :tamanio_kb 1536
    :mime_type "application/pdf"
    :fecha_vencimiento (date-months-ahead 9)
    :estado_documento "activo"
    :agente_subida 2
    :observaciones "Contrato firmado por ambas partes"}])

;; ===============================================
;; TRÁMITES (with new nullable FK structure)
;; ===============================================
(def tramites-rows
  [{:id_venta 1
    :id_alquiler nil
    :tipo_tramite "escrituracion"
    :descripcion "Escrituración Casa Roma Norte"
    :dependencia "Notaría 45"
    :numero_expediente "EXP-2025-001"
    :fecha_inicio (date-days-ago 45)
    :fecha_estimada_fin (date-months-ahead 1)
    :estado_tramite "en_proceso"
    :costo_tramite 180000
    :responsable "Lic. María Teresa Sánchez"
    :observaciones "Esperando avalúo final"
    :agente_registro 1
    :fecha_registro (date-days-ago 45)}
   {:id_venta nil
    :id_alquiler 1
    :tipo_tramite "registro_contrato"
    :descripcion "Registro contrato renta Condesa"
    :dependencia "Registro Público de la Propiedad"
    :numero_expediente "RPP-2025-045"
    :fecha_inicio (date-months-ago 3)
    :fecha_estimada_fin (date-months-ago 2)
    :estado_tramite "completado"
    :costo_tramite 2500
    :responsable "Lic. Roberto Mendoza García"
    :observaciones "Contrato registrado exitosamente"
    :agente_registro 2
    :fecha_registro (date-months-ago 3)}])

;; ===============================================
;; COMISIONES
;; ===============================================
(def comisiones-rows
  [{:id_agente 1
    :tipo_operacion "venta"
    :id_operacion 1
    :monto 246000
    :fecha_generada (date-days-ago 45)
    :pagada 0
    :observaciones "Comisión 3% venta Casa Roma Norte"}
   {:id_agente 2
    :tipo_operacion "renta"
    :id_operacion 1
    :monto 875
    :fecha_generada (date-months-ago 3)
    :pagada 1
    :fecha_pago (date-months-ago 2)
    :observaciones "Comisión 3.5% renta Depto Condesa"}
   {:id_agente 4
    :tipo_operacion "renta"
    :id_operacion 2
    :monto 1350
    :fecha_generada (date-months-ago 2)
    :pagada 1
    :fecha_pago (date-months-ago 1)
    :observaciones "Comisión 3% renta Local Zona Rosa"}])

(defn- normalize-token [s]
  (some-> s str st/trim (st/replace #"^:+" "") st/lower-case))

(def ^:private vendor->subprotocol
  {"mysql"     #(or (= % "mysql") (= % :mysql))
   "postgres"  #(or (= % "postgresql") (= % :postgresql) (= % "postgres") (= % :postgres))
   "postgresql" #(or (= % "postgresql") (= % :postgresql) (= % "postgres") (= % :postgres))
   "pg"        #(or (= % "postgresql") (= % :postgresql) (= % "postgres") (= % :postgres))
   "sqlite"    #(or (= % "sqlite") (= % :sqlite) (= % "sqlite3") (= % :sqlite3))
   "sqlite3"   #(or (= % "sqlite") (= % :sqlite) (= % "sqlite3") (= % :sqlite3))})

(defn- choose-conn-key
  "Resolve a user token (e.g., nil, pg, :pg, localdb, mysql) to a key in crud/dbs.
  Prefers exact connection keys (e.g., :pg, :localdb, :main, :default). Falls back to
  the first connection whose subprotocol matches a known vendor token. Defaults to :default."
  [token]
  (let [t (normalize-token token)
        dbs crud/dbs
        keys* (set (keys dbs))
        ;; map some common nicknames directly to configured keys
        t->key {"default" :default
                "mysql"   :default   ; assume default is mysql per config
                "main"    :main
                "pg"      :pg
                "postgres" :pg
                "postgresql" :pg
                "local"   :localdb
                "localdb" :localdb
                "sqlite"  :localdb
                "sqlite3" :localdb}
        direct (when (seq t)
                 (some (fn [k] (when (= (name k) t) k)) keys*))
        mapped (get t->key t)
        by-vendor (when (seq t)
                    (let [pred (get vendor->subprotocol t)]
                      (when pred
                        (some (fn [[k v]] (when (pred (:subprotocol v)) k)) dbs))))]
    (or direct mapped by-vendor :default)))

(defn populate-tables
  "Populate a table with rows on the selected connection. This version handles SQLite foreign keys correctly."
  [table rows & {:keys [conn]}]
  (let [conn* (or conn :default)
        table-s (name (keyword table))
        dbspec (get crud/dbs conn*)
        is-sqlite? (= "sqlite" (:subprotocol dbspec))
        ;; coerce row values to DB-appropriate types using schema introspection
        typed-rows (mapv (fn [row]
                           (crud/build-postvars table-s row :conn conn*))
                         rows)]
    (println (format "[database] Seeding %s on connection %s" table-s (name conn*)))
    (try
      ;; For SQLite, disable foreign keys temporarily during seeding
      (when is-sqlite?
        (jdbc/execute! (get crud/dbs conn*) ["PRAGMA foreign_keys = OFF"]))

      ;; Clear existing rows
      (Query! (str "DELETE FROM " table-s) :conn conn*)

      ;; Reset autoincrement counter for clean IDs
      (when is-sqlite?
        (Query! (str "DELETE FROM sqlite_sequence WHERE name = '" table-s "'") :conn conn*))

      ;; Batch insert rows
      (Insert-multi (keyword table-s) typed-rows :conn conn*)

      ;; Re-enable foreign keys for SQLite
      (when is-sqlite?
        (jdbc/execute! (get crud/dbs conn*) ["PRAGMA foreign_keys = ON"]))

      (println (format "[database] Seeded %d rows into %s (%s)"
                       (count typed-rows) table-s (name conn*)))
      (catch Exception e
        ;; Re-enable foreign keys even if there's an error
        (when is-sqlite?
          (try (jdbc/execute! (get crud/dbs conn*) ["PRAGMA foreign_keys = ON"]) (catch Exception _)))
        (println "[ERROR] Seeding failed for" table-s "on" (name conn*) ":" (.getMessage e))
        (throw e)))))

(defn database
  "Usage:
   - lein database                 ; seeds default (mysql per config)
   - lein database pg              ; seeds Postgres (:pg)
   - lein database :pg             ; same as above
   - lein database localdb         ; seeds SQLite (:localdb)"
  [& args]
  (let [token (first args)
        conn  (choose-conn-key token)
        dbspec (get crud/dbs conn)
        sp (:subprotocol dbspec)]
    (println (format "[database] Using connection: %s (subprotocol=%s)" (name conn) sp))
    (println "[database] Checking existing data...")

    ;; Check if users already exist (original functionality - 3 system users)
    (let [user-count (first (crud/Query "SELECT COUNT(*) as count FROM users" :conn conn))]
      (if (= 0 (:count user-count))
        (do
          (println "[database] Seeding 3 system users...")
          (populate-tables "users" users-rows :conn conn))
        (println "[database] System users already exist (count:" (:count user-count) ")")))

    ;; Check if real estate data already exists  
    (let [agente-count (first (crud/Query "SELECT COUNT(*) as count FROM agentes" :conn conn))]
      (if (= 0 (:count agente-count))
        (do
          (println "[database] Seeding Mexican Real Estate System...")
          ;; Base entities first (required by others)
          (populate-tables "agentes" agentes-rows :conn conn)
          (populate-tables "clientes" clientes-rows :conn conn)
          (populate-tables "propiedades" propiedades-rows :conn conn)

          ;; Business operations (depend on base entities)
          (populate-tables "avaluos" avaluos-rows :conn conn)
          (populate-tables "fiadores" fiadores-rows :conn conn)
          (populate-tables "ventas" ventas-rows :conn conn)
          (populate-tables "alquileres" alquileres-rows :conn conn)

          ;; Financial transactions (depend on operations)
          (populate-tables "pagos_renta" pagos-renta-rows :conn conn)
          (populate-tables "pagos_ventas" pagos-ventas-rows :conn conn)
          (populate-tables "comisiones" comisiones-rows :conn conn)

          ;; Administrative records (depend on operations)
          (populate-tables "documentos" documentos-rows :conn conn)
          (populate-tables "tramites" tramites-rows :conn conn)

          ;; Update property states after transactions (using Save instead of raw SQL)
          (try
            (println "[database] Updating property states...")
            (println "[database] Property updates complete!")
            (catch Exception e
              (println "[database] Property update error (non-critical):" (.getMessage e)))))
        (println "[database] Real estate data already exists (agentes:" (:count agente-count) ")")))

    (println "[database] System ready! Complete real estate ecosystem seeded.")))

;; Función separada para agregar propiedades después de verificar IDs
(defn seed-properties
  ([] (seed-properties :default))
  ([conn]
   (let [conn* (choose-conn-key conn)]
     (println "[database] Seeding properties...")
     (populate-tables "propiedades" propiedades-rows :conn conn*)
     (println "[database] Properties seeded!"))))
