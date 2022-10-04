drop table if exists  books;
drop table if exists authors;
drop table if exists genres;

create table authors(
	id IDENTITY PRIMARY KEY,
	surname varchar(255) not null,
	name varchar(255) not null,
	patronymic varchar(255) default 'not set' not null,
	birthYear bigint not null,
	deathYear bigint default null,
	constraint author_natural_key unique (surname,name,patronymic,birthYear)
);
create table genres(
	id IDENTITY PRIMARY KEY,
	name varchar(255) not null,
	constraint genre_natural_key unique (name)
);

create table books(
	id IDENTITY  PRIMARY KEY,
	title varchar(255) not null,
	isbn varchar(17) not null,
	author_id bigint not null REFERENCES authors(id) ON DELETE CASCADE,
	genre_id bigint not null REFERENCES genres(id) ON DELETE CASCADE,
	constraint book_natural_key unique(isbn,title,author_id)
);
