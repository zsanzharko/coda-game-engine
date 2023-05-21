package kz.zsanzharrko.service.game.gamecard;

import kz.zsanzharrko.model.GameCard;
import kz.zsanzharrko.utils.DatabaseConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class GameCardService {
  private static GameCardService service;
  private List<GameCard> cards;

  private GameCardService() throws SQLException {
    initCards();
  }

  public GameCardService(List<GameCard> cards) {
    this.cards = cards;
  }

  private void initCards() throws SQLException {
    DatabaseConnector connector = DatabaseConnector.getService();
    cards = new LinkedList<>();
    Statement statement = connector.getConnection().createStatement();
    ResultSet sqlResultCards = statement.executeQuery("select * from game.cards");
    while (sqlResultCards.next()) {
      cards.add(new GameCard(
              sqlResultCards.getString("title"),
              sqlResultCards.getString("description"),
              sqlResultCards.getInt("power")
      ));
    }
    statement.close();
  }

  public List<GameCard> getCards() {
    return new LinkedList<>(cards);
  }

  public Optional<GameCard> getCardByTitle(String title) {
    return cards.stream()
            .filter(gameCard -> gameCard.getTitle().equals(title))
            .findFirst();
  }

  public static GameCardService getInstance() throws SQLException {
    if (service == null) {
      service = new GameCardService();
      log.info("Created Game Card Service.");
    }
    return service;
  }

  public static GameCardService getInstance(List<GameCard> cards) {
    if (service == null) {
      service = new GameCardService(cards);
      log.debug("Created Game Card Service.");
    }
    return service;
  }
}
