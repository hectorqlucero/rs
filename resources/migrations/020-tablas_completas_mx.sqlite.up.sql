-- Migración completa de TODAS las tablas para bienes raíces México
-- Incluye tablas básicas + mejoras mexicanas

-- Tabla de usuarios (ya existente mejorada)
-- La tabla users ya existe en 001-users, no la recreamos

-- Tabla de clientes con campos mexicanos completos
DROP TABLE IF EXISTS clientes;
CREATE TABLE clientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    -- Información básica
    nombre TEXT NOT NULL,
    apellido_paterno TEXT,
    apellido_materno TEXT,
    
    -- Identificación mexicana
    rfc TEXT,
    curp TEXT,
    fecha_nacimiento DATE,
    estado_civil TEXT, -- soltero, casado, divorciado, viudo, union_libre
    nacionalidad TEXT DEFAULT 'mexicana',
    identificacion_oficial TEXT, -- INE, pasaporte, cedula
    numero_identificacion TEXT,

    -- Contacto
    telefono TEXT,
    email TEXT,
    
    -- Dirección completa mexicana
    calle TEXT,
    numero_exterior TEXT,
    numero_interior TEXT,
    colonia TEXT,
    municipio TEXT,
    estado TEXT,
    codigo_postal TEXT,
    pais TEXT DEFAULT 'México',

    -- Información financiera y laboral
    ocupacion TEXT,
    empresa_trabajo TEXT,
    telefono_trabajo TEXT,
    ingresos_mensuales REAL,
    otros_ingresos REAL,
    scoring_crediticio INTEGER, -- 1-999 (Buró de Crédito)
    banco_principal TEXT,
    numero_cuenta TEXT,
    clabe_interbancaria TEXT,

    -- Referencias
    referencia_personal_nombre TEXT,
    referencia_personal_telefono TEXT,
    referencia_comercial_nombre TEXT,
    referencia_comercial_telefono TEXT,

    -- INFONAVIT
    tiene_credito_infonavit TEXT DEFAULT 'no', -- si, no, usado
    numero_infonavit TEXT,
    
    -- Control
    observaciones TEXT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo TEXT DEFAULT 'si'
);

-- Tabla de agentes
CREATE TABLE IF NOT EXISTS agentes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    apellido_paterno TEXT,
    apellido_materno TEXT,
    telefono TEXT,
    email TEXT,
    comision_porcentaje REAL DEFAULT 0.05,
    cedula_profesional TEXT,
    activo TEXT DEFAULT 'si',
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de propiedades con campos mexicanos completos
CREATE TABLE propiedades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    
    -- Información básica
    titulo TEXT NOT NULL,
    descripcion TEXT,
    tipo TEXT, -- casa, departamento, terreno, local_comercial, oficina, bodega
    modalidad TEXT, -- venta, renta, venta_renta
    precio REAL,
    moneda TEXT DEFAULT 'MXN',
    
    -- Ubicación
    direccion TEXT,
    colonia TEXT,
    municipio TEXT,
    estado TEXT,
    codigo_postal TEXT,
    coordenadas_gps TEXT, -- latitud,longitud
    
    -- Características físicas
    superficie_terreno REAL, -- m2 de terreno
    superficie_construccion REAL, -- m2 de construcción
    recamaras INTEGER DEFAULT 0,
    banos INTEGER DEFAULT 0,
    medio_banos INTEGER DEFAULT 0,
    estacionamientos INTEGER DEFAULT 0,
    niveles INTEGER DEFAULT 1,
    antiguedad INTEGER, -- años
    
    -- Servicios y amenidades
    amenidades TEXT, -- JSON o texto separado por comas
    servicios TEXT, -- agua, luz, gas, drenaje, etc.
    orientacion TEXT, -- norte, sur, este, oeste
    
    -- Información legal mexicana
    uso_suelo TEXT, -- habitacional, comercial, mixto
    regimen_propiedad TEXT, -- propiedad privada, ejidal, comunal
    escrituras TEXT, -- número de escrituras
    folio_real TEXT, -- folio real del RPP
    clave_catastral TEXT, -- clave catastral municipal
    valor_catastral REAL, -- valor según catastro
    estatus_legal TEXT, -- al_corriente, adeudos, litigio
    
    -- Gastos
    predial_anual REAL, -- impuesto predial anual
    mantenimiento_mensual REAL, -- cuota de mantenimiento
    gastos_comunes REAL, -- gastos comunes condominiales
    
    -- Control y relaciones
    fecha_publicacion DATE,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_propietario INTEGER,
    id_agente INTEGER,
    estado_propiedad TEXT DEFAULT 'disponible', -- disponible, rentado, vendido, retirado
    observaciones TEXT,
    
    FOREIGN KEY (id_propietario) REFERENCES clientes(id),
    FOREIGN KEY (id_agente) REFERENCES agentes(id)
);

-- Tabla de ventas con campos mexicanos
CREATE TABLE ventas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_propiedad INTEGER NOT NULL,
    id_comprador INTEGER NOT NULL,
    id_agente INTEGER,
    
    -- Información de venta
    fecha_venta DATE,
    precio_venta REAL,
    
    -- Financiamiento mexicano
    enganche REAL, -- pago inicial
    financiamiento_banco TEXT, -- banco que otorga crédito
    monto_credito REAL,
    plazo_credito_meses INTEGER,
    tasa_interes REAL,
    tipo_credito TEXT, -- infonavit, fovissste, bancario, particular
    
    -- Gastos mexicanos
    gastos_escrituracion REAL,
    avaluo_bancario REAL,
    impuesto_adquisicion REAL, -- ISAI
    otros_gastos REAL,
    
    -- Proceso legal mexicano
    notario_publico TEXT,
    numero_notaria TEXT,
    fecha_escrituracion DATE,
    numero_escritura TEXT,
    registro_publico_propiedad TEXT,
    folio_mercantil TEXT,
    
    FOREIGN KEY (id_propiedad) REFERENCES propiedades(id),
    FOREIGN KEY (id_comprador) REFERENCES clientes(id),
    FOREIGN KEY (id_agente) REFERENCES agentes(id)
);

-- Tabla de alquileres con características mexicanas
CREATE TABLE alquileres (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_propiedad INTEGER NOT NULL,
    id_inquilino INTEGER NOT NULL,
    id_agente INTEGER,
    
    -- Fechas del contrato
    fecha_inicio DATE,
    fecha_fin DATE,
    
    -- Información financiera
    monto_mensual REAL,
    deposito_garantia REAL, -- generalmente 2 meses de renta
    primer_mes REAL, -- pago del primer mes
    ultimo_mes REAL, -- pago del último mes por adelantado
    incremento_anual REAL DEFAULT 0.05, -- % de incremento anual
    dia_pago INTEGER DEFAULT 1, -- día del mes para pago
    
    -- Condiciones del contrato
    incluye_mantenimiento TEXT DEFAULT 'no',
    incluye_servicios TEXT, -- qué servicios incluye
    permite_mascotas TEXT DEFAULT 'no',
    numero_ocupantes INTEGER,
    uso_permitido TEXT DEFAULT 'habitacional', -- habitacional, comercial, oficina
    clausulas_especiales TEXT,
    
    -- Proceso legal mexicano
    notario_publico TEXT, -- nombre del notario que ratificó
    numero_notaria TEXT,
    fecha_ratificacion DATE,
    
    -- Control
    estado_alquiler TEXT DEFAULT 'activo', -- activo, cancelado, vencido
    
    FOREIGN KEY (id_propiedad) REFERENCES propiedades(id),
    FOREIGN KEY (id_inquilino) REFERENCES clientes(id),
    FOREIGN KEY (id_agente) REFERENCES agentes(id)
);

-- Tabla de pagos de renta mejorada
CREATE TABLE pagos_renta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_alquiler INTEGER NOT NULL,
    mes_correspondiente TEXT,
    monto REAL,
    fecha_pago DATE,
    metodo_pago TEXT, -- efectivo, cheque, transferencia, credito
    estado_pago TEXT DEFAULT 'pendiente', -- pendiente, pagado, atrasado
    
    -- Campos mexicanos
    recargo_mora REAL DEFAULT 0,
    dias_atraso INTEGER DEFAULT 0,
    numero_recibo TEXT,
    observaciones_pago TEXT,
    agente_registro INTEGER,
    
    FOREIGN KEY (id_alquiler) REFERENCES alquileres(id),
    FOREIGN KEY (agente_registro) REFERENCES agentes(id)
);

-- Nueva tabla para pagos de ventas (abonos, enganche, etc.)
CREATE TABLE pagos_ventas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_venta INTEGER NOT NULL,
    tipo_pago TEXT NOT NULL, -- enganche, abono, liquidacion, escrituracion
    monto REAL NOT NULL,
    fecha_pago DATE,
    metodo_pago TEXT, -- efectivo, cheque, transferencia, credito
    numero_referencia TEXT, -- número de cheque, transferencia, etc.
    banco_origen TEXT,
    observaciones TEXT,
    numero_recibo TEXT,
    agente_registro INTEGER,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_venta) REFERENCES ventas(id),
    FOREIGN KEY (agente_registro) REFERENCES agentes(id)
);

-- Tabla de contratos
CREATE TABLE contratos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo TEXT NOT NULL, -- venta, renta
    id_referencia INTEGER NOT NULL -- referencia a ventas.id o alquileres.id
);

-- Tabla de comisiones
CREATE TABLE comisiones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_agente INTEGER NOT NULL,
    tipo_operacion TEXT, -- venta, renta
    id_operacion INTEGER, -- referencia a ventas.id o alquileres.id
    monto REAL,
    fecha_generada DATE,
    pagada INTEGER DEFAULT 0, -- 0 = no pagada, 1 = pagada
    fecha_pago DATE,
    observaciones TEXT,
    FOREIGN KEY (id_agente) REFERENCES agentes(id)
);

-- Tabla para gestión de documentos
CREATE TABLE documentos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo_documento TEXT NOT NULL, -- escritura, identificacion, comprobante_ingresos, contrato, avaluo, etc.
    nombre_documento TEXT NOT NULL,
    descripcion TEXT,
    -- Polymorphic references with nullable FKs (exactly one should be non-null)
    id_propiedad INTEGER REFERENCES propiedades(id),
    id_cliente INTEGER REFERENCES clientes(id),
    id_venta INTEGER REFERENCES ventas(id),
    id_alquiler INTEGER REFERENCES alquileres(id),
    ruta_archivo TEXT, -- path del archivo físico
    nombre_archivo TEXT,
    tamanio_kb INTEGER,
    mime_type TEXT,
    fecha_subida DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento DATE, -- para documentos que vencen como identificaciones
    estado_documento TEXT DEFAULT 'activo', -- activo, vencido, cancelado
    agente_subida INTEGER REFERENCES agentes(id),
    observaciones TEXT
    -- Removed CHECK constraint for SQLite compatibility
);

-- Tabla para avalúos profesionales
CREATE TABLE avaluos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_propiedad INTEGER NOT NULL,
    perito_valuador TEXT NOT NULL,
    cedula_perito TEXT,
    institucion_perito TEXT, -- INDAABIN, FOVISSSTE, etc.
    fecha_avaluo DATE NOT NULL,
    vigencia_meses INTEGER DEFAULT 6, -- meses de vigencia del avalúo
    valor_avaluo REAL NOT NULL,
    moneda TEXT DEFAULT 'MXN',
    metodo_valuacion TEXT, -- fisico, comparativo_mercado, residual, etc.
    proposito_avaluo TEXT, -- credito_hipotecario, venta, seguro, fiscal
    superficie_terreno_avaluo REAL,
    superficie_construccion_avaluo REAL,
    estado_conservacion TEXT, -- excelente, bueno, regular, malo
    observaciones_perito TEXT,
    numero_avaluo TEXT, -- número oficial del avalúo
    fecha_vencimiento DATE, -- calculada automáticamente
    estado_avaluo TEXT DEFAULT 'vigente', -- vigente, vencido, cancelado
    archivo_avaluo TEXT, -- referencia al documento
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_propiedad) REFERENCES propiedades(id)
);

-- Tabla para fiadores y avales (muy importante en México)
CREATE TABLE fiadores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_cliente INTEGER NOT NULL, -- cliente que necesita fiador
    tipo_garantia TEXT NOT NULL, -- fiador, aval_bancario, deposito, hipoteca
    nombre_fiador TEXT,
    rfc_fiador TEXT,
    telefono_fiador TEXT,
    email_fiador TEXT,
    direccion_fiador TEXT,
    ingresos_fiador REAL,
    relacion_cliente TEXT, -- familiar, amigo, conocido, empresa
    monto_garantia REAL,
    fecha_constitucion DATE,
    fecha_vencimiento DATE,
    estado_garantia TEXT DEFAULT 'activa', -- activa, liberada, ejecutada
    observaciones TEXT,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

-- Tabla para seguimiento de trámites (muy importante en México)
CREATE TABLE tramites (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    -- Polymorphic references (only ventas and alquileres for tramites)
    id_venta INTEGER REFERENCES ventas(id),
    id_alquiler INTEGER REFERENCES alquileres(id),
    tipo_tramite TEXT NOT NULL, -- escrituracion, registro_rpp, avaluo, credito, etc.
    descripcion TEXT,
    dependencia TEXT, -- notaría, RPP, banco, IMSS, etc.
    numero_expediente TEXT,
    fecha_inicio DATE,
    fecha_estimada_fin DATE,
    fecha_real_fin DATE,
    estado_tramite TEXT DEFAULT 'en_proceso', -- pendiente, en_proceso, completado, cancelado
    costo_tramite REAL,
    responsable TEXT, -- quién está encargado del trámite
    observaciones TEXT,
    agente_registro INTEGER REFERENCES agentes(id),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
    -- Removed CHECK constraint for SQLite compatibility
);

-- Tabla de bitácora/auditoría
CREATE TABLE bitacora (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tabla TEXT,
    operacion TEXT, -- INSERT, UPDATE, DELETE
    id_registro INTEGER,
    fecha_evento DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario INTEGER, -- quien hizo el cambio
    descripcion TEXT,
    datos_anteriores TEXT, -- JSON con datos antes del cambio
    datos_nuevos TEXT, -- JSON con datos después del cambio
    FOREIGN KEY (usuario) REFERENCES users(id)
);

-- Índices para optimizar búsquedas
CREATE INDEX idx_clientes_rfc ON clientes(rfc);
CREATE INDEX idx_clientes_curp ON clientes(curp);
CREATE INDEX idx_clientes_activo ON clientes(activo);

CREATE INDEX idx_propiedades_tipo ON propiedades(tipo);
CREATE INDEX idx_propiedades_modalidad ON propiedades(modalidad);
CREATE INDEX idx_propiedades_estado ON propiedades(estado_propiedad);
CREATE INDEX idx_propiedades_municipio ON propiedades(municipio, estado);
CREATE INDEX idx_propiedades_precio ON propiedades(precio);

CREATE INDEX idx_ventas_fecha ON ventas(fecha_venta);
CREATE INDEX idx_alquileres_activos ON alquileres(estado_alquiler);
CREATE INDEX idx_pagos_renta_estado ON pagos_renta(estado_pago);

CREATE INDEX idx_documentos_propiedad ON documentos(id_propiedad);
CREATE INDEX idx_documentos_cliente ON documentos(id_cliente);
CREATE INDEX idx_documentos_venta ON documentos(id_venta);
CREATE INDEX idx_documentos_alquiler ON documentos(id_alquiler);
CREATE INDEX idx_documentos_tipo ON documentos(tipo_documento);
CREATE INDEX idx_documentos_vencimiento ON documentos(fecha_vencimiento);

CREATE INDEX idx_avaluos_propiedad ON avaluos(id_propiedad);
CREATE INDEX idx_avaluos_vigencia ON avaluos(estado_avaluo, fecha_vencimiento);

CREATE INDEX idx_fiadores_cliente ON fiadores(id_cliente);
CREATE INDEX idx_tramites_venta ON tramites(id_venta);
CREATE INDEX idx_tramites_alquiler ON tramites(id_alquiler);
CREATE INDEX idx_tramites_estado ON tramites(estado_tramite);

CREATE INDEX idx_bitacora_tabla ON bitacora(tabla, id_registro);
CREATE INDEX idx_bitacora_fecha ON bitacora(fecha_evento);