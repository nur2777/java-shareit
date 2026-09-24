package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

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

    /** Метод для получения списка запросов
     * @param ownerId -идентификатор автора запросов
     * @param all - true - режим получения всех запросов созданных другими пользователями.
     *              false - режим получения своих запросов
     * @return список запросов вместе с данными об ответах на них
     */
    List<ItemRequestDto> getItemRequests(Long ownerId, boolean all);

    /** Метод для получения данных об одном конкретном запросе вместе с данными об ответах на него
     * @param requestId -идентификатор запроса
     * @return запрос вместе с данными об ответах на них
     */
    ItemRequestDto getItemRequestById(Long requestId);
}
