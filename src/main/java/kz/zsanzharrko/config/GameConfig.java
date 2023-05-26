package kz.zsanzharrko.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class GameConfig {
  private Integer sessionPlayers = 2;
  private Integer sessionArenaRows = 2;
  private Integer gameCardsInitSize = 5;
}
