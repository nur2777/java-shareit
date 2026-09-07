package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;

import java.util.List;

/**
 * Интерфейс контроллера для функционала бронирования
 */
public interface BookingController {
    /**
     * Эндпоинт на Добавление нового запроса на бронирование.
     * @param newBookingDto данные нового бронирования
     * @param renterId пользователь арендатор вещи
     * @return объект созданного бронирования
     */
    BookingResponseDTO addNewBooking(BookingRequestDTO newBookingDto, Long renterId);

    /** Эндпоинт для подтверждения или отклонения запроса на бронирование.
     * @param bookingId идентификатор брони
     * @param itemOwnerId идентификатор владельца вещи
     * @param approved действие, может принимать значения true или false.
     *
     * @return объект обновленного бронирования
     */
    BookingResponseDTO confirmReject(Long bookingId, Long itemOwnerId, Boolean approved);

    /**
     * Эндпоинт получения конкретного бронирования
     *
     * @param bookingId идентификатор бронирования
     * @param userId идентификатор пользователя делающего запрос
     * @return объект бронирования
     */
    BookingResponseDTO getBookingById(Long bookingId, Long userId);

    /**
     * Эндпоинт получения списка всех бронирований текущего пользователя.
     *
     * @param currentUserId идентификатор текущего пользователя делающего запрос
     * @param state статус бронирования
     * @return объект бронирования
     */
    List<BookingResponseDTO> getAllBookingByUserId(Long currentUserId, String state);
}
