package com.vexios.inventory;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("items")
public class InventoryController {

    final Map<Long, Item> itemsById = new HashMap<>();

    public InventoryController() {
        itemsById.put(1L, new Item(1, "ITEM 1", "description", 5, System.currentTimeMillis()));
    }

    @GetMapping
    public String getItems() {
        return "Greetings from Spring Boot!";
    }
}