DROP DATABASE IF EXISTS hospital;
CREATE DATABASE hospital; 
USE hospital;

CREATE TABLE user (
id INT(11) NOT NULL AUTO_INCREMENT,
userType ENUM('admin', 'doctor', 'patient') NOT NULL,
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
patronymic VARCHAR(50),
login VARCHAR(50) NOT NULL,
`password` VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
constraint user_AltPK unique (id, userType)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE admin (
id INT(11) NOT NULL,
userType VARCHAR(50) NOT NULL default 'admin' check (userType = 'admin'),
position VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE doctor (
id INT(11) NOT NULL,
userType VARCHAR(50) NOT NULL default 'doctor' check (userType = 'doctor'),
speciality VARCHAR(50) NOT NULL,
room VARCHAR(50) NOT NULL,
dateStart VARCHAR(50) NOT NULL,
dateEnd VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE day_schedule (
id INT(11) NOT NULL AUTO_INCREMENT,
doctor_id INT(11) NOT NULL,
weekDay VARCHAR(50) NOT NULL,
timeStart VARCHAR(50) NOT NULL,
timeEnd VARCHAR(50) NOT NULL,
duration VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (doctor_id) REFERENCES user (id) 
) ENGINE=INNODB DEFAULT CHARSET=utf8;
 

CREATE TABLE patient (
id INT(11) NOT NULL,
userType VARCHAR(50) NOT NULL default 'patient' check (userType = 'patient'),
email VARCHAR(50) NOT NULL,
address VARCHAR(50) NOT NULL,
phone VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

begin;
INSERT INTO user
 VALUES(NULL,'admin',"admin","Иванов",null,"SuperAdmin", "SuperAdminPassword");
 insert into admin
 VALUES(LAST_INSERT_ID(),"admin","superAdmin");
commit;
