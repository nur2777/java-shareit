package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

/**
 * Интерфейс реализует хранение данных для сущности Вещь
 */
public interface ItemDAO {

    /** Создание вещи
     * @param item данные о вещи
     * @return объект созданной вещи
     */
    Item createItem(Item item);

    /** Обновление вещи
     * @param item измененные данные
     * @return обновлённая вещь
     */
    Item updateItem(Item item);

    /** Удаление вещи
     * @param itemId идентификатор удаляемой вещи
     */
    void deleteItem(Long itemId);

    /** Получение вещи по ID
     * @param itemId идентификатор вещи
     * @return объект вещи
     */
    Item getItemById(Long itemId);

    /** Метод получения списка всех вещей у заданного пользователя
     * @param ownerId идентификатор владельца
     * @return список вещей
     */
    Collection<Item> getAllOwnerItems(Long ownerId);

    /** Метод поиска вещей по имени и описанию
     * @return список вещей
     */
    Collection<Item> findItemsByNameAndDesc(String text);
}
