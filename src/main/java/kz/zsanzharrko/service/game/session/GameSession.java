package kz.zsanzharrko.service.game.session;


import kz.zsanzharrko.model.GameCard;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.model.PlayerState;
import kz.zsanzharrko.service.game.session.preparation.SessionGameBalancerService;
import kz.zsanzharrko.service.game.statistic.GameStatisticsState;
import kz.zsanzharrko.service.game.statistic.SendGameStatistics;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

  public GameSession(List<Player> players, Properties gameRuleProperties) throws Exception {
    this.gameArena = new GameArena(players, gameRuleProperties);
    this.gameBalancerService = SessionGameBalancerService.getInstance(gameRuleProperties);
    this.playersWithCard = this.gameBalancerService.preparationBalanceCards(players);
    if (this.playersWithCard == null) {
      throw new Exception("Can init cards on players");
    }
    this.roundState = GameRoundState.NONE;
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
    if (notExist(player) || invalidCard(player, card)) {
      return null;
    }
    return gameArena.addCard(player, row, card);
  }

  @Override
  public boolean removeCard(Player player, Integer row, GameCard card) {
    if (notExist(player) || invalidCard(player, card)) {
      return false;
    }
    return gameArena.removeCard(player, row, card);
  }

  /**
   * Get information about a current on game arena.
   * This method send statistics to implemented services
   * like server side or mobile app
   *
   * @param sender send statistics to implemented services
   */
  public void sendStatistics(SendGameStatistics sender) {
    Map<Player, Map<GameStatisticsState, String>> playersStatistics = getStatistics();

    sender.send(playersStatistics);
  }

  @Override
  public Map<Player, Map<GameStatisticsState, String>> getStatistics() {
    return gameArena.getStatistics();
  }

  @Override
  public Map<Integer, List<GameCard>> getArenaFromPlayer(Player player) {
    if (notExist(player)) {
      return null;
    }
    return gameArena.getArena(player);
  }

  @Override
  public List<GameCard> getArenaFromPlayer(Player player, int row) {
    if (notExist(player)) {
      return null;
    }
    return gameArena.getArena(player, row);
  }

  private boolean notExist(Player player) {
    return !gameArena.getPlayers().contains(player)
            || !gameArena.getArena().containsKey(player)
            || player.getState() != PlayerState.IN_GAME;
  }

  private boolean invalidCard(Player player, GameCard card) {
    return !playersWithCard.containsKey(player)
            || !playersWithCard.get(player).contains(card);
  }
}
