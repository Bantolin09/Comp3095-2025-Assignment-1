package com.gb.wellness.wellness_resource_service.config;

import com.gb.wellness.wellness_resource_service.resource.domain.ResourceEntity;
import com.gb.wellness.wellness_resource_service.resource.repo.ResourceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedConfig {

    @Bean
    CommandLineRunner seed(ResourceRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(newResource("Counseling Center", "Book student counseling appointments", "counseling", "https://gbc.ca/counseling"));
                repo.save(newResource("Mindfulness Guide", "10-minute guided breathing", "mindfulness", "https://gbc.ca/mindfulness"));
                repo.save(newResource("Fitness Workshops", "Weekly yoga and stretch sessions", "fitness", "https://gbc.ca/fitness"));
            }
        };
    }

    private ResourceEntity newResource(String title, String description, String category, String url) {
        ResourceEntity e = new ResourceEntity();
        e.setTitle(title);
        e.setDescription(description);
        e.setCategory(category);
        e.setUrl(url);
        return e;
    }
}