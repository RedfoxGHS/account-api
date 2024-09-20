package br.com.coderbank.redfoxghs.account_api.exception.dbexceptions;

public class GeneralDatabaseException extends RuntimeException {

    public GeneralDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
