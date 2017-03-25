package com.vexios.inventory;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController("items")
public class InventoryController {

    private final Map<Long, Item> items = new HashMap<>();

    public InventoryController() {

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<Object> addItem(@Valid @RequestBody final Item item, final BindingResult result) {
        if (result.hasErrors()) {
            final List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        final boolean itemWithSameNameAlreadyExists =
                items.values().stream().anyMatch(existingItem -> existingItem.getName().equals(item.getName()));

        if (itemWithSameNameAlreadyExists) {
            return new ResponseEntity<>(Collections.singletonList("Item with name " + item.getName() + " already exists!"), HttpStatus.CONFLICT);
        }

        items.put(item.getId(), item);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Item>> getItems() {
        return new ResponseEntity<>(new ArrayList<>(items.values()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getItem(@PathVariable final long id) {
        final Item matchingItem = items.get(id);
        if (matchingItem == null) {
            return new ResponseEntity<>("Item with id " + id + " not found!" + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(matchingItem, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<Object> updateItem(@PathVariable final long id, @Valid final Item updatedItem, final BindingResult result) {
        if (result.hasErrors()) {
            final List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        final Item itemBeingUpdated = items.get(id);
        if (itemBeingUpdated == null) {
            return new ResponseEntity<>("Item with id " + id + " not found!" + id, HttpStatus.NOT_FOUND);
        }

        final boolean itemWithSameNameDifferentId =
                items.values().stream().anyMatch(item -> item.getName().equals(updatedItem.getName()) && item.getId() != id);

        // the user could have updated the name of the item to an existing item name
        if (itemWithSameNameDifferentId) {
            return new ResponseEntity<>(Collections.singletonList("Item with name " + updatedItem.getName() + " already exists!"), HttpStatus.CONFLICT);
        }

        items.put(id, updatedItem);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }
}