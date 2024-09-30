package ewm.comment.controller;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.CreateCommentDto;
import ewm.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getEventComments(@PathVariable Long eventId) {
        return commentService.getEventComments(eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/comments")
    public List<CommentDto> getEventCommentsByUserId(@PathVariable Long userId, @PathVariable Long eventId) {
        return commentService.getEventCommentsByUserId(userId, eventId);
    }

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid CreateCommentDto createCommentDto
    ) {
        return commentService.addComment(userId, eventId, createCommentDto);
    }
}
