package dev.mcwelling.springbootreactiveproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class TemplateDatabaseLoader {

    @Bean
    CommandLineRunner initialize(ReactiveMongoOperations mongo) {
        return args -> {
            mongo.dropCollection("items")
                            .then(mongo.createCollection("items"))
                                    .thenMany(
                                            Flux.just(
                                                    new Item("Alf alarm clock", "uggo clock", 19.99),
                                                    new Item("Smurf TV tray", "blue guys", 24.99),
                                                    new Item("Care Bear plushie", "da bears", 12.50),
                                                    new Item("Cabbage Patch Kid", "evil children", 35.00)
                                            )
                                                    .flatMap(mongo::save)
                                    )
                    .subscribe(
                            item -> System.out.println("Inserted: " + item.getName()),
                            error -> System.out.println("Error: " + error.getMessage()),
                            () -> System.out.println("Test data loaded successfully")
                    );
        };
    }
}
