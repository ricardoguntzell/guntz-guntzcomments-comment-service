package br.com.guntz.comments.comment.api.controller;

import br.com.guntz.comments.comment.api.model.Comment;
import br.com.guntz.comments.comment.api.model.CommentInput;
import br.com.guntz.comments.comment.api.model.CommentOutput;
import br.com.guntz.comments.comment.common.IdGenerator;
import br.com.guntz.comments.comment.domain.repository.CommentRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;

    @GetMapping
    public ResponseEntity<Page<CommentOutput>> findAllComments(@PageableDefault Pageable pageable) {
        var comments = commentRepository.findAll(pageable)
                .map(this::convertToOutput);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentOutput> findAllComments(@PathVariable UUID commentId) {
        Comment comment = getCommentById(commentId);

        return ResponseEntity.ok(convertToOutput(comment));
    }

    @PostMapping
    public ResponseEntity<CommentOutput> create(@Valid @RequestBody CommentInput input) {
        Comment comment = commentRepository.saveAndFlush(convertToNewComment(input));

        return ResponseEntity.ok(convertToOutput(comment));
    }

    private Comment getCommentById(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private CommentOutput convertToOutput(Comment comment) {
        return CommentOutput.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(comment.getAuthor())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private Comment convertToNewComment(CommentInput comment) {
        return Comment.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .text(comment.getText())
                .author(comment.getAuthor())
                .createdAt(OffsetDateTime.now())
                .build();
    }

}
