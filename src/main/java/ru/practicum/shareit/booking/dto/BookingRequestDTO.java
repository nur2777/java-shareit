package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для запроса бронирования
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    /**
     * Идентификатор бронируемой вещи
     */
    @NotNull(message = "Идентификатор бронируемой вещи не может быть пустым")
    private Long itemId;

    /**
     * Дата и время начала брони
     */
    @NotNull(message = "Дата и время начала брони не может быть пустой")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    /**
     * Дата и время окончания брони
     */
    @NotNull(message = "Дата и время окончания брони не может быть пустой")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;
}
