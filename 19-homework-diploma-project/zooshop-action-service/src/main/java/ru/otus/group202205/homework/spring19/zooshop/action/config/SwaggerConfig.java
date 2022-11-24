package ru.otus.group202205.homework.spring19.zooshop.action.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@OpenAPIDefinition(info = @Info(version = "0.1.0",
    title = "Zooshop category service",
    description = "Intended for creating, reading, updating and deleting category information. Has its own database.\n"
        + "There is a search in several fields (name, parentId)."))
@Configuration
public class SwaggerConfig {

  @Bean
  ForwardedHeaderFilter forwardedHeaderFilter() {
    return new ForwardedHeaderFilter();
  }

}
