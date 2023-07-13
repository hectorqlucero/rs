create table clientes (
    id int unsigned not null auto_increment primary key,
    nombre varchar(255) default null,
    paterno varchar(255) default null,
    materno varchar(255) default null,
    telefono varchar(255) default null,
    celular varchar(255) default null,
    email varchar(255) default null,
    ingresos decimal(15,2) default 0,
    pc decimal(15,2) default 0,
    tipo_creditos_id int unsigned default 0
) engine=innodb default charset=utf8;