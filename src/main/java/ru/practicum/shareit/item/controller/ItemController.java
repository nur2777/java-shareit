package ru.practicum.shareit.item.controller;

import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.Collection;

/**
 * Интерфейс для контроллера по работе с пользователями
 */
public interface ItemController {
    /**
     * Эндпоинт на создание вещи
     * @param newItem данные новой вещи
     * @param ownerId владелец новой вещи
     * @return объект созданной вещи
     */
    ItemDTO add(ItemDTO newItem,Long ownerId);

    /**
     * Эндпоинт на обновление данных о вещи
     * @param itemId идентификатор обновляемой вещи
     * @param itemDTO измененные данные о вещи
     * @param ownerId владелец обновляемой вещи
     * @return объект обновленного пользователя
     */
    ItemDTO update(Long itemId, ItemDTO itemDTO, Long ownerId);

    /**
     * Эндпоинт получения конкретной вещи
     * @param id идентификатор вещи
     * @return объект
     */
    ItemDTO getItem(Long id);

    /**
     * Эндпоинт получения списка всех вещей заданного пользователя
     * @return список всех вещей владельца
     */
    Collection<ItemDTO> getAllOwnerItems(Long ownerId);

    /**
     * Эндпоинт поиска вещи по названию и описанию
     * @return список найденных вещей
     */
    Collection<ItemDTO> findByNameDescription(String text);
}
