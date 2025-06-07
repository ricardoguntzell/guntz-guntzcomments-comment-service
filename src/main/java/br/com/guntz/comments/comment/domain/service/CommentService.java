package br.com.guntz.comments.comment.domain.service;

import br.com.guntz.comments.comment.api.client.ModerationServiceClient;
import br.com.guntz.comments.comment.api.config.exception.CommentNotValidException;
import br.com.guntz.comments.comment.api.model.ModerationInput;
import br.com.guntz.comments.comment.api.model.ModerationOutput;
import br.com.guntz.comments.comment.domain.model.Comment;
import br.com.guntz.comments.comment.domain.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModerationServiceClient moderationServiceClient;

    public Comment save(Comment comment){
        ModerationInput moderationInput = convertToModerationInput(comment);
        ModerationOutput moderation = moderationServiceClient.validatedComment(moderationInput);

        validatedComment(moderation);

        return commentRepository.saveAndFlush(comment);
    }

    private void validatedComment(ModerationOutput moderation) {
        if (!moderation.getApproved()) {
            log.warn("Comment Prohibited: {}", moderation.getReason());
            throw new CommentNotValidException(moderation.getReason());
        }
    }

    private ModerationInput convertToModerationInput(Comment comment) {
        return ModerationInput.builder()
                .commentId(comment.getId().toString())
                .text(comment.getText())
                .build();
    }


}
