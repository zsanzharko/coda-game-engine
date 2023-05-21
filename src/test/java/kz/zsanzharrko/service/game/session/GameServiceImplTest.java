package kz.zsanzharrko.service.game.session;

import kz.zsanzharrko.exception.game.GameStartException;
import kz.zsanzharrko.model.GameCard;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.model.PlayerState;
import kz.zsanzharrko.utils.DatabaseConnector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import kz.zsanzharrko.utils.DataSourceConfiguration;
import kz.zsanzharrko.utils.FileUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceImplTest {
  private static GameServiceImpl gameService;
  private static Properties properties;

  @BeforeAll
  public static void initDatasource() throws IOException, SQLException, ClassNotFoundException {
    DatabaseConnector.getInstance(new DataSourceConfiguration().getDatasourceConfig());
    properties = FileUtils.getPropertiesFromFile("game-rule.properties");
    gameService = GameServiceImpl.getInstance(properties);
  }

  @Test
  void startWithEmptyCards() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    // add two players
    players.add(new Player("Player1", cards));
    players.add(new Player("Player2", cards));
    // get game session if session will start
    assertThrows(GameStartException.class, () -> gameService.startSession(players));
  }

  @Test
  void startWithOnePlayer() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    // add two players
    players.add(new Player("Player1", cards));
    // get game session if session will start
    assertThrows(GameStartException.class, () -> gameService.startSession(players));
  }

  @Test
  void startWithNull() {
    assertThrows(GameStartException.class, () ->
            gameService.startSession(null));
  }

  @Test
  void start() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    int cardSize = Integer.parseInt(properties.getProperty("game.cards.init.size"));
    int playerSize = Integer.parseInt(properties.getProperty("session.players"));
    for (int i = 0; i < cardSize; i++) {
      cards.add(new GameCard("Card", "Desc", 10));
    }
    for (int i = 0; i < playerSize; i++) {
      players.add(new Player(String.format("Player %d", i), cards));
    }
    // get game session if session will start
    UUID session = gameService.startSession(players);
    assertNotNull(session);
    // get GameSession by session
    GameSession gameSession = gameService.getGameBySession(session);
    assertEquals(2, gameSession.getPlayers().size());
    assertEquals(GameRoundState.NONE, gameSession.getRoundState());
  }

  @Test
  void startGameAndGetGameSessionObjectBySession() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    int cardSize = Integer.parseInt(properties.getProperty("game.cards.init.size"));
    int playerSize = Integer.parseInt(properties.getProperty("session.players"));
    for (int i = 0; i < cardSize; i++) {
      cards.add(new GameCard("Card", "Desc", 10));
    }
    for (int i = 0; i < playerSize; i++) {
      players.add(new Player(String.format("Player %d", i), cards));
    }
    // get game session if session will start
    UUID session = gameService.startSession(players);
    assertNotNull(session);
    // get GameSession by session
    assertNotNull(gameService.getGameBySession(session));
  }

  @Test
  void stop() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    int cardSize = Integer.parseInt(properties.getProperty("game.cards.init.size"));
    int playerSize = Integer.parseInt(properties.getProperty("session.players"));
    for (int i = 0; i < cardSize; i++) {
      cards.add(new GameCard(String.format("Card %d", i), "Desc", 11));
    }
    for (int i = 0; i < playerSize; i++) {
      players.add(new Player(String.format("Player %d", i), cards));
    }
    UUID session = gameService.startSession(players);
    assertNotNull(session);
    gameService.stopSession(session);
    assertNull(gameService.getGameBySession(session));
    players.forEach(player -> assertEquals(PlayerState.NONE, player.getState()));
  }
}