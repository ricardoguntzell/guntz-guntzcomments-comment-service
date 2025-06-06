package br.com.guntz.comments.comment.api.config.exception;

public class InvalidUUIDException extends RuntimeException {

    public InvalidUUIDException(Exception e) {
        super(e.getMessage());
    }
}
