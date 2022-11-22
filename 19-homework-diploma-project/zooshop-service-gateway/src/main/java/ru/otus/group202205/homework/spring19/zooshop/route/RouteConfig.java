package ru.otus.group202205.homework.spring19.zooshop.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.group202205.homework.spring19.zooshop.filter.LoggingFilterFactory;
import ru.otus.group202205.homework.spring19.zooshop.filter.LoggingFilterFactory.LoggingFilterFactoryConfig;

@Configuration
@ComponentScan
public class RouteConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder, LoggingFilterFactory loggingFilterFactory) {
    return routeLocatorBuilder
        .routes()
        .route("auth-service-route",
            r -> r
                .path("/api/auth/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/auth/([\\w]*)",
                        "/$1"))
                .uri("http://localhost:8101/auth"))
        .route("goods-service-route",
            r -> r
                .path("/api/goods/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/goods/([\\w]*)",
                        "/$1"))
                .uri("lb://zooshop-good-service"))
        .build();
  }

}
