package com.vexios.inventory.services;

import com.vexios.inventory.models.ItemRequest;
import com.vexios.inventory.models.ItemResponse;

import java.util.List;

public interface InventoryService {

    ItemResponse addItem(ItemRequest item);

    ItemResponse getItem(long id);

    List<ItemResponse> getItems();

    ItemResponse updateItem(long id, ItemRequest item);
}
