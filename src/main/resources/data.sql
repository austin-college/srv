drop table if exists reasons;

CREATE TABLE reasons (
	rid integer auto_increment,
	reason VARCHAR(255),
	primary key (rid)
	);

insert into reasons (reason) values ('Assembly Drawing');
insert into reasons (reason) values ('Piece Part Drawing');


