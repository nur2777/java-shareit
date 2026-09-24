package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentsDTO;
import ru.practicum.shareit.item.dto.ItemDTO;

import static ru.practicum.shareit.Constants.SHARER_USER_ID;

/**
 * Контроллер для функционала по работе с вещами
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    /**
     * Эндпоинт на создание вещи
     * @param itemDTO данные новой вещи
     * @param ownerId владелец новой вещи
     * @return объект созданной вещи
     */
    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody ItemDTO itemDTO,
                                      @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.createItem(itemDTO,ownerId);
    }

    /**
     * Эндпоинт на обновление данных о вещи
     * @param itemId идентификатор обновляемой вещи
     * @param itemDTO измененные данные о вещи
     * @param ownerId владелец обновляемой вещи
     * @return объект обновленного пользователя
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId,
                          @RequestBody ItemDTO itemDTO,
                          @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.updateItem(itemId,itemDTO,ownerId);
    }

    /**
     * Эндпоинт получения конкретной вещи
     * @param itemId идентификатор вещи
     * @param ownerId идентификатор владельца
     * @return объект
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@Valid @PathVariable Long itemId,
                           @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.getItem(itemId,ownerId);
    }

    /**
     * Эндпоинт получения списка всех вещей заданного пользователя
     * @return список всех вещей владельца
     */
    @GetMapping
    public ResponseEntity<Object> getAllOwnerItems(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.getAllOwnerItems(ownerId);
    }

    /**
     * Эндпоинт поиска вещи по названию и описанию
     * @return список найденных вещей
     */
    @GetMapping("/search")
    public ResponseEntity<Object> findByNameDescription(
            @RequestParam String text,
            @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.findByNameDescription(text,ownerId);
    }

    /**
     * Эндпоинт для создания отзыва о вещи
     * @param itemId идентификатор обновляемой вещи
     * @param commentsDTO отзыв
     * @param authorId владелец отзыва
     * @return объект созданного отзыва
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@PathVariable Long itemId,
                                    @Valid @RequestBody CommentsDTO commentsDTO,
                                    @RequestHeader(SHARER_USER_ID) Long authorId) {
        return itemClient.addCommentToItem(itemId,commentsDTO,authorId);
    }
}
