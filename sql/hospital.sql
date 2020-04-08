DROP DATABASE IF EXISTS hospital;
CREATE DATABASE hospital; 
USE hospital;

CREATE TABLE user (
id INT(11) NOT NULL AUTO_INCREMENT,
userType ENUM('ADMIN', 'DOCTOR', 'PATIENT') NOT NULL,
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
patronymic VARCHAR(50),
login VARCHAR(50) NOT NULL,
`password` VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY user (login),
constraint user_AltPK unique (id, userType)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE admin (
id INT(11) NOT NULL,
position VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE speciality (
id INT(11) NOT NULL AUTO_INCREMENT,
speciality VARCHAR(50) NOT NULL,
PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO speciality
 VALUES (NULL,'хирург'),
        (NULL,'лор');
        
CREATE TABLE room (
id INT(11) NOT NULL AUTO_INCREMENT,
room VARCHAR(50) NOT NULL,
PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO room
 VALUES (NULL,'100'),
        (NULL,'300');


CREATE TABLE doctor (
id INT(11) NOT NULL,
speciality_id INT(11) NOT NULL,
room_id INT(11) NOT NULL,
dateStart VARCHAR(50) NOT NULL,
dateEnd VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE,
FOREIGN KEY (speciality_id) REFERENCES speciality (id),
FOREIGN KEY (room_id) REFERENCES room (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

            
CREATE TABLE appointment (
id INT(11) NOT NULL AUTO_INCREMENT,
doctor_id INT(11) NOT NULL,
date_of_appointment VARCHAR(50) NOT NULL,
timeStart VARCHAR(50) NOT NULL,
timeEnd VARCHAR(50) NOT NULL,
is_free BOOLEAN default true,
is_locked_for_commission BOOLEAN default true,
PRIMARY KEY (id),
FOREIGN KEY (doctor_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
CREATE TABLE ticket (
id INT(11) NOT NULL AUTO_INCREMENT,
ticket VARCHAR(50) NOT NULL,
patient_id INT(11) NOT NULL,
appointment_id INT(11) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (appointment_id) REFERENCES appointment (id) ON DELETE CASCADE,
constraint ticket_AltPK unique (patient_id, appointment_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE patient (
id INT(11) NOT NULL,
email VARCHAR(50) NOT NULL,
address VARCHAR(50) NOT NULL,
phone VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

begin;
INSERT INTO user
 VALUES(NULL,'ADMIN',"Admin","admin",null,"SuperAdmin", "SuperAdminPassword");
 insert into admin
 VALUES(LAST_INSERT_ID(),"superAdmin");
commit;
