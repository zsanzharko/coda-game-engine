package kz.zsanzharrko.service.game.session;

import kz.zsanzharrko.exception.game.GameException;
import kz.zsanzharrko.exception.game.GameStartException;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.model.PlayerState;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import static kz.zsanzharrko.service.game.rule.RuleManager.prepareToGameSession;

@Slf4j
public class GameServiceImpl implements GameService {
  private static GameServiceImpl service;
  private final Map<UUID, GameSession> uuidGameSessionMap;
  private final Properties gameProperties;

  private GameServiceImpl(Properties gameProperties) {
    this.uuidGameSessionMap = new HashMap<>();
    this.gameProperties = gameProperties;
  }

  @Override
  public UUID startSession(List<Player> players) {
    if (players == null) {
      throw new GameStartException("Players can not be null");
    } else if (!Objects.equals(gameProperties.get("session.players"), String.valueOf(players.size()))) {
      // TODO: 1/25/2023 need to check how to except method
      throw new GameStartException("Players is not be compatibility size player on game",
              new GameException("Have problem with connect service... Check rule connect player." +
                      " Also check waiting room"));
    }
    log.debug("Players size: " + players.size());
    final UUID session = UUID.randomUUID();
    // check preparation for start playing game
    log.debug("Game properties");
    log.debug(gameProperties.toString());
    for (Player player : players) {
      if (!(prepareToGameSession(player))) {
        log.debug(String.format("Can't prepared player %s to this game", player));
        // FIXME: 1/9/2023 Take interface and send information about can't start game
        //  cause player is not prepared. and remove exception if problem on player side.
        throw new GameStartException("Can't start cause preparation is been successfully.");
      }
    }
    // set players on state in game
    players.forEach(player -> player.setState(PlayerState.IN_GAME));
    log.debug("Adding players to game session");
    GameSession gameSession;
    try {
      gameSession = new GameSession(players, gameProperties);
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

  public static GameServiceImpl getInstance(Properties gameProperties) {
    if (service == null) {
      service = new GameServiceImpl(gameProperties);
    }
    return service;
  }
}
