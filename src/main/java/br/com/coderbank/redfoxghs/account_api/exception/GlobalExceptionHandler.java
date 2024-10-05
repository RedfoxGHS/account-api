package br.com.coderbank.redfoxghs.account_api.exception;

import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.ConflictDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.CustomDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.GeneralDatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String error = "Validation failed";

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), error, errors.toString());
    }

    @ExceptionHandler(CustomDatabaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomDatabaseException(CustomDatabaseException ex) {
        String error = "BadRequest";

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), error, ex.getMessage());
    }

    @ExceptionHandler(ConflictDatabaseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictDatabaseException(ConflictDatabaseException ex) {
        String error = "Conflict";

        return new ErrorResponse(HttpStatus.CONFLICT.value(), error, ex.getMessage());
    }

    @ExceptionHandler(GeneralDatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralDatabaseException(GeneralDatabaseException ex) {
        String error = "InternalServerError";
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), error, ex.getMessage());
    }
}

