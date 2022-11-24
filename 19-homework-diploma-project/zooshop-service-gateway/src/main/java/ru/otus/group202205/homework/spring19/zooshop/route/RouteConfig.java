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
        .route("zooshop-address-service-route",
            r -> r
                .path("/api/addresses/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/addresses/([\\w]*)",
                        "/$1"))
                .uri("lb://zooshop-address-service"))
        .route("zooshop-goods-service-route",
            r -> r
                .path("/api/goods/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/goods/([\\w]*)",
                        "/$1"))
                .uri("lb://zooshop-good-service"))
        .route("zooshop-producers-service-route",
            r -> r
                .path("/api/producers/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/producers/([\\w]*)",
                        "/$1"))
                .uri("lb://zooshop-producer-service"))
        .build();
  }

}
