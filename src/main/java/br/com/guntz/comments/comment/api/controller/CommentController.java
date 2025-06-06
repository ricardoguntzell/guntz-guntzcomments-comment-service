package br.com.guntz.comments.comment.api.controller;

import br.com.guntz.comments.comment.api.client.ModerationServiceClient;
import br.com.guntz.comments.comment.api.config.exception.CommentNotFoundException;
import br.com.guntz.comments.comment.api.config.exception.CommentNotValidException;
import br.com.guntz.comments.comment.api.model.CommentInput;
import br.com.guntz.comments.comment.api.model.CommentOutput;
import br.com.guntz.comments.comment.api.model.ModerationInput;
import br.com.guntz.comments.comment.api.model.ModerationOutput;
import br.com.guntz.comments.comment.common.IdGenerator;
import br.com.guntz.comments.comment.domain.model.Comment;
import br.com.guntz.comments.comment.domain.repository.CommentRepository;
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
    private final ModerationServiceClient moderationServiceClient;

    @GetMapping
    public ResponseEntity<Page<CommentOutput>> findAllComments(@PageableDefault Pageable pageable) {
        var comments = commentRepository.findAll(pageable)
                .map(this::convertToOutput);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentOutput> findCommentById(@PathVariable UUID commentId) {
        log.info("Executando busca do comentário: {}", commentId);

        Comment comment = getCommentById(commentId);

        return ResponseEntity.ok(convertToOutput(comment));
    }

    @PostMapping
    public ResponseEntity<CommentOutput> create(@Valid @RequestBody CommentInput input, UriComponentsBuilder uriBuilder) {
        UUID uuid = IdGenerator.generateTimeBasedUUID();

        ModerationInput moderationInput = ModerationInput.builder()
                .commentId(uuid.toString())
                .text(input.getText())
                .build();

        ModerationOutput moderation = moderationServiceClient.validatedComment(moderationInput);

        if (!moderation.getApproved()) {
            throw new CommentNotValidException(moderation.getReason());
        }

        Comment comment = commentRepository.saveAndFlush(convertToNewComment(input));

        var uri = uriBuilder.path("/api/comments/{commentId}").buildAndExpand(comment.getId()).toUri();

        return ResponseEntity.created(uri).body(convertToOutput(comment));
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
