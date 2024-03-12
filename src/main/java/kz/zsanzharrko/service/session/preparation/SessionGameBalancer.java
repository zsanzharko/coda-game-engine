package kz.zsanzharrko.service.session.preparation;


import java.util.*;

import kz.zsanzharrko.config.GameConfig;
import kz.zsanzharrko.gamecard.GameCard;
import kz.zsanzharrko.model.Player;

public class SessionGameBalancer {
  private static SessionGameBalancer balancer;
  private final int maxCardOnSession;
  private final Random random;

  private SessionGameBalancer(GameConfig gameConfig) {
    this.maxCardOnSession = gameConfig.getGameCardsInitSize();
    this.random = new Random();
  }

  /**
   * Put cards from players card deck. Have limit size to put on card play.
   * This method will be balance cards on players, that a game need to be normal.
   *
   * @param players for get cards from card deck
   * @return playable cards on current session game
   * todo check algorithm
   */
  public Map<Player, List<GameCard>> preparationBalanceCards(Set<Player> players) {
    if (!correctCardSizeForGame(maxCardOnSession, players)) return null;
    boolean cardsIsBalanced = false;
    Map<Player, List<GameCard>> preparationCardsPlayers = null;
    while (!cardsIsBalanced) {
      preparationCardsPlayers = prepareCards(players);
      List<Integer> sumPowersOnPlayers = new ArrayList<>(players.size());
      preparationCardsPlayers.forEach((player, cards) -> sumPowersOnPlayers.add(cards.stream()
          .map(GameCard::getPower)
          .max(Integer::sum)
          .orElse(-1)));
      // Example
      // player 1     player 2
      //  52            51 == true  3
      //  55            52
      //  52            55
      //  60            50 == false
      //  | a
      // [52, 50, 50, 53]
      //      | b
      // [52, 50, 50, 53]
      int result;
      for (int i = 0; i < sumPowersOnPlayers.size(); i++) {
        for (int j = 1; j < sumPowersOnPlayers.size(); j++) {
          result = Math.abs(sumPowersOnPlayers.get(i) - sumPowersOnPlayers.get(j));
          if (!(result >= 0 && result <= 3)) {
            cardsIsBalanced = false;
            break;
          }
          cardsIsBalanced = true;
        }
        if (!cardsIsBalanced) break;
      }
    }
    return preparationCardsPlayers;
  }

  Map<Player, List<GameCard>> prepareCards(Set<Player> players) {
    Map<Player, List<GameCard>> preparationCardsPlayers = new HashMap<>(players.size());
    for (Player player : players) {
      preparationCardsPlayers.put(player, new LinkedList<>());
      for (int cardIndex = 0; cardIndex < maxCardOnSession; cardIndex++) {
        preparationCardsPlayers.get(player).add(player
            .getCardDeck()
            .get(random.nextInt(maxCardOnSession)));
      }
    }
    return preparationCardsPlayers;
  }

  private boolean correctCardSizeForGame(int maxCardOnSession, Set<Player> players) {
    for (Player player : players) {
      if (!(player.getCardDeck().size() >= maxCardOnSession)) {
        return false;
      }
    }
    return true;
  }


  public static SessionGameBalancer getInstance(GameConfig gameConfig) {
    if (balancer == null) {
      balancer = new SessionGameBalancer(gameConfig);
    }
    return balancer;
  }
}
