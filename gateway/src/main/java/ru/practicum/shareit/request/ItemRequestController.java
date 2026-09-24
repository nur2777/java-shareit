package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static ru.practicum.shareit.Constants.SHARER_USER_ID;

/**
 * Контроллер для функционала по работе c запросами о вещах
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    /**
     * Эндпоинт на создание нового запроса вещи
     * @param newRequest данные нового запроса
     * @param authorId автор запроса
     * @return данные созданного запроса
     */
    @PostMapping
    public ResponseEntity<Object> addNewRequest(@Valid @RequestBody ItemRequestDto newRequest,
                                                @RequestHeader(SHARER_USER_ID) Long authorId) {
        return itemRequestClient.addNewRequest(newRequest,authorId);
    }

    /** Эндпоинт для получения списка своих запросов
     * @param ownerId -идентификатор автора запросов
     * @return список запросов вместе с данными об ответах на них
     */
    @GetMapping
    public ResponseEntity<Object> getItemRequests(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemRequestClient.getItemRequests(ownerId, false);
    }

    /** Эндпоинт для получения списка запросов созданных другими пользователями.
     * @param ownerId -идентификатор автора запроса
     * @return список запросов вместе с данными об ответах на них
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemRequestClient.getItemRequests(ownerId,true);
    }

    /** Эндпоинт для получения данных об одном конкретном запросе вместе с данными об ответах на него
     * @param authorId -автор запроса
     * @param requestId -идентификатор запроса
     * @return список запросов вместе с данными об ответах на них
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(SHARER_USER_ID) Long authorId,
                                             @PathVariable Long requestId) {
        return itemRequestClient.getItemRequestById(authorId,requestId);
    }
}
