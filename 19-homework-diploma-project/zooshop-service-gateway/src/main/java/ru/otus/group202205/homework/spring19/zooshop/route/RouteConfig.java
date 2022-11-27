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
        .route("zooshop-categories-service-route",
            r -> r
                .path("/api/categories/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/categories/([\\w]*)",
                        "/$1"))
                .uri("lb://zooshop-category-service"))
        .route("zooshop-actions-service-route",
            r -> r
                .path("/api/actions/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/actions/([\\w]*)",
                        "/$1"))
                .uri("lb://zooshop-action-service"))
        .route("zooshop-order-positions-service-route",
            r -> r
                .path("/api/order-positions/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/order-positions/([\\w]*)",
                        "/$1"))
                .uri("lb://zooshop-order-position-service"))
        .route("zooshop-order-service-route",
            r -> r
                .path("/api/orders/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/orders/([\\w]*)",
                        "/$1"))
                .uri("lb://zooshop-order-service"))
        .route("zooshop-customers-service-route",
            r -> r
                .path("/api/customers/**")
                .filters(g -> g
                    .filter(loggingFilterFactory.apply(new LoggingFilterFactoryConfig()))
                    .rewritePath("/api/customers/([\\w]*)",
                        "/$1"))
                .uri("lb://zooshop-customer-service"))
        .build();
  }

}
