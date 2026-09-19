package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static ru.practicum.shareit.Constants.SHARER_USER_ID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addNewRequest(@Valid @RequestBody ItemRequestDto newRequest,
                                                @RequestHeader(SHARER_USER_ID) Long authorId) {
        return itemRequestClient.addNewRequest(newRequest,authorId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemRequestClient.getItemRequests(ownerId, false);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemRequestClient.getItemRequests(ownerId,true);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(SHARER_USER_ID) Long authorId,
                                             @PathVariable Long requestId) {
        return itemRequestClient.getItemRequestById(authorId,requestId);
    }
}
