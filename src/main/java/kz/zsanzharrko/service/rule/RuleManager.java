package kz.zsanzharrko.service.rule;

import kz.zsanzharrko.model.Player;

public class RuleManager {

  public static boolean prepareToGameSession(Player player) {
    return new PreparationPlayerGameRule(player).rule();
  }
}
