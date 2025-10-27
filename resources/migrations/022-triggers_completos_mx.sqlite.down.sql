-- Rollback de triggers básicos mexicanos

-- Triggers de auditoría
DROP TRIGGER IF EXISTS trg_log_propiedades;
DROP TRIGGER IF EXISTS trg_log_alquiler;
DROP TRIGGER IF EXISTS trg_log_venta;

-- Triggers de comisiones
DROP TRIGGER IF EXISTS trg_comision_renta;
DROP TRIGGER IF EXISTS trg_comision_venta;

-- Triggers de propiedades
DROP TRIGGER IF EXISTS trg_propiedad_disponible_renta;
DROP TRIGGER IF EXISTS trg_propiedad_rentada;
DROP TRIGGER IF EXISTS trg_propiedad_vendida;