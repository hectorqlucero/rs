-- Rollback de todas las vistas mexicanas
DROP VIEW IF EXISTS vista_resumen_agentes;
DROP VIEW IF EXISTS vista_tramites_pendientes;
DROP VIEW IF EXISTS vista_ventas_completadas;
DROP VIEW IF EXISTS vista_pagos_atrasados;
DROP VIEW IF EXISTS vista_documentos_por_vencer;
DROP VIEW IF EXISTS vista_avaluos_vigentes;
DROP VIEW IF EXISTS vista_contratos_renta_activos;
DROP VIEW IF EXISTS vista_clientes_completa;
DROP VIEW IF EXISTS vista_propiedades_disponibles;