package org.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BookstoreStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreStarterApplication.class, args);
    }

}
