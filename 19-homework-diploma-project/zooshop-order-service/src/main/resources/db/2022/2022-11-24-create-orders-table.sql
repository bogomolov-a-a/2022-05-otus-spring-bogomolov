CREATE TABLE orders(
	id IDENTITY not null primary key,
	customer_id bigint not null,
	order_date timestamp not null,
  delivered_address_id bigint not null,
  description varchar(1024),
  delivered_date timestamp,
  order_status varchar(20),
	CONSTRAINT natural_key UNIQUE (customer_id,order_date,delivered_address_id)
);