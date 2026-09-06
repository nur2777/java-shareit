package ru.practicum.shareit.booking.mapping;

import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMap {

    public static Booking toBooking(BookingRequestDTO bookingDTO, Item item, User user) {
        Booking booking = new Booking();
        if (item != null) {
            booking.setItem(item);
        }
        if (user != null) {
            booking.setUser(user);
        }
        if (bookingDTO.getStart() != null) {
            booking.setBookingStart(bookingDTO.getStart());
        }
        if (bookingDTO.getEnd() != null) {
            booking.setBookingEnd(bookingDTO.getEnd());
        }
        return booking;
    }

    public static BookingResponseDTO toBookingDTO(Booking booking) {

        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setId(bookingResponseDTO.getId());
        Item item = booking.getItem();
        bookingResponseDTO.setItemId(item.getId());
        bookingResponseDTO.setItem(item);
        bookingResponseDTO.setBooker(booking.getUser());
        bookingResponseDTO.setStart(booking.getBookingStart());
        bookingResponseDTO.setEnd(booking.getBookingEnd());
        bookingResponseDTO.setStatus(booking.getStatus());

        return bookingResponseDTO;
    }
}
