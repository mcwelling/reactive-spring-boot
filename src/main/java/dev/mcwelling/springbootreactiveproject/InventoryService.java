package dev.mcwelling.springbootreactiveproject;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class InventoryService {
    private ItemRepository itemRepository;
    private ReactiveFluentMongoOperations fluentMongoOperations;

    InventoryService(
            ItemRepository itemRepository,
            ReactiveFluentMongoOperations fluentMongoOperations
    ) {
        this.itemRepository = itemRepository;
        this.fluentMongoOperations = fluentMongoOperations;
    }

    // build probe using matching logic to find similar entries
    Flux<Item> searchByExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher =
                (useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny())
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnorePaths("price");

        Example<Item> probe = Example.of(item, matcher);
        return itemRepository.findAll(probe);
    }

    Flux<Item> searchByFluentExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher =
                (useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny())
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase()
                        .withIgnorePaths("price");

        return fluentMongoOperations.query(Item.class)
                .matching(query(byExample(Example.of(item, matcher))))
                .all();
    }
}
