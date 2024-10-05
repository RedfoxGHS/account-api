package br.com.coderbank.redfoxghs.account_api.exception.dbexceptions;

public class ConflictDatabaseException extends CustomDatabaseException {

    public ConflictDatabaseException(String message) {
        super(message);
    }

    public ConflictDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
