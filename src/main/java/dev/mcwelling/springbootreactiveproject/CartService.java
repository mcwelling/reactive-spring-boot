package dev.mcwelling.springbootreactiveproject;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    CartService(CartRepository cartRepository, ItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    Mono<Cart> addToCart(String cartId, String id) {
        return this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem()
                                .getId().equals(id))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart);
                        })
                        .orElseGet(() ->
                            this.itemRepository.findById(id)
                                    .map(CartItem::new)
                                    .doOnNext(cartItem ->
                                            cart.getCartItems().add(cartItem))
                                    .map(cartItem -> cart)))
                .flatMap(this.cartRepository::save);
    }
}
