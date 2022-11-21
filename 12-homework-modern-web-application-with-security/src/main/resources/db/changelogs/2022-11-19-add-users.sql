drop table if exists users;
create table users(
	id IDENTITY PRIMARY KEY,
	login varchar2(255) not null unique,
	password varchar2(255) not null
);
insert into users (login,password) values ('admin','admin');
insert into users (login,password) values ('user','user');

create table roles(
	id IDENTITY PRIMARY KEY,
	name varchar2(255) not null unique
);
insert into roles (name) values ('admin');
insert into roles (name) values ('user');

create table user_roles(
	id IDENTITY PRIMARY KEY,
	user_id bigint not null references users(id),
	role_id bigint not null references roles(id),
	CONSTRAINT user_roles_natural_key UNIQUE (user_id,role_id)
);
insert into user_roles(user_id,role_id) values (1,1);
insert into user_roles(user_id,role_id) values (2,2);
