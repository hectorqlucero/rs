create table casas (
    id int unsigned not null auto_increment primary key,
    modelo varchar(255) default null,
    recamaras tinyint default 1,
    baños float default 1,
    plantas tinyint default 1,
    costo decimal(15,2) default 0,
    mtc float default 0,
    mtt float default 0,
    comentarios text default null,
    fraccionamientos_id int unsigned default 0,
    constraint fk_casas_fraccionamientos_id foreign key (fraccionamientos_id) references fraccionamientos(id)
) engine=innodb default charset=utf8;