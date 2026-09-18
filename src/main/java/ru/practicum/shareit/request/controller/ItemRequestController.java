package ru.practicum.shareit.request.controller;

import ru.practicum.shareit.request.dto.ItemRequestDto;

/**
 * Интерфейс для контроллера по работе c запросами о вещах
 */
public interface ItemRequestController {
    /**
     * Эндпоинт на создание нового запроса вещи
     * @param newRequest данные нового запроса
     * @param authorId автор запроса
     * @return данные созданного запроса
     */
    ItemRequestDto addNewRequest(ItemRequestDto newRequest, Long authorId);
}
