CREATE TABLE actions(
	id IDENTITY not null primary key,
	name varchar(255) not null,
	description varchar(1024),
	start_date timestamp not null,
	end_date timestamp,
	discount_type varchar(100),
	discount_value float,
	good_id bigint not null,
	CONSTRAINT natural_key UNIQUE (name, good_id, start_date)
);