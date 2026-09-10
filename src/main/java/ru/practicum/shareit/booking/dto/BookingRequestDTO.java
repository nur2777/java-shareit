package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static ru.practicum.shareit.constants.Constants.DATE_TIME_PATTERN;

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
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime start;

    /**
     * Дата и время окончания брони
     */
    @NotNull(message = "Дата и время окончания брони не может быть пустой")
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime end;
}
