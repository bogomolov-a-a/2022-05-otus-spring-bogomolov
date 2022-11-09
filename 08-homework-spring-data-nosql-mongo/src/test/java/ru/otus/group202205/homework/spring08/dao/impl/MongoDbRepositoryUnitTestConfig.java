package ru.otus.group202205.homework.spring08.dao.impl;

import de.flapdoodle.embed.mongo.config.ImmutableMongoCmdOptions;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoCmdOptions;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version.Main;
import de.flapdoodle.embed.process.runtime.Network;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
/**
 * https://github.com/spring-projects/spring-boot/issues/20182
 * */
@RequiredArgsConstructor
public class MongoDbRepositoryUnitTestConfig {


  @Bean
  public MongodConfig mongodConfig(EmbeddedMongoProperties embeddedMongoProperties, MongoProperties mongoProperties) throws
      IOException {
    MongoCmdOptions cmdOptions = ImmutableMongoCmdOptions
        .builder()
        .useNoPrealloc(false)
        .useSmallFiles(false)
        .master(false)
        .useNoJournal(false)
        .syncDelay(0)
        .build();

    return ImmutableMongodConfig
        .builder()
        .version(Main.PRODUCTION)
        .net(new Net(mongoProperties.getPort(),
            Network.localhostIsIPv6()))
        .replication(new Storage(null,
            embeddedMongoProperties
                .getStorage()
                .getReplSetName(),
            5000))
        .cmdOptions(cmdOptions)
        .build();
  }

}
