package kz.zsanzharrko.service.gamecard;

import java.util.List;
import java.util.Optional;
import kz.zsanzharrko.gamecard.GameCard;
import kz.zsanzharrko.gamecard.GameCardService;
import kz.zsanzharrko.gamecard.service.SimpleCardServiceImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameCardServiceTest {

  @Test
  void createInstance() {
    GameCardService service = GameCardService.getInstance(new SimpleCardServiceImpl());

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


    GameCardService service = GameCardService.getInstance(new SimpleCardServiceImpl(testCards));

    assertNotNull(service);
    assertEquals(testCards.size(), service.getCards().size());
  }

  @Test
  void assertionInitCache() {
    GameCardService service = GameCardService.getInstance(new SimpleCardServiceImpl());

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
    GameCardService service = GameCardService.getInstance(new SimpleCardServiceImpl(testCards));

    Optional<GameCard> card = service.getCardByTitle("test-3");

    assertTrue(card.isPresent());
    assertEquals("test-3", card.get().getTitle());

    card = service.getCardByTitle("test-5");

    assertFalse(card.isPresent());
  }

  @Test
  void getCards() {
    GameCardService service = GameCardService.getInstance(new SimpleCardServiceImpl());
    assertNotNull(service);
    assertNotNull(service.getCards());
    assertNotEquals(0, service.getCards().size());
  }
}