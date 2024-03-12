package kz.zsanzharrko.service.rule;

import kz.zsanzharrko.exception.GameStartException;
import kz.zsanzharrko.model.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuleManager {

  public static void prepareToGameSession(Player player) {
    boolean ruled = new PreparationPlayerGameRule(player).rule();
    if (!ruled) {
      log.debug(String.format("Can't prepared player %s to this game", player));
      throw new GameStartException("Can't start cause preparation is been successfully.");
    }
  }
}
