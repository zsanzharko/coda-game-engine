package kz.zsanzharrko.service.game.rule;

import kz.zsanzharrko.model.Player;
import kz.zsanzharrko.model.PlayerState;

public class PreparationPlayerGameRule extends AbstactGameRule implements GameRule {
  private Player player;
  protected PreparationPlayerGameRule(Object o) {
    super(o);
    if (o instanceof Player) {
      this.player = (Player) o;
    }
  }

  @Override
  public boolean rule() {
    return player != null
            && !player.getCardDeck().isEmpty()
            && player.getState() == PlayerState.NONE;
  }
}
