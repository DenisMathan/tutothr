INSERT INTO user (username, password, email, active) values ('Lilly', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'adam@email', 1);
INSERT INTO user (username, password, email, active) values ('Paul', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'anja@email', 1);
INSERT INTO user (username, password, email, active) values ('Denis', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'denis@email', 1);
INSERT INTO user (username, password, email, active) values ('Tutor', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'martin@email', 1);
INSERT INTO user (username, password, email, active) values ('Student', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'lisa@email', 1);


INSERT INTO user (username, password, email, active) values ('anja', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'anja@email', 1);
INSERT INTO user (username, password, email, active) values ('tanja', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'tanja@email', 1);
INSERT INTO user (username, password, email, active) values ('martin', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'martin@email', 1);
INSERT INTO user (username, password, email, active) values ('lisa', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'lisa@email', 1);
INSERT INTO user (username, password, email, active) values ('mario', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'mario@email', 1);
INSERT INTO user (username, password, email, active) values ('peter', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'peter@email', 1);
INSERT INTO user (username, password, email, active) values ('leonard', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'leonard@email', 1);
INSERT INTO user (username, password, email, active) values ('markus', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'marcus@email', 1);


INSERT INTO role (description, type) VALUES ('ADMIN', 'ADMIN');
INSERT INTO role (description, type) VALUES ('STUDENT', 'STUDENT');
INSERT INTO role (description, type) VALUES ('TUTOR', 'TUTOR');

-- INSERT INTO authority (description) VALUES ( 'CREATE_STUDENT');
-- INSERT INTO authority (description) VALUES ( 'LIST_STUDENT');
-- INSERT INTO authority (description) VALUES ( 'REGISTRATION');

-- thomas is ADMIN
INSERT INTO userrole(iduser, idrole) VALUES (1,1);
INSERT INTO userrole(iduser, idrole) VALUES (2,1);
INSERT INTO userrole(iduser, idrole) VALUES (3,1);
INSERT INTO userrole(iduser, idrole) VALUES (3,2);

-- Student is Student
INSERT INTO userrole(iduser, idrole) VALUES (4,2);
-- Tutor is Tutor
INSERT INTO userrole(iduser, idrole) VALUES (5,3);
 

-- INSERT INTO roleauthority(idrole, idauthority) VALUES (1,1);
-- INSERT INTO roleauthority(idrole, idauthority) VALUES (1,2);
-- INSERT INTO roleauthority(idrole, idauthority) VALUES (2,3);




-- INSERT INTO course (description) VALUES ( 'Business Administration');
-- INSERT INTO course (description) VALUES ( 'Computer Science');
-- INSERT INTO course (description) VALUES ( 'Law');
-- INSERT INTO course (description) VALUES ( 'Mathematic');



-- INSERT INTO ADDRESS(HOUSE_NUMBER, STREET, ZPL) VALUES ('1', 'Markusplatz', '93047');
-- INSERT INTO ADDRESS(HOUSE_NUMBER, STREET, ZPL) VALUES ('2', 'Markusplatz', '93047');
-- INSERT INTO ADDRESS(HOUSE_NUMBER, STREET, ZPL) VALUES ('3', 'Markusplatz', '93047');
-- INSERT INTO ADDRESS(HOUSE_NUMBER, STREET, ZPL) VALUES ('4', 'Markusplatz', '93047');
-- INSERT INTO ADDRESS(HOUSE_NUMBER, STREET, ZPL) VALUES ('5', 'Markusplatz', '93047');
-- INSERT INTO ADDRESS(HOUSE_NUMBER, STREET, ZPL) VALUES ('6', 'Markusplatz', '93047');
-- INSERT INTO ADDRESS(HOUSE_NUMBER, STREET, ZPL) VALUES ('7', 'Markusplatz', '93047');
-- INSERT INTO ADDRESS(HOUSE_NUMBER, STREET, ZPL) VALUES ('8', 'Markusplatz', '93047');

-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (1, 'Lilly Fischer',  'FEMALE', 1, 2);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (2, 'Paul Klein',  'MALE', 2, 1);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (3, 'Denis Fischer',  'MALE', 3, 2);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (4, 'Tutor Fischer',  'FEMALE', 4, 1);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (5, 'Student Fischer',  'MALE', 5, 2);

-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (6, 'Anja Fischer',  'FEMALE', 1, 2);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (7, 'Tanja Klein',  'FEMALE', 2, 1);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (8, 'Martin Fischer',  'MALE', 3, 2);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (9, 'Lisa Fischer',  'FEMALE', 4, 1);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (10, 'Mario Fischer',  'MALE', 5, 2);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (11, 'Peter Fischer',  'MALE', 6, 1);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (12, 'Leonard Fischer',  'MALE', 7, 3);
-- INSERT INTO student (id, name, gender, address_id, course_id) VALUES (13, 'Markus Fischer',  'MALE', 8, 3);
