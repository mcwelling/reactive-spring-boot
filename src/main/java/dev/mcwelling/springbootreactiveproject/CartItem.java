package dev.mcwelling.springbootreactiveproject;

import lombok.Data;

@Data
public class CartItem {
    private Item item;
    private int quantity;
    private CartItem() {}

    CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }

    public void increment() {
        this.quantity++;
    }

    public void decrement() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }
}
