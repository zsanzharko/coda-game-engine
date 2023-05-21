package kz.zsanzharrko.service.game.session;

import kz.zsanzharrko.model.GameCard;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.utils.DatabaseConnector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import kz.zsanzharrko.utils.DataSourceConfiguration;
import kz.zsanzharrko.utils.FileUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameArenaTest {

  private static GameServiceImpl gameService;
  private static Properties properties;

  @BeforeAll
  public static void init() throws IOException, SQLException, ClassNotFoundException {
    DatabaseConnector.getInstance(new DataSourceConfiguration().getDatasourceConfig());
    properties = FileUtils.getPropertiesFromFile("game-rule.properties");
    gameService = GameServiceImpl.getInstance(properties);
  }

  @Test
  void initArena() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    int cardSize = Integer.parseInt(properties.getProperty("game.cards.init.size"));
    int playerSize = Integer.parseInt(properties.getProperty("session.players"));
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
    int cardSize = Integer.parseInt(properties.getProperty("game.cards.init.size"));
    int playerSize = Integer.parseInt(properties.getProperty("session.players"));
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
    gameSession.getGameArena().addCard(player.get(),0, card.get());
    gameSession.getGameArena().addCard(player.get(),1, card.get());
    // assert place cards
    assertEquals(1, gameSession.getGameArena().getArena(player.get(), 0).size());
    assertEquals(1, gameSession.getGameArena().getArena(player.get(), 1).size());

    assertNull(gameSession.getGameArena().addCard(player.get(),2, card.get()));

  }

  @Test
  void removeCard() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    int cardSize = Integer.parseInt(properties.getProperty("game.cards.init.size"));
    int playerSize = Integer.parseInt(properties.getProperty("session.players"));
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
    gameSession.getGameArena().addCard(player.get(),0, card.get());

    assertTrue(gameSession.getGameArena().removeCard(player.get(), 0, card.get()));

    GameCard otherCard = new GameCard("Other Card", "Desc", 1000);
    assertFalse(gameSession.getGameArena().removeCard(player.get(), 0, otherCard));
    assertFalse(gameSession.getGameArena().removeCard(player.get(), 1, otherCard));
  }

  @Test
  void getStatistics() {
  }
}