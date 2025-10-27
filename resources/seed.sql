-- Seed data para sistema de bienes raíces México
-- Datos de prueba realistas para testing

-- ===============================================
-- USUARIOS DEL SISTEMA
-- ===============================================

INSERT INTO users (lastname, firstname, username, password, email, level, active) VALUES
('García', 'María', 'maria.garcia', 'password123', 'maria.garcia@inmobiliaria.mx', 'A', 'T'),
('López', 'Carlos', 'carlos.lopez', 'password123', 'carlos.lopez@inmobiliaria.mx', 'U', 'T'),
('Hernández', 'Ana', 'ana.hernandez', 'password123', 'ana.hernandez@inmobiliaria.mx', 'U', 'T'),
('Martínez', 'Roberto', 'roberto.martinez', 'password123', 'roberto.martinez@inmobiliaria.mx', 'A', 'T'),
('Rodríguez', 'Patricia', 'patricia.rodriguez', 'password123', 'patricia.rodriguez@inmobiliaria.mx', 'U', 'T');

-- ===============================================
-- AGENTES INMOBILIARIOS
-- ===============================================

INSERT INTO agentes (nombre, apellido_paterno, apellido_materno, telefono, email, comision_porcentaje, cedula_profesional, activo) VALUES
('Carlos', 'López', 'Ramírez', '5555-1234', 'carlos.lopez@inmobiliaria.mx', 0.03, 'CED12345', 'si'),
('Ana', 'Hernández', 'García', '5555-5678', 'ana.hernandez@inmobiliaria.mx', 0.035, 'CED67890', 'si'),
('Roberto', 'Martínez', 'Silva', '5555-9012', 'roberto.martinez@inmobiliaria.mx', 0.04, 'CED11111', 'si'),
('Patricia', 'Rodríguez', 'Torres', '5555-3456', 'patricia.rodriguez@inmobiliaria.mx', 0.03, 'CED22222', 'si'),
('Miguel', 'González', 'Pérez', '5555-7890', 'miguel.gonzalez@inmobiliaria.mx', 0.025, 'CED33333', 'si');

-- ===============================================
-- CLIENTES MEXICANOS
-- ===============================================

-- Propietarios
INSERT INTO clientes (
    nombre, apellido_paterno, apellido_materno, rfc, curp, fecha_nacimiento, estado_civil, 
    telefono, email, calle, numero_exterior, colonia, municipio, estado, codigo_postal,
    ocupacion, ingresos_mensuales, scoring_crediticio, tiene_credito_infonavit, activo
) VALUES
('Juan', 'Pérez', 'González', 'PEGJ850315ABC', 'PEGJ850315HDFRZN09', '1985-03-15', 'casado', 
 '5551234567', 'juan.perez@email.com', 'Av. Reforma', '123', 'Roma Norte', 'Cuauhtémoc', 'CDMX', '06700',
 'Ingeniero', 45000, 720, 'no', 'si'),

('María', 'Sánchez', 'López', 'SALM780922DEF', 'SALM780922MDFNPR08', '1978-09-22', 'soltera',
 '5552345678', 'maria.sanchez@email.com', 'Calle Insurgentes', '456', 'Condesa', 'Cuauhtémoc', 'CDMX', '06140',
 'Doctora', 65000, 780, 'si', 'si'),

('Luis', 'García', 'Hernández', 'GAHL901205GHI', 'GAHL901205HDFRCR07', '1990-12-05', 'casado',
 '5553456789', 'luis.garcia@email.com', 'Blvd. Adolfo López Mateos', '789', 'Las Águilas', 'Álvaro Obregón', 'CDMX', '01710',
 'Contador', 35000, 650, 'si', 'si'),

('Carmen', 'Rodríguez', 'Martínez', 'ROMC821118JKL', 'ROMC821118MDFDRR06', '1982-11-18', 'divorciada',
 '5554567890', 'carmen.rodriguez@email.com', 'Av. Universidad', '321', 'Copilco Universidad', 'Coyoacán', 'CDMX', '04360',
 'Abogada', 55000, 700, 'no', 'si'),

('Fernando', 'López', 'Castro', 'LOCF750430MNO', 'LOCF750430HDFPSR05', '1975-04-30', 'casado',
 '5555678901', 'fernando.lopez@email.com', 'Calz. de Tlalpan', '654', 'Portales Norte', 'Benito Juárez', 'CDMX', '03300',
 'Arquitecto', 48000, 680, 'usado', 'si');

-- Compradores/Inquilinos potenciales
INSERT INTO clientes (
    nombre, apellido_paterno, apellido_materno, rfc, curp, fecha_nacimiento, estado_civil,
    telefono, email, calle, numero_exterior, colonia, municipio, estado, codigo_postal,
    ocupacion, ingresos_mensuales, scoring_crediticio, tiene_credito_infonavit, activo
) VALUES
('Andrea', 'Torres', 'Jiménez', 'TOJA920815PQR', 'TOJA920815MDFRRN04', '1992-08-15', 'soltera',
 '5556789012', 'andrea.torres@email.com', 'Eje Central', '147', 'Centro', 'Cuauhtémoc', 'CDMX', '06000',
 'Marketing Manager', 38000, 710, 'si', 'si'),

('Diego', 'Morales', 'Vázquez', 'MOVD881203STU', 'MOVD881203HDFRZG03', '1988-12-03', 'casado',
 '5557890123', 'diego.morales@email.com', 'Av. Patriotismo', '258', 'San Pedro de los Pinos', 'Benito Juárez', 'CDMX', '03800',
 'Desarrollador', 42000, 740, 'si', 'si'),

('Sofía', 'Ramírez', 'Flores', 'RAFS851127VWX', 'RAFS851127MDFMLF02', '1985-11-27', 'casada',
 '5558901234', 'sofia.ramirez@email.com', 'Av. División del Norte', '369', 'Narvarte Poniente', 'Benito Juárez', 'CDMX', '03020',
 'Diseñadora', 32000, 620, 'no', 'si'),

('Alejandro', 'Cruz', 'Mendoza', 'CUMA790616YZA', 'CUMA790616HDFRNL01', '1979-06-16', 'soltero',
 '5559012345', 'alejandro.cruz@email.com', 'Circuito Interior', '741', 'Doctores', 'Cuauhtémoc', 'CDMX', '06720',
 'Consultor', 28000, 580, 'si', 'si');

-- ===============================================
-- PROPIEDADES MEXICANAS VARIADAS
-- ===============================================

INSERT INTO propiedades (
    titulo, descripcion, tipo, modalidad, precio, direccion, colonia, municipio, estado, codigo_postal,
    superficie_terreno, superficie_construccion, recamaras, banos, medio_banos, estacionamientos,
    antiguedad, amenidades, servicios, uso_suelo, escrituras, clave_catastral, valor_catastral,
    predial_anual, mantenimiento_mensual, id_propietario, id_agente, estado_propiedad
) VALUES
-- Casa en Roma Norte (venta)
('Casa Colonial en Roma Norte', 'Hermosa casa colonial completamente renovada, ideal para familia.', 
 'casa', 'venta', 8500000, 'Calle Orizaba 125', 'Roma Norte', 'Cuauhtémoc', 'CDMX', '06700',
 180, 220, 3, 2, 1, 2, 15, 'Jardín, Terraza, Estudio', 'Agua, Luz, Gas, Drenaje, Internet',
 'habitacional', 'ESC-001-2020', 'CAT-001-ROMA', 7800000, 45000, 3500, 1, 1, 'disponible'),

-- Departamento en Condesa (renta)
('Departamento Moderno Condesa', 'Depto nuevo con acabados de lujo, edificio con amenidades.',
 'departamento', 'renta', 25000, 'Av. Tamaulipas 85', 'Condesa', 'Cuauhtémoc', 'CDMX', '06140',
 0, 85, 2, 2, 0, 1, 2, 'Gym, Alberca, Roof Garden, Seguridad 24hrs', 'Todos los servicios incluidos',
 'habitacional', 'ESC-002-2021', 'CAT-002-COND', 4200000, 18000, 4500, 2, 2, 'disponible'),

-- Casa en Las Águilas (venta/renta)
('Casa Residencial Las Águilas', 'Amplia casa familiar con jardín y cochera doble.',
 'casa', 'venta_renta', 6200000, 'Calle Delfín Madrigal 456', 'Las Águilas', 'Álvaro Obregón', 'CDMX', '01710',
 200, 180, 4, 3, 1, 2, 8, 'Jardín, Cochera techada, Cuarto de servicio', 'Agua, Luz, Gas, Drenaje',
 'habitacional', 'ESC-003-2019', 'CAT-003-AGUI', 5800000, 32000, 2800, 3, 3, 'disponible'),

-- Local comercial (renta)
('Local Comercial Zona Rosa', 'Excelente ubicación para restaurante o boutique.',
 'local_comercial', 'renta', 45000, 'Calle Londres 89', 'Zona Rosa', 'Cuauhtémoc', 'CDMX', '06600',
 0, 120, 0, 2, 0, 0, 10, 'Vitrina grande, Acceso independiente', 'Agua, Luz, Gas',
 'comercial', 'ESC-004-2018', 'CAT-004-ZONA', 3500000, 25000, 0, 4, 4, 'disponible'),

-- Terreno (venta)
('Terreno Residencial Coyoacán', 'Terreno plano ideal para desarrollo habitacional.',
 'terreno', 'venta', 4800000, 'Av. Universidad 1234', 'Copilco Universidad', 'Coyoacán', 'CDMX', '04360',
 300, 0, 0, 0, 0, 0, 0, 'Terreno plano, Todos los servicios', 'Factibilidad de servicios',
 'habitacional', 'ESC-005-2020', 'CAT-005-COYO', 4500000, 28000, 0, 4, 5, 'disponible'),

-- Oficina (renta)
('Oficina Corporativa Polanco', 'Oficina ejecutiva en edificio inteligente.',
 'oficina', 'renta', 35000, 'Av. Presidente Masaryk 201', 'Polanco', 'Miguel Hidalgo', 'CDMX', '11560',
 0, 95, 0, 1, 0, 2, 5, 'Recepción, Sala de juntas, Vista panorámica', 'Aire acondicionado, Internet',
 'comercial', 'ESC-006-2020', 'CAT-006-POLA', 2800000, 15000, 8000, 5, 1, 'disponible');

-- ===============================================
-- AVALÚOS PROFESIONALES
-- ===============================================

INSERT INTO avaluos (
    id_propiedad, perito_valuador, cedula_perito, institucion_perito, fecha_avaluo,
    valor_avaluo, metodo_valuacion, proposito_avaluo, superficie_terreno_avaluo,
    superficie_construccion_avaluo, estado_conservacion, numero_avaluo, fecha_vencimiento
) VALUES
(1, 'Ing. José Manuel Pérez Avalúos', 'AVA123456', 'INDAABIN', '2024-10-01', 8200000, 
 'comparativo_mercado', 'credito_hipotecario', 180, 220, 'bueno', 'AVA-2024-001', '2025-04-01'),

(2, 'Arq. Laura González Valuaciones', 'AVA789012', 'FOVISSSTE', '2024-09-15', 4100000,
 'fisico', 'seguro', 0, 85, 'excelente', 'AVA-2024-002', '2025-03-15'),

(3, 'Ing. Ricardo Morales y Asociados', 'AVA345678', 'INDAABIN', '2024-08-20', 6000000,
 'comparativo_mercado', 'venta', 200, 180, 'bueno', 'AVA-2024-003', '2025-02-20');

-- ===============================================
-- FIADORES Y GARANTÍAS
-- ===============================================

INSERT INTO fiadores (
    id_cliente, tipo_garantia, nombre_fiador, rfc_fiador, telefono_fiador,
    ingresos_fiador, relacion_cliente, monto_garantia, fecha_constitucion, estado_garantia
) VALUES
(6, 'fiador', 'Carlos Torres Jiménez', 'TOCJ650312ABC', '5551111111', 60000, 'padre', 50000, '2024-10-01', 'activa'),
(7, 'aval_bancario', 'Banco HSBC México', 'HSB891225XYZ', '5552222222', 0, 'institucion', 84000, '2024-09-15', 'activa'),
(8, 'deposito', NULL, NULL, NULL, 0, 'propio', 64000, '2024-08-30', 'activa');

-- ===============================================
-- VENTAS REALIZADAS
-- ===============================================

INSERT INTO ventas (
    id_propiedad, id_comprador, id_agente, fecha_venta, precio_venta, enganche,
    tipo_credito, financiamiento_banco, monto_credito, gastos_escrituracion,
    impuesto_adquisicion, notario_publico, numero_notaria
) VALUES
-- Venta ya realizada (cambiaremos el estado de la propiedad después)
(1, 6, 1, '2024-09-15', 8200000, 1640000, 'infonavit', 'INFONAVIT', 6560000, 
 180000, 164000, 'Lic. María Teresa Sánchez', 'Notaría 45');

-- ===============================================
-- ALQUILERES ACTIVOS
-- ===============================================

INSERT INTO alquileres (
    id_propiedad, id_inquilino, id_agente, fecha_inicio, fecha_fin, monto_mensual,
    deposito_garantia, primer_mes, incremento_anual, dia_pago, incluye_mantenimiento,
    permite_mascotas, numero_ocupantes, notario_publico, estado_alquiler
) VALUES
-- Renta del departamento en Condesa
(2, 7, 2, '2024-08-01', '2025-08-01', 25000, 50000, 25000, 0.05, 1, 'si', 'no', 2,
 'Lic. Roberto Mendoza García', 'activo'),

-- Renta del local comercial
(4, 8, 4, '2024-07-15', '2025-07-15', 45000, 90000, 45000, 0.08, 5, 'no', 'no', 0,
 'Lic. Patricia Ruiz López', 'activo');

-- ===============================================
-- PAGOS DE RENTA
-- ===============================================

INSERT INTO pagos_renta (
    id_alquiler, mes_correspondiente, monto, fecha_pago, metodo_pago, estado_pago, numero_recibo
) VALUES
-- Pagos del departamento (inquilino puntual)
(1, '2024-08', 25000, '2024-08-01', 'transferencia', 'pagado', 'REC-001-08'),
(1, '2024-09', 25000, '2024-09-01', 'transferencia', 'pagado', 'REC-001-09'),
(1, '2024-10', 25000, '2024-10-01', 'transferencia', 'pagado', 'REC-001-10'),

-- Pagos del local (con un atraso)
(2, '2024-07', 45000, '2024-07-15', 'cheque', 'pagado', 'REC-002-07'),
(2, '2024-08', 45000, '2024-08-10', 'cheque', 'pagado', 'REC-002-08'),
(2, '2024-09', 45000, '2024-09-08', 'efectivo', 'pagado', 'REC-002-09'),
(2, '2024-10', 45000, '2024-10-25', 'transferencia', 'atrasado', NULL);

-- ===============================================
-- PAGOS DE VENTAS
-- ===============================================

INSERT INTO pagos_ventas (
    id_venta, tipo_pago, monto, fecha_pago, metodo_pago, numero_recibo
) VALUES
(1, 'enganche', 1640000, '2024-09-15', 'transferencia', 'VEN-001-ENG'),
(1, 'escrituracion', 344000, '2024-10-01', 'cheque', 'VEN-001-ESC');

-- ===============================================
-- DOCUMENTOS
-- ===============================================

INSERT INTO documentos (
    tipo_documento, nombre_documento, tabla_referencia, id_referencia,
    fecha_vencimiento, estado_documento, usuario_subida
) VALUES
('escritura', 'Escritura Casa Roma Norte', 'propiedades', 1, NULL, 'activo', 1),
('identificacion', 'INE Juan Pérez', 'clientes', 1, '2029-03-15', 'activo', 1),
('comprobante_ingresos', 'Recibos de nómina Andrea Torres', 'clientes', 6, '2025-01-15', 'activo', 2),
('contrato', 'Contrato renta Condesa', 'alquileres', 1, '2025-08-01', 'activo', 2),
('avaluo', 'Avalúo Casa Roma Norte', 'propiedades', 1, '2025-04-01', 'activo', 1);

-- ===============================================
-- TRÁMITES EN PROCESO
-- ===============================================

INSERT INTO tramites (
    tabla_referencia, id_referencia, tipo_tramite, descripcion, dependencia,
    fecha_inicio, fecha_estimada_fin, estado_tramite, responsable
) VALUES
('ventas', 1, 'escrituracion', 'Escrituración Casa Roma Norte', 'Notaría 45', 
 '2024-09-15', '2024-12-15', 'en_proceso', 'Lic. María Teresa Sánchez'),
 
('propiedades', 5, 'factibilidad', 'Factibilidad de servicios terreno Coyoacán', 'CFE/SACMEX',
 '2024-10-01', '2024-11-15', 'pendiente', 'Arq. Luis González');

-- ===============================================
-- ACTUALIZAR ESTADOS DE PROPIEDADES
-- ===============================================

-- La casa Roma Norte ya se vendió
UPDATE propiedades SET estado_propiedad = 'vendido' WHERE id = 1;

-- El departamento Condesa está rentado
UPDATE propiedades SET estado_propiedad = 'rentado' WHERE id = 2;

-- El local comercial está rentado
UPDATE propiedades SET estado_propiedad = 'rentado' WHERE id = 4;