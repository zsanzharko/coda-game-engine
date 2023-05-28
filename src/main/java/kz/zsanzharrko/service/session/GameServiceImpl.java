package kz.zsanzharrko.service.session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import kz.zsanzharrko.config.GameConfig;
import kz.zsanzharrko.exception.game.GameException;
import kz.zsanzharrko.exception.game.GameStartException;
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
  public UUID startSession(List<Player> players) {
    if (players == null) {
      throw new GameStartException("Players can not be null");
    } else if (!(gameConfig.getSessionPlayers() == players.size())) {
      throw new GameStartException("Players is not be compatibility size player on game",
              new GameException("Have problem with connect service... Check rule connect player." +
                      " Also check waiting room"));
    }
    log.debug("Players size: " + players.size());
    final UUID session = UUID.randomUUID();
    // check preparation for start playing game
    log.debug("Game properties");
    log.debug(gameConfig.toString());
    for (Player player : players) {
      if (!(RuleManager.prepareToGameSession(player))) {
        log.debug(String.format("Can't prepared player %s to this game", player));
        throw new GameStartException("Can't start cause preparation is been successfully.");
      }
    }
    // set players on state in game
    players.forEach(player -> player.setState(PlayerState.IN_GAME));
    log.debug("Adding players to game session");
    GameSession gameSession;
    try {
      gameSession = new GameSession(players, gameConfig);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    uuidGameSessionMap.put(session, gameSession);
    return session;
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
