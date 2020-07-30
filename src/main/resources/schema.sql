DROP TABLE IF EXISTS serviceClients;
drop table if exists users;
drop table if exists eventParticipants;
drop table if exists eventType;
drop table if exists events;
drop table if exists serviceGroups;
DROP TABLE IF EXISTS servantUsers;
DROP TABLE IF EXISTS boardMemberUsers;
DROP TABLE IF EXISTS adminUsers;
DROP TABLE IF EXISTS contacts;

CREATE TABLE spotlight (
	sid INTEGER AUTO_INCREMENT,
	img BLOB,
	imgtype VARCHAR(10),
	imgsize INTEGER,
	spottxt VARCHAR,
	PRIMARY KEY (sid)
);

INSERT into spotlight (spottxt) values ('Welcome to the Austin College Service Station! <b>We help you help others</b>.');

CREATE TABLE contacts (
	contactId INTEGER AUTO_INCREMENT,
	firstName VARCHAR(255),
	lastName VARCHAR(255),
	email VARCHAR(255),
	primaryPhone VARCHAR(255),
	secondaryPhone VARCHAR(255),
	str VARCHAR(255),
	city VARCHAR(255),
	st VARCHAR(255),
	zip VARCHAR(255),
	PRIMARY KEY (contactId)
	);
	
CREATE TABLE users (
	userId integer auto_increment,
	username VARCHAR(255),
	contactId int,
	primary key (userId),
	foreign key (contactId)
		references contacts(contactId)
		on delete set NULL
	);


CREATE TABLE serviceClients (
	serviceClientId INTEGER AUTO_INCREMENT,
	title VARCHAR(255),
	primaryContactId INT,
	boardMemId INT,
	category VARCHAR(255),
	PRIMARY KEY (serviceClientId),
	FOREIGN KEY (primaryContactId)
		REFERENCES contacts(contactId)
		ON DELETE SET NULL,
	FOREIGN KEY (boardMemId)
		REFERENCES users(userId)
		ON DELETE SET NULL
	);
	
CREATE TABLE serviceGroups (
	serviceGroupId INTEGER AUTO_INCREMENT,
	shortName VARCHAR(255),
	title VARCHAR(255),
	contactId INT,
	primary key (serviceGroupId),
	foreign key (contactId)
		references contacts(contactId)
		on delete set NULL
	);
	
CREATE TABLE eventTypes (
	eventTypeId INTEGER AUTO_INCREMENT,
	name VARCHAR(255),
	description VARCHAR(255),
	defaultHours DOUBLE,
	pinHours boolean,
	serviceClientId INT,
	primary key (eventTypeId),
	foreign key (serviceClientId)
		references serviceClients(serviceClientId)
		on delete set NULL
	);

CREATE TABLE events (
	eventId integer auto_increment,
	title VARCHAR(255),
	address VARCHAR(255),
	contactId int,
	dateOf TIMESTAMP,
	eventTypeId int not null,
	continuous boolean,
	volunteersNeeded int,
	serviceClientId int,
	neededVolunteerHours double,
	rsvpVolunteerHours double,
	note VARCHAR(255),
	
	primary key (eventId),
	foreign key (contactId)
		references contacts(contactId)
		on delete set NULL,
	foreign key (eventTypeId)
		references eventTypes(eventTypeId)
		on delete set NULL,
	foreign key (serviceClientId)
		references serviceClients(serviceClientId)
		on delete set NULL
);

CREATE TABLE eventParticipants (
	eventParticipantId integer auto_increment,
	eventId integer,
	userId integer,
	primary key (eventParticipantId),
	foreign key (eventId)
		references events(eventId)
		on delete set NULL,
	foreign key (userId)
		references users(userId)
		on delete set NULL
);


CREATE TABLE serviceHours (
	serviceHourId INTEGER AUTO_INCREMENT,
	serviceClientId int,
	userId int,
	eventId int,
	hours VARCHAR(255),
	status VARCHAR(255),
	reflection VARCHAR(255),
	description VARCHAR(255),
	feedback VARCHAR(255),
	contactName VARCHAR(255),
	contactContact VARCHAR(255),
	primary key (serviceHourId),
	foreign key (serviceClientId)
		references serviceClients(serviceClientId)
		on delete set NULL,
	foreign key (userId)
		references users(userId)
		on delete set NULL,
	foreign key (eventId)
		references events(eventId)
		on delete set NULL
	);

CREATE TABLE servantUsers (
	userId INT,
	sgid INT,
	expectedGradYear INT,
	hasCar BOOLEAN,
	carCapacity INT,
	PRIMARY KEY (userId),
	FOREIGN KEY (userId)
		REFERENCES users(userId)
		ON DELETE CASCADE,
	FOREIGN KEY (sgid)
		REFERENCES serviceGroups(serviceGroupId)
		ON DELETE SET NULL
);

CREATE TABLE boardMemberUsers (
	userId INT,
	isCoChair BOOLEAN,
	PRIMARY KEY (userId),
	FOREIGN KEY (userId)
		REFERENCES servantUsers(userId)
		ON DELETE CASCADE
); 

CREATE TABLE adminUsers (
	userId INT,
	PRIMARY KEY (userId),
	FOREIGN KEY (userId)
		REFERENCES users(userId)
		ON DELETE CASCADE
);

INSERT INTO contacts (firstName, lastName, email, primaryPhone, secondaryPhone, str, city, st, zip) VALUES
	('Tom', 'Hanks', 'thanks@gmail.com', '903-420-1212', '400-232-1211', '626 E Main Street', 'Sherman', 'TX', '75090');

INSERT INTO contacts (firstName, lastName, email, primaryPhone, secondaryPhone, str, city, st, zip) VALUES
	('Lois', 'Lane', 'llane86@gmail.com', '803-423-1257', '800-232-1211', '118 NW Crawford Street', 'Sherman', 'TX', '75090');

INSERT INTO contacts (firstName, lastName, email, primaryPhone, secondaryPhone, str, city, st, zip) VALUES
	('Joe', 'Smith', 'jsmith12@gmail.com', '903-444-4440', '401-322-1201', '25 Frieda Drive', 'Gunter', 'TX', '75058');

INSERT INTO contacts (firstName, lastName, email, primaryPhone, secondaryPhone, str, city, st, zip) VALUES
	('Susan', 'Atkins', 'satkins67@gmail.com', '803-426-1527', '800-191-9412', '23 First Street', 'Denison', 'TX', '75021');

INSERT INTO contacts (firstName, lastName, email, primaryPhone, secondaryPhone, str, city, st, zip) VALUES
	('AJ', 'Pritchard', 'apritchard18@austincollege.edu', '253-886-2125', '253-886-2125', '23 First Street', 'Denison', 'TX', '75021');

INSERT INTO contacts (firstName, lastName, email, primaryPhone, secondaryPhone, str, city, st, zip) VALUES
	('Hunter', 'Couturier', 'hCouturier@gmail.com', '803-426-1527', '800-191-9412', '24 First Street', 'Denison', 'TX', '75021');

INSERT INTO contacts (firstName, lastName, email, primaryPhone, secondaryPhone, str, city, st, zip) VALUES
	('Emma', 'Driscoll', 'eDriscoll@gmail.com', '803-426-1527', '800-191-9412', '25 First Street', 'Denison', 'TX', '75021');

insert into users (username, contactId) values ('apritchard', 5);
insert into users (username, contactId) values ('hCouturier', 6);
insert into users (username, contactId) values ('eDriscoll', 7);
insert into users (username, contactId) values ('user', 1);

INSERT INTO serviceClients (title, primaryContactId,  boardMemId, category) VALUES ('Austin College Service Station', 1, 1, 'Variety');
INSERT INTO serviceClients (title, primaryContactId,  boardMemId, category) VALUES ('Habitat for Humanity', 2, 2, 'Community');
INSERT INTO serviceClients (title, primaryContactId,  boardMemId, category) VALUES ('Crisis Center', 3, 3, 'Crisis Support');
INSERT INTO serviceClients (title, primaryContactId,  boardMemId, category) VALUES ('For Testing Only', 2, 4, 'Crisis Support');

INSERT INTO serviceGroups (shortName, title, contactID) VALUES('DummyName01', 'DummyTitle01', 1);
INSERT INTO serviceGroups (shortName, title, contactID) VALUES('DummyName02', 'DummyTitle02', 2);
INSERT INTO serviceGroups (shortName, title, contactID) VALUES('DummyName03', 'DummyTitle03', 3);

INSERT INTO eventTypes (name, description, defaultHours, pinHours, serviceClientId) VALUES('gds', 'Great Day of Service', 2, false, 1);
INSERT INTO eventTypes (name, description, defaultHours, pinHours, serviceClientId) VALUES('fws', 'First We Serve', 2, true, 1);
INSERT INTO eventTypes (name, description, defaultHours, pinHours, serviceClientId) VALUES('rbd', 'Roo Bound', 3, true, 1);
INSERT INTO eventTypes (name, description, defaultHours, pinHours, serviceClientId) VALUES('ext', 'Community Service Client', 3, false, 1);

insert into events(title, address, contactId, dateOf, eventTypeId, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, note) values ('GDS2020', 'distributed', 1, '2020-01-01 00:00:00', 1, false, 5, 1, 5.0, 3.0, 'free text field');
insert into events(title, address, contactId, dateOf, eventTypeId, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, note) values ('Dummy Event 2', 'Dummy Address 2', 2, '2020-08-08 00:00:00', 2, true, 10, 2, 3.0, 1.5, 'free text field');
insert into events(title, address, contactId, dateOf, eventTypeId, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, note) values ('Dummy Event 3', 'Dummy Address 3', 3, '2020-03-03 00:00:00', 3, false, 15, 1, 4.0, 2.0, 'free text field');
insert into events(title, address, contactId, dateOf, eventTypeId, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, note) values ('Dummy Event 4', 'Dummy Address 4', 4, '2020-05-23 14:30:00', 1, false, 15, 3, 4.0, 2.0, 'free text field');
insert into events(title, address, contactId, dateOf, eventTypeId, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, note) values ('Dummy Event 5', 'Dummy Address 5', 5, '2020-06-30 10:15:00', 3, false, 15, 4, 4.0, 2.0, 'free text field');

insert into eventParticipants(eventId, userId) values (1, 2);
insert into eventParticipants(eventId, userId) values (2, 2);
insert into eventParticipants(eventId, userId) values (3, 2);
insert into eventParticipants(eventId, userId) values (1, 1);
insert into eventParticipants(eventId, userId) values (3, 1);

INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description, feedback, contactName, contactContact) VALUES (1, 1, 1, '3.0', 'Approved', 'I hated it', 'House building', 'Good work!', 'Billy Joel', '111-222-3333');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description, feedback, contactName, contactContact) VALUES (2, 2, 3, '2.0', 'Pending', 'Made food', 'Crisis Center', '', 'Bob Joe', 'joeyBob@gmail.com');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description, feedback, contactName, contactContact) VALUES (2, 3, 2, '1.5', 'Approved', 'Made friends', 'Crisis Center', 'Thanks for the help!', 'Ashley', '222-333-4444 dummyEmail@gmail.com');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description, feedback, contactName, contactContact) VALUES (1, 4, 4, '2.3', 'Approved', 'Met a guy named Randy', 'Landscaping', 'Keep up the good work', 'Rusty Buckle', 'rbuckle@yahoo.com');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description, feedback, contactName, contactContact) VALUES (1, 4, 5, '69', 'Pending', 'Met a MAN named Sandy', 'Landscoping', '', 'Whitney Houston', '333-444-5555');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description, feedback, contactName, contactContact) VALUES (1, 1, 5, '5.25', 'Approved', '', 'kitchen trim', '', 'Jack Reacher', 'jReacher18@gmail.com');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description, feedback, contactName, contactContact) VALUES (1, 1, 5, '4.5', 'Approved', '', 'clean up site', '', 'Jackie Chan', '444-555-6666');

INSERT INTO servantUsers (userId, sgid, expectedGradYear, hasCar, carCapacity) VALUES (1, 1, 2021, false, 0);
INSERT INTO servantUsers (userId, sgid, expectedGradYear, hasCar, carCapacity) VALUES (2, 3, 2023, true, 1);
INSERT INTO servantUsers (userId, sgid, expectedGradYear, hasCar, carCapacity) VALUES (4, 2, 2024, true, 3);

INSERT INTO boardMemberUsers (userId, isCoChair) VALUES (2, true);
INSERT INTO boardMemberUsers (userId, isCoChair) VALUES (4, false);

INSERT INTO adminUsers (userId) VALUES (3); 