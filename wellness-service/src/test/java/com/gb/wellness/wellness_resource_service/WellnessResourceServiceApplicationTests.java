package com.gb.wellness.wellness_resource_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(properties = {
        "spring.cache.type=none",
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6379",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class WellnessResourceServiceApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the Spring application context loads without errors
    }
}