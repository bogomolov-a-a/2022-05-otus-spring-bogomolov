package ru.otus.group202205.homework.spring14;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.group202205.homework.spring14.config.FromSqlToNosqlDbMigratingJobConfig.FROM_SQL_TO_NOSQL_DB_MIGRATING_JOB_NAME;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import ru.otus.group202205.homework.spring14.config.BatchConfig;
import ru.otus.group202205.homework.spring14.config.FromSqlToNosqlDbMigratingJobConfig;
import ru.otus.group202205.homework.spring14.dao.jpa.AuthorEntityRepository;
import ru.otus.group202205.homework.spring14.dao.jpa.BookCommentEntityRepository;
import ru.otus.group202205.homework.spring14.dao.jpa.BookEntityRepository;
import ru.otus.group202205.homework.spring14.dao.jpa.DaoJpaConfig;
import ru.otus.group202205.homework.spring14.dao.jpa.GenreEntityRepository;
import ru.otus.group202205.homework.spring14.dao.mongo.AuthorDocumentRepository;
import ru.otus.group202205.homework.spring14.dao.mongo.BookCommentDocumentRepository;
import ru.otus.group202205.homework.spring14.dao.mongo.BookDocumentRepository;
import ru.otus.group202205.homework.spring14.dao.mongo.DaoMongoConfig;
import ru.otus.group202205.homework.spring14.dao.mongo.GenreDocumentRepository;
import ru.otus.group202205.homework.spring14.service.ServiceConfig;
import ru.otus.group202205.homework.spring14.testdata.TestDataConfig;
import ru.otus.group202205.homework.spring14.testdata.jpa.AuthorEntityTestDataComponent;
import ru.otus.group202205.homework.spring14.testdata.jpa.BookCommentEntityTestDataComponent;
import ru.otus.group202205.homework.spring14.testdata.jpa.BookEntityTestDataComponent;
import ru.otus.group202205.homework.spring14.testdata.jpa.GenreEntityTestDataComponent;
import ru.otus.group202205.homework.spring14.testdata.mongo.AuthorDocumentTestDataComponent;
import ru.otus.group202205.homework.spring14.testdata.mongo.BookCommentDocumentTestDataComponent;
import ru.otus.group202205.homework.spring14.testdata.mongo.BookDocumentTestDataComponent;
import ru.otus.group202205.homework.spring14.testdata.mongo.GenreDocumentTestDataComponent;

@SpringBatchTest
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@AutoConfigureDataMongo
@ImportAutoConfiguration
@SpringBootTest(classes = {FromSqlToNosqlDbMigratingJobConfig.class, ServiceConfig.class, DaoJpaConfig.class, DaoMongoConfig.class, BatchConfig.class,
    TestDataConfig.class})
class FromSqlToNosqlDbMigratingJobTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils;

  @Autowired
  private AuthorEntityRepository authorEntityRepository;
  @Autowired
  private AuthorEntityTestDataComponent authorEntityTestDataComponent;
  @Autowired
  private AuthorDocumentRepository authorDocumentRepository;
  @Autowired
  private AuthorDocumentTestDataComponent authorDocumentTestDataComponent;
  @Autowired
  private GenreEntityRepository genreEntityRepository;
  @Autowired
  private BookEntityRepository bookEntityRepository;
  @Autowired
  private BookEntityTestDataComponent bookEntityTestDataComponent;
  @Autowired
  private BookCommentEntityRepository bookCommentEntityRepository;
  @Autowired
  private BookCommentEntityTestDataComponent bookCommentEntityTestDataComponent;
  @Autowired
  private GenreEntityTestDataComponent genreEntityTestDataComponent;
  @Autowired
  private GenreDocumentRepository genreDocumentRepository;
  @Autowired
  private GenreDocumentTestDataComponent genreDocumentTestDataComponent;
  @Autowired
  private BookDocumentRepository bookDocumentRepository;
  @Autowired
  private BookDocumentTestDataComponent bookDocumentTestDataComponent;
  @Autowired
  private BookCommentDocumentRepository bookCommentDocumentRepository;
  @Autowired
  private BookCommentDocumentTestDataComponent bookCommentDocumentTestDataComponent;

  @Test
  void shouldBeFromSqlToNosqlDbDataMigratedWithRelations() throws
      Exception {
    Sort sortByIdAsc = Sort.by(Direction.ASC,
        "id");
    assertThat(authorEntityRepository.findAll(sortByIdAsc))
        .isNotEmpty()
        .isEqualTo(authorEntityTestDataComponent.getAllExistingAuthors());
    assertThat(genreEntityRepository.findAll(sortByIdAsc))
        .isNotEmpty()
        .isEqualTo(genreEntityTestDataComponent.getAllExistingGenres());
    assertThat(bookEntityRepository.findAll(sortByIdAsc))
        .isNotEmpty()
        .isEqualTo(bookEntityTestDataComponent.getAllExistingBooks());
    assertThat(bookCommentEntityRepository.findAll(sortByIdAsc))
        .isNotEmpty()
        .isEqualTo(bookCommentEntityTestDataComponent.getAllBookComments());
    assertThat(authorDocumentRepository.findAll(sortByIdAsc)).isEmpty();
    assertThat(genreDocumentRepository.findAll(sortByIdAsc)).isEmpty();
    assertThat(bookDocumentRepository.findAll(sortByIdAsc)).isEmpty();
    assertThat(bookCommentDocumentRepository.findAll(sortByIdAsc)).isEmpty();
    Job job = jobLauncherTestUtils.getJob();
    assertThat(job)
        .isNotNull()
        .extracting(Job::getName)
        .isEqualTo(FROM_SQL_TO_NOSQL_DB_MIGRATING_JOB_NAME);
    JobExecution jobExecution = jobLauncherTestUtils.launchJob();
    assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    assertThat(authorDocumentRepository.findAll(sortByIdAsc))
        .isNotEmpty()
        .isEqualTo(authorDocumentTestDataComponent.getAllExistingAuthors());
    assertThat(genreDocumentRepository.findAll(sortByIdAsc))
        .isNotEmpty()
        .isEqualTo(genreDocumentTestDataComponent.getAllExistingGenres());
    assertThat(bookDocumentRepository.findAll(sortByIdAsc))
        .isNotEmpty()
        .isEqualTo(bookDocumentTestDataComponent.getAllExistingBooks());
    assertThat(bookCommentDocumentRepository.findAll(sortByIdAsc))
        .isNotEmpty()
        .isEqualTo(bookCommentDocumentTestDataComponent.getAllBookComments());
  }

}
