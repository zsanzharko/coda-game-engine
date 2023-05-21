package kz.zsanzharrko.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Presentation card for get information.
 */
@Getter
@Setter
@AllArgsConstructor
public class GameCard {
  private String title;
  private String description;
  private Integer power;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GameCard card = (GameCard) o;
    return Objects.equals(title, card.title) && Objects.equals(power, card.power);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }
}
