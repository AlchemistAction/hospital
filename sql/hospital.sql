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
        (NULL,'офтальмолог'),
        (NULL,'лор'),
        (NULL,'терапевт'),
        (NULL,'уролог'),
        (NULL,'дерматолог');
        
CREATE TABLE room (
id INT(11) NOT NULL AUTO_INCREMENT,
room VARCHAR(50) NOT NULL,
PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO room
 VALUES (NULL,'100'),
		(NULL,'200'),
        (NULL,'300'),
        (NULL,'400'),
        (NULL,'500'),
        (NULL,'600');


CREATE TABLE doctor (
id INT(11) NOT NULL,
speciality_id INT(11) NOT NULL,
room_id INT(11) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE,
FOREIGN KEY (speciality_id) REFERENCES speciality (id),
UNIQUE KEY user (room_id),
FOREIGN KEY (room_id) REFERENCES room (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE day_schedule (
id INT(11) NOT NULL AUTO_INCREMENT,
doctor_id INT(11) NOT NULL,
`date` datetime NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (doctor_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;
            
CREATE TABLE appointment (
id INT(11) NOT NULL AUTO_INCREMENT,
day_schedule_id INT(11) NOT NULL,
timeStart time NOT NULL,
timeEnd time NOT NULL,
state ENUM('FREE', 'APPOINTMENT', 'COMMISSION') NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (day_schedule_id) REFERENCES day_schedule (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 CREATE TABLE commission (
id INT(11) NOT NULL AUTO_INCREMENT,
`date` datetime NOT NULL,
timeStart time NOT NULL,
timeEnd time NOT NULL,
room_id INT(11) NULL,
PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE ticket (
id INT(11) NOT NULL AUTO_INCREMENT,
number VARCHAR(50) NULL,
patient_id INT(11) NULL,
appointment_id INT(11) NULL,
commission_id INT(11) NULL,
PRIMARY KEY (id),
FOREIGN KEY (commission_id) REFERENCES commission (id) ON DELETE CASCADE,
FOREIGN KEY (appointment_id) REFERENCES appointment (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE commission_doctor (
id INT(11) NOT NULL AUTO_INCREMENT,
commission_id INT(11) NULL,
doctor_id INT(11) NULL,
PRIMARY KEY (id),
UNIQUE KEY commission_doctor (commission_id, doctor_id),
FOREIGN KEY (commission_id) REFERENCES commission (id) ON DELETE CASCADE,
FOREIGN KEY (doctor_id) REFERENCES doctor (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE patient (
id INT(11) NOT NULL,
email VARCHAR(50) NOT NULL,
address VARCHAR(50) NOT NULL,
phone VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE session (
id INT(11) NOT NULL AUTO_INCREMENT,
user_id INT(11) NOT NULL,
uuid VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY user_session (user_id, uuid),
FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

begin;
INSERT INTO user
 VALUES(NULL,'ADMIN',"Admin","admin",null,"SuperAdmin", "SuperAdminPassword");
 insert into admin
 VALUES(LAST_INSERT_ID(),"superAdmin");
commit;