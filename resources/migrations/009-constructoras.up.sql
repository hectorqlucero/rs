create table constructoras (
    id int unsigned not null auto_increment PRIMARY KEY,
    razon_social varchar(255) default null,
    rfc varchar(255) default null,
    telefono varchar(255) default null,
    celular varchar(255) default null,
    fax varchar(255) default null,
    email varchar(255) default null
) engine=innodb default charset=utf8;