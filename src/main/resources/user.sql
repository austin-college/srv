drop table if exists users;

CREATE TABLE users (
	uid integer auto_increment,
	userID VARCHAR(255),
	password VARCHAR(255),
	totalHoursServed double,
	primary key (uid),
	cid int FOREIGN KEY REFERENCES contacts(cid)
	);

insert into users (userID, password, totalHoursServed) values ('apritchard', '1234', 0);
insert into users (userID, password, totalHoursServed) values ('hCouturier', '5678', 0);
insert into users (userID, password, totalHoursServed) values ('eDriscoll', '1234', 0);
