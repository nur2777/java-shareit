package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.StateEnum;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.mapping.BookingMap;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusEnum;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.StateEnum.*;

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
        idIsNullCheck(renterId,"Идентификатор пользователя-арендатора");
        User user = userRepository.findById(renterId)
                .orElseThrow(() -> new NotFoundException("Пользователь-арендатор с id " + renterId + " не найден "));
        idIsNullCheck(bookingDTO.getItemId(),"Идентификатор вещи");
        Item item = itemRepository.findById(bookingDTO.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingDTO.getItemId() + " не найдена"));
        itemAvailableCheck(item);
        Booking booking = BookingMap.toBooking(bookingDTO,item,user);
        booking.setStatus(StatusEnum.WAITING.name());
        return BookingMap.toBookingDTO(bookingRepository.save(booking));
    }


    @Override
    @Transactional
    public BookingResponseDTO confirmReject(Long bookingId, Long itemOwnerId, Boolean approved) {
        idIsNullCheck(bookingId,"Идентификатор бронирования bookingId");
        Booking booking = getBooking(bookingId);
        idIsNullCheck(itemOwnerId,"Идентификатор пользователя владельца");
        if (!itemOwnerId.equals(booking.getItem().getOwnerId())) {
            throw new ValidationException("Подтверждение или отказ может быть выполнено только владельцем вещи!");
        }
        booking.setStatus(approved ? StatusEnum.APPROVED.name() : StatusEnum.REJECTED.name());
        Booking newBooking = bookingRepository.save(booking);
        return BookingMap.toBookingDTO(newBooking);
    }

    @Override
    public BookingResponseDTO getBookingById(Long bookingId, Long userId) {
        idIsNullCheck(bookingId,"Идентификатор бронирования bookingId");
        Booking booking = getBooking(bookingId);
        idIsNullCheck(userId,"Идентификатор пользователя делающего запрос");
        if (!userId.equals(booking.getItem().getOwnerId()) && !userId.equals(booking.getUser().getId())) {
            throw new ValidationException(" Запрос может быть выполнено только владельцем вещи или автором бронирования!");
        }
        return BookingMap.toBookingDTO(booking);
    }

    @Override
    @Transactional
    public List<BookingResponseDTO> getAllBookingByUserId(Long currentUserId, StateEnum state) {
        stateIsNullCheck(state);
        idIsNullCheck(currentUserId,"Идентификатор пользователя делающего запрос");
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByUserId(currentUserId);
            case PAST -> bookingRepository.findAllByUserIdAndState(currentUserId,StatusEnum.APPROVED.name(),PAST.name(), LocalDateTime.now());
            case CURRENT -> bookingRepository.findAllByUserIdAndState(currentUserId,StatusEnum.APPROVED.name(),CURRENT.name(), LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllByUserIdAndState(currentUserId,StatusEnum.APPROVED.name(),FUTURE.name(), LocalDateTime.now());
            case WAITING -> bookingRepository.findAllByUserIdAndState(currentUserId,StatusEnum.WAITING.name(),WAITING.name(), LocalDateTime.now());
            case REJECTED -> bookingRepository.findAllByUserIdAndState(currentUserId,StatusEnum.REJECTED.name(),REJECTED.name(), LocalDateTime.now());
        };

        return bookings
                .stream()
                .map(BookingMap::toBookingDTO)
                .toList();
    }

    private static void stateIsNullCheck(StateEnum state) {
        if (state == null) {
            throw new ValidationException("Параметр статуса бронирования должен быть заполнен!");
        }
    }

    private static void idIsNullCheck(Long id, String msgPrefix) {
        if (id == null) {
            throw new ValidationException(msgPrefix + " должен быть заполнен!");
        }
    }

    private static void itemAvailableCheck(Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования!");
        }
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
    }
}
