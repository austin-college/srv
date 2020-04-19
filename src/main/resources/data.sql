DROP TABLE IF EXISTS serviceClients;
drop table if exists users;
drop table if exists reasons;
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
	password VARCHAR(255),
	totalHoursServed double,
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

INSERT INTO serviceClients (title, primaryContactId, secondContactId, boardMem, category) VALUES ('Habitat for Humanity', (select contactId from contacts where contactId = 1), (select contactId from contacts where contactId = 4), 'Billy Bob', 'Housing, Community');
INSERT INTO serviceClients (title, primaryContactId, secondContactId, boardMem, category) VALUES ('Crisis Center', (select contactId from contacts where contactId = 2), (select contactId from contacts where contactId = 3), 'Rick Astley', 'Women, Crisis Support');

insert into users (username, password, totalHoursServed, contactId) values ('apritchard', '1234', 0, (select contactId from contacts where contactId = 4));
insert into users (username, password, totalHoursServed, contactId) values ('hCouturier', '5678', 0, (select contactId from contacts where contactId = 5));
insert into users (username, password, totalHoursServed, contactId) values ('eDriscoll', '1234', 0, (select contactId from contacts where contactId = 6));

insert into reasons (reason) values ('Assembly Drawing');
insert into reasons (reason) values ('Piece Part Drawing');


