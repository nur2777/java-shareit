package ru.practicum.shareit.common;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

/**
 * Класс для вспомогательных методов
 */
public final class ShareItUtils {
    private ShareItUtils() {

    }

    /** Метод проверки идентификатора на null
     * @param id идентификатор
     * @param msgPrefix  префикс для сообщения об ошибке
     */
    public static void idIsNullCheck(Long id, String msgPrefix) {
        if (id == null) {
            throw new ValidationException(msgPrefix + " должен быть заполнен!");
        }
    }

    public static void itemAvailableCheck(Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования!");
        }
    }
}
