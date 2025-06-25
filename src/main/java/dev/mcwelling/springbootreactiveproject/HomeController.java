package dev.mcwelling.springbootreactiveproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller // template based rather than raw JSON or XML
public class HomeController {
    private final InventoryService inventoryService;
    private ItemRepository itemRepository;
    private CartRepository cartRepository;
    private final CartService cartService;

    public HomeController(
            ItemRepository itemRepository,
            CartRepository cartRepository,
            CartService cartService,
            InventoryService inventoryService) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    Mono<Rendering> home() {
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("items", this.itemRepository.findAll())
                .modelAttribute("cart", this.cartRepository.findById("My Cart")
                        .defaultIfEmpty(new Cart("My Cart")))
                .build());
    }

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return this.cartService.addToCart("My Cart", id)
                .thenReturn("redirect:/");
    }

    @GetMapping("/search")
    Mono<Rendering> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam boolean useAnd
    ) {
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("items",
                        inventoryService.searchByFluentExample(name, description, useAnd))
                        .modelAttribute("cart", this.cartRepository.findById("My Cart")
                                .defaultIfEmpty(new Cart("My Cart")))
                .build()
        );
    }

    @PostMapping
    Mono<String> createItem(@ModelAttribute Item newItem) {
        return this.itemRepository.save(newItem)
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/delete/{id}")
    Mono<String> deleteItem(@PathVariable String id) {
        return this.itemRepository.deleteById(id)
                .thenReturn("redirect:/");
    }
}
