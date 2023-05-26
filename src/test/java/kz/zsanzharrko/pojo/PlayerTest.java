package kz.zsanzharrko.pojo;

import kz.zsanzharrko.gamecard.GameCard;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.model.PlayerState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

  @Test
  void createObjectAllArgs() {
    new Player("id", "Name", 123, new ArrayList<>(), PlayerState.NONE);
  }

  @Test
  void createObject() {
    new Player("Name", new ArrayList<>());
  }

  @Test
  void getId() {
    Player player = new Player("id", "Name", 123, new ArrayList<>(), PlayerState.NONE);
    assertEquals("id", player.getId());
  }

  @Test
  void getName() {
    Player player = new Player("id", "Name", 123, new ArrayList<>(), PlayerState.NONE);
    assertEquals("Name", player.getName());
  }

  @Test
  void getGameRating() {
    Player player = new Player("id", "Name", 123, new ArrayList<>(), PlayerState.NONE);
    assertEquals(123, player.getGameRating());
  }

  @Test
  void getCardDeck() {
    Player player = new Player("id", "Name", 123, new ArrayList<>(), PlayerState.NONE);
    assertEquals(0, player.getCardDeck().size());
  }

  @Test
  void setName() {
    Player player = new Player("id", "Name", 123, new ArrayList<>(), PlayerState.NONE);
    assertEquals("Name", player.getName());
    player.setName("AnotherName");
    assertEquals("AnotherName", player.getName());
  }

  @Test
  void setGameRating() {
    Player player = new Player("id", "Name", 123, new ArrayList<>(), PlayerState.NONE);
    assertEquals(123, player.getGameRating());
    player.setGameRating(1234);
    assertEquals(1234, player.getGameRating());
  }

  @Test
  void setCardDeck() {
    Player player = new Player("id", "Name", 123, new ArrayList<>(), PlayerState.NONE);
    assertEquals(0, player.getCardDeck().size());
    player.getCardDeck().add(new GameCard("Title", "desc", 12));
    assertEquals(0, player.getCardDeck().size());
  }
}