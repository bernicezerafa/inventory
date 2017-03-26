package com.vexios.inventory.resources;

import com.vexios.inventory.errors.BadRequestException;
import com.vexios.inventory.models.ItemRequest;
import com.vexios.inventory.models.ItemResponse;
import com.vexios.inventory.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping(value = "/items", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ItemResponse addItem(@RequestBody @Valid final ItemRequest item, final BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException(result);
        }

        return inventoryService.addItem(item);
    }

    @GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody List<ItemResponse> getItems() {
        return inventoryService.getItems();
    }

    @GetMapping(value = "/items/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ItemResponse getItem(@PathVariable final long id) {
        return inventoryService.getItem(id);
    }

    @PutMapping(value = "/items/{id}",
                consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ItemResponse updateItem(@PathVariable final long id,
                                                 @RequestBody @Valid final ItemRequest updatedItem,
                                                 final BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException(result);
        }

        return inventoryService.updateItem(id, updatedItem);
    }
}