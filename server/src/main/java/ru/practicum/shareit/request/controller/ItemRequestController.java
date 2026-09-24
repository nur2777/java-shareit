package ru.practicum.shareit.request.controller;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

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

    /** Эндпоинт для получения списка своих запросов
     * @param ownerId -идентификатор автора запросов
     * @return список запросов вместе с данными об ответах на них
     */
    List<ItemRequestDto> getItemRequests(Long ownerId);

    /** Эндпоинт для получения списка запросов созданных другими пользователями.
     * @param ownerId -идентификатор автора запроса
     * @return список запросов вместе с данными об ответах на них
     */
    List<ItemRequestDto> getAllItemRequests(Long ownerId);

    /** Эндпоинт для получения данных об одном конкретном запросе вместе с данными об ответах на него
     * @param authorId -автор запроса
     * @param requestId -идентификатор запроса
     * @return список запросов вместе с данными об ответах на них
     */
    ItemRequestDto getItemRequestById(Long authorId, Long requestId);
}
