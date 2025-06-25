package dev.mcwelling.springbootreactiveproject;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
@Data
public class Cart {

    private @Id String id;
    private List<CartItem> cartItems;
    private Cart() {}

    public Cart(String id) {
        this.id = id;
        this.cartItems = new ArrayList<>();
    }

    public Cart(String id, List<CartItem> cartItems) {
        this.id = id;
        this.cartItems = cartItems;
    }
}
