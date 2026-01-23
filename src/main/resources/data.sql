-- Passwords are bcrypt-hashed versions of 'Password123'
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('Lilli', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'lilli@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('Paul', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'paul@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('Denis', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'denis@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);

-- 4th user is Tutor
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('Tutor', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'tutor@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
-- 5th user is Student
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('Student', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'student@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);

INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('anja', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'anja@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('tanja', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'tanja@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('martin', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'martin@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('lisa', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'lisa@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('mario', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'mario@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('peter', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'peter@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('leonard', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'leonard@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);
INSERT INTO user (username, password, email, active, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by, verified, two_factor_enabled, account_non_locked, strikes)
VALUES ('markus', '{bcrypt}$2a$12$aqjGiDTeIey5KFiZ.q/MjuvrnNAixFwqP1P4zQUOmXKjFi87pcClW', 'markus@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL, true, false, true, 0);




INSERT INTO user_roles(user_id, role) VALUES (1, 'ADMIN');
INSERT INTO user_roles(user_id, role) VALUES (1, 'STUDENT');
INSERT INTO user_roles(user_id, role) VALUES (1, 'TUTOR');
INSERT INTO user_roles(user_id, role) VALUES (2, 'ADMIN');
INSERT INTO user_roles(user_id, role) VALUES (2, 'STUDENT');
INSERT INTO user_roles(user_id, role) VALUES (2, 'TUTOR');
INSERT INTO user_roles(user_id, role) VALUES (3, 'ADMIN');
INSERT INTO user_roles(user_id, role) VALUES (3, 'STUDENT');
INSERT INTO user_roles(user_id, role) VALUES (3, 'TUTOR');
INSERT INTO user_roles(user_id, role) VALUES (4, 'TUTOR');
INSERT INTO user_roles(user_id, role) VALUES (5, 'STUDENT');
INSERT INTO user_roles(user_id, role) VALUES (6, 'STUDENT');



-- Categories
INSERT INTO category (title, description, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Math', 'Mathematics related tutorials', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO category (title, description, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Science', 'Science related tutorials', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO category (title, description, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Programming', 'Programming related tutorials', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO category (title, description, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Languages', 'Language learning tutorials', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);

-- Courses
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Algebra Basics', 'Learn the fundamentals of algebra.', 49.99, 4.5, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Introduction to Physics', 'Explore the basic concepts of physics.', 59.99, 4.7, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Java Programming', 'Start your journey into Java programming.', 69.99, 4.8, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Spanish for Beginners', 'Learn basic Spanish phrases and grammar.', 39.99, 4.6, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Calculus I', 'An introduction to differential and integral calculus.', 79.99, 4.4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Chemistry Fundamentals', 'Understand the building blocks of matter.', 54.99, 4.5, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) VALUES ('Music Theory 101', 'Learn the basics of music theory and notation.', 44.99, 4.3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);


-- ========================================================================================================================
-- Paul - Fuer Tests
INSERT INTO course (title, description, price, rating, owner_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES ('Tutor-Testkurs', 'Ein Testkurs', 29.99, 4.0, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);

-- Hashtags (ohne creator_id - jetzt pro-Kurs ueber CourseHashtagLink)
INSERT INTO hashtag (name, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES ('anfaenger', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO hashtag (name, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES ('klausurvorbereitung', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO hashtag (name, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES ('fortgeschritten', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO hashtag (name, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES ('praxisorientiert', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO hashtag (name, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES ('theorie', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);

-- Verknuepfungen: course_hashtag_link (course_id, hashtag_id, added_by_id)
INSERT INTO course_hashtag_link (course_id, hashtag_id, added_by_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES (1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course_hashtag_link (course_id, hashtag_id, added_by_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES (1, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course_hashtag_link (course_id, hashtag_id, added_by_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES (2, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course_hashtag_link (course_id, hashtag_id, added_by_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES (2, 5, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course_hashtag_link (course_id, hashtag_id, added_by_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES (3, 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course_hashtag_link (course_id, hashtag_id, added_by_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES (3, 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course_hashtag_link (course_id, hashtag_id, added_by_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES (5, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
INSERT INTO course_hashtag_link (course_id, hashtag_id, added_by_id, created_at, updated_at, created_by, updated_by, deleted_at, deleted_by) 
VALUES (5, 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL);
-- ========================================================================================================================

-- Verknuepfungen: course_categories (id_course, id_category)
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

-- Chapter Preise (fuer paywalled Chapters)
UPDATE chapter SET price = 5.00, paywalled = true WHERE id = 1;
UPDATE chapter SET price = 7.50, paywalled = true WHERE id = 3;
UPDATE chapter SET price = 3.00, paywalled = true WHERE id = 5;

-- ========================================================================================================================
-- FUER Booking-Refactoring (21.01.2026)
-- Hourly Rates fuer ALLE Kurs-Owner
UPDATE user SET hourly_rate = 25.00 WHERE id = 1;  -- Lilly
UPDATE user SET hourly_rate = 30.00 WHERE id = 2;  -- Paul
UPDATE user SET hourly_rate = 25.00 WHERE id = 3;  -- Denis
UPDATE user SET hourly_rate = 30.00 WHERE id = 4;
UPDATE user SET hourly_rate = 20.00 WHERE id = 5;  -- Tutor

-- TimeSlots fuer Tutor (id=4, User "Tutor" / thomi@web.de)
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-25', '14:00:00', '15:00:00', true, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-26', '10:00:00', '11:00:00', true, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-27', '16:00:00', '17:00:00', true, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-25', '14:00:00', '15:00:00', true, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-26', '10:00:00', '11:00:00', true, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-27', '16:00:00', '17:00:00', true, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-25', '14:00:00', '15:00:00', true, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-26', '10:00:00', '11:00:00', true, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-27', '16:00:00', '17:00:00', true, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 

VALUES ('2026-01-25', '14:00:00', '15:00:00', true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-26', '10:00:00', '11:00:00', true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO timeslot (date, start_time, end_time, available, tutor_id, created_at, updated_at) 
VALUES ('2026-01-27', '16:00:00', '17:00:00', true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
