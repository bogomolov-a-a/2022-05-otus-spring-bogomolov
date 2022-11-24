CREATE TABLE CATEGORIES(
	id IDENTITY not null primary key,
	name varchar(255) not null,
	parent_id bigint,
	CONSTRAINT natural_key UNIQUE (name, parent_id)
);