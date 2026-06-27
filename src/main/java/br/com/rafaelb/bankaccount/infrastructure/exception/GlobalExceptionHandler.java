package br.com.rafaelb.bankaccount.infrastructure.exception;

import br.com.rafaelb.bankaccount.application.exception.AccountNotFoundException;
import br.com.rafaelb.bankaccount.application.exception.DuplicateAccountException;
import br.com.rafaelb.bankaccount.application.exception.InvalidDateRangeException;
import br.com.rafaelb.bankaccount.application.exception.OperationNotFoundException;
import br.com.rafaelb.bankaccount.domain.exception.InsufficientFundsException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {

        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<Field> fields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Field.builder()
                        .field(error.getField())
                        .description(error.getDefaultMessage())
                        .build())
                .toList();

        ApiError apiError = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("To submit the request, you must fill in all the required fields.")
                .fields(fields)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String message = "Request body is invalid.";

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidFormat) {

            String field = invalidFormat.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .reduce((a, b) -> b)
                    .orElse("unknown");

            message = String.format(
                    "Field '%s' has an invalid value.",
                    field
            );
        }

        if (cause instanceof PropertyBindingException propertyBinding) {

            message = String.format(
                    "Field '%s' is not recognized.",
                    propertyBinding.getPropertyName()
            );
        }

        ApiError error = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handle(
            AccountNotFoundException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.NOT_FOUND,
                ErrorCode.ACCOUNT_NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiError> handle(
            InsufficientFundsException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INSUFFICIENT_FUNDS,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<ApiError> handle(
            DuplicateAccountException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.BAD_REQUEST,
                ErrorCode.DUPLICATE_ACCOUNT,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ApiError> handle(
            InvalidDateRangeException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_DATE_RANGE,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(OperationNotFoundException.class)
    public ResponseEntity<ApiError> handle(
            OperationNotFoundException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.NOT_FOUND,
                ErrorCode.OPERATION_NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            ErrorCode code,
            String message,
            HttpServletRequest request) {

        ApiError error = ApiError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .code(code)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(error);
    }

}