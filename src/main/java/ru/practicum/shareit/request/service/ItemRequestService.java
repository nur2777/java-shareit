package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

/**
 * Интерфейс для класса с бизнес-логикой по созданию запроса о новой вещи
 */
public interface ItemRequestService {
    /** Метод создания нового запроса
     * @param itemRequestDto данные нового запроса
     * @param userId автор запроса
     * @return данные созданного запроса
     */
    ItemRequestDto addNewRequest(ItemRequestDto itemRequestDto,Long userId);
}
