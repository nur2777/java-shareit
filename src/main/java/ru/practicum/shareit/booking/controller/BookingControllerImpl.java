package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.StateEnum;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingControllerImpl implements BookingController {
    public static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @Autowired
    public BookingControllerImpl(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    @PostMapping
    public BookingResponseDTO addNewBooking(@Valid @RequestBody BookingRequestDTO newBookingDto,
                                            @RequestHeader(SHARER_USER_ID) Long renterId) {
        return bookingService.createNewBooking(newBookingDto,renterId);
    }

    @Override
    @PatchMapping("/{bookingId}")
    public BookingResponseDTO confirmReject(@Valid @PathVariable Long bookingId,
                                            @RequestHeader(SHARER_USER_ID) Long itemOwnerId,
                                            @RequestParam Boolean approved) {
        return bookingService.confirmReject(bookingId, itemOwnerId, approved);
    }

    @Override
    @GetMapping("/{bookingId}")
    public BookingResponseDTO getBookingById(@Valid @PathVariable Long bookingId,
                                         @RequestHeader(SHARER_USER_ID) Long userId) {
        return bookingService.getBookingById(bookingId,userId);
    }

    @Override
    @GetMapping
    public List<BookingResponseDTO> getAllBookingByUserId(@RequestHeader(SHARER_USER_ID) Long currentUserId,
                                                          @RequestParam(defaultValue = "ALL") String state) {
        try { // проверяем корректность параметра state
            StateEnum stateEnum = StateEnum.valueOf(state.toUpperCase());
            return bookingService.getAllBookingByUserId(currentUserId, stateEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверное значение параметра state: " + state.toUpperCase());
        }
    }
}
