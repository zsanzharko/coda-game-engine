package kz.zsanzharrko.exception.game;

public class GameException extends RuntimeException {

  public GameException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public GameException(String message) {
    super(message);
  }

}
