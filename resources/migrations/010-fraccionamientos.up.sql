create table fraccionamientos (
    id int unsigned not null auto_increment primary key,
    nombre varchar(255) default null,
    estado varchar(5) default null,
    ciudad varchar(255) default null,
    constructoras_id int unsigned not null default 0,
    zonas_id int unsigned not null default 0,
    tipo_creditos_id int unsigned not null default 0,
    constraint fk_fraccionamientos_constructoras_id foreign key (constructoras_id) references constructoras(id),
    constraint fk_fraccionamientos_zonas_id foreign key (zonas_id) references zonas(id),
    constraint fk_fraccionamientos_tipo_creditos_id foreign key (tipo_creditos_id) references tipo_creditos(id)
) engine=innodb default charset=utf8;