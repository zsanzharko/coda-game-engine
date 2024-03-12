package kz.zsanzharrko.model;

import kz.zsanzharrko.gamecard.GameCard;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Player {
  private String id;
  private String name;
  private Number gameRating;
  private List<GameCard> cardDeck;
  private PlayerState state;

  public Player(String name, List<GameCard> cardDeck) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.gameRating = 0;
    this.cardDeck = cardDeck;
    this.state = PlayerState.NONE;
  }

  public List<GameCard> getCardDeck() {
    return new ArrayList<>(cardDeck);
  }
}
