#drop database app_losjardines3;
create database if not exists app_losjardines3;
use app_losjardines3;
SELECT NOW() AS fecha_hora_actual;
SET lc_time_names = 'es_ES'; #CAMBIAR A ESPAÑOL EL IDIOMA

create table Cliente(
Dni_Cli char(8) primary key,
Nomb_Cli varchar(20) not null,
Ape_Cli varchar(20) not null,
Correo_Cli varchar(30) unique not null,
Contra_Cli varchar(20) not null,
Cel_Cli varchar(15) unique not null,
Fecha_registro datetime default current_timestamp
);


insert into cliente (dni_cli, nomb_cli, ape_cli, correo_cli, contra_cli, cel_cli) values
('72673554', 'Gerardo', 'cahuana', 'ge@g.com', '123', '997653086' ),
('70829460', 'Luis', 'sala', 'lu@g.com', '123', '969599087' );


create procedure sp_ListarCLI()#--------
select * from Cliente;

create procedure sp_InsertarCLI(#--------
Dni char(8) ,
Nombre varchar(20),
Apellido varchar(20) ,
Correo varchar(30) ,
Contrasena varchar(20),
Celular varchar(10)
) insert into Cliente (dni_cli, nomb_cli, ape_cli, correo_cli, contra_cli, cel_cli)
values (Dni,Nombre,Apellido,Correo,Contrasena,Celular);

create procedure sp_EliminarCLI( Dni char(8) )
delete from Cliente where Dni_Cli=Dni;

create procedure sp_ConsultarCLI(Correo varchar(30),Pass varchar(20))
select * from Cliente where Correo_Cli = Correo and Contra_Cli = Pass;

create procedure sp_EditarPassCLI(Dni char(8) , Contra varchar(20))
update Cliente set Contra_Cli=Contra where Dni_Cli=Dni;

create procedure sp_ConsultarDniCLI(Dni char(8))
select * from Cliente where Dni_Cli=Dni;

create procedure sp_ConsultarCorreoCLI(Correo char(20))
select * from Cliente where Correo_Cli=Correo;

create procedure sp_ConsultarCelularCLI(Celular varchar(15))
select * from Cliente where Cel_cli = celular;

create procedure sp_EditarDatosCLI(#-----------------------
Dni char(8) ,
Correo varchar(53),
Celular varchar(15))
update Cliente set Correo_cli=correo , cel_cli=celular where Dni_Cli=Dni;

#-------------------------ADMIN--------
create table Admin(
Dni_Adm char(8) primary key,
Nomb_Adm varchar(20) not null,
Ape_Adm varchar(20) not null,
Correo_Adm varchar(30) unique not null,
Contra_Adm varchar(20) not null,
Cel_Admin varchar(15) unique not null
);

insert into admin values
('72673554', 'Gerardo', 'Cahuana', 'ge_adm@g.com', '123', '997653086' ),
('70829460', 'Luis', 'Sala', 'lu_adm@g.com', '123', '969599087' );

create procedure sp_ConsultarADM(
Correo varchar(30),
Pass varchar(20))
select * from Admin where Correo_Adm = Correo and Contra_Adm = Pass;

create procedure sp_ConsultarDniADM(Dni char(9))
select * from Admin where Dni_Adm=Dni;

create procedure sp_ConsultarCorreoADM(Correo char(20))
select * from Admin where Correo_Adm=Correo;

#------------TABLA LOSA------------
create table tb_losa(
id int  auto_increment primary key,
id_Cli char(8),
id_Adm char(8),
nombre_losa varchar(30) not null,
descripcion varchar(80),
horario varchar(50) not null,
direccion varchar (80) not null,
mantenimiento boolean default 0,
precio_hora decimal default 0,
nombre_tabla varchar(20) not null unique,
foreign key(id_Cli) references Cliente(Dni_Cli),
foreign key(id_Adm) references Admin(Dni_Adm)
);


insert into tb_losa (nombre_losa, horario, direccion,nombre_tabla) values
('Losa del pueblo','L-V', 'direccion 1','reserva_losa1'),
('Losa del barrio','L-V', 'direccion 2' ,'reserva_losa2'),
('Losa los patas','L-V', 'direccion 3','reserva_losa3'),
('Losa la Familia','L-V', 'direccion 4','reserva_losa4');


#------------TABLA RESERVAS------------

create table reserva_losa1(
id int  auto_increment primary key,
id_losa int,
fecha_rsv char(10) unique, #'2023-01-01'
3pm char(8),
5pm char(8),
7pm char(8),
foreign key(id_losa) references tb_losa(id)
);


create table reserva_losa2(
id int  auto_increment primary key,
id_losa int,
fecha_rsv char(10) unique, #'2023-01-01'
3pm char(8),
5pm char(8),
7pm char(8),
foreign key(id_losa) references tb_losa(id)
);


create table reserva_losa3(
id int  auto_increment primary key,
id_losa int,
fecha_rsv char(10) unique, #'2023-01-01'
3pm char(8),
5pm char(8),
7pm char(8),
foreign key(id_losa) references tb_losa(id)
);


create table reserva_losa4(
id int  auto_increment primary key,
id_losa int,
fecha_rsv char(10) unique, #'2023-01-01'
3pm char(8),
5pm char(8),
7pm char(8),
foreign key(id_losa) references tb_losa(id)
);


#----SP TABLA RESERVA--------
DELIMITER //
CREATE PROCEDURE LLenarTablaReservas(in id_cancha int)
BEGIN
    DECLARE fecha_inicio DATE;
    DECLARE fecha_fin DATE;
    DECLARE fecha_actual DATE;

    SET fecha_inicio = CONCAT(YEAR(CURDATE()), '-01-01');
    SET fecha_fin = CONCAT(YEAR(CURDATE()), '-12-31');
    SET fecha_actual = fecha_inicio;

    WHILE fecha_actual <= fecha_fin DO
		CASE id_cancha
			when 1 then
				INSERT INTO reserva_losa1 (fecha_rsv, id_losa)  VALUES (fecha_actual,id_cancha);
			when 2 then
				INSERT INTO reserva_losa2 (fecha_rsv, id_losa) VALUES (fecha_actual,id_cancha);
			when 3 then
                INSERT INTO reserva_losa3 (fecha_rsv, id_losa)  VALUES (fecha_actual,id_cancha);
			when 4 then
                INSERT INTO reserva_losa4 (fecha_rsv, id_losa)  VALUES (fecha_actual,id_cancha);
		END CASE;
        SET fecha_actual = DATE_ADD(fecha_actual, INTERVAL 1 DAY);
    END WHILE;

    SELECT 'Tablas llenadas correctamente.' AS mensaje;
END //
DELIMITER ;

call LLenarTablaReservas(1);
call LLenarTablaReservas(2);
call LLenarTablaReservas(3);
call LLenarTablaReservas(4);


###		LISTAR RESERVAS SEMANALES O EN UN RANGO DETERMINADO ###
DELIMITER //
CREATE PROCEDURE sp_ListarRsv(IN tabla varchar(50), in dia_min int, in dia_max int)
BEGIN
	SET @query = CONCAT('SELECT * FROM ',tabla, ' WHERE id between ', dia_min, ' and ', dia_max);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END //
DELIMITER ;

### REALIZAR UNA RESERVA ### -> CLIENTE COMPRA
DELIMITER //
CREATE PROCEDURE sp_RESERVAR(IN tabla varchar(50), IN dia CHAR(10),IN hora char(5), IN dni_user CHAR(8) )
BEGIN
	SET @query =
		CONCAT('UPDATE ', tabla, ' SET ', hora, '=\'', dni_user, '\' WHERE fecha_rsv=\'', dia, '\'');
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	SELECT 'Reserva insertada correctamente.' AS mensaje;
END //
DELIMITER ;


### LISTAR RESERVAS INDIVIDUAL POR CLIENTE ###
DELIMITER //
CREATE PROCEDURE sp_ConsultarRsvCLI(IN tabla VARCHAR(30),IN dni char(8) )
BEGIN
    SET @sql = CONCAT('SELECT * FROM ', tabla, ' WHERE 3pm = ', dni ,' OR 5pm= ',dni,' OR 7pm = ',dni);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //
DELIMITER ;

### LISTAR TODAS LAS RESERVAS COMPRADAS ### -> PARA EL ADMINISTRADOR
DELIMITER //
CREATE PROCEDURE sp_ListarReservasCLI(IN tabla VARCHAR(30))
BEGIN
    SET @sql = CONCAT('SELECT *
                       FROM ', tabla, '
                       WHERE
                       3pm IS NOT NULL OR
                       5pm IS NOT NULL OR
                       7pm IS NOT NULL');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //
DELIMITER ;

SELECT 'FINISH' AS mensaje;
##############<----------------->###############