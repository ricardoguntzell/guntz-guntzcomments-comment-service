package br.com.guntz.comments.comment.api.config.exception;

public class CommentNotValidException extends RuntimeException {

    public CommentNotValidException(String message) {
        super(message);
    }
}
