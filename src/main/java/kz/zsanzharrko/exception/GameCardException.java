package kz.zsanzharrko.exception;

public class GameCardException extends GameSessionException {
    public GameCardException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GameCardException(String message) {
        super(message);
    }
}
