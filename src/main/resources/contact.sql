DROP TABLE IF EXISTS contacts;

CREATE TABLE contacts (
	cid INTEGER AUTO_INCREMENT,
	firstName VARCHAR(255),
	lastName VARCHAR(255),
	email VARCHAR(255),
	workPhone VARCHAR(255),
	mobilePhone VARCHAR(255),
	str VARCHAR(255),
	city VARCHAR(255),
	st VARCHAR(255),
	zip VARCHAR(255), 
	PRIMARY KEY (cid)
	);
	
INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES 
	('Tom', 'Hanks', 'thanks@gmail.com', '903-420-121', '400-232-121', '626 E Main Street', 'Sherman', 'TX', '75090');
	
INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES
	('Lois', 'Lane', 'llane86@gmail.com', '803-423-125', '800-232-121', '118 NW Crawford Street', 'Sherman', 'TX', '75090');