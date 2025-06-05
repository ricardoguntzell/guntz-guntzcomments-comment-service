package br.com.guntz.comments.comment.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentInput {

    @NotBlank
    private String text;

    @NotBlank
    private String author;

}
