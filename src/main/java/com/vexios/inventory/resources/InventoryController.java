package com.vexios.inventory.resources;

import com.vexios.inventory.errors.BadRequestException;
import com.vexios.inventory.models.Item;
import com.vexios.inventory.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("items")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Item addItem(@RequestBody @Valid final Item item, final BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException(result);
        }

        return inventoryService.addItem(item);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Item> getItems() {
        return inventoryService.getItems();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Item getItem(@PathVariable final long id) {
        return inventoryService.getItem(id);
    }

    @PutMapping(value = "/{id}",
                consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Item updateItem(@PathVariable final long id,
                                         @RequestBody @Valid final Item updatedItem,
                                         final BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException(result);
        }

        return inventoryService.updateItem(id, updatedItem);
    }
}