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
	contactId INT,
	boardMem VARCHAR(255),
	category VARCHAR(255),
	PRIMARY KEY (serviceClientId),
	FOREIGN KEY (contactId) 
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
	foreign key (contactId) references contacts(contactId)
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

INSERT INTO serviceClients (title, contactId, boardMem, category) VALUES ('Habitat for Humanity', 1, 'Billy Bob', 'Housing, Community');
INSERT INTO serviceClients (title, contactId, boardMem, category) VALUES ('Crisis Center', 2, 'Lois Lane', 'Women, Crisis Support');

insert into users (username, password, totalHoursServed) values ('apritchard', '1234', 0);
insert into users (username, password, totalHoursServed) values ('hCouturier', '5678', 0);
insert into users (username, password, totalHoursServed) values ('eDriscoll', '1234', 0);

insert into reasons (reason) values ('Assembly Drawing');
insert into reasons (reason) values ('Piece Part Drawing');


