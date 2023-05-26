package kz.zsanzharrko.gamecard;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import kz.zsanzharrko.gamecard.service.CardService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameCardService {
  private static GameCardService service;

  private final CardService cardService;
  private List<GameCard> cards;

  private GameCardService(@NonNull CardService cardService) {
    this.cardService = cardService;
    initCards();
  }

  private void initCards() {
    cards = cardService.getAllCards();
  }

  public List<GameCard> getCards() {
    return new LinkedList<>(cards);
  }

  public Optional<GameCard> getCardByTitle(String title) {
    return cards.stream()
        .filter(gameCard -> gameCard.getTitle().equals(title))
        .findFirst();
  }

  public static GameCardService getInstance(@NonNull CardService cardService) {
    if (service == null) {
      service = new GameCardService(cardService);
      log.info("Created Game Card Service");
    }
    return service;
  }
}
