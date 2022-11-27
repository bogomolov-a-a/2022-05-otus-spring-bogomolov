CREATE TABLE PRODUCERS(
	id IDENTITY not null primary key,
	name varchar(1024) not null,
	address_id bigint not null,
	CONSTRAINT natural_key UNIQUE (name,address_id)
);