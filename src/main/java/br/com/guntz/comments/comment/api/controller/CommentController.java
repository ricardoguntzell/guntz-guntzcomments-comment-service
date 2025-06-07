package br.com.guntz.comments.comment.api.controller;

import br.com.guntz.comments.comment.api.config.exception.CommentNotFoundException;
import br.com.guntz.comments.comment.api.model.CommentInput;
import br.com.guntz.comments.comment.api.model.CommentOutput;
import br.com.guntz.comments.comment.common.IdGenerator;
import br.com.guntz.comments.comment.domain.model.Comment;
import br.com.guntz.comments.comment.domain.repository.CommentRepository;
import br.com.guntz.comments.comment.domain.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comments")
@Slf4j
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentOutput>> findAllComments(@PageableDefault Pageable pageable) {
        log.info("Executing Listing the Comments");

        var comments = commentRepository.findAll(pageable)
                .map(this::convertToOutput);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentOutput> findCommentById(@PathVariable UUID commentId) {
        log.info("Executing Search the Comment: {}", commentId);

        Comment comment = getCommentById(commentId);

        return ResponseEntity.ok(convertToOutput(comment));
    }

    @PostMapping
    public ResponseEntity<CommentOutput> create(@Valid @RequestBody CommentInput input, UriComponentsBuilder uriBuilder) {
        Comment commentSaved = commentService.save(convertToNewComment(input));

        var uri = uriBuilder.path("/api/comments/{commentId}").buildAndExpand(commentSaved.getId()).toUri();

        log.info("Comment Registred: {}", commentSaved.getId());

        return ResponseEntity.created(uri).body(convertToOutput(commentSaved));
    }

    private Comment getCommentById(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
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
