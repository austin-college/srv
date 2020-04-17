drop table if exists users;

CREATE TABLE users (
	uid integer auto_increment,
	userID VARCHAR(255),
	password VARCHAR(255),
	totalHoursServed double,
	primary key (uid),
	cid int FOREIGN KEY REFERENCES contacts(cid)
	);

insert into serviceClients (title, contact, boardMem, category) values ('Habitat for Humanity', 'Tom Hanks', 'Billy Bob', 'Housing, Community');
insert into serviceClients (title, contact, boardMem, category) values ('Crisis Center', 'Jane Smith', 'Lois Lane', 'Women, Crisis Support');
insert into serviceClients (title, contact, boardMem, category) values ('Meals on Wheels', 'Rick Astley', 'Morgan Freeman', 'Senior, Community');
