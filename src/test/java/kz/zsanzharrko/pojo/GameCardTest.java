package kz.zsanzharrko.pojo;

import kz.zsanzharrko.gamecard.GameCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameCardTest {

  @Test
  void createNewObject() {
    new GameCard("Title", "", 1);
  }

  @Test
  void createObjectAndUpdateTitle() {
    GameCard card = new GameCard("Title", "", 1);
    card.setTitle("New_Title");
    Assertions.assertEquals("New_Title", card.getTitle());
  }

  @Test
  void createObjectAndUpdateDescription() {
    GameCard card = new GameCard("Title", "", 1);
    card.setDescription("New_Desc");
    Assertions.assertEquals("New_Desc", card.getDescription());
  }

  @Test
  void createObjectAndUpdatePower() {
    GameCard card = new GameCard("Title", "", 1);
    card.setPower(2);
    Assertions.assertEquals(2, card.getPower());
  }
}