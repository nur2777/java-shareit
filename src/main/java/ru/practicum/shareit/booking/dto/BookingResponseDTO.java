package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

/**
 * DTO для бронирования
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    /**
     * Идентификатор бронирования
     */
    private Long id;
    /**
     * Идентификатор бронируемой вещи
     */
    private Long itemId;

    /**
     * Дата и время начала брони
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    /**
     * Дата и время окончания брони
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    /**
     * Статус бронирования
     */
    private String status;

    /**
     * Вещь
     */
    private ItemDTO item;

    /**
     * Пользователь арендатор
     */
    private UserDTO booker;
}
