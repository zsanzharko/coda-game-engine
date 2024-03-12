package kz.zsanzharrko.exception;

public class GameException extends Throwable {

  public GameException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public GameException(String message) {
    super(message);
  }

}
