alter table casas
add column status char(1) default 'A' COMMENT 'A=Activo,P=Proceso,I=Inactivo';
