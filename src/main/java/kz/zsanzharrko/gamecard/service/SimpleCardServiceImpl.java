package kz.zsanzharrko.gamecard.service;

import kz.zsanzharrko.gamecard.GameCard;

import java.util.ArrayList;
import java.util.List;

public class SimpleCardServiceImpl implements CardService {
  private final List<GameCard> gameCardList;

  public SimpleCardServiceImpl() {
    this.gameCardList = new ArrayList<>();
  }

  public SimpleCardServiceImpl(List<GameCard> gameCardList) {
    this.gameCardList = gameCardList;
  }

  @Override
  public List<GameCard> getAllCards() {
    return gameCardList;
  }
}
