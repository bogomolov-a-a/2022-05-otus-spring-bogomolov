CREATE TABLE GOODS(
	id IDENTITY not null primary key,
	name varchar(1024) not null,
	description varchar(8192),
	price REAL not null,
	quantity REAL not null,
	good_unit varchar(100) not null,
	producer_id bigint not null,
	category_id bigint not null,
	CONSTRAINT natural_key UNIQUE (name,producer_id,price)
);