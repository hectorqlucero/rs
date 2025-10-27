-- Migración completa de TODOS los triggers para bienes raíces México

-- ===============================================
-- TRIGGERS BÁSICOS PARA PROPIEDADES
-- ===============================================

-- Trigger para marcar propiedad como vendida
CREATE TRIGGER trg_propiedad_vendida
AFTER INSERT ON ventas
FOR EACH ROW
BEGIN
    UPDATE propiedades
    SET estado_propiedad = 'vendido'
    WHERE id = NEW.id_propiedad;
END;

-- Trigger para marcar propiedad como rentada  
CREATE TRIGGER trg_propiedad_rentada
AFTER INSERT ON alquileres
FOR EACH ROW
BEGIN
    UPDATE propiedades
    SET estado_propiedad = 'rentado'
    WHERE id = NEW.id_propiedad;
END;

-- Trigger para liberar propiedad cuando termina contrato de renta
CREATE TRIGGER trg_propiedad_disponible_renta
AFTER UPDATE ON alquileres
FOR EACH ROW
WHEN NEW.estado_alquiler IN ('cancelado', 'vencido') AND OLD.estado_alquiler = 'activo'
BEGIN
    UPDATE propiedades
    SET estado_propiedad = 'disponible'
    WHERE id = NEW.id_propiedad;
END;

-- ===============================================
-- TRIGGERS PARA COMISIONES
-- ===============================================

-- Trigger para generar comisión de venta
CREATE TRIGGER trg_comision_venta
AFTER INSERT ON ventas
FOR EACH ROW
WHEN NEW.id_agente IS NOT NULL
BEGIN
    INSERT INTO comisiones (id_agente, tipo_operacion, id_operacion, monto, fecha_generada, pagada)
    SELECT 
        NEW.id_agente,
        'venta',
        NEW.id,
        NEW.precio_venta * a.comision_porcentaje,
        CURRENT_DATE,
        0
    FROM agentes a 
    WHERE a.id = NEW.id_agente;
END;

-- Trigger para generar comisión de renta
CREATE TRIGGER trg_comision_renta
AFTER INSERT ON alquileres  
FOR EACH ROW
WHEN NEW.id_agente IS NOT NULL
BEGIN
    INSERT INTO comisiones (id_agente, tipo_operacion, id_operacion, monto, fecha_generada, pagada)
    SELECT 
        NEW.id_agente,
        'renta', 
        NEW.id,
        NEW.monto_mensual * a.comision_porcentaje,
        CURRENT_DATE,
        0
    FROM agentes a 
    WHERE a.id = NEW.id_agente;
END;

-- ===============================================
-- TRIGGERS PARA AUDITORÍA BÁSICA
-- ===============================================

-- Trigger para auditoría de ventas
CREATE TRIGGER trg_log_venta
AFTER INSERT ON ventas
FOR EACH ROW
BEGIN
    INSERT INTO bitacora (tabla, operacion, id_registro, descripcion)
    VALUES (
        'ventas', 
        'INSERT', 
        NEW.id, 
        'Nueva venta registrada por $' || COALESCE(NEW.precio_venta, 0)
    );
END;

-- Trigger para auditoría de alquileres
CREATE TRIGGER trg_log_alquiler
AFTER INSERT ON alquileres
FOR EACH ROW
BEGIN
    INSERT INTO bitacora (tabla, operacion, id_registro, descripcion)
    VALUES (
        'alquileres', 
        'INSERT', 
        NEW.id, 
        'Nuevo contrato de renta: $' || COALESCE(NEW.monto_mensual, 0) || '/mes'
    );
END;

-- Trigger para auditoría de propiedades
CREATE TRIGGER trg_log_propiedades
AFTER UPDATE ON propiedades
FOR EACH ROW
BEGIN
    INSERT INTO bitacora (tabla, operacion, id_registro, descripcion)
    VALUES (
        'propiedades', 
        'UPDATE', 
        NEW.id, 
        'Actualización de propiedad: ' || NEW.titulo
    );
END;