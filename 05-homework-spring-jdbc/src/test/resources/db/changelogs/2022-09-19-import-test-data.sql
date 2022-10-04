--authors
insert into authors(surname,name,birthYear,deathYear) values('Bulychev','Kir',1934,2003);
insert into authors(surname,name,patronymic,birthYear,deathYear) values('Tolstoy','Lev','Nikolayevich',1828,1910);
--genres
insert into genres(name) values ('Novell');
insert into genres(name) values ('Science fiction');
--books
insert into books(title,isbn,author_id,genre_id) values('Girl from the Earth','978-5-699-11438-2',1,2);
insert into books(title,isbn,author_id,genre_id) values('Childhood. Boyhood. Youth','978-5-04-116640-3',2,1);