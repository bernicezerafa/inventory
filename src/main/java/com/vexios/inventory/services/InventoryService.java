package com.vexios.inventory.services;

import com.vexios.inventory.errors.NotFoundException;
import com.vexios.inventory.models.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    private final Map<Long, Item> items = new HashMap<>();

    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    public Item getItem(final long id) {
        final Item matchingItem = items.get(id);
        if (matchingItem == null) {
            throw new NotFoundException(String.format("Item with id %s not found!", id));
        }
        return matchingItem;
    }

    public Item addItem(final Item item) {
        final boolean nameNotUnique = items.values().stream().anyMatch(v -> v.getName().equals(item.getName()));
        if (nameNotUnique) {
            throw new IllegalStateException(String.format("Item with name %s already exists!", item.getName()));
        }

        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(final long id, final Item item) {
        final Item itemBeingUpdated = items.get(id);
        if (itemBeingUpdated == null) {
            throw new NotFoundException(String.format("Item with id %s not found!", id));
        }

        // the user might have updated the name of the item to an existing item name
        final boolean nameNotUnique =
                items.values().stream().anyMatch(v -> v.getName().equals(item.getName()) && v.getId() != id);

        if (nameNotUnique) {
            throw new IllegalStateException(String.format("Item with name %s already exists!", item.getName()));
        }

        items.put(id, item);
        return item;
    }
}
