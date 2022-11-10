package ru.otus.group202205.homework.spring14.config;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BatchConfig extends DefaultBatchConfigurer {

  @Bean
  public JobRegistryBeanPostProcessor postProcessor(JobRegistry jobRegistry) {
    var processor = new JobRegistryBeanPostProcessor();
    processor.setJobRegistry(jobRegistry);
    return processor;
  }

  @Override
  public void setDataSource(DataSource dataSource) {
    super.setDataSource(null);
  }

}
