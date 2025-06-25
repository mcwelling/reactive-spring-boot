package dev.mcwelling.springbootreactiveproject;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "items")
@Data
public class Item {

    private @Id String id;
    private String name;
    private String description;
    private double price;
    private Item() {}
    public Item(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
