package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentsDTO;
import ru.practicum.shareit.item.dto.ItemDTO;

import static ru.practicum.shareit.Constants.SHARER_USER_ID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody ItemDTO itemDTO,
                                      @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.createItem(itemDTO,ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId,
                          @RequestBody ItemDTO itemDTO,
                          @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.updateItem(itemId,itemDTO,ownerId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@Valid @PathVariable Long itemId,
                           @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.getItem(itemId,ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnerItems(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.getAllOwnerItems(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByNameDescription(
            @RequestParam String text,
            @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemClient.findByNameDescription(text,ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@PathVariable Long itemId,
                                    @Valid @RequestBody CommentsDTO commentsDTO,
                                    @RequestHeader(SHARER_USER_ID) Long authorId) {
        return itemClient.addCommentToItem(itemId,commentsDTO,authorId);
    }
}
