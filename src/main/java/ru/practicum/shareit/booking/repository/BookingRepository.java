package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    /** Список всех бронирований текущего пользователя
     * @param userId текущий пользователь
     * @return Список бронирований
     */
    List<Booking> findAllByUserId(Long userId);

    /** Список всех завершенных и подтвержденных бронирований текущего пользователя
     * @param userId текущий пользователь
     * @param status  статус бронирования
     * @param bookingDateTime  дата и время бронирования
     * @return Список бронирований
     */
    @Query("select b " +
            "from Booking as b " +
            "join b.user as u " +
            "where u.id = ?1 " +
            "      and b.status = ?2 " +
            "      and ( " +
            "          (?3 = 'PAST' and b.bookingEnd < ?4) or " +
            "          (?3 = 'CURRENT' and ?4 between b.bookingStart and b.bookingEnd) or " +
            "          (?3 = 'FUTURE' and b.bookingStart > ?4) or " +
            "          (?3 = 'WAITING')  or  (?3 = 'REJECTED') " +
            "      ) " +
            "order by b.bookingStart desc")
    List<Booking> findAllByUserIdAndState(Long userId, String status, String state, LocalDateTime bookingDateTime);

}
