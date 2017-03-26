package com.vexios.inventory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.vexios.inventory.models.ErrorInfo;
import com.vexios.inventory.dao.Item;
import com.vexios.inventory.models.ItemRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {Application.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InventoryControllerIT {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private Gson gson;

    @Before
    public void setup() {
        final DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        mockMvc = builder.build();
        gson = new Gson();
    }

    @Test
    public void addItem_successfulResponse() throws Exception {
        final ItemRequest request = buildItemRequest("test_name", "test_description", 50);

        final MockHttpServletRequestBuilder createItemRequest =
                post("/items").contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(request));

        final MvcResult createItemResult = this.mockMvc.perform(createItemRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        final Item response = gson.fromJson(createItemResult.getResponse().getContentAsString(), Item.class);
        assertSuccessfulItemResponse(buildItemResponse(request), response);
    }

    @Test
    public void addItem_nameExceeds50Characters_shouldReturnValidationError() throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest(RandomStringUtils.randomAlphabetic(52), "test_description", 50)));

        assertBadRequest(this.mockMvc.perform(createItemRequest), "name");
    }

    @Test
    public void addItem_nameNull_shouldReturnValidationError() throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest(null, "test_description", 50)));

        assertBadRequest(this.mockMvc.perform(createItemRequest), "name");
    }

    @Test
    public void addItem_nameEmpty_shouldReturnValidationError() throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest("", "test_description", 50)));

        assertBadRequest(this.mockMvc.perform(createItemRequest), "name");
    }

    @Test
    public void addItem_descriptionNull_shouldReturnValidationError() throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest("test_name", null, 50)));

        assertBadRequest(this.mockMvc.perform(createItemRequest), "description");
    }

    @Test
    public void addItem_descriptionEmpty_shouldReturnValidationError() throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest("test_name", "", 50)));

        assertBadRequest(this.mockMvc.perform(createItemRequest), "description");
    }

    @Test
    public void addItem_descriptionExceeds150Characters_shouldReturnValidationError() throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest("test_name", RandomStringUtils.random(151), 50)));

        assertBadRequest(this.mockMvc.perform(createItemRequest), "description");
    }

    @Test
    public void addItem_countNull_shouldReturnValidationError() throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest("test_name", "test_description", null)));

        assertBadRequest(this.mockMvc.perform(createItemRequest), "count");
    }

    @Test
    public void addItem_countLessThan1_shouldReturnValidationError() throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest("test_name", "test_description", 0)));

        assertBadRequest(this.mockMvc.perform(createItemRequest), "count");
    }

    @Test
    public void addItem_countMoreThan100_shouldReturnValidationError() throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest("test_name", "test_description", 101)));

        assertBadRequest(this.mockMvc.perform(createItemRequest), "count");
    }

    @Test
    public void addItem_duplicateItemName_shouldReturnUnprocessableEntity() throws Exception {
        final String duplicateItemName = "test_name";
        createItem(duplicateItemName, "test_description1", 50);

        final MockHttpServletRequestBuilder createItemRequest =
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest(duplicateItemName, "test_description", 66)));

        final MvcResult response = this.mockMvc.perform(createItemRequest)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        final ErrorInfo errorInfo = gson.fromJson(response.getResponse().getContentAsString(), ErrorInfo.class);
        assertEquals(String.format("Item with name %s already exists!", duplicateItemName), errorInfo.getMessage());
    }

    @Test
    public void getItems_successfulResponse_shouldReturnListOfItems() throws Exception {
        createItem("test_name1", "test_description1", 50);
        createItem("test_name2", "test_description2", 40);

        final MockHttpServletRequestBuilder getItemsRequest = get("/items");

        final MvcResult getItemsResult = this.mockMvc.perform(getItemsRequest)
                        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        final List<Item> items = gson.fromJson(getItemsResult.getResponse().getContentAsString(), new TypeReference<List<Item>>() {}.getType());
        assertEquals("Expecting 2 items", 2, items.size());
        assertTrue("One of the items should be test_name1", items.stream().anyMatch(item -> item.getName().equals("test_name1")));
        assertTrue("One of the items should be test_name2", items.stream().anyMatch(item -> item.getName().equals("test_name2")));
    }

    @Test
    public void getItemById_itemExists_shouldReturnItem() throws Exception {
        final Item expectedItem = createItem("test_name1", "test_description1", 50);

        final MockHttpServletRequestBuilder getItemRequest =
                MockMvcRequestBuilders.get("/items/{id}", expectedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8);

        final MvcResult getItemResult = this.mockMvc.perform(getItemRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final Item response = gson.fromJson(getItemResult.getResponse().getContentAsString(), Item.class);
        assertSuccessfulItemResponse(expectedItem, response);
    }

    @Test
    public void getItemById_itemWithIdDoesNotExist_shouldReturnNotFound() throws Exception {
        final MockHttpServletRequestBuilder getItemRequest =
                MockMvcRequestBuilders.get("/items/{id}", 1L);

        final MvcResult getItemResult = this.mockMvc.perform(getItemRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        final ErrorInfo response = gson.fromJson(getItemResult.getResponse().getContentAsString(), ErrorInfo.class);
        assertEquals(String.format("Item with id %s not found!", 1), response.getMessage());
    }

    @Test
    public void updateItem_itemExists_shouldReturnUpdatedItem() throws Exception {
        final Item itemBeforeUpdate = createItem("test_name1", "test_description1", 50);
        final ItemRequest request = buildItemRequest("updatedName", "updatedDescription", 20);

        final MockHttpServletRequestBuilder getItemRequest =
                MockMvcRequestBuilders.put("/items/{id}", itemBeforeUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(request));

        final MvcResult getItemResult = this.mockMvc.perform(getItemRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final Item response = gson.fromJson(getItemResult.getResponse().getContentAsString(), Item.class);
        assertSuccessfulItemResponse(buildItemResponse(request), response);
    }

    @Test
    public void updateItemName_itemWithNameAlreadyExists_shouldReturnUnprocessableEntity() throws Exception {
        final Item itemBeforeUpdate = createItem("test_name1", "test_description1", 50);
        final String duplicateItemName = "test_name2";
        createItem(duplicateItemName, "test_description2", 50);

        final ItemRequest request = buildItemRequest(duplicateItemName, "updatedDescription", 20);

        final MockHttpServletRequestBuilder getItemRequest =
                MockMvcRequestBuilders.put("/items/{id}", itemBeforeUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(request));

        final MvcResult getItemResult = this.mockMvc.perform(getItemRequest)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andReturn();

        final ErrorInfo errorInfo = gson.fromJson(getItemResult.getResponse().getContentAsString(), ErrorInfo.class);
        assertEquals(String.format("Item with name %s already exists!", duplicateItemName), errorInfo.getMessage());
    }

    @Test
    public void updateItem_itemWithIdDoesNotExist_shouldReturnNotFound() throws Exception {
        final ItemRequest request = buildItemRequest("updatedName", "updatedDescription", 20);

        final MockHttpServletRequestBuilder getItemRequest =
                MockMvcRequestBuilders.put("/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(request));

        final MvcResult getItemResult = this.mockMvc.perform(getItemRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        final ErrorInfo response = gson.fromJson(getItemResult.getResponse().getContentAsString(), ErrorInfo.class);
        assertEquals(String.format("Item with id %s not found!", 1L), response.getMessage());
    }

    private ItemRequest buildItemRequest(final String name, final String description, final Integer count) {
        final ItemRequest item = new ItemRequest();
        item.setName(name);
        item.setDescription(description);
        item.setCount(count);
        return item;
    }

    private Item buildItemResponse(final ItemRequest request) {
        final Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setCount(request.getCount());
        return item;
    }

    private Item createItem(final String name, final String description, final Integer count) throws Exception {
        final MockHttpServletRequestBuilder createItemRequest =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(buildItemRequest(name, description, count)));
        final MvcResult mvcResult = this.mockMvc.perform(createItemRequest).andReturn();
        return gson.fromJson(mvcResult.getResponse().getContentAsString(), Item.class);
    }

    private void assertBadRequest(final ResultActions resultActions, final String errorField) throws Exception {
        final MvcResult result = resultActions
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        final List<ErrorInfo> errors = gson.fromJson(result.getResponse().getContentAsString(), new TypeReference<List<ErrorInfo>>() {}.getType());
        assertEquals("Expecting 1 validation error", errors.size(), 1);
        assertEquals(String.format("Expecting validation error on %s field", errorField), errors.get(0).getField(), errorField);
        assertNotNull("Error message should not be null", errors.get(0).getMessage());
    }

    private void assertSuccessfulItemResponse(final Item expectedItem, final Item actualItem) {
        assertNotNull("id should not be null", actualItem.getId());
        assertEquals("name not as expected", expectedItem.getName(), actualItem.getName());
        assertEquals("description not as expected", expectedItem.getDescription(), actualItem.getDescription());
        assertEquals("count not as expected", expectedItem.getCount(), actualItem.getCount());
        assertNotNull("timestamp should not be null", actualItem.getTimestamp());
    }
}
