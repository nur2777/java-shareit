package ru.practicum.shareit.user.model;


import jakarta.persistence.*;
import lombok.*;

/**
 * Модель данных пользователя
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    /**
     * Идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_Id", nullable = false)
    private Long id;
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * Электронная почта пользователя
     */
    @Column(name = "email")
    private String email;
}
