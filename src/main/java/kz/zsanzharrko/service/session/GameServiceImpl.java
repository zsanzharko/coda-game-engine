package kz.zsanzharrko.service.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import kz.zsanzharrko.config.GameConfig;
import kz.zsanzharrko.exception.GameException;
import kz.zsanzharrko.exception.GameStartException;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.model.PlayerState;
import kz.zsanzharrko.service.rule.RuleManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameServiceImpl implements GameService {
  private static GameServiceImpl service;
  private final GameConfig gameConfig;
  private final Map<UUID, GameSession> uuidGameSessionMap;

  private GameServiceImpl(GameConfig gameConfig) {
    this.gameConfig = gameConfig;
    this.uuidGameSessionMap = new HashMap<>();
  }

  @Override
  public UUID startSession(Set<Player> players) throws GameException {
    if (players == null) {
      throw new GameStartException("When session is starting, players can not be null.");
    } else if (!(gameConfig.getSessionPlayers() == players.size())) {
      //todo change exception
      throw new GameStartException("Players is not be compatibility size player on game",
              new GameException("Have problem with connect service... Check rule connect player." +
                      " Also check waiting room"));
    }
    log.debug("Players size: " + players.size());
    final UUID sessionId = UUID.randomUUID();
    players.forEach(RuleManager::prepareToGameSession);
    players.forEach(player -> player.setState(PlayerState.IN_GAME));
    log.debug("Adding players to game session");
    GameSession gameSession = new GameSession(players, gameConfig);
    uuidGameSessionMap.put(sessionId, gameSession);
    return sessionId;
  }

  @Override
  public void stopSession(UUID uuidSession) {
    GameSession gameSession = uuidGameSessionMap.get(uuidSession);
    if (gameSession != null) {
      gameSession.stopSession();
      uuidGameSessionMap.remove(uuidSession);
    }
  }

  public GameSession getGameBySession(UUID session) {
    return uuidGameSessionMap.get(session);
  }

  public static GameServiceImpl getInstance(GameConfig gameConfig) {
    if (service == null) {
      service = new GameServiceImpl(gameConfig);
    }
    return service;
  }
}
