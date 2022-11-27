package ru.otus.group202205.homework.spring19.zooshop.filter;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import ru.otus.group202205.homework.spring19.zooshop.filter.LoggingFilterFactory.LoggingFilterFactoryConfig;

@Component
@Slf4j
public class LoggingFilterFactory extends AbstractGatewayFilterFactory<LoggingFilterFactoryConfig> {

  @Override
  public GatewayFilter apply(LoggingFilterFactoryConfig config) {
    return ((exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      String requestMethod = Objects
          .requireNonNull(request.getMethod())
          .toString();
      String path = request
          .getPath()
          .toString();
      String headers = request
          .getHeaders()
          .toSingleValueMap()
          .toString();
      Set<URI> uris = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR,
          Collections.emptySet());
      String originalUri = (uris.isEmpty()) ? "Unknown" : uris
          .iterator()
          .next()
          .toString();
      URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
      log.debug("Incoming request from {} to {}",originalUri,routeUri);
      log.debug("Handling request {} {} with headers {}",
          requestMethod,
          path,
          headers);

      return chain
          .filter(exchange)
          .then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            String responseHeaders = response
                .getHeaders()
                .toSingleValueMap()
                .toString();
            int status = Objects
                .requireNonNull(response.getStatusCode())
                .value();
            log.debug("Get response with code {} and headers {}",
                status,
                responseHeaders);
          }));
    });
  }

  public static class LoggingFilterFactoryConfig {

  }

}
