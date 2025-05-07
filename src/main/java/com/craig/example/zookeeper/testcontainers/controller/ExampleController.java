package com.craig.example.zookeeper.testcontainers.controller;

import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zookeeper")
public class ExampleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleController.class);

    private final SharedCount sharedCount;
    private final DistributedAtomicInteger distributedAtomicInteger;

    public ExampleController(SharedCount sharedCount, DistributedAtomicInteger distributedAtomicInteger) {
        this.sharedCount = sharedCount;
        this.distributedAtomicInteger = distributedAtomicInteger;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void doPost(@RequestParam int count) throws Exception {
        sharedCount.setCount(count);
        AtomicValue<Integer> value = distributedAtomicInteger.increment();

        if (value.succeeded()) {
            LOGGER.info("Incremented value from {} to {}", value.preValue(), value.postValue());
        } else {
            LOGGER.error("Failed to increment value");
        }
    }
}
