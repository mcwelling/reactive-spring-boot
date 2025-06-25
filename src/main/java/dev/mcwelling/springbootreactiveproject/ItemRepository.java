package dev.mcwelling.springbootreactiveproject;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {
    Flux<Item> findByNameContaining(String partialName);
    Mono<Item> findByPrice(Double price);
}
