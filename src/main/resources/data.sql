
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('Lilly', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'lilly@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('Paul', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'paul@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('Denis', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'denis@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('Tutor', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'tutor@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('Student', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'student@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);

INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('anja', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'anja@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('tanja', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'tanja@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('martin', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'martin@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('lisa', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'lisa@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('mario', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'mario@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('peter', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'peter@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('leonard', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'leonard@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by)
VALUES ('markus', '{bcrypt}$2a$12$69GBDheB9KxZ4p4Zl9BLueq.C3ONV1VMxvx/cyoIVmzkgRziB9uFa', 'markus@email', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);


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

INSERT INTO course (title, description, price, rating) VALUES ('Algebra Basics', 'Learn the fundamentals of algebra.', 49.99, 4.5);
INSERT INTO course (title, description, price, rating) VALUES ('Introduction to Physics', 'Explore the basic concepts of physics.', 59.99, 4.7);
INSERT INTO course (title, description, price, rating) VALUES ('Java Programming', 'Start your journey into Java programming.', 69.99, 4.8);
INSERT INTO course (title, description, price, rating) VALUES ('Spanish for Beginners', 'Learn basic Spanish phrases and grammar.', 39.99, 4.6);
INSERT INTO course (title, description, price, rating) VALUES ('Calculus I', 'An introduction to differential and integral calculus.', 79.99, 4.4);
INSERT INTO course (title, description, price, rating) VALUES ('Chemistry Fundamentals', 'Understand the building blocks of matter.', 54.99, 4.5);
INSERT INTO course (title, description, price, rating) VALUES ('Music Theory 101', 'Learn the basics of music theory and notation.', 44.99, 4.3);


INSERT INTO course_categories (id_course, id_category) VALUES (1, 1);
INSERT INTO course_categories (id_course, id_category) VALUES (1, 2);
INSERT INTO course_categories (id_course, id_category) VALUES (2, 2);
INSERT INTO course_categories (id_course, id_category) VALUES (3, 3);
INSERT INTO course_categories (id_course, id_category) VALUES (4, 4);
INSERT INTO course_categories (id_course, id_category) VALUES (5, 1);
INSERT INTO course_categories (id_course, id_category) VALUES (6, 2);