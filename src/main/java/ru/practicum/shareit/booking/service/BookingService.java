package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.StateEnum;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;

import java.util.List;

/**
 * Интерфейс реализует логику CRUD-операций для функционала бронирования
 */
public interface BookingService {

    /** Метод создания нового бронирования
     * @param bookingDTO данные нового бронирования
     * @return объект созданной брони
     */
    BookingResponseDTO createNewBooking(BookingRequestDTO bookingDTO, Long renterId);

    /** Метод подтверждения или отказа
     * @param bookingId идентификатор брони
     * @param itemOwnerId идентификатор владельца вещи
     * @param approved действие, может принимать значения true или false.
     * @return объект обновленного бронирования
     */
    BookingResponseDTO confirmReject(Long bookingId, Long itemOwnerId, Boolean approved);

    /** Метод получения данных о конкретном бронировании
     * @param bookingId  идентификатор брони
     * @param userId идентификатор пользователя делающего запрос
     * @return объект брони
     */
    BookingResponseDTO getBookingById(Long bookingId, Long userId);

    /**
     * Метод получения списка всех бронирований текущего пользователя.
     *
     * @param currentUserId идентификатор текущего пользователя делающего запрос
     * @param state статус бронирования
     * @return список объектов бронирования текущего пользователя
     */
    List<BookingResponseDTO> getAllBookingByUserId(Long currentUserId, StateEnum state);
}
