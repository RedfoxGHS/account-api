package br.com.coderbank.redfoxghs.account_api.exception;

import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.CustomDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.GeneralDatabaseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String mensagem = "Erro ao processar a solicitação.";
        String error = "BadRequest";

        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType().equals(UUID.class)) {
                mensagem += " O UUID fornecido é inválido. Certifique-se de que ele tenha o formato correto.";
            }
        }

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), error, mensagem);
    }

    @ExceptionHandler(CustomDatabaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomDatabaseException(CustomDatabaseException ex) {
        String error = "BadRequest";

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), error, ex.getMessage());
    }

    @ExceptionHandler(GeneralDatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralDatabaseException(GeneralDatabaseException ex) {
        String error = "InternalServerError";
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), error, ex.getMessage());
    }
}

