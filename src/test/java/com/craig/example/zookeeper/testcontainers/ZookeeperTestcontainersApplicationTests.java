package com.craig.example.zookeeper.testcontainers;

import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
class ZookeeperTestcontainersApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SharedCount sharedCount;

    @Autowired
    private DistributedAtomicInteger distributedAtomicInteger;

    @Test
    void contextLoads() throws Exception {
        mockMvc.perform(post("/zookeeper").param("count", "100"))
                .andExpect(status().isCreated());

        assertThat(sharedCount.getCount()).isEqualTo(100);

        AtomicValue<Integer> value = distributedAtomicInteger.get();
        assertThat(value.succeeded()).isTrue();
        assertThat(value.postValue()).isGreaterThan(0);
    }

}
