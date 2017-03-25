package com.vexios.inventory;

import org.springframework.web.bind.annotation.*;

@RestController
public class InventoryController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}