package ru.practicum.shareit.item.mapping;

import ru.practicum.shareit.item.dto.CommentsDTO;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMap {

    public static Comment toComment(CommentsDTO commentsDTO, Item item, User user) {
        Comment comment = new Comment();
        if (item != null) {
            comment.setItem(item);
        }
        if (user != null) {
            comment.setAuthor(user);
        }
        if (commentsDTO.getText() != null) {
            comment.setText(commentsDTO.getText());
        }
        return comment;
    }

    public static CommentsDTO toCommentsDTO(Comment comment) {
        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setId(comment.getId());
        commentsDTO.setText(comment.getText());
        commentsDTO.setCreated(comment.getCreationDate());
        if (comment.getAuthor() != null) {
            commentsDTO.setAuthorName(comment.getAuthor().getName());
        }
        return commentsDTO;
    }
}
