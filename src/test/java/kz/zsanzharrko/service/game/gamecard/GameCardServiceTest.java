package kz.zsanzharrko.service.game.gamecard;

import kz.zsanzharrko.model.GameCard;
import kz.zsanzharrko.utils.DatabaseConnector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import kz.zsanzharrko.utils.DataSourceConfiguration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameCardServiceTest {
  @BeforeAll
  public static void initDatasource() throws IOException, SQLException, ClassNotFoundException {
    DatabaseConnector.getInstance(new DataSourceConfiguration().getDatasourceConfig());
  }

  @Test
  void createInstance() throws SQLException {
    GameCardService service = GameCardService.getInstance();

    assertNotNull(service);
  }

  @Test
  void createInstanceWithTestCards() {
    List<GameCard> testCards = List.of(
            new GameCard("test-1", "", 0),
            new GameCard("test-2", "", 0),
            new GameCard("test-3", "", 0),
            new GameCard("test-4", "", 0)
    );
    GameCardService service = GameCardService.getInstance(testCards);

    assertNotNull(service);
    assertEquals(testCards.size(), service.getCards().size());
  }

  @Test
  void createInstanceWithCustomManager() throws SQLException {
    GameCardService service = GameCardService.getInstance();

    assertNotNull(service);
    assertNotEquals(0, service.getCards().size());
  }

  @Test
  void assertionInitCache() throws SQLException {
    GameCardService service = GameCardService.getInstance();

    assertNotNull(service);
    assertNotEquals(0, service.getCards().size());
  }

  @Test
  void getCardByTitle() {
    List<GameCard> testCards = List.of(
            new GameCard("test-1", "", 0),
            new GameCard("test-2", "", 0),
            new GameCard("test-3", "", 0),
            new GameCard("test-4", "", 0)
    );
    GameCardService service = GameCardService.getInstance(testCards);

    Optional<GameCard> card = service.getCardByTitle("test-3");

    assertTrue(card.isPresent());
    assertEquals("test-3", card.get().getTitle());

    card = service.getCardByTitle("test-5");

    assertFalse(card.isPresent());
  }

  @Test
  void getCards() throws SQLException {
    GameCardService service = GameCardService.getInstance();
    assertNotNull(service);
    assertNotNull(service.getCards());
    assertNotEquals(0, service.getCards().size());
  }
}