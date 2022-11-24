CREATE TABLE ADDRESSES(
	id IDENTITY not null primary key,
	postal_code varchar (10) not null,
  country varchar(255) not null,
  state varchar(255),
  city  varchar(255) not null,
  locality varchar(255),
	district varchar(255),
  street varchar(255),
  house varchar(255),
  room int,
  specific_part varchar(255),
	CONSTRAINT natural_key UNIQUE (postal_code,country,state,city,locality,district,street,house,room,specific_part)
);