package com.craig.example.zookeeper.testcontainers;

import org.springframework.boot.SpringApplication;

public class TestZookeeperTestcontainersApplication {

    public static void main(String[] args) {
        SpringApplication.from(ZookeeperTestcontainersApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
