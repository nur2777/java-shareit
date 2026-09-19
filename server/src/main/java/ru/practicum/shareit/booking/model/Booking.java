package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Модель данных для бронирования
 */
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "bookings")
public class Booking {
    /**
     * Идентификатор бронирования
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Пользователь арендатор вещи
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id")
    @ToString.Exclude
    private User user;
    /**
     * Бронируемая вещь
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private Item item;

    /**
     * Дата и время начала брони
     */
    @Column(name = "booking_start_datetime")
    private LocalDateTime bookingStart;

    /**
     * Дата и время окончания брони
     */
    @Column(name = "booking_end_datetime")
    private LocalDateTime bookingEnd;

    /**
     * Статус бронирования
     */
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        return id != null && id.equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
