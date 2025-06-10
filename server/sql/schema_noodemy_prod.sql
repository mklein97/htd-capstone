DROP DATABASE IF EXISTS noodemy;
CREATE database noodemy;
USE noodemy;

CREATE TABLE `app_role`(
    `role_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `role_name` VARCHAR(255) NOT NULL,
    `role_description` VARCHAR(255) NOT NULL
);
ALTER TABLE
    `app_role` ADD UNIQUE `app_role_role_name_unique`(`role_name`);
CREATE TABLE `enrollment`(
    `enrollment_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `course_id` INT NOT NULL
);
CREATE TABLE `user_profile`(
    `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `first_name` VARCHAR(255) NOT NULL,
    `last_name` VARCHAR(255) NOT NULL,
    `dob` DATE NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `app_user_id` INT NOT NULL
);
ALTER TABLE
    `user_profile` ADD UNIQUE `user_profile_email_unique`(`email`);
CREATE TABLE `app_user`(
    `app_user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL,
    `password_hash` VARCHAR(255) NOT NULL,
    `disabled` TINYINT NOT NULL,
    `role_id` INT NOT NULL
);
CREATE TABLE `course`(
    `course_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `course_name` VARCHAR(255) NOT NULL,
    `course_description` VARCHAR(255) NOT NULL,
    `price` DECIMAL(8, 2) NOT NULL,
    `estimate_duration` INT NOT NULL,
    `category_id` INT NOT NULL
);
CREATE TABLE `comment`(
    `comment_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `enrollment_id` INT NOT NULL,
    `created_at` DATE NOT NULL,
    `comment` VARCHAR(255) NOT NULL
);
CREATE TABLE `category`(
    `category_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `category_name` VARCHAR(255) NOT NULL,
    `category_description` VARCHAR(255) NOT NULL,
    `category_code` VARCHAR(255) NOT NULL
);

ALTER TABLE
    `app_user` ADD CONSTRAINT `app_user_role_id_foreign` FOREIGN KEY(`role_id`) REFERENCES `app_role`(`role_id`);
ALTER TABLE
    `user_profile` ADD CONSTRAINT `user_profile_app_user_id_foreign` FOREIGN KEY(`app_user_id`) REFERENCES `app_user`(`app_user_id`);
ALTER TABLE
    `comment` ADD CONSTRAINT `comment_enrollment_id_foreign` FOREIGN KEY(`enrollment_id`) REFERENCES `enrollment`(`enrollment_id`) ON DELETE CASCADE;
ALTER TABLE
    `enrollment` ADD CONSTRAINT `enrollment_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user_profile`(`user_id`);
ALTER TABLE
    `course` ADD CONSTRAINT `course_category_id_foreign` FOREIGN KEY(`category_id`) REFERENCES `category`(`category_id`);
ALTER TABLE
    `enrollment` ADD CONSTRAINT `enrollment_course_id_foreign` FOREIGN KEY(`course_id`) REFERENCES `course`(`course_id`);
    
-- Default role --
INSERT INTO app_role(role_name, role_description) VALUES
    ('ADMIN', 'Instructor'),
    ('USER', 'Student');

INSERT INTO app_user(username, password_hash, disabled, role_id) VALUES
     ('noodemyAdmin', 'admin', 0, 1), -- ADMIN
     ('quangdu', 'quangdu', 0, 2), -- USER
	 ('matthew', 'matthew', 0, 2), -- USER
     ('maryjane', 'maryjane', 1, 2); -- USER

INSERT INTO user_profile(first_name, last_name, dob, email, app_user_id) VALUES
     ('Quang', 'Pham', '1996-01-15', 'admin@gmail.com', 1), -- ADMIN
     ('Quang', 'Du', '1980-09-09', 'student1@gmail.com', 2), -- STUDENT
     ('Matthew', 'Klein', '2000-03-05', 'student2@gmail.com', 3),
     ('Mary', 'Jane', '2000-03-05', 'maryjane@gmail.com', 4); -- STUDENT

INSERT INTO category(category_name, category_description, category_code) VALUES
    ('Computer Science', 'Covers programming and IT-related subjects', 'CS'),
    ('Literature', 'Focusing on literature, reading comprehension, and writing skills', 'LIT'),
    ('Business', 'Includes topics on management, finance, and entrepreneurship', 'BUS'),
    ('Psychology', 'Study of the mind and behavior', 'PSY'),
    ('Chemistry', 'Covers the study of substances, their properties, and reactions', 'CHEM'),
    ('Philosophy', 'Explores fundamental questions about existence, knowledge, and ethics', 'PHIL');

INSERT INTO course (course_name, course_description, price, estimate_duration, category_id) VALUES
	-- Computer Science
	('Introduction to Python Programming', 'Learn the basics of Python programming, including syntax, data types, and control structures.', 49, 10, 1),
	('Data Structures and Algorithms in Java', 'Master the fundamentals of data structures and algorithms using Java, a key skill for technical interviews.', 69, 15, 1),

	-- Literature
	('Shakespearean Tragedies: An In-Depth Analysis', 'Explore the themes, characters, and historical context of Shakespeare''s greatest tragedies.', 39, 8, 2),
	('Modern American Literature', 'A survey of major works and movements in 20th-century American literature.', 44, 12, 2),

	-- Business
	('Principles of Marketing', 'Understand core marketing concepts, market research, and customer segmentation.', 59, 10, 3),
	('Financial Accounting Basics', 'Learn the essentials of financial accounting for small businesses and startups.', 69, 14, 3),

	-- Psychology
	('Introduction to Cognitive Psychology', 'Discover how we perceive, remember, and think through the lens of cognitive psychology.', 39, 7, 4),
	('Social Psychology: The Science of Human Behavior', 'Learn how social interactions and group dynamics shape human behavior.', 49, 10, 4),

	-- Chemistry
	('General Chemistry: Foundations', 'A complete guide to atomic structure, periodic trends, chemical bonding, and more.', 59, 15, 5),
	('Organic Chemistry Basics', 'Learn the essential concepts of organic chemistry, including hydrocarbons, functional groups, and reactions.', 69, 20, 5),

	-- Philosophy
	('Introduction to Ethics', 'Explore major ethical theories and their application to contemporary issues.', 34, 8, 6),
	('The Philosophy of Mind', 'Investigate questions of consciousness, identity, and the mind-body problem.', 44, 10, 6);

INSERT INTO enrollment(user_id, course_id) VALUES
	(2, 1),
	(2, 3),
	(2, 5),
	(3, 2),
	(3, 4),
	(3, 6),
	(4, 1),
	(4, 3),
	(4, 5),
	(4, 7);

INSERT INTO comment (enrollment_id, created_at, comment) VALUES
	(1, '2025-04-10', 'Great course! Learned a lot.'),
	(2, '2025-03-25', 'The instructor was very helpful.'),
	(3, '2025-02-18', 'I would love more examples next time.'),
	(4, '2025-04-02', 'Awesome course, highly recommended!'),
	(5, '2025-05-05', 'Challenging, but worth it!'),
	(6, '2025-03-14', 'Good course, but could use more visuals.'),
	(7, '2025-02-28', 'I enjoyed the group discussions.'),
	(8, '2025-04-21', 'Very engaging, thank you.'),
	(9, '2025-05-10', 'A bit advanced for me, but well structured.'),
	(10, '2025-03-05', 'Excellent coverage of the topic.');