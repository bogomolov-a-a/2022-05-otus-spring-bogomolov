package ru.otus.group202205.homework.spring14.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.group202205.homework.spring14.model.jpa.AuthorEntity;
import ru.otus.group202205.homework.spring14.model.jpa.BookCommentEntity;
import ru.otus.group202205.homework.spring14.model.jpa.BookEntity;
import ru.otus.group202205.homework.spring14.model.jpa.GenreEntity;
import ru.otus.group202205.homework.spring14.model.mongo.AuthorDocument;
import ru.otus.group202205.homework.spring14.model.mongo.BookCommentDocument;
import ru.otus.group202205.homework.spring14.model.mongo.BookDocument;
import ru.otus.group202205.homework.spring14.model.mongo.GenreDocument;
import ru.otus.group202205.homework.spring14.service.processor.AuthorProcessor;
import ru.otus.group202205.homework.spring14.service.processor.BookCommentProcessor;
import ru.otus.group202205.homework.spring14.service.processor.BookProcessor;
import ru.otus.group202205.homework.spring14.service.processor.GenreProcessor;
import ru.otus.group202205.homework.spring14.service.reader.AuthorEntityRepositoryItemReader;
import ru.otus.group202205.homework.spring14.service.reader.BookCommentEntityRepositoryItemReader;
import ru.otus.group202205.homework.spring14.service.reader.BookEntityRepositoryItemReader;
import ru.otus.group202205.homework.spring14.service.reader.GenreEntityRepositoryItemReader;
import ru.otus.group202205.homework.spring14.service.writer.AuthorDocumentRepositoryItemWriter;
import ru.otus.group202205.homework.spring14.service.writer.BookCommentDocumentItemWriter;
import ru.otus.group202205.homework.spring14.service.writer.BookDocumentItemWriter;
import ru.otus.group202205.homework.spring14.service.writer.GenreDocumentRepositoryItemWriter;

@Configuration
@RequiredArgsConstructor
public class FromSqlToNosqlDbMigratingJobConfig {

  public static final String FROM_SQL_TO_NOSQL_DB_MIGRATING_JOB_NAME = "fromSqlToNosqlDbMigratingJob";
  private static final String AUTHOR_MIGRATING_STEP_NAME = "authorMigratingStep";
  private static final String GENRE_MIGRATING_STEP_NAME = "genreMigratingStep";
  private static final String BOOK_MIGRATING_STEP_NAME = "bookMigratingStep";
  private static final String BOOK_COMMENT_MIGRATING_STEP_NAME = "bookCommentMigratingStep";
  private static final int CHUNK_SIZE = 5;

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job fromSqlToNosqlDbMigratingJob(Step authorMigratingStep, Step genreMigratingStep, Step bookMigratingStep, Step bookCommentMigratingStep) {
    return jobBuilderFactory
        .get(FROM_SQL_TO_NOSQL_DB_MIGRATING_JOB_NAME)
        .incrementer(new RunIdIncrementer())
        .flow(authorMigratingStep)
        .next(genreMigratingStep)
        .next(bookMigratingStep)
        .next(bookCommentMigratingStep)
        .end()
        .build();
  }

  @Bean
  public Step authorMigratingStep(AuthorEntityRepositoryItemReader authorEntityRepositoryItemReader, AuthorProcessor authorProcessor,
      AuthorDocumentRepositoryItemWriter authorDocumentRepositoryWriter) {
    return stepBuilderFactory
        .get(AUTHOR_MIGRATING_STEP_NAME)
        .<AuthorEntity, AuthorDocument>chunk(CHUNK_SIZE)
        .reader(authorEntityRepositoryItemReader)
        .processor(authorProcessor)
        .writer(authorDocumentRepositoryWriter)
        .build();
  }

  @Bean
  public Step genreMigratingStep(GenreEntityRepositoryItemReader genreEntityRepositoryItemReader, GenreProcessor genreProcessor,
      GenreDocumentRepositoryItemWriter genreDocumentRepositoryWriter) {
    return stepBuilderFactory
        .get(GENRE_MIGRATING_STEP_NAME)
        .<GenreEntity, GenreDocument>chunk(CHUNK_SIZE)
        .reader(genreEntityRepositoryItemReader)
        .processor(genreProcessor)
        .writer(genreDocumentRepositoryWriter)
        .build();
  }

  @Bean
  public Step bookMigratingStep(BookEntityRepositoryItemReader bookEntityRepositoryItemReader, BookProcessor bookProcessor,
      BookDocumentItemWriter bookDocumentRepositoryWriter) {
    return stepBuilderFactory
        .get(BOOK_MIGRATING_STEP_NAME)
        .<BookEntity, BookDocument>chunk(CHUNK_SIZE)
        .reader(bookEntityRepositoryItemReader)
        .processor(bookProcessor)
        .writer(bookDocumentRepositoryWriter)
        .build();
  }

  @Bean
  public Step bookCommentMigratingStep(BookCommentEntityRepositoryItemReader bookCommentEntityRepositoryItemReader, BookCommentProcessor bookCommentProcessor,
      BookCommentDocumentItemWriter bookCommentDocumentRepositoryWriter) {

    return stepBuilderFactory
        .get(BOOK_COMMENT_MIGRATING_STEP_NAME)
        .<BookCommentEntity, BookCommentDocument>chunk(CHUNK_SIZE)
        .reader(bookCommentEntityRepositoryItemReader)
        .processor(bookCommentProcessor)
        .writer(bookCommentDocumentRepositoryWriter)
        .build();
  }

}
