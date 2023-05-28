package kz.zsanzharrko.service.session;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kz.zsanzharrko.config.GameConfig;
import kz.zsanzharrko.gamecard.GameCard;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.model.PlayerState;
import kz.zsanzharrko.service.session.preparation.SessionGameBalancerService;
import kz.zsanzharrko.service.statistic.GameStatisticsState;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provide full game control on session with players.
 *
 * @author Sanzhar
 * @see GameServiceImpl
 */
@Slf4j
@Getter
public class GameSession implements ArenaService {
  @Setter
  private GameRoundState roundState;
  private Map<Player, List<GameCard>> playersWithCard;
  private GameArena gameArena;
  private SessionGameBalancerService gameBalancerService;

  public GameSession(List<Player> players, GameConfig gameConfig) throws Exception {
    this.gameArena = new GameArena(players, gameConfig);
    this.gameBalancerService = SessionGameBalancerService.getInstance(gameConfig);
    this.playersWithCard = this.gameBalancerService.preparationBalanceCards(players);
    if (this.playersWithCard == null) {
      throw new Exception("Can't init cards on players, cause ");
    }
    this.roundState = GameRoundState.NONE;
    log.debug("Game session is setted");
  }

  public Set<Player> getPlayers() {
    return new HashSet<>(playersWithCard.keySet());
  }

  void stopSession() {
    if (roundState == GameRoundState.NONE) {
      playersWithCard.keySet().forEach(p -> p.setState(PlayerState.NONE));
      playersWithCard = null;
      gameArena = null;
      gameBalancerService = null;
    }
  }

  @Override
  public GameCard addCard(Player player, Integer row, GameCard card) {
    if (playerNotExist(player) || invalidCard(player, card)) {
      return null;
    }
    return gameArena.addCard(player, row, card);
  }

  @Override
  public boolean removeCard(Player player, Integer row, GameCard card) {
    if (playerNotExist(player) || invalidCard(player, card)) {
      return false;
    }
    return gameArena.removeCard(player, row, card);
  }

  @Override
  public Map<Player, Map<GameStatisticsState, String>> getStatistics() {
    return gameArena.getStatistics();
  }

  @Override
  public Map<Integer, List<GameCard>> getArenaFromPlayer(Player player) {
    if (playerNotExist(player)) {
      return null;
    }
    return gameArena.getArena(player);
  }

  @Override
  public Map<Player, Map<Integer, List<GameCard>>> getArenaForPlayerId(String id) {
    if (playerNotExist(id)) {
      return null;
    }
    return gameArena.getArena();
  }

  private boolean playerNotExist(Player player) {
    return !gameArena.getPlayers().contains(player)
            || !gameArena.getArena().containsKey(player);
  }

  private boolean playerNotExist(String playerId) {
    return gameArena.getPlayers().stream().noneMatch((p) -> p.getId().equals(playerId))
            || gameArena.getArena().keySet().stream().noneMatch((p) -> p.getId().equals(playerId));
  }

  private boolean invalidCard(Player player, GameCard card) {
    return !playersWithCard.containsKey(player)
            || !playersWithCard.get(player).contains(card);
  }
}
