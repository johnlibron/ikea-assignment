package com.ikea.warehouseapp.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String LIST_JOIN_DELIMITER = ", ";
    public static final String FIELD_ERROR_SEPARATOR = ": ";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        final List<String> validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + FIELD_ERROR_SEPARATOR + error.getDefaultMessage())
                .collect(Collectors.toList());
        return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, request, validationErrors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, request, "Malformed JSON request");
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, request,
                exception.getParameterName() + " parameter is missing");
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return getExceptionResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, request,
                exception.getMethod() + " method is not supported. Supported " +
                        (exception.getSupportedHttpMethods().size() > 0 ? "methods are "  : "method is ") +
                        StringUtils.join(exception.getSupportedHttpMethods(), LIST_JOIN_DELIMITER));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return getExceptionResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, request,
                exception.getContentType() + " media type is not supported. Supported media " +
                        (exception.getSupportedMediaTypes().size() > 0 ? "types are "  : "type is ") +
                        StringUtils.join(exception.getSupportedMediaTypes(), LIST_JOIN_DELIMITER));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return getExceptionResponseEntity(HttpStatus.NOT_FOUND, request,
                "No handler found for " + exception.getHttpMethod() + " " + exception.getRequestURL());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception, WebRequest request) {
        return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, request,
                exception.getName() + " should be of type " + exception.getRequiredType().getName());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException exception,
            WebRequest request) {
        final List<String> validationErrors = exception.getConstraintViolations().stream()
                .map(violation -> {
                    String[] param = String.valueOf(violation.getPropertyPath()).split("\\.");
                    return param[param.length-1] + FIELD_ERROR_SEPARATOR + violation.getMessage();
                })
                .collect(Collectors.toList());
        return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, request, validationErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        final HttpStatus status = responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
        final String localizedMessage = exception.getLocalizedMessage();
        final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String message = StringUtils.isNotEmpty(localizedMessage) ? localizedMessage : status.getReasonPhrase();
        logger.error(String.format("message: %s, requested uri: %s", message, path), exception);
        return getExceptionResponseEntity(status, request, message);
    }

    private ResponseEntity<Object> getExceptionResponseEntity(
            HttpStatus status,
            WebRequest request,
            List<String> errors) {
        final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error("errors {} for path {}", errors, path);
        return new ResponseEntity<>(new ApiError(status, errors, getMessageForStatus(status), path), status);
    }

    private ResponseEntity<Object> getExceptionResponseEntity(
            HttpStatus status,
            WebRequest request,
            String errorMessage) {
        final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error("error {} for path {}", errorMessage, path);
        return new ResponseEntity<>(new ApiError(status, Collections.singletonList(errorMessage),
                getMessageForStatus(status), path), status);
    }

    private String getMessageForStatus(HttpStatus status) {
        switch (status) {
            case BAD_REQUEST:
                return "Invalid Request";
            case INTERNAL_SERVER_ERROR:
                return "Unexpected Error";
            default:
                return status.getReasonPhrase();
        }
    }
}
