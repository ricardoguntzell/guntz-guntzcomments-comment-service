package br.com.guntz.comments.comment.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class CommentOutput {

    private UUID id;

    private String text;

    private String author;

    private OffsetDateTime createdAt;

}
