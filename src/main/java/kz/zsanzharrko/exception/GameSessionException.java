package kz.zsanzharrko.exception;

public class GameSessionException extends GameException{
    public GameSessionException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GameSessionException(String message) {
        super(message);
    }
}
