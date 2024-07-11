create table rentas (
  id int unsigned not null auto_increment primary key,
  cliente_id int unsigned not null,
  casa_id int unsigned not null,
  creado timestamp default current_timestamp
) engine=innodb default charset=utf8;
