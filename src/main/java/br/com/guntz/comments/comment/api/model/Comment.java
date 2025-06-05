package br.com.guntz.comments.comment.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class Comment {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    private String text;

    private String author;

    private OffsetDateTime createdAt;

}
