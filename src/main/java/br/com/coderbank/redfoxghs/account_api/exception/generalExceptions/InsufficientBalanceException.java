package br.com.coderbank.redfoxghs.account_api.exception.generalExceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}