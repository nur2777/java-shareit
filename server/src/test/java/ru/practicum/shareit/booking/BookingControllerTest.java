package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingControllerImpl;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constants.Constants.SHARER_USER_ID;

@WebMvcTest(BookingControllerImpl.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private BookingRequestDTO requestDTO;
    private BookingResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new BookingRequestDTO();
        requestDTO.setItemId(1L);
        requestDTO.setStart(LocalDateTime.now().plusDays(1));
        requestDTO.setEnd(LocalDateTime.now().plusDays(3));

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setName("Шкаф");
        itemDTO.setDescription("Икеевский");
        itemDTO.setAvailable(true);

        UserDTO bookerDTO = UserDTO.builder()
                .id(100L)
                .name("Тестов Тест")
                .email("test@test.com")
                .build();

        responseDTO = new BookingResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setItemId(1L);
        responseDTO.setStart(LocalDateTime.now().plusDays(1));
        responseDTO.setEnd(LocalDateTime.now().plusDays(3));
        responseDTO.setStatus("WAITING");
        responseDTO.setItem(itemDTO);
        responseDTO.setBooker(bookerDTO);
    }

    @Test
    void testAddNewBooking() throws Exception {
        Mockito.when(bookingService.createNewBooking(any(BookingRequestDTO.class), eq(100L)))
                .thenReturn(responseDTO);

        mvc.perform(post("/bookings")
                        .header(SHARER_USER_ID, 100L)
                        .content(mapper.writeValueAsString(requestDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.item.id").value(1))
                .andExpect(jsonPath("$.booker.id").value(100));
    }

    @Test
    void testConfirmReject_whenApproved() throws Exception {
        responseDTO.setStatus("APPROVED");
        Mockito.when(bookingService.confirmReject(eq(1L), eq(100L), eq(true)))
                .thenReturn(responseDTO);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(SHARER_USER_ID, 100L)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testConfirmReject_whenRejected() throws Exception {
        responseDTO.setStatus("REJECTED");
        Mockito.when(bookingService.confirmReject(eq(1L), eq(100L), eq(false)))
                .thenReturn(responseDTO);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(SHARER_USER_ID, 100L)
                        .param("approved", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void testGetBookingById() throws Exception {
        Mockito.when(bookingService.getBookingById(1L, 100L))
                .thenReturn(responseDTO);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header(SHARER_USER_ID, 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void testGetAllBookingByUserId_whenAll() throws Exception {
        Mockito.when(bookingService.getAllBookingByUserId(100L, "ALL"))
                .thenReturn(List.of(responseDTO));

        mvc.perform(get("/bookings")
                        .header(SHARER_USER_ID, 100L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    void testGetAllBookingByUserId_whenDefaultState() throws Exception {
        Mockito.when(bookingService.getAllBookingByUserId(100L, "ALL"))
                .thenReturn(List.of(responseDTO));

        mvc.perform(get("/bookings")
                        .header(SHARER_USER_ID, 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetAllBookingByUserId_whenLowerCaseState() throws Exception {
        Mockito.when(bookingService.getAllBookingByUserId(100L, "WAITING"))
                .thenReturn(List.of(responseDTO));

        mvc.perform(get("/bookings")
                        .header(SHARER_USER_ID, 100L)
                        .param("state", "WAITING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetAllBookingByOwnerId_whenAll() throws Exception {
        Mockito.when(bookingService.getAllBookingByOwnerId(100L, "ALL"))
                .thenReturn(List.of(responseDTO));

        mvc.perform(get("/bookings/owner")
                        .header(SHARER_USER_ID, 100L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetAllBookingByOwnerId_whenDefaultState() throws Exception {
        Mockito.when(bookingService.getAllBookingByOwnerId(100L, "ALL"))
                .thenReturn(List.of(responseDTO));

        mvc.perform(get("/bookings/owner")
                        .header(SHARER_USER_ID, 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}