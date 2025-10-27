-- Migración completa de TODAS las vistas para bienes raíces México

-- Vista de propiedades disponibles con información completa mexicana
CREATE VIEW vista_propiedades_disponibles AS
SELECT
    p.id,
    p.titulo,
    p.descripcion,
    p.tipo,
    p.modalidad,
    p.precio,
    p.moneda,
    p.direccion,
    p.colonia,
    p.municipio,
    p.estado,
    p.codigo_postal,
    p.superficie_terreno,
    p.superficie_construccion,
    p.recamaras,
    p.banos,
    p.medio_banos,
    p.estacionamientos,
    p.niveles,
    p.antiguedad,
    p.amenidades,
    p.servicios,
    p.orientacion,
    p.uso_suelo,
    p.regimen_propiedad,
    p.escrituras,
    p.folio_real,
    p.clave_catastral,
    p.valor_catastral,
    p.predial_anual,
    p.mantenimiento_mensual,
    p.gastos_comunes,
    p.coordenadas_gps,
    p.estatus_legal,
    p.fecha_publicacion,
    p.fecha_actualizacion,
    (c.nombre || ' ' || COALESCE(c.apellido_paterno, '') || ' ' || COALESCE(c.apellido_materno, '')) AS propietario,
    c.telefono AS telefono_propietario,
    c.email AS email_propietario,
    c.rfc AS rfc_propietario,
    a.nombre AS agente,
    a.telefono AS telefono_agente,
    p.estado_propiedad,
    -- Calcular precio por m2
    CASE 
        WHEN p.superficie_construccion > 0 THEN ROUND(p.precio / p.superficie_construccion, 2)
        ELSE NULL 
    END AS precio_m2_construccion,
    CASE 
        WHEN p.superficie_terreno > 0 THEN ROUND(p.precio / p.superficie_terreno, 2)
        ELSE NULL 
    END AS precio_m2_terreno,
    -- Avalúo más reciente
    av.valor_avaluo AS avaluo_reciente,
    av.fecha_avaluo AS fecha_avaluo_reciente,
    av.perito_valuador
FROM propiedades p
JOIN clientes c ON p.id_propietario = c.id
LEFT JOIN agentes a ON p.id_agente = a.id
LEFT JOIN (
    SELECT id_propiedad, valor_avaluo, fecha_avaluo, perito_valuador,
           ROW_NUMBER() OVER (PARTITION BY id_propiedad ORDER BY fecha_avaluo DESC) as rn
    FROM avaluos WHERE estado_avaluo = 'vigente'
) av ON p.id = av.id_propiedad AND av.rn = 1
WHERE p.estado_propiedad = 'disponible'
ORDER BY p.fecha_actualizacion DESC;

-- Vista de clientes completa con información mexicana
CREATE VIEW vista_clientes_completa AS
SELECT
    c.id,
    c.nombre,
    c.apellido_paterno,
    c.apellido_materno,
    (c.nombre || ' ' || COALESCE(c.apellido_paterno, '') || ' ' || COALESCE(c.apellido_materno, '')) AS nombre_completo,
    c.rfc,
    c.curp,
    c.fecha_nacimiento,
    CASE 
        WHEN c.fecha_nacimiento IS NOT NULL THEN 
            ROUND((julianday('now') - julianday(c.fecha_nacimiento)) / 365.25)
        ELSE NULL 
    END AS edad,
    c.estado_civil,
    c.nacionalidad,
    c.identificacion_oficial,
    c.numero_identificacion,
    c.telefono,
    c.email,
    -- Dirección completa
    (COALESCE(c.calle, '') || ' ' || 
     COALESCE(c.numero_exterior, '') || ' ' || 
     COALESCE(c.numero_interior, '') || ', ' || 
     COALESCE(c.colonia, '') || ', ' || 
     COALESCE(c.municipio, '') || ', ' || 
     COALESCE(c.estado, '') || ' ' || 
     COALESCE(c.codigo_postal, '')) AS direccion_completa,
    c.ocupacion,
    c.empresa_trabajo,
    c.telefono_trabajo,
    c.ingresos_mensuales,
    c.otros_ingresos,
    (COALESCE(c.ingresos_mensuales, 0) + COALESCE(c.otros_ingresos, 0)) AS ingresos_totales,
    c.scoring_crediticio,
    CASE 
        WHEN c.scoring_crediticio >= 700 THEN 'Excelente'
        WHEN c.scoring_crediticio >= 600 THEN 'Bueno'
        WHEN c.scoring_crediticio >= 500 THEN 'Regular'
        WHEN c.scoring_crediticio < 500 THEN 'Malo'
        ELSE 'Sin información'
    END AS calificacion_crediticia,
    c.banco_principal,
    c.tiene_credito_infonavit,
    c.numero_infonavit,
    c.referencia_personal_nombre,
    c.referencia_personal_telefono,
    c.referencia_comercial_nombre,
    c.referencia_comercial_telefono,
    c.fecha_registro,
    c.activo,
    -- Contar propiedades relacionadas
    prop_propietario.total_propiedades,
    prop_inquilino.contratos_renta,
    prop_comprador.total_compras
FROM clientes c
LEFT JOIN (
    SELECT id_propietario, COUNT(*) as total_propiedades
    FROM propiedades 
    GROUP BY id_propietario
) prop_propietario ON c.id = prop_propietario.id_propietario
LEFT JOIN (
    SELECT id_inquilino, COUNT(*) as contratos_renta
    FROM alquileres 
    WHERE estado_alquiler = 'activo'
    GROUP BY id_inquilino
) prop_inquilino ON c.id = prop_inquilino.id_inquilino
LEFT JOIN (
    SELECT id_comprador, COUNT(*) as total_compras
    FROM ventas 
    GROUP BY id_comprador
) prop_comprador ON c.id = prop_comprador.id_comprador
WHERE c.activo = 'si'
ORDER BY c.apellido_paterno, c.apellido_materno, c.nombre;

-- Vista de contratos de renta activos con información detallada
CREATE VIEW vista_contratos_renta_activos AS
SELECT
    al.id,
    p.titulo AS propiedad,
    p.direccion,
    p.colonia,
    p.municipio,
    p.estado AS estado_propiedad,
    (c.nombre || ' ' || COALESCE(c.apellido_paterno, '') || ' ' || COALESCE(c.apellido_materno, '')) AS inquilino,
    c.telefono AS telefono_inquilino,
    c.email AS email_inquilino,
    c.rfc AS rfc_inquilino,
    al.fecha_inicio,
    al.fecha_fin,
    al.monto_mensual,
    al.deposito_garantia,
    al.incremento_anual,
    al.dia_pago,
    al.incluye_mantenimiento,
    al.incluye_servicios,
    al.permite_mascotas,
    al.numero_ocupantes,
    al.uso_permitido,
    (a.nombre || ' ' || COALESCE(a.apellido_paterno, '') || ' ' || COALESCE(a.apellido_materno, '')) AS agente,
    a.telefono AS telefono_agente,
    -- Calcular días restantes del contrato
    CASE 
        WHEN al.fecha_fin IS NOT NULL THEN 
            ROUND(julianday(al.fecha_fin) - julianday('now'))
        ELSE NULL 
    END AS dias_restantes_contrato,
    -- Último pago
    pr.fecha_pago AS ultimo_pago,
    pr.estado_pago AS estado_ultimo_pago,
    pr.dias_atraso,
    pr.recargo_mora,
    -- Fiadores
    f.nombre_fiador,
    f.telefono_fiador,
    f.tipo_garantia,
    f.monto_garantia,
    -- Notario
    al.notario_publico,
    al.numero_notaria
FROM alquileres al
JOIN propiedades p ON al.id_propiedad = p.id
JOIN clientes c ON al.id_inquilino = c.id
LEFT JOIN agentes a ON al.id_agente = a.id
LEFT JOIN (
    SELECT id_alquiler, fecha_pago, estado_pago, dias_atraso, recargo_mora,
           ROW_NUMBER() OVER (PARTITION BY id_alquiler ORDER BY fecha_pago DESC) as rn
    FROM pagos_renta
) pr ON al.id = pr.id_alquiler AND pr.rn = 1
LEFT JOIN fiadores f ON c.id = f.id_cliente AND f.estado_garantia = 'activa'
WHERE al.estado_alquiler = 'activo'
ORDER BY al.fecha_inicio DESC;

-- Vista de avalúos vigentes
CREATE VIEW vista_avaluos_vigentes AS
SELECT
    av.id,
    av.id_propiedad,
    p.titulo AS propiedad,
    p.direccion,
    p.municipio,
    p.estado,
    av.perito_valuador,
    av.cedula_perito,
    av.institucion_perito,
    av.fecha_avaluo,
    av.vigencia_meses,
    av.valor_avaluo,
    av.moneda,
    av.metodo_valuacion,
    av.proposito_avaluo,
    av.superficie_terreno_avaluo,
    av.superficie_construccion_avaluo,
    av.estado_conservacion,
    av.numero_avaluo,
    av.fecha_vencimiento,
    -- Días hasta vencimiento
    ROUND(julianday(av.fecha_vencimiento) - julianday('now')) AS dias_hasta_vencimiento,
    -- Comparar con valor catastral y precio de venta
    p.valor_catastral,
    p.precio AS precio_actual,
    CASE 
        WHEN p.valor_catastral > 0 THEN 
            ROUND(((av.valor_avaluo - p.valor_catastral) / p.valor_catastral) * 100, 2)
        ELSE NULL 
    END AS diferencia_porcentual_catastral,
    CASE 
        WHEN p.precio > 0 THEN 
            ROUND(((av.valor_avaluo - p.precio) / p.precio) * 100, 2)
        ELSE NULL 
    END AS diferencia_porcentual_precio
FROM avaluos av
JOIN propiedades p ON av.id_propiedad = p.id
WHERE av.estado_avaluo = 'vigente'
ORDER BY av.fecha_vencimiento ASC;

-- Vista de documentos por vencer
CREATE VIEW vista_documentos_por_vencer AS
SELECT
    d.id,
    d.tipo_documento,
    d.nombre_documento,
    d.tabla_referencia,
    d.id_referencia,
    d.fecha_vencimiento,
    ROUND(julianday(d.fecha_vencimiento) - julianday('now')) AS dias_hasta_vencimiento,
    CASE 
        WHEN d.tabla_referencia = 'propiedades' THEN p.titulo
        WHEN d.tabla_referencia = 'clientes' THEN (c.nombre || ' ' || COALESCE(c.apellido_paterno, ''))
        WHEN d.tabla_referencia = 'ventas' THEN ('Venta: ' || pv.titulo)
        WHEN d.tabla_referencia = 'alquileres' THEN ('Renta: ' || pa.titulo)
        ELSE 'N/A'
    END AS referencia_nombre,
    a.nombre || ' ' || COALESCE(a.apellido_paterno, '') AS agente_responsable,
    d.observaciones
FROM documentos d
LEFT JOIN propiedades p ON d.tabla_referencia = 'propiedades' AND d.id_referencia = p.id
LEFT JOIN clientes c ON d.tabla_referencia = 'clientes' AND d.id_referencia = c.id
LEFT JOIN ventas v ON d.tabla_referencia = 'ventas' AND d.id_referencia = v.id
LEFT JOIN propiedades pv ON v.id_propiedad = pv.id
LEFT JOIN alquileres al ON d.tabla_referencia = 'alquileres' AND d.id_referencia = al.id
LEFT JOIN propiedades pa ON al.id_propiedad = pa.id
LEFT JOIN agentes a ON d.agente_subida = a.id
WHERE d.fecha_vencimiento IS NOT NULL 
  AND d.estado_documento = 'activo'
  AND d.fecha_vencimiento >= date('now')
ORDER BY d.fecha_vencimiento ASC;

-- Vista de pagos atrasados detallada
CREATE VIEW vista_pagos_atrasados AS
SELECT
    pr.id AS id_pago,
    al.id AS id_alquiler,
    p.titulo AS propiedad,
    p.direccion,
    (c.nombre || ' ' || COALESCE(c.apellido_paterno, '') || ' ' || COALESCE(c.apellido_materno, '')) AS inquilino,
    c.telefono,
    c.rfc,
    pr.mes_correspondiente,
    pr.monto,
    pr.fecha_pago,
    pr.metodo_pago,
    pr.estado_pago,
    pr.dias_atraso,
    pr.recargo_mora,
    (pr.monto + COALESCE(pr.recargo_mora, 0)) AS total_adeudo,
    (a.nombre || ' ' || COALESCE(a.apellido_paterno, '') || ' ' || COALESCE(a.apellido_materno, '')) AS agente,
    a.telefono AS telefono_agente,
    -- Información del fiador si existe
    f.nombre_fiador,
    f.telefono_fiador,
    f.tipo_garantia
FROM pagos_renta pr
JOIN alquileres al ON pr.id_alquiler = al.id
JOIN propiedades p ON al.id_propiedad = p.id
JOIN clientes c ON al.id_inquilino = c.id
LEFT JOIN agentes a ON al.id_agente = a.id
LEFT JOIN fiadores f ON c.id = f.id_cliente AND f.estado_garantia = 'activa'
WHERE pr.estado_pago IN ('pendiente', 'atrasado')
ORDER BY pr.dias_atraso DESC, pr.mes_correspondiente DESC;

-- Vista de ventas completadas con información financiera
CREATE VIEW vista_ventas_completadas AS
SELECT
    v.id,
    p.titulo AS propiedad,
    p.direccion,
    p.municipio,
    p.estado,
    (cb.nombre || ' ' || COALESCE(cb.apellido_paterno, '') || ' ' || COALESCE(cb.apellido_materno, '')) AS comprador,
    cb.telefono AS telefono_comprador,
    cb.rfc AS rfc_comprador,
    (cp.nombre || ' ' || COALESCE(cp.apellido_paterno, '') || ' ' || COALESCE(cp.apellido_materno, '')) AS propietario,
    (a.nombre || ' ' || COALESCE(a.apellido_paterno, '') || ' ' || COALESCE(a.apellido_materno, '')) AS agente,
    v.fecha_venta,
    v.precio_venta,
    v.enganche,
    v.monto_credito,
    v.tipo_credito,
    v.financiamiento_banco,
    v.gastos_escrituracion,
    v.impuesto_adquisicion,
    v.otros_gastos,
    (COALESCE(v.gastos_escrituracion, 0) + COALESCE(v.impuesto_adquisicion, 0) + COALESCE(v.otros_gastos, 0)) AS total_gastos,
    v.notario_publico,
    v.numero_escritura,
    v.fecha_escrituracion,
    -- Comisión del agente
    com.monto AS comision_agente,
    com.pagada AS comision_pagada,
    -- Trámites relacionados
    tr.estado_tramite AS estado_escrituracion
FROM ventas v
JOIN propiedades p ON v.id_propiedad = p.id
JOIN clientes cb ON v.id_comprador = cb.id
JOIN clientes cp ON p.id_propietario = cp.id
LEFT JOIN agentes a ON v.id_agente = a.id
LEFT JOIN comisiones com ON v.id_agente = com.id_agente AND com.tipo_operacion = 'venta' AND com.id_operacion = v.id
LEFT JOIN tramites tr ON tr.tabla_referencia = 'ventas' AND tr.id_referencia = v.id AND tr.tipo_tramite = 'escrituracion'
ORDER BY v.fecha_venta DESC;

-- Vista de trámites pendientes
CREATE VIEW vista_tramites_pendientes AS
SELECT
    t.id,
    t.tipo_tramite,
    t.descripcion,
    t.dependencia,
    t.numero_expediente,
    t.fecha_inicio,
    t.fecha_estimada_fin,
    t.estado_tramite,
    t.responsable,
    t.costo_tramite,
    ROUND(julianday('now') - julianday(t.fecha_inicio)) AS dias_transcurridos,
    CASE 
        WHEN t.fecha_estimada_fin IS NOT NULL THEN 
            ROUND(julianday(t.fecha_estimada_fin) - julianday('now'))
        ELSE NULL 
    END AS dias_restantes,
    -- Información de la referencia
    CASE 
        WHEN t.tabla_referencia = 'ventas' THEN ('Venta: ' || pv.titulo)
        WHEN t.tabla_referencia = 'alquileres' THEN ('Renta: ' || pa.titulo)
        ELSE 'N/A'
    END AS referencia_descripcion,
    CASE 
        WHEN t.tabla_referencia = 'ventas' THEN (cv.nombre || ' ' || COALESCE(cv.apellido_paterno, ''))
        WHEN t.tabla_referencia = 'alquileres' THEN (ca.nombre || ' ' || COALESCE(ca.apellido_paterno, ''))
        ELSE 'N/A'
    END AS cliente_nombre,
    a.nombre || ' ' || COALESCE(a.apellido_paterno, '') AS agente_registro
FROM tramites t
LEFT JOIN ventas v ON t.tabla_referencia = 'ventas' AND t.id_referencia = v.id
LEFT JOIN propiedades pv ON v.id_propiedad = pv.id
LEFT JOIN clientes cv ON v.id_comprador = cv.id
LEFT JOIN alquileres al ON t.tabla_referencia = 'alquileres' AND t.id_referencia = al.id
LEFT JOIN propiedades pa ON al.id_propiedad = pa.id
LEFT JOIN clientes ca ON al.id_inquilino = ca.id
LEFT JOIN agentes a ON t.agente_registro = a.id
WHERE t.estado_tramite IN ('pendiente', 'en_proceso')
ORDER BY t.fecha_estimada_fin ASC, t.fecha_inicio ASC;

-- Vista de resumen financiero por agente
CREATE VIEW vista_agentes_performance AS
SELECT
    a.id,
    (a.nombre || ' ' || COALESCE(a.apellido_paterno, '') || ' ' || COALESCE(a.apellido_materno, '')) AS agente,
    a.telefono,
    a.email,
    a.comision_porcentaje,
    -- Estadísticas de ventas
    COALESCE(ventas_stats.total_ventas, 0) AS total_ventas,
    COALESCE(ventas_stats.monto_ventas, 0) AS monto_total_ventas,
    -- Estadísticas de rentas
    COALESCE(rentas_stats.total_rentas, 0) AS total_contratos_renta,
    COALESCE(rentas_stats.monto_rentas, 0) AS monto_mensual_rentas,
    -- Comisiones
    COALESCE(comisiones_stats.total_comisiones, 0) AS total_comisiones,
    COALESCE(comisiones_stats.comisiones_pagadas, 0) AS comisiones_pagadas,
    COALESCE(comisiones_stats.comisiones_pendientes, 0) AS comisiones_pendientes,
    -- Propiedades activas
    COALESCE(props_stats.propiedades_activas, 0) AS propiedades_en_cartera
FROM agentes a
LEFT JOIN (
    SELECT id_agente, COUNT(*) as total_ventas, SUM(precio_venta) as monto_ventas
    FROM ventas 
    WHERE fecha_venta >= date('now', '-12 months')
    GROUP BY id_agente
) ventas_stats ON a.id = ventas_stats.id_agente
LEFT JOIN (
    SELECT id_agente, COUNT(*) as total_rentas, SUM(monto_mensual) as monto_rentas
    FROM alquileres 
    WHERE estado_alquiler = 'activo'
    GROUP BY id_agente
) rentas_stats ON a.id = rentas_stats.id_agente
LEFT JOIN (
    SELECT id_agente, 
           COUNT(*) as total_comisiones,
           SUM(CASE WHEN pagada = 1 THEN monto ELSE 0 END) as comisiones_pagadas,
           SUM(CASE WHEN pagada = 0 THEN monto ELSE 0 END) as comisiones_pendientes
    FROM comisiones 
    GROUP BY id_agente
) comisiones_stats ON a.id = comisiones_stats.id_agente
LEFT JOIN (
    SELECT id_agente, COUNT(*) as propiedades_activas
    FROM propiedades 
    WHERE estado_propiedad = 'disponible'
    GROUP BY id_agente
) props_stats ON a.id = props_stats.id_agente
WHERE a.activo = 'si'
ORDER BY monto_total_ventas DESC;