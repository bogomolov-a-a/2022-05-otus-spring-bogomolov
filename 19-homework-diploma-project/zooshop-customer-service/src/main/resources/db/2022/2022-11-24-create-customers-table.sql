CREATE TABLE CUSTOMERS(
	id IDENTITY not null primary key,
	name varchar(1024) not null,
	surname varchar(1024) not null,
	patronymic varchar(1024),
	birth_date timestamp not null,
	phone varchar(20),
	email varchar(255),
	address_id bigint not null,
	CONSTRAINT natural_key UNIQUE (name,surname,patronymic,birth_date)
);