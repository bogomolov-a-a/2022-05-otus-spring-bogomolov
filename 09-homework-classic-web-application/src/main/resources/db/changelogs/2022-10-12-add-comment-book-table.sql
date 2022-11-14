drop table if exists book_comments;
create table book_comments(
	id IDENTITY PRIMARY KEY,
	text varchar(255) not null,
	created timestamp not null,
	book_id bigint not null references books(id) ON DELETE CASCADE,
	constraint book_comments_natural_key unique (text,created,book_id)
);