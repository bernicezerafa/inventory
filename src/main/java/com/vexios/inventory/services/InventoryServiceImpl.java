package com.vexios.inventory.services;

import com.vexios.inventory.errors.NotFoundException;
import com.vexios.inventory.models.ItemRequest;
import com.vexios.inventory.models.ItemResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final Map<Long, ItemResponse> items = new HashMap<>();

    public List<ItemResponse> getItems() {
        return new ArrayList<>(items.values());
    }

    public ItemResponse getItem(final long id) {
        final ItemResponse matchingItem = items.get(id);
        if (matchingItem == null) {
            throw new NotFoundException(String.format("Item with id %s not found!", id));
        }
        return matchingItem;
    }

    public ItemResponse addItem(final ItemRequest item) {
        final boolean nameNotUnique = items.values().stream().anyMatch(v -> v.getName().equals(item.getName()));
        if (nameNotUnique) {
            throw new IllegalStateException(String.format("Item with name %s already exists!", item.getName()));
        }

        final ItemResponse itemResponse = buildItemResponse(item);
        items.put(itemResponse.getId(), itemResponse);
        return itemResponse;
    }

    public ItemResponse updateItem(final long id, final ItemRequest item) {
        final ItemResponse itemBeingUpdated = items.get(id);
        if (itemBeingUpdated == null) {
            throw new NotFoundException(String.format("Item with id %s not found!", id));
        }

        // the user might have updated the name of the item to an existing item name
        final boolean nameNotUnique =
                items.values().stream().anyMatch(v -> v.getName().equals(item.getName()) && v.getId() != id);

        if (nameNotUnique) {
            throw new IllegalStateException(String.format("Item with name %s already exists!", item.getName()));
        }

        final ItemResponse itemResponse = buildItemResponse(item);
        items.put(itemResponse.getId(), itemResponse);
        return itemResponse;
    }

    private ItemResponse buildItemResponse(final ItemRequest item) {
        final ItemResponse itemResponse = new ItemResponse();
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setCount(item.getCount());
        return itemResponse;
    }
}
