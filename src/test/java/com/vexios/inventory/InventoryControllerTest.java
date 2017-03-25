package com.vexios.inventory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
public class InventoryControllerTest {

    @Autowired
    private InventoryController inventoryController;

    @Test
    public void addItem_successfulResponse() {
        final Item item = new Item();
        item.setName("item 1");
        item.setDescription("description");
        item.setCount(50);

        final Item createdItem = inventoryController.addItem(item, mock(BindingResult.class));
        assertEquals(item, createdItem);
    }

    @Test
    public void addItem_nameExceeds50Characters_shouldReturnValidationError() {

    }

    @Test
    public void addItem_nameNull_shouldReturnValidationError() {

    }

    @Test
    public void addItem_nameEmpty_shouldReturnValidationError() {

    }

    @Test
    public void addItem_descriptionNull_shouldReturnValidationError() {

    }

    @Test
    public void addItem_descriptionEmpty_shouldReturnValidationError() {

    }

    @Test
    public void addItem_descriptionExceeds150Characters_shouldReturnValidationError() {

    }

    @Test
    public void addItem_countNull_shouldReturnValidationError() {

    }

    @Test
    public void addItem_countLessThan1_shouldReturnValidationError() {

    }

    @Test
    public void addItem_countMoreThan100_shouldReturnValidationError() {

    }

    @Test
    public void getItems_successfulResponse_shouldReturnListOfItems() {

    }

    @Test
    public void getItemById_itemExists_shouldReturnItem() {

    }

    @Test
    public void getItemById_itemWithIdDoesNotExist_shouldReturnNotFound() {

    }

    @Test
    public void updateItem_itemExists_shouldReturnUpdatedItem() {

    }

    @Test
    public void updateItem_itemWithIdDoesNotExist_shouldReturnNotFound() {

    }
}
