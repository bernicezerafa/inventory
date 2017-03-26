package com.vexios.inventory;

import com.google.gson.Gson;
import com.vexios.inventory.models.Item;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Application.class })
public class InventoryControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private Gson gson;

    @Before
    public void setup () {
        final DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        mockMvc = builder.build();
        gson = new Gson();
    }

    @Test
    public void addItem_successfulResponse() throws Exception {
        final Item item = new Item();
        item.setName("test_name");
        item.setDescription("test_description");
        item.setCount(50);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("test_name")))
                .andExpect(jsonPath("$.description", is("test_description")))
                .andExpect(jsonPath("$.count", is(50)))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    public void addItem_nameExceeds50Characters_shouldReturnValidationError() throws Exception {
        final Item item = new Item();
        item.setName(RandomStringUtils.randomAlphabetic(52));
        item.setDescription("test_description");
        item.setCount(50);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", notNullValue()));
    }

    @Test
    public void addItem_nameNull_shouldReturnValidationError() throws Exception {
        final Item item = new Item();
        item.setDescription("test_description");
        item.setCount(50);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", notNullValue()));
    }

    @Test
    public void addItem_nameEmpty_shouldReturnValidationError() throws Exception {
        final Item item = new Item();
        item.setName("");
        item.setDescription("test_description");
        item.setCount(50);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", notNullValue()));
    }

    @Test
    public void addItem_descriptionNull_shouldReturnValidationError() throws Exception {
        final Item item = new Item();
        item.setName("test_name");
        item.setCount(50);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("description")))
                .andExpect(jsonPath("$[0].message", notNullValue()));
    }

    @Test
    public void addItem_descriptionEmpty_shouldReturnValidationError() throws Exception {
        final Item item = new Item();
        item.setName("test_name");
        item.setDescription("");
        item.setCount(50);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("description")))
                .andExpect(jsonPath("$[0].message", notNullValue()));
    }

    @Test
    public void addItem_descriptionExceeds150Characters_shouldReturnValidationError() throws Exception {
        final Item item = new Item();
        item.setName("test_name");
        item.setDescription(RandomStringUtils.random(151));
        item.setCount(50);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("description")))
                .andExpect(jsonPath("$[0].message", notNullValue()));
    }

    @Test
    public void addItem_countNull_shouldReturnValidationError() throws Exception {
        final Item item = new Item();
        item.setName("test_name");
        item.setDescription("test_description");
        item.setCount(null);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("count")))
                .andExpect(jsonPath("$[0].message", notNullValue()));
    }

    @Test
    public void addItem_countLessThan1_shouldReturnValidationError() throws Exception {
        final Item item = new Item();
        item.setName("test_name");
        item.setDescription("test_description");
        item.setCount(0);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("count")))
                .andExpect(jsonPath("$[0].message", notNullValue()));
    }

    @Test
    public void addItem_countMoreThan100_shouldReturnValidationError() throws Exception {
        final Item item = new Item();
        item.setName("test_name");
        item.setDescription("test_description");
        item.setCount(101);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(item));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("count")))
                .andExpect(jsonPath("$[0].message", notNullValue()));
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
