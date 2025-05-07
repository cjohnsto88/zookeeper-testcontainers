package com.craig.example.zookeeper.testcontainers;

import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    GenericContainer<?> container(Environment environment) {
        GenericContainer<?> zookeeperContainer = new GenericContainer<>(DockerImageName.parse("zookeeper:3.7.0"))
                .withExposedPorts(2181)
                .withEnv("ZOOKEEPER_CLIENT_PORT", "2181")
                .withEnv("ZOOKEEPER_TICK_TIME", "2000")
                .withEnv("ZOOKEEPER_INIT_LIMIT", "5")
                .withEnv("ZOOKEEPER_SYNC_LIMIT", "2")
                .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("zookeeper")))
                .waitingFor(Wait.forListeningPort());

        zookeeperContainer.start();

        // Dynamically set the Zookeeper port in the environment
        int mappedPort = zookeeperContainer.getMappedPort(2181);
        if (environment instanceof StandardEnvironment standardEnvironment) {
            MutablePropertySources propertySources = standardEnvironment.getPropertySources();
            propertySources.addFirst(new MapPropertySource("testcontainers", Map.of(
                    "spring.cloud.zookeeper.connect-string", "localhost:" + mappedPort
            )));
        }

        return zookeeperContainer;
    }



}
