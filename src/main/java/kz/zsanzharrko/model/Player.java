package kz.zsanzharrko.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Player {
  private String id;
  @Setter
  private String name;
  @Setter
  private Number gameRating;
  @Setter
  private List<GameCard> cardDeck;
  @Setter
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
