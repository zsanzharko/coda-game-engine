package kz.zsanzharrko.service.session;

import java.util.*;

import kz.zsanzharrko.config.GameConfig;
import kz.zsanzharrko.gamecard.GameCard;
import kz.zsanzharrko.model.Player;
import lombok.Getter;

/**
 * Control arena on game. Need for Game Session
 *
 * @author Sanzhar
 * @see GameSession
 */
@Getter
//@Immutable
public class GameArena extends Arena {
  private Map<Player, Map<Integer, List<GameCard>>> arena;
  private final GameConfig configuration;

  public GameArena(Set<Player> players, GameConfig gameConfig) {
    super();
    this.configuration = gameConfig;
    initArena(players);
  }

  public void initArena(Set<Player> players) {
    this.arena = new HashMap<>(players.size());
    for (Player player : players) {
      Map<Integer, List<GameCard>> playerArena = new HashMap<>();
      for (int row = 0; row < configuration.getSessionArenaRows(); row++) {
        playerArena.put(row, new LinkedList<>());
      }
      this.arena.put(player, playerArena);
    }
  }

  @Override
  protected GameCard addCard(Player player, Integer row, GameCard card) {
    if (invalidRow(row)) return null;
    Map<Integer, List<GameCard>> playerArena = arena.get(player);
    playerArena.computeIfAbsent(row, k -> new LinkedList<>());
    playerArena.get(row).add(card);
    return card;
  }

  @Override
  protected boolean removeCard(Player player, Integer row, GameCard card) {
    if (invalidRow(row)) return false;
    Map<Integer, List<GameCard>> playerArena = arena.get(player);
    return playerArena.get(row).remove(card);
  }

  @Override
  public Map<Integer, List<GameCard>> getArena(Player player) {
    return arena.get(player);
  }

  @Override
  public List<GameCard> getArena(Player player, int row) {
    if (row >= 0 && row < configuration.getSessionArenaRows()) {
      Map<Integer, List<GameCard>> playerArena = arena.get(player);
      return playerArena.get(row);
    }
    return null;
  }


  private boolean invalidRow(Integer row) {
    return row < 0 || row >= configuration.getSessionArenaRows();
  }
}
