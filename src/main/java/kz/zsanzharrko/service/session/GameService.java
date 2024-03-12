package kz.zsanzharrko.service.session;

import kz.zsanzharrko.exception.GameException;
import kz.zsanzharrko.model.Player;

import java.util.Set;
import java.util.UUID;

public interface GameService {

  UUID startSession(Set<Player> players) throws Exception, GameException;

  void stopSession(UUID session);
}
