package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.mapping.BookingMap;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingResponseDTO createNewBooking(BookingRequestDTO bookingDTO, Long renterId) {

        if (renterId == null) {
            throw new ValidationException("Идентификатор пользователя-арендатора должен быть заполнен!");
        }
        User user = userRepository.findById(renterId)
                .orElseThrow(() -> new NotFoundException("Пользователь-арендатор с id " + renterId + " не найден "));
        if (bookingDTO.getItemId() == null) {
            throw new ValidationException("Идентификатор вещи должен быть заполнен!");
        }
        Item item = itemRepository.findById(bookingDTO.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingDTO.getItemId() + " не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования!");
        }
        Booking booking = BookingMap.toBooking(bookingDTO,item,user);
        booking.setStatus("WAITING");
        return BookingMap.toBookingDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDTO confirmReject(Long bookingId, Long itemOwnerId, Boolean approved) {
        if (bookingId == null) {
            throw new ValidationException("Идентификатор бронирования bookingId должен быть заполнен!");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        if (itemOwnerId == null) {
            throw new ValidationException("Идентификатор пользователя владельца вещи должен быть заполнен!");
        }
        if (!itemOwnerId.equals(booking.getItem().getOwnerId())) {
            throw new ValidationException("Подтверждение или отказ может быть выполнено только владельцем вещи!");
        }
        booking.setStatus(approved ? "APPROVED" : "REJECTED");
        Booking newBooking = bookingRepository.save(booking);
        return BookingMap.toBookingDTO(newBooking);
    }

    @Override
    public BookingResponseDTO getBooking(Long bookingId, Long userId) {
        if (bookingId == null) {
            throw new ValidationException("Идентификатор бронирования bookingId должен быть заполнен!");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        if (userId == null) {
            throw new ValidationException("Идентификатор пользователя делающего запрос должен быть заполнен!");
        }
        if (!userId.equals(booking.getItem().getOwnerId()) && !userId.equals(booking.getUser().getId())) {
            throw new ValidationException(" Запрос может быть выполнено только владельцем вещи или автором бронирования!");
        }
        return BookingMap.toBookingDTO(booking);
    }
}
