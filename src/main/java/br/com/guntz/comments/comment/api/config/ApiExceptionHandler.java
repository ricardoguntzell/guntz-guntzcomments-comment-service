package br.com.guntz.comments.comment.api.config;

import br.com.guntz.comments.comment.api.config.exception.CommentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.channels.ClosedChannelException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CommentNotFoundException.class)
    public ProblemDetail handler(CommentNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Not Found");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("/errors/not-found"));

        return problemDetail;
    }

    @ExceptionHandler({
            SocketTimeoutException.class,
            ConnectException.class,
            ClosedChannelException.class
    })
    public ProblemDetail handler(IOException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);

        problemDetail.setTitle("Gateway timeout");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("/errors/gateway-timeout"));

        return problemDetail;
    }

//    @ExceptionHandler(SensorMonitoringClientBadGatewayException.class)
//    public ProblemDetail handler(SensorMonitoringClientBadGatewayException e){
//        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
//
//        problemDetail.setTitle("Bad Gateway");
//        problemDetail.setDetail(e.getMessage());
//        problemDetail.setType(URI.create("/errors/bad-gateway"));
//
//        return problemDetail;
//    }

}
