package com.vexios.inventory.services;

import com.vexios.inventory.errors.NotFoundException;
import com.vexios.inventory.dao.Item;
import com.vexios.inventory.models.ItemRequest;
import com.vexios.inventory.dao.ItemsRepositoryImpl;
import com.vexios.inventory.models.ItemResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private ItemsRepositoryImpl itemsRepositoryImpl;

    @Autowired
    public InventoryServiceImpl(final ItemsRepositoryImpl itemsRepositoryImpl) {
        this.itemsRepositoryImpl = itemsRepositoryImpl;
    }

    public List<Item> getItems() {
        return itemsRepositoryImpl.getItems();
    }

    public Item getItem(final long id) {
        final Item matchingItem = itemsRepositoryImpl.getItem(id);
        if (matchingItem == null) {
            throw new NotFoundException(String.format("Item with id %s not found!", id));
        }
        return matchingItem;
    }

    public Item addItem(final ItemRequest item) {
        final boolean nameNotUnique = itemsRepositoryImpl.getItems().stream().anyMatch(v -> v.getName().equals(item.getName()));
        if (nameNotUnique) {
            throw new IllegalStateException(String.format("Item with name %s already exists!", item.getName()));
        }

        final Item itemToCreate = buildItem(item);
        itemsRepositoryImpl.addItem(itemToCreate);
        return itemToCreate;
    }

    public Item updateItem(final long id, final ItemRequest item) {
        final Item itemBeingUpdated = itemsRepositoryImpl.getItem(id);
        if (itemBeingUpdated == null) {
            throw new NotFoundException(String.format("Item with id %s not found!", id));
        }

        // the user might have updated the name of the item to an existing item name
        final boolean nameNotUnique =
                itemsRepositoryImpl.getItems().stream().anyMatch(v -> v.getName().equals(item.getName()) && v.getId() != id);

        if (nameNotUnique) {
            throw new IllegalStateException(String.format("Item with name %s already exists!", item.getName()));
        }

        itemBeingUpdated.setName(item.getName());
        itemBeingUpdated.setDescription(item.getDescription());
        itemBeingUpdated.setCount(item.getCount());

        itemsRepositoryImpl.updateItem(itemBeingUpdated);
        return itemBeingUpdated;
    }

    private Item buildItem(final ItemRequest item) {
        final Item itemResponse = new Item();
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setCount(item.getCount());
        itemResponse.setTimestamp(System.currentTimeMillis());
        return itemResponse;
    }
}
