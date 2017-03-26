package com.vexios.inventory.services;

import com.vexios.inventory.models.ItemRequest;
import com.vexios.inventory.dao.Item;

import java.util.List;

public interface InventoryService {

    Item addItem(ItemRequest item);

    Item getItem(long id);

    List<Item> getItems();

    Item updateItem(long id, ItemRequest item);
}
