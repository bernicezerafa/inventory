package com.vexios.inventory.dao;

import java.util.List;

public interface ItemsRepository {

    void addItem(Item item);

    void updateItem(Item item);

    List<Item> getItems();

    Item getItem(long id);
}
