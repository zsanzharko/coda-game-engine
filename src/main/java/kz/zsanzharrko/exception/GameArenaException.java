package kz.zsanzharrko.exception;

public class GameArenaException extends GameSessionException {
    public GameArenaException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GameArenaException(String message) {
        super(message);
    }
}
