package kz.zsanzharrko.service.game.session;


import kz.zsanzharrko.model.GameCard;
import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.service.game.statistic.GameStatisticsState;

import java.util.List;
import java.util.Map;

public abstract class Arena {

  abstract protected GameCard addCard(Player player, Integer row, GameCard card);
  abstract protected boolean removeCard(Player player, Integer row, GameCard card);

  abstract protected Map<Player, Map<GameStatisticsState, String>> getStatistics();

  abstract Map<Integer, List<GameCard>> getArena(Player player);

  abstract List<GameCard> getArena(Player player, int row);
}
