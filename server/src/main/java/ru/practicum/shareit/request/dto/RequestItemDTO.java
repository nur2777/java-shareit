package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO-объект для списка вещей в запросе
 */
@Data
@Builder
public class RequestItemDTO {
    /**
     * Идентификатор вещи
     */
    private Long id;
    /**
     * Название
     */
    private String name;
    /**
     * Идентификатор владельца
     */
    private Long ownerId;
}
