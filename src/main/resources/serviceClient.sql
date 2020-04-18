DROP TABLE IF EXISTS serviceClients;
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
	
CREATE TABLE serviceClients (
	scid INTEGER AUTO_INCREMENT,
	title VARCHAR(255),
	cid INT,
	boardMem VARCHAR(255),
	category VARCHAR(255),
	PRIMARY KEY (scid),
	FOREIGN KEY (cid) REFERENCES contacts(cid)
	);

INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES 
	('Tom', 'Hanks', 'thanks@gmail.com', '903-420-1212', '400-232-1211', '626 E Main Street', 'Sherman', 'TX', '75090');
	
INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES
	('Lois', 'Lane', 'llane86@gmail.com', '803-423-1257', '800-232-1211', '118 NW Crawford Street', 'Sherman', 'TX', '75090');
	
INSERT INTO serviceClients (title, cid, boardMem, category) VALUES ('Habitat for Humanity', 1, 'Billy Bob', 'Housing, Community');
INSERT INTO serviceClients (title, cid, boardMem, category) VALUES ('Crisis Center', 2, 'Lois Lane', 'Women, Crisis Support');

