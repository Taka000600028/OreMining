package plugin.gameStart.mapper.data;

import lombok.Getter;
import lombok.Setter;

/**
 *プレイヤーのスコア情報を扱うオブジェクト。
 *DBに存在するテーブルと連動する。
 */
@Getter
@Setter
public class PlayerScore {

  private int id;
  private String playerName;
  private int score;
  private String registeredAt;


}
