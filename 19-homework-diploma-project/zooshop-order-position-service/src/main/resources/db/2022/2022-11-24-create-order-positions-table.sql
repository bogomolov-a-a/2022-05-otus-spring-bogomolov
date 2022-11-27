CREATE TABLE order_positions(
	id IDENTITY not null primary key,
	name varchar(255) not null,
	good_id bigint not null,
	action_id bigint not null,
	order_id bigint not null,
	quantity float not null,
	CONSTRAINT natural_key UNIQUE (good_id, action_id, order_id)
);