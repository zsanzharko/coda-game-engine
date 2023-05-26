package kz.zsanzharrko.service.statistic;

import kz.zsanzharrko.model.Player;

import java.util.Map;

public interface SendGameStatistics {
  void send(Map<Player, Map<GameStatisticsState, String>> statistic);
}
