package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestControllerImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constants.Constants.SHARER_USER_ID;

@WebMvcTest(ItemRequestControllerImpl.class)
public class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = ItemRequestDto.builder()
                .description("Нужна машина на выходные")
                .id(1L)
                .build();
    }

    @Test
    void testAddNewRequest() throws Exception {
        Mockito.when(itemRequestService.addNewRequest(any(ItemRequestDto.class), eq(100L)))
                .thenReturn(requestDto);
        mvc.perform(post("/requests")
                        .header(SHARER_USER_ID, 100L)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна машина на выходные"));
    }

    @Test
    void testGetItemRequests_whenOnlyOwn() throws Exception {
        Mockito.when(itemRequestService.getItemRequests(100L, false))
                .thenReturn(List.of(requestDto));
        mvc.perform(get("/requests")
                        .header(SHARER_USER_ID, 100L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Нужна машина на выходные"));
    }

    @Test
    void testGetAllItemRequests() throws Exception {
        ItemRequestDto otherRequest = ItemRequestDto.builder()
                .description("Нужен стол")
                .id(2L)
                .build();
        Mockito.when(itemRequestService.getItemRequests(100L, true))
                .thenReturn(List.of(otherRequest));
        mvc.perform(get("/requests/all")
                        .header(SHARER_USER_ID, 100L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].description").value("Нужен стол"));
    }

    @Test
    void testGetItemRequestById() throws Exception {
        Mockito.when(itemRequestService.getItemRequestById(1L))
                .thenReturn(requestDto);
        mvc.perform(get("/requests/{requestId}", 1L)
                        .header(SHARER_USER_ID, 100L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна машина на выходные"));
    }

    @Test
    void testGetItemRequests_whenEmpty() throws Exception {
        Mockito.when(itemRequestService.getItemRequests(100L, false))
                .thenReturn(List.of());
        mvc.perform(get("/requests")
                        .header(SHARER_USER_ID, 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetAllItemRequests_whenEmpty() throws Exception {
        Mockito.when(itemRequestService.getItemRequests(100L, true))
                .thenReturn(List.of());
        mvc.perform(get("/requests/all")
                        .header(SHARER_USER_ID, 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}