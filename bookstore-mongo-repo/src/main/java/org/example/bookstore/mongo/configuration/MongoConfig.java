package org.example.bookstore.mongo.configuration;

import org.example.bookstore.mongo.repo.BookCommentRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = BookCommentRepo.class)
public class MongoConfig {
}
