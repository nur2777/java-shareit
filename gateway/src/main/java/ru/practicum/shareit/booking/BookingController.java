package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.dto.StateEnum;

import static ru.practicum.shareit.Constants.SHARER_USER_ID;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addNewBooking(@Valid @RequestBody BookingRequestDTO newBookingDto,
                                            @RequestHeader(SHARER_USER_ID) Long renterId) {
        return bookingClient.createNewBooking(newBookingDto,renterId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmReject(@Valid @PathVariable Long bookingId,
                                            @RequestHeader(SHARER_USER_ID) Long itemOwnerId,
                                            @RequestParam Boolean approved) {
        return bookingClient.confirmReject(bookingId, itemOwnerId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@Valid @PathVariable Long bookingId,
                                             @RequestHeader(SHARER_USER_ID) Long userId) {
        return bookingClient.getBookingById(bookingId,userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingByUserId(@RequestHeader(SHARER_USER_ID) Long currentUserId,
                                                          @RequestParam(defaultValue = "ALL") String state) {
        try {
            StateEnum stateEnum = StateEnum.valueOf(state.toUpperCase());
            return bookingClient.getAllBookingByUserId(currentUserId, stateEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверное значение параметра state: " + state.toUpperCase());
        }
    }
}
