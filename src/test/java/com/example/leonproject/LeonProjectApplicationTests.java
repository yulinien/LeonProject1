package com.example.leonproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest(classes = LeonProjectApplication.class)
@TestPropertySource(properties = "spring.profiles.active=dev")
class LeonProjectApplicationTests {

    @Test
    void contextLoads() {
    }

}
