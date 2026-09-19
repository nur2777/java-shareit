package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentsDTO;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.Collection;

/**
 * Интерфейс реализует логику CRUD-операций для сущности Вещь
 */
public interface ItemService {
    /** Метод создания вещи
     * @param itemDTO данные о вещи
     * @param ownerId идентификатор владельца
     * @return созданный объект вещи
     */
    ItemDTO createItem(ItemDTO itemDTO, Long ownerId);

    /** Метод обновления вещи
     * @param itemId идентификатор вещи
     * @param itemDTO данные о вещи
     * @param ownerId идентификатор владельца
     * @return обновлённый объект вещи
     */
    ItemDTO updateItem(Long itemId, ItemDTO itemDTO, Long ownerId);

    /** Метод удаления вещи
     * @param itemId идентификатор вещи
     */
    void deleteItem(Long itemId);

    /** Метод получения данных об указанной вещи
     * @param itemId идентификатор вещи
     * @return данные найденной вещи
     */
    ItemDTO getItem(Long itemId, Long ownerId);

    /** Метод просмотра владельцем списка всех его вещей
     * @param ownerId идентификатор владельца
     * @return список вещей
     */
    Collection<ItemDTO> getAllOwnerItems(Long ownerId);

    /** Метод поиска вещи по названию и описанию
     * @param text текст поиска
     * @return список вещей
     */
    Collection<ItemDTO> findByNameDescription(String text);

    /**
     * Метод создания отзыва о вещи
     * @param itemId идентификатор обновляемой вещи
     * @param comment отзыв
     * @param authorId владелец отзыва
     * @return объект созданного отзыва
     */
    CommentsDTO addCommentToItem(Long itemId, CommentsDTO comment, Long authorId);

}
