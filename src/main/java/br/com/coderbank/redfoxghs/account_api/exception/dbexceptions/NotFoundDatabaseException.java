package br.com.coderbank.redfoxghs.account_api.exception.dbexceptions;

public class NotFoundDatabaseException extends RuntimeException {

    public NotFoundDatabaseException(String message) {
        super(message);
    }
}
