package kz.zsanzharrko.config;

import java.util.Properties;

public class GamePropConfigResolver {

  public static GameConfig getGameConfig(Properties gameProperties){
    Integer sessionGames = Integer.parseInt(gameProperties.getProperty("session.arena.rows"));
    Integer sessionArenaRows = Integer.parseInt(gameProperties.getProperty("session.players"));
    Integer gameCardsInitSize = Integer.parseInt(gameProperties.getProperty("game.cards.init.size"));
    return new GameConfig(sessionGames, sessionArenaRows, gameCardsInitSize);
  }

  public static GameConfig getGameConfig(){
    return new GameConfig();
  }
}
