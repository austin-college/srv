drop table if exists serviceClients;

CREATE TABLE serviceClients (
	scid integer auto_increment,
	title VARCHAR(255),
	contact VARCHAR(255),
	boardMem VARCHAR(255),
	category VARCHAR(255),
	primary key (scid)
	);

insert into serviceClients (title) values ('Work pl0x');
insert into serviceClients (contact) values ('Tom Hanks');
insert into serviceClients (boardMem) values ('Wilson');
insert into serviceClients (category) values ('Community, Housing');