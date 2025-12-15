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
INSERT INTO userrole(iduser, idrole) VALUES (3,3);

-- Student is Student
INSERT INTO userrole(iduser, idrole) VALUES (4,2);
-- Tutor is Tutor
INSERT INTO userrole(iduser, idrole) VALUES (5,3);



INSERT INTO category (title, description, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Math', 'Mathematics related tutorials', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO category (title, description, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Science', 'Science related tutorials', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO category (title, description, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Programming', 'Programming related tutorials', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO category (title, description, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Languages', 'Language learning tutorials', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);

INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Algebra Basics', 'Learn the fundamentals of algebra.', 49.99, 4.5, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Introduction to Physics', 'Explore the basic concepts of physics.', 59.99, 4.7, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Java Programming', 'Start your journey into Java programming.', 69.99, 4.8, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Spanish for Beginners', 'Learn basic Spanish phrases and grammar.', 39.99, 4.6, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Calculus I', 'An introduction to differential and integral calculus.', 79.99, 4.4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Chemistry Fundamentals', 'Understand the building blocks of matter.', 54.99, 4.5, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Music Theory 101', 'Learn the basics of music theory and notation.', 44.99, 4.3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);


INSERT INTO course_categories (id_course, id_category) VALUES (1, 1);
INSERT INTO course_categories (id_course, id_category) VALUES (1, 2);
INSERT INTO course_categories (id_course, id_category) VALUES (2, 2);
INSERT INTO course_categories (id_course, id_category) VALUES (3, 3);
INSERT INTO course_categories (id_course, id_category) VALUES (4, 4);
INSERT INTO course_categories (id_course, id_category) VALUES (5, 1);
INSERT INTO course_categories (id_course, id_category) VALUES (6, 2);

-- Chapters for 'Introduction to Physics' (course_id = 2)
INSERT INTO chapter (title, description, position, course_id, paywalled, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Einführung in die Physik', 'Was ist Physik? Überblick über die wichtigsten Themen und Methoden.', 1, 2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO chapter (title, description, position, course_id, paywalled, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Kräfte und Bewegung', 'Grundlagen der Mechanik: Kräfte, Masse, Beschleunigung, Newtonsche Gesetze.', 2, 2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO chapter (title, description, position, course_id, paywalled, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Energie und Arbeit', 'Energieformen, Energieerhaltung, Arbeit und Leistung in physikalischen Systemen.', 3, 2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO chapter (title, description, position, course_id, paywalled, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Wellen und Schwingungen', 'Eigenschaften von Wellen, Schwingungen, Schall und Licht.', 4, 2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO chapter (title, description, position, course_id, paywalled, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Elektrizität und Magnetismus', 'Grundlagen der Elektrizitätslehre und des Magnetismus.', 5, 2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);