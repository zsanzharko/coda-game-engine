package kz.zsanzharrko.service.session;


import java.util.*;

import kz.zsanzharrko.config.GameConfig;
import kz.zsanzharrko.exception.GameArenaException;
import kz.zsanzharrko.exception.GameCardException;
import kz.zsanzharrko.exception.GameSessionException;
import kz.zsanzharrko.gamecard.GameCard;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.service.session.preparation.SessionGameBalancer;
import kz.zsanzharrko.service.statistic.GameStatisticsState;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static kz.zsanzharrko.model.PlayerState.NONE;

/**
 * Provide full game control on session with players.
 *
 * @author Sanzhar
 * @see GameServiceImpl
 */
@Slf4j
@Getter
public class GameSession implements GameSessionService {
  @Setter
  private GameRoundState state;
  private Set<Player> players;
  private GameArena arena;
  private SessionGameBalancer balancer;

  public GameSession(Set<Player> players, GameConfig gameConfig) {
    this.arena = new GameArena(players, gameConfig);
    this.balancer = SessionGameBalancer.getInstance(gameConfig);
    this.state = GameRoundState.NONE;
  }

  @Override
  public GameCard addCard(Player player, Integer row, GameCard card) throws GameSessionException {
    validateCardAndPlayer(player, card);
    return arena.addCard(player, row, card);
  }

  @Override
  public boolean removeCard(Player player, Integer row, GameCard card) throws GameSessionException {
    validateCardAndPlayer(player, card);
    return arena.removeCard(player, row, card);
  }

  @Override
  public Map<Player, Map<GameStatisticsState, String>> getStatistics() {
    Map<Player, Map<GameStatisticsState, String>> statistics = new HashMap<>(players.size());
    players.forEach(player -> {
      Map<GameStatisticsState, String> playerStatistics = new HashMap<>();
      Map<Integer, List<GameCard>> playerArena = arena.getArena(player);
      int maxPower = 0;
      for (List<GameCard> cards : playerArena.values()) {
        Optional<Integer> cardPowerSum = cards.stream()
                .map(GameCard::getPower)
                .max(Integer::sum);
        if (cardPowerSum.isPresent()) {
          maxPower += cardPowerSum.get();
        }
      }
      playerStatistics.put(GameStatisticsState.PLAYER_POWER, Integer.toString(maxPower));
      statistics.put(player, playerStatistics);
    });
    return statistics;
  }

  @Override
  public Map<Player, Map<Integer, List<GameCard>>> getArena() {
    if (arena != null) {
      return arena.getArena();
    }
    return null;
  }

  private void validateCardAndPlayer(Player player, GameCard card) throws GameSessionException {
    if (arena.getArena(player) != null) {
      throw new GameArenaException("Can't find player in arena");
    }
    if (!players.contains(player) || !player.getCardDeck().contains(card)) {
      throw new GameCardException("Can't find player in player list or card is currently contains with card deck");
    }
  }

  void stopSession() {
    if (state == GameRoundState.NONE) {
      players.forEach(p -> p.setState(NONE));
      players = null;
      arena = null;
      balancer = null;
    }
  }
}
