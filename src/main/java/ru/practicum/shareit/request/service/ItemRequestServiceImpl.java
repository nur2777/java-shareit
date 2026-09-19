package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.ShareItUtils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapping.ItemRequestMap;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemReqRep;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto addNewRequest(ItemRequestDto itemRequestDto, Long userId) {
        ShareItUtils.idIsNullCheck(userId,"Идентификатор пользователя, автора запроса,");
        if (itemRequestDto == null) {
            throw new ValidationException("Не заполнено тело запроса!");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден "));
        ItemRequest itemRequest = ItemRequestMap.itemRequestDtoToItemRequest(itemRequestDto);
        itemRequest.setUser(user);
        return ItemRequestMap.itemRequestToItemRequestDTO(itemReqRep.save(itemRequest),getAddedItems(List.of(itemRequest)));
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Long ownerId,boolean all) {
        ShareItUtils.idIsNullCheck(ownerId,"Идентификатор пользователя, автора запроса,");
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + ownerId + " не найден "));
        List<ItemRequest> itemRequests = (all) ? itemReqRep.findByUserIdNotOrderByCreationDateDesc(ownerId) :
                itemReqRep.findByUserIdOrderByCreationDateDesc(ownerId);
        return itemRequests.stream()
                .map(itemRequest ->   ItemRequestMap.itemRequestToItemRequestDTO(itemRequest,getAddedItems(List.of(itemRequest))))
                .toList();
    }

    @Override
    public ItemRequestDto getItemRequestById(Long requestId) {
        ShareItUtils.idIsNullCheck(requestId,"Идентификатор запроса");
        ItemRequest itemRequest = itemReqRep.findById(requestId).orElseThrow(() ->
                new ValidationException("Запрос с id " + requestId + "не найден"));
        return ItemRequestMap.itemRequestToItemRequestDTO(itemRequest,getAddedItems(List.of(itemRequest)));
    }

    /** Метод для получения списка вещей
     * @param itemRequests - список запросов для которых нужно получить этот список
     * @return список вещей по указанным запросам
     */
    private List<Item> getAddedItems(List<ItemRequest> itemRequests) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> requestIds = itemRequests.stream()
                                .map(ItemRequest::getId)
                                .toList();
        return itemRepository.findAllByRequestIdIn(requestIds);
    }

}
