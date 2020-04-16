drop table if exists serviceClients;

CREATE TABLE serviceClients (
	scid integer auto_increment,
	title VARCHAR(255),
	contact VARCHAR(255),
	boardMem VARCHAR(255),
	category VARCHAR(255),
	primary key (scid)
	);

insert into serviceClients (title, contact, boardMem, category) values ('Meals on Wheels', 'Tom Hanks', 'Billy Bob', 'Housing, Community');
