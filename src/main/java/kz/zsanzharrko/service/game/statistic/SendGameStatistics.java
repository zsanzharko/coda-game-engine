package kz.zsanzharrko.service.game.statistic;

import kz.zsanzharrko.model.Player;

import java.util.Map;

public interface SendGameStatistics {
  void send(Map<Player, Map<GameStatisticsState, String>> statistic);
}
