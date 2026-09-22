package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusEnum;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.StateEnum.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Тестов Тест");
        owner.setEmail("test@test.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Петров Петр");
        booker.setEmail("petrov@test.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Шкаф");
        item.setDescription("Икеевский");
        item.setAvailable(true);
        item.setOwnerId(owner.getId());
        item = itemRepository.save(item);
    }

    @Test
    void testCreateNewBooking() {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setItemId(item.getId());
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(3));

        BookingResponseDTO result = bookingService.createNewBooking(request, booker.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(StatusEnum.WAITING.name());

        Booking saved = bookingRepository.findById(result.getId()).orElseThrow();
        assertThat(saved.getItem().getId()).isEqualTo(item.getId());
        assertThat(saved.getUser().getId()).isEqualTo(booker.getId());
    }

    @Test
    void testCreateNewBooking_whenUserNotFound() {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setItemId(item.getId());
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(3));

        assertThrows(NotFoundException.class,
                () -> bookingService.createNewBooking(request, 999L));
    }

    @Test
    void testCreateNewBooking_whenItemNotFound() {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setItemId(999L);
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(3));

        assertThrows(NotFoundException.class,
                () -> bookingService.createNewBooking(request, booker.getId()));
    }

    @Test
    void testCreateNewBooking_whenItemNotAvailable() {
        item.setAvailable(false);
        itemRepository.save(item);

        BookingRequestDTO request = new BookingRequestDTO();
        request.setItemId(item.getId());
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(3));

        assertThrows(ValidationException.class,
                () -> bookingService.createNewBooking(request, booker.getId()));
    }

    @Test
    void testConfirmReject_whenApproved() {
        Booking booking = createBooking(booker, item, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));
        booking.setStatus(StatusEnum.WAITING.name());
        booking = bookingRepository.save(booking);

        BookingResponseDTO result = bookingService.confirmReject(booking.getId(), owner.getId(), true);

        assertThat(result.getStatus()).isEqualTo(StatusEnum.APPROVED.name());
    }

    @Test
    void testConfirmReject_whenRejected() {
        Booking booking = createBooking(booker, item, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));
        booking.setStatus(StatusEnum.WAITING.name());
        booking = bookingRepository.save(booking);

        BookingResponseDTO result = bookingService.confirmReject(booking.getId(), owner.getId(), false);

        assertThat(result.getStatus()).isEqualTo(StatusEnum.REJECTED.name());
    }

    @Test
    void testConfirmReject_whenNotOwner() {
        Booking booking = createBooking(booker, item, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));
        booking.setStatus(StatusEnum.WAITING.name());
        booking = bookingRepository.save(booking);

        final Long bookingId = booking.getId();
        assertThrows(ValidationException.class,
                () -> bookingService.confirmReject(bookingId, booker.getId(), true));
    }

    @Test
    void testConfirmReject_whenBookingNotFound() {
        assertThrows(NotFoundException.class,
                () -> bookingService.confirmReject(999L, owner.getId(), true));
    }

    @Test
    void testGetBookingById_whenOwner() {
        Booking booking = createBooking(booker, item, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));
        booking = bookingRepository.save(booking);

        BookingResponseDTO result = bookingService.getBookingById(booking.getId(), owner.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(booking.getId());
    }

    @Test
    void testGetBookingById_whenBooker() {
        Booking booking = createBooking(booker, item, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));
        booking = bookingRepository.save(booking);

        BookingResponseDTO result = bookingService.getBookingById(booking.getId(), booker.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(booking.getId());
    }

    @Test
    void testGetBookingById_whenStranger() {
        User stranger = new User();
        stranger.setName("Чужак");
        stranger.setEmail("stranger@test.com");
        stranger = userRepository.save(stranger);

        Booking booking = createBooking(booker, item, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));
        booking = bookingRepository.save(booking);

        final Long strangerId = stranger.getId();
        final Long bookingId = booking.getId();
        assertThrows(ValidationException.class,
                () -> bookingService.getBookingById(bookingId, strangerId));
    }

    @Test
    void testGetBookingById_whenBookingNotFound() {
        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(999L, owner.getId()));
    }

    @Test
    void testGetAllBookingByUserId_whenAll() {
        bookingRepository.save(createBooking(booker, item,
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3)));

        List<BookingResponseDTO> result = bookingService.getAllBookingByUserId(booker.getId(), ALL);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isNotNull();
    }

    @Test
    void testGetAllBookingByUserId_whenStateIsNull() {
        assertThrows(ValidationException.class,
                () -> bookingService.getAllBookingByUserId(booker.getId(), null));
    }

    @Test
    void testGetAllBookingByOwnerId_whenAll() {
        bookingRepository.save(createBooking(booker, item,
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3)));

        List<BookingResponseDTO> result = bookingService.getAllBookingByOwnerId(owner.getId(), ALL);

        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByOwnerId_whenOwnerHasNoItems() {
        User userWithoutItems = new User();
        userWithoutItems.setName("Без вещей");
        userWithoutItems.setEmail("noitems@test.com");
        userWithoutItems = userRepository.save(userWithoutItems);

        final Long emptyOwnerId = userWithoutItems.getId();
        assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookingByOwnerId(emptyOwnerId, ALL));
    }

    @Test
    void testGetAllBookingByOwnerId_whenStateIsNull() {
        assertThrows(ValidationException.class,
                () -> bookingService.getAllBookingByOwnerId(owner.getId(), null));
    }

    // ---------- getAllBookingByUserId: полное покрытие по state ----------

    @Test
    void testGetAllBookingByUserId_whenPast() {
        // завершённое бронирование: end < now, статус APPROVED
        Booking past = createBooking(booker, item,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(3));
        past.setStatus(StatusEnum.APPROVED.name());
        bookingRepository.save(past);

        List<BookingResponseDTO> result = bookingService.getAllBookingByUserId(booker.getId(), PAST);

        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByUserId_whenCurrent() {
        // текущее: start < now < end, статус APPROVED
        Booking current = createBooking(booker, item,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));
        current.setStatus(StatusEnum.APPROVED.name());
        bookingRepository.save(current);

        List<BookingResponseDTO> result = bookingService.getAllBookingByUserId(booker.getId(), CURRENT);

        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByUserId_whenFuture() {
        // будущее: start > now, статус APPROVED
        Booking future = createBooking(booker, item,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3));
        future.setStatus(StatusEnum.APPROVED.name());
        bookingRepository.save(future);

        List<BookingResponseDTO> result = bookingService.getAllBookingByUserId(booker.getId(), FUTURE);

        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByUserId_whenWaiting() {
        Booking waiting = createBooking(booker, item,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3));
        waiting.setStatus(StatusEnum.WAITING.name());
        bookingRepository.save(waiting);

        List<BookingResponseDTO> result = bookingService.getAllBookingByUserId(booker.getId(), WAITING);

        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByUserId_whenRejected() {
        Booking rejected = createBooking(booker, item,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3));
        rejected.setStatus(StatusEnum.REJECTED.name());
        bookingRepository.save(rejected);

        List<BookingResponseDTO> result = bookingService.getAllBookingByUserId(booker.getId(), REJECTED);

        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByOwnerId_whenPast() {
        Booking past = createBooking(booker, item,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(3));
        past.setStatus(StatusEnum.APPROVED.name());
        bookingRepository.save(past);
        List<BookingResponseDTO> result = bookingService.getAllBookingByOwnerId(owner.getId(), PAST);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByOwnerId_whenCurrent() {
        Booking current = createBooking(booker, item,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));
        current.setStatus(StatusEnum.APPROVED.name());
        bookingRepository.save(current);
        List<BookingResponseDTO> result = bookingService.getAllBookingByOwnerId(owner.getId(), CURRENT);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByOwnerId_whenFuture() {
        Booking future = createBooking(booker, item,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3));
        future.setStatus(StatusEnum.APPROVED.name());
        bookingRepository.save(future);
        List<BookingResponseDTO> result = bookingService.getAllBookingByOwnerId(owner.getId(), FUTURE);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByOwnerId_whenWaiting() {
        Booking waiting = createBooking(booker, item,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3));
        waiting.setStatus(StatusEnum.WAITING.name());
        bookingRepository.save(waiting);
        List<BookingResponseDTO> result = bookingService.getAllBookingByOwnerId(owner.getId(), WAITING);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllBookingByOwnerId_whenRejected() {
        Booking rejected = createBooking(booker, item,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3));
        rejected.setStatus(StatusEnum.REJECTED.name());
        bookingRepository.save(rejected);
        List<BookingResponseDTO> result = bookingService.getAllBookingByOwnerId(owner.getId(), REJECTED);
        assertThat(result).hasSize(1);
    }

    @Test
    void testBookingToString() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus("WAITING");

        assertThat(booking.toString()).contains("id=1");
        assertThat(booking.toString()).contains("status=WAITING");
    }

    private Booking createBooking(User user, Item item, LocalDateTime start, LocalDateTime end) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setItem(item);
        booking.setBookingStart(start);
        booking.setBookingEnd(end);
        booking.setStatus(StatusEnum.WAITING.name());
        return booking;
    }
}