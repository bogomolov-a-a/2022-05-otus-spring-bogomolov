package ru.otus.group202205.homework.spring08.dao;

import com.mongodb.BasicDBList;
import com.mongodb.ServerAddress;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ServerDescription;
import java.time.Duration;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "ru.otus.group202205.homework.spring08.dao")
@RequiredArgsConstructor
@Slf4j
public class DaoConfig {

  private final MongoClient mongoClient;

  @Bean
  public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  @PostConstruct
  private void postConstruct() {
    List<ServerDescription> servers = mongoClient
        .getClusterDescription()
        .getServerDescriptions();
    ServerAddress address = servers
        .get(0)
        .getAddress();
    BasicDBList members = new BasicDBList();
    members.add(new Document("_id",
        0).append("host",
        address.getHost() + ":" + address.getPort()));
    Document config = new Document("_id",
        "rs0");
    config.put("members",
        members);
    MongoDatabase admin = mongoClient.getDatabase("admin");
    admin.runCommand(new Document("replSetInitiate",
        config));
    Awaitility
        .await()
        .atMost(Duration.ofMinutes(1))
        .until(() -> {
          try (ClientSession session = mongoClient.startSession()) {
            log.info("Application connected to mongodb cluster mode to node, session info {}",
                session);
            return true;
          } catch (Exception ex) {
            return false;
          }
        });
  }

}
