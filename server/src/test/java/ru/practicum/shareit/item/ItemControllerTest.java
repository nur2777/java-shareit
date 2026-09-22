package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemControllerImpl;
import ru.practicum.shareit.item.dto.CommentsDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constants.Constants.SHARER_USER_ID;

@WebMvcTest(ItemControllerImpl.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemServiceImpl itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDTO itemDTO;
    private CommentsDTO commentsDTO;

    @BeforeEach
    void setUp() {
        itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setName("Шкаф");
        itemDTO.setDescription("Икеевский");
        itemDTO.setAvailable(true);

        commentsDTO = new CommentsDTO();
        commentsDTO.setId(1L);
        commentsDTO.setText("Отличная вещь");
        commentsDTO.setAuthorName("Петров Петр");
    }

    @Test
    void testAdd() throws Exception {
        Mockito.when(itemService.createItem(any(ItemDTO.class), eq(100L)))
                .thenReturn(itemDTO);

        mvc.perform(post("/items")
                        .header(SHARER_USER_ID, 100L)
                        .content(mapper.writeValueAsString(itemDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Шкаф"))
                .andExpect(jsonPath("$.description").value("Икеевский"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testUpdate() throws Exception {
        ItemDTO updated = new ItemDTO();
        updated.setId(1L);
        updated.setName("Стол");
        updated.setDescription("Из массива лиственницы");
        updated.setAvailable(false);

        Mockito.when(itemService.updateItem(eq(1L), any(ItemDTO.class), eq(100L)))
                .thenReturn(updated);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .header(SHARER_USER_ID, 100L)
                        .content(mapper.writeValueAsString(updated))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Стол"))
                .andExpect(jsonPath("$.description").value("Из массива лиственницы"))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void testGetItem() throws Exception {
        Mockito.when(itemService.getItem(1L, 100L))
                .thenReturn(itemDTO);

        mvc.perform(get("/items/{itemId}", 1L)
                        .header(SHARER_USER_ID, 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Шкаф"))
                .andExpect(jsonPath("$.description").value("Икеевский"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testGetAllOwnerItems() throws Exception {
        Mockito.when(itemService.getAllOwnerItems(100L))
                .thenReturn(List.of(itemDTO));

        mvc.perform(get("/items")
                        .header(SHARER_USER_ID, 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Шкаф"));
    }

    @Test
    void testFindByNameDescription() throws Exception {
        Mockito.when(itemService.findByNameDescription("Шкаф"))
                .thenReturn(List.of(itemDTO));

        mvc.perform(get("/items/search")
                        .param("text", "Шкаф")
                        .header(SHARER_USER_ID, 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Шкаф"));
    }

    @Test
    void testAddCommentToItem() throws Exception {
        Mockito.when(itemService.addCommentToItem(eq(1L), any(CommentsDTO.class), eq(100L)))
                .thenReturn(commentsDTO);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(SHARER_USER_ID, 100L)
                        .content(mapper.writeValueAsString(commentsDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Отличная вещь"))
                .andExpect(jsonPath("$.authorName").value("Петров Петр"));
    }
}