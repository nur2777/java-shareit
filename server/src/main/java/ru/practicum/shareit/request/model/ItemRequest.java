package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


/**
 * Модель данных для сущности запроса вещей
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "requests")
public class ItemRequest {
    /**
     * Идентификатор бронирования
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Описание запроса
     */
    @Column(name = "description")
    private String description;
    /**
     * Пользователь создавший запрос вещи
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id")
    @ToString.Exclude
    private User user;
    /**
     * Дата и время создания запроса
     */
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();
}
