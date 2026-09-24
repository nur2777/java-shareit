package ru.practicum.shareit.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserControllerImpl;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerImpl.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mvc;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .id(100L)
                .name("Тестов Тест Тестович")
                .email("test@test.com")
                .build();
    }

    @Test
    void testAdd() throws Exception {
        Mockito.when(userService.addNewUser(any(UserDTO.class)))
                    .thenReturn(userDTO);
        mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDTO))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(100))
                    .andExpect(jsonPath("$.name").value("Тестов Тест Тестович"))
                    .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void testGetUser() throws Exception {
        Mockito.when(userService.getUser(100L))
                    .thenReturn(userDTO);
        mvc.perform(get("/users/{userId}", 100L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(100))
                    .andExpect(jsonPath("$.name").value("Тестов Тест Тестович"))
                    .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void testUpdate() throws Exception {
        Mockito.when(userService.updateUser(any(UserDTO.class), eq(100L)))
                    .thenReturn(userDTO);
        mvc.perform(patch("/users/{userId}", 100L)
                        .content(mapper.writeValueAsString(userDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(100))
                    .andExpect(jsonPath("$.name").value("Тестов Тест Тестович"))
                    .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(100L);
        mvc.perform(delete("/users/{userId}", 100L))
                    .andExpect(status().isOk());
    }
}
