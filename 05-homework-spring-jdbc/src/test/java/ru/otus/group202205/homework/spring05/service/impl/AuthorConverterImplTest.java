package ru.otus.group202205.homework.spring05.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.group202205.homework.spring05.model.Author;
import ru.otus.group202205.homework.spring05.service.AuthorConverter;

@SpringBootTest(classes = {AuthorConverterImpl.class})
class AuthorConverterImplTest {

  @Autowired
  private AuthorConverter authorConverter;

  @Test
  void shouldBeConvertAuthorWithFullInfoToString() {
    Author author = new Author();
    author.setSurname("Makise");
    author.setName("Kurisu");
    author.setBirthYear(1992L);
    author.setId(3L);
    String actualAuthorOutput = authorConverter.convertAuthor(author);
    assertThat(actualAuthorOutput)
        .isNotNull()
        .isEqualTo("Author id: 3" + System.lineSeparator() + "surname: Makise" + System.lineSeparator() + "name: Kurisu" + System.lineSeparator()
            + "patronymic: not set" + System.lineSeparator() + "birth year: 1992" + System.lineSeparator() + "death year: null" + System.lineSeparator());
  }

  @Test
  void shouldBeConvertAuthorsWithFullInfoToString() {
    Author firstAuthor = new Author();
    firstAuthor.setSurname("Makise");
    firstAuthor.setName("Kurisu");
    firstAuthor.setBirthYear(1992L);
    firstAuthor.setId(3L);
    Author secondAuthor = new Author();
    secondAuthor.setSurname("Houoin");
    secondAuthor.setName("Kyoma");
    secondAuthor.setPatronymic("Phoenix");
    secondAuthor.setBirthYear(1991L);
    secondAuthor.setDeathYear(2025L);
    secondAuthor.setId(4L);

    String actualAuthorOutput = authorConverter.convertAuthors(List.of(firstAuthor,
        secondAuthor));
    assertThat(actualAuthorOutput)
        .isNotNull()
        .isEqualTo(
            "Author list" + System.lineSeparator() + "Author id: 3" + System.lineSeparator() + "surname: Makise" + System.lineSeparator() + "name: Kurisu"
                + System.lineSeparator()
                + "patronymic: not set" + System.lineSeparator() + "birth year: 1992" + System.lineSeparator() + "death year: null" + System.lineSeparator()
                + "Author id: 4" + System.lineSeparator() + "surname: Houoin" + System.lineSeparator() + "name: Kyoma" + System.lineSeparator()
                + "patronymic: Phoenix" + System.lineSeparator() + "birth year: 1991" + System.lineSeparator() + "death year: 2025" + System.lineSeparator());
  }

}