package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;

import static ru.practicum.shareit.Constants.SHARER_USER_ID;

/**
 * Контроллер для функционала бронирования
 */
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

    /**
     * Эндпоинт на Добавление нового запроса на бронирование.
     * @param newBookingDto данные нового бронирования
     * @param renterId пользователь арендатор вещи
     * @return объект созданного бронирования
     */
    @PostMapping
    public ResponseEntity<Object> addNewBooking(@Valid @RequestBody BookingRequestDTO newBookingDto,
                                            @RequestHeader(SHARER_USER_ID) Long renterId) {
        return bookingClient.createNewBooking(newBookingDto,renterId);
    }

    /** Эндпоинт для подтверждения или отклонения запроса на бронирование.
     * @param bookingId идентификатор брони
     * @param itemOwnerId идентификатор владельца вещи
     * @param approved действие, может принимать значения true или false.
     *
     * @return объект обновленного бронирования
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmReject(@Valid @PathVariable Long bookingId,
                                            @RequestHeader(SHARER_USER_ID) Long itemOwnerId,
                                            @RequestParam Boolean approved) {
        return bookingClient.confirmReject(bookingId, itemOwnerId, approved);
    }

    /**
     * Эндпоинт получения конкретного бронирования
     *
     * @param bookingId идентификатор бронирования
     * @param userId идентификатор пользователя делающего запрос
     * @return объект бронирования
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@Valid @PathVariable Long bookingId,
                                             @RequestHeader(SHARER_USER_ID) Long userId) {
        return bookingClient.getBookingById(bookingId,userId);
    }

    /**
     * Эндпоинт получения списка всех бронирований текущего пользователя.
     *
     * @param currentUserId идентификатор текущего пользователя делающего запрос
     * @param state статус бронирования
     * @return список бронирований
     */
    @GetMapping
    public ResponseEntity<Object> getAllBookingByUserId(@RequestHeader(SHARER_USER_ID) Long currentUserId,
                                                          @RequestParam(defaultValue = "ALL") String state) {
            return bookingClient.getAllBookingByUserId(currentUserId, state);
    }

    /**
     * Эндпоинт получения списка бронирований для всех вещей текущего пользователя.
     *
     * @param ownerId идентификатор текущего пользователя делающего запрос
     * @param state статус бронирования
     * @return список бронирований
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingByOwnerId(@RequestHeader(SHARER_USER_ID) Long ownerId,
                                                         @RequestParam(defaultValue = "ALL") String state) {
            return bookingClient.getAllBookingByOwnerId(ownerId, state);
    }
}
