package ru.practicum.shareit.booking.controller;

import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;

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
    BookingResponseDTO getBooking(Long bookingId, Long userId);
}
