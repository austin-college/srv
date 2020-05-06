DROP TABLE IF EXISTS serviceClients;
drop table if exists users;
drop table if exists reasons;
drop table if exists eventParticipants;
drop table if exists eventType;
drop table if exists events;
drop table if exists serviceGroups;
DROP TABLE IF EXISTS contacts;

CREATE TABLE contacts (
	contactId INTEGER AUTO_INCREMENT,
	firstName VARCHAR(255),
	lastName VARCHAR(255),
	email VARCHAR(255),
	workPhone VARCHAR(255),
	mobilePhone VARCHAR(255),
	str VARCHAR(255),
	city VARCHAR(255),
	st VARCHAR(255),
	zip VARCHAR(255),
	PRIMARY KEY (contactId)
	);

CREATE TABLE serviceClients (
	serviceClientId INTEGER AUTO_INCREMENT,
	title VARCHAR(255),
	primaryContactId INT,
	secondContactId INT,
	boardMem VARCHAR(255),
	category VARCHAR(255),
	PRIMARY KEY (serviceClientId),
	FOREIGN KEY (primaryContactId)
		REFERENCES contacts(contactId)
		ON DELETE SET NULL,
	FOREIGN KEY (secondContactId)
		REFERENCES contacts(contactId)
		ON DELETE SET NULL
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

CREATE TABLE reasons (
	rid integer auto_increment,
	reason VARCHAR(255),
	primary key (rid)
	);

CREATE TABLE events (
	eventId integer auto_increment,
	title VARCHAR(255),
	address VARCHAR(255),
	contactId int,
	dateOf VARCHAR(255),
	eventType VARCHAR(255),
	continuous boolean,
	volunteersNeeded int,
	serviceClientId int,
	neededVolunteerHours double,
	rsvpVolunteerHours double,
	freeTextField VARCHAR(255),
	primary key (eventId),
	foreign key (contactId)
		references contacts(contactId)
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
	defaultHours INT,
	pinHours boolean,
	serviceClientId INT,
	serviceGroupId INT,
	primary key (eventTypeId),
	foreign key (serviceClientId)
		references events(serviceClientId)
		on delete set NULL,
	foreign key (serviceGroupId)
		references serviceGroups(serviceGroupId)
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


INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES
	('Tom', 'Hanks', 'thanks@gmail.com', '903-420-1212', '400-232-1211', '626 E Main Street', 'Sherman', 'TX', '75090');

INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES
	('Lois', 'Lane', 'llane86@gmail.com', '803-423-1257', '800-232-1211', '118 NW Crawford Street', 'Sherman', 'TX', '75090');

INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES
	('Joe', 'Smith', 'jsmith12@gmail.com', '903-444-4440', '401-322-1201', '25 Frieda Drive', 'Gunter', 'TX', '75058');

INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES
	('Susan', 'Atkins', 'satkins67@gmail.com', '803-426-1527', '800-191-9412', '23 First Street', 'Denison', 'TX', '75021');

INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES
	('AJ', 'Pritchard', 'apritchard18@austincollege.edu', '253-886-2125', '253-886-2125', '23 First Street', 'Denison', 'TX', '75021');

INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES
	('Hunter', 'Couturier', 'hCouturier@gmail.com', '803-426-1527', '800-191-9412', '24 First Street', 'Denison', 'TX', '75021');

INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES
	('Emma', 'Driscoll', 'eDriscoll@gmail.com', '803-426-1527', '800-191-9412', '25 First Street', 'Denison', 'TX', '75021');

INSERT INTO serviceClients (title, primaryContactId, secondContactId, boardMem, category) VALUES ('Habitat for Humanity', 1, 4, 'Billy Bob', 'Community');
INSERT INTO serviceClients (title, primaryContactId, secondContactId, boardMem, category) VALUES ('Crisis Center', 2, 3, 'Rick Astley', 'Crisis Support');

insert into users (username, contactId) values ('apritchard', 4);
insert into users (username, contactId) values ('hCouturier', 5);
insert into users (username, contactId) values ('eDriscoll', 6);

insert into reasons (reason) values ('Assembly Drawing');
insert into reasons (reason) values ('Piece Part Drawing');

insert into events(title, address, contactId, dateOf, eventType, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, freeTextField) values ('Dummy Event 1', 'Dummy Address 1', 1, 'JANSERVE', '01/01/2020', false, 5, 1, 5.0, 3.0, 'free text field');
insert into events(title, address, contactId, dateOf, eventType, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, freeTextField) values ('Dummy Event 2', 'Dummy Address 2', 2, 'GREENSERVE', '05/05/2020', false, 10, 2, 3.0, 1.5, 'free text field');
insert into events(title, address, contactId, dateOf, eventType, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, freeTextField) values ('Dummy Event 3', 'Dummy Address 3', 3, 'HELPFUL EVENT', '03/03/2020', false, 15, 1, 4.0, 2.0, 'free text field');

insert into eventParticipants(eventId, userId) values (1, 2);
insert into eventParticipants(eventId, userId) values (2, 2);
insert into eventParticipants(eventId, userId) values (3, 2);
insert into eventParticipants(eventId, userId) values (1, 1);
insert into eventParticipants(eventId, userId) values (3, 1);


INSERT INTO serviceGroups (shortName, title, contactID) VALUES('DummyName01', 'DummyTitle01', 1);
INSERT INTO serviceGroups (shortName, title, contactID) VALUES('DummyName02', 'DummyTitle02', 2);
INSERT INTO serviceGroups (shortName, title, contactID) VALUES('DummyName03', 'DummyTitle03', 3);


INSERT INTO eventTypes (name, description, defaultHours, pinHours, serviceClientId, serviceGroupId) VALUES('gds', 'Great Day of Service', 2, true, 1, 1);
INSERT INTO eventTypes (name, description, defaultHours, pinHours, serviceClientId, serviceGroupId) VALUES('fws', 'First We Serve', 2, true, 2, 2);
INSERT INTO eventTypes (name, description, defaultHours, pinHours, serviceClientId, serviceGroupId) VALUES('rbd', 'Roo Bound', 3, true, 1, 3);


INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description) VALUES (1, 1, 1, '3.0', 'Approved', 'I hated it', 'House building');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description) VALUES (2, 2, 1, '2.0', 'Pending', 'Made food', 'Crisis Center');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description) VALUES (2, 3, 2, '1.5', 'Approved', 'Made friends', 'Crisis Center');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description) VALUES (1, 2, 1, '2.3', 'Approved', 'Met a guy named Randy', 'Landscaping');
INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description) VALUES (1, 2, 1, '69', 'Pending', 'Met a MAN named Sandy', 'Landscoping');
