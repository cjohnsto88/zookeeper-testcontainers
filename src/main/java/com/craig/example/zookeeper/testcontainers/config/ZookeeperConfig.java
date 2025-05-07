package com.craig.example.zookeeper.testcontainers.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.springframework.cloud.zookeeper.CuratorFactory;
import org.springframework.cloud.zookeeper.ZookeeperProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    @Bean(initMethod = "start", destroyMethod = "close")
    SharedCount sharedCount(CuratorFramework curatorFramework) {
        return new SharedCount(curatorFramework, "/sharedCount", 0);
    }

    @Bean
    DistributedAtomicInteger distributedAtomicInteger(CuratorFramework curatorFramework, ZookeeperProperties properties) {
        return new DistributedAtomicInteger(curatorFramework, "/distributedAtomicInteger", CuratorFactory.retryPolicy(properties));
    }
}
