INSERT INTO user (username, password, email, active) values ('Lilly', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'lilly@email', 1);
INSERT INTO user (username, password, email, active) values ('Paul', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'paul@email', 1);
INSERT INTO user (username, password, email, active) values ('Denis', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'denis@email', 1);
INSERT INTO user (username, password, email, active) values ('Tutor', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'tutor@email', 1);
INSERT INTO user (username, password, email, active) values ('Student', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'student@email', 1);


INSERT INTO user (username, password, email, active) values ('anja', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'anja@email', 1);
INSERT INTO user (username, password, email, active) values ('tanja', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'tanja@email', 1);
INSERT INTO user (username, password, email, active) values ('martin', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'martin@email', 1);
INSERT INTO user (username, password, email, active) values ('lisa', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'lisa@email', 1);
INSERT INTO user (username, password, email, active) values ('mario', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'mario@email', 1);
INSERT INTO user (username, password, email, active) values ('peter', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'peter@email', 1);
INSERT INTO user (username, password, email, active) values ('leonard', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'leonard@email', 1);
INSERT INTO user (username, password, email, active) values ('markus', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'markus@email', 1);


INSERT INTO role (description, type) VALUES ('ADMIN', 'ADMIN');
INSERT INTO role (description, type) VALUES ('STUDENT', 'STUDENT');
INSERT INTO role (description, type) VALUES ('TUTOR', 'TUTOR');

-- INSERT INTO authority (description) VALUES ( 'CREATE_STUDENT');
-- INSERT INTO authority (description) VALUES ( 'LIST_STUDENT');
-- INSERT INTO authority (description) VALUES ( 'REGISTRATION');


INSERT INTO userrole(iduser, idrole) VALUES (1,1);
INSERT INTO userrole(iduser, idrole) VALUES (2,1);
INSERT INTO userrole(iduser, idrole) VALUES (3,1);
INSERT INTO userrole(iduser, idrole) VALUES (3,2);

-- Student is Student
INSERT INTO userrole(iduser, idrole) VALUES (4,2);
-- Tutor is Tutor
INSERT INTO userrole(iduser, idrole) VALUES (5,3);



INSERT INTO category (title, description) VALUES ('Math', 'Mathematics related tutorials');
INSERT INTO category (title, description) VALUES ('Science', 'Science related tutorials');
INSERT INTO category (title, description) VALUES ('Programming', 'Programming related tutorials');
INSERT INTO category (title, description) VALUES ('Languages', 'Language learning tutorials');
