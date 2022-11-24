--authors
insert into authors(surname,name,birth_year,death_year) values('Bulychev','Kir',1934,2003);
insert into authors(surname,name,patronymic,birth_year,death_year) values('Tolstoy','Lev','Nikolayevich',1828,1910);
--genres
insert into genres(name) values ('Novell');
insert into genres(name) values ('Science fiction');
--books
insert into books(title,isbn,author_id,genre_id) values('Girl from the Earth','978-5-699-11438-2',1,2);
insert into books(title,isbn,author_id,genre_id) values('Childhood. Boyhood. Youth','978-5-04-116640-3',2,1);
--book comments
insert into book_comments(text,created,book_id) values('Nice book!',parsedatetime('2022-10-26 22:35:31','yyyy-MM-dd HH:mm:ss'),1);
insert into book_comments(text,created,book_id) values('I like this author!',parsedatetime('2022-10-26 22:35:32','yyyy-MM-dd HH:mm:ss'),1);
insert into book_comments(text,created,book_id) values('Complicated book!',parsedatetime('2022-10-26 22:35:32','yyyy-MM-dd HH:mm:ss'),2);
