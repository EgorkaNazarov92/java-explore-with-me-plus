package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.CreateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, CreateCommentDto createCommentDto);

    List<CommentDto> getEventCommentsByUserId(Long userId, Long eventId);

    List<CommentDto> getEventComments(Long eventId);
}
