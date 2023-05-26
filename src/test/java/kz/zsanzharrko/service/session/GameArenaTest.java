package kz.zsanzharrko.service.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kz.zsanzharrko.config.GameConfig;
import kz.zsanzharrko.config.GamePropConfigResolver;
import kz.zsanzharrko.gamecard.GameCard;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.utils.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameArenaTest {

  private static GameServiceImpl gameService;
  private static GameConfig gameConfig;

  @BeforeAll
  public static void init() throws IOException {
    gameConfig = GamePropConfigResolver.getGameConfig(
        FileUtils.getPropertiesFromFile("game-rule.properties")
    );
    gameService = GameServiceImpl.getInstance(gameConfig);
  }

  @Test
  void initArena() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    int cardSize = gameConfig.getGameCardsInitSize();
    int playerSize = gameConfig.getSessionPlayers();
    for (int i = 0; i < cardSize; i++) {
      cards.add(new GameCard("Card1", "Desc", 101 + i));
    }
    // add players
    for (int i = 0; i < playerSize; i++) {
      players.add(new Player(String.format("Player %s", i + 1), cards));
    }
    UUID sessionGame = gameService.startSession(players);
    assertNotNull(sessionGame);
    GameSession gameSession = gameService.getGameBySession(sessionGame);

    assertNotNull(gameSession.getGameArena());
    assertNotNull(gameSession.getGameArena().getArena());
    assertEquals(2, gameSession.getGameArena().getArena().size());
  }

  @Test
  void addCard() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    int cardSize = gameConfig.getGameCardsInitSize();
    int playerSize = gameConfig.getSessionPlayers();
    for (int i = 0; i < cardSize; i++) {
      cards.add(new GameCard(String.format("Card %d", i), "Desc", 101 + i));
    }
    // add players
    for (int i = 0; i < playerSize; i++) {
      players.add(new Player(String.format("Player %s", i + 1), cards));
    }
    UUID sessionGame = gameService.startSession(players);
    assertNotNull(sessionGame);
    GameSession gameSession = gameService.getGameBySession(sessionGame);

    Optional<GameCard> card = cards.stream().findFirst();
    Optional<Player> player = players.stream().findFirst();
    assertTrue(card.isPresent());
    assertTrue(player.isPresent());
    // add cards to arena
    gameSession.getGameArena().addCard(player.get(), 0, card.get());
    gameSession.getGameArena().addCard(player.get(), 1, card.get());
    // assert place cards
    assertEquals(1, gameSession.getGameArena().getArena(player.get(), 0).size());
    assertEquals(1, gameSession.getGameArena().getArena(player.get(), 1).size());

    assertNull(gameSession.getGameArena().addCard(player.get(), 2, card.get()));

  }

  @Test
  void removeCard() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    int cardSize = gameConfig.getGameCardsInitSize();
    int playerSize = gameConfig.getSessionPlayers();
    for (int i = 0; i < cardSize; i++) {
      cards.add(new GameCard(String.format("Card %d", i), "Desc", 101 + i));
    }
    // add players
    for (int i = 0; i < playerSize; i++) {
      players.add(new Player(String.format("Player %s", i + 1), cards));
    }
    UUID sessionGame = gameService.startSession(players);
    assertNotNull(sessionGame);
    GameSession gameSession = gameService.getGameBySession(sessionGame);

    Optional<GameCard> card = cards.stream().findFirst();
    Optional<Player> player = players.stream().findFirst();
    assertTrue(card.isPresent());
    assertTrue(player.isPresent());
    // add cards to arena
    gameSession.getGameArena().addCard(player.get(), 0, card.get());

    assertTrue(gameSession.getGameArena().removeCard(player.get(), 0, card.get()));

    GameCard otherCard = new GameCard("Other Card", "Desc", 1000);
    assertFalse(gameSession.getGameArena().removeCard(player.get(), 0, otherCard));
    assertFalse(gameSession.getGameArena().removeCard(player.get(), 1, otherCard));
  }

  @Test
  void getStatistics() {
  }
}