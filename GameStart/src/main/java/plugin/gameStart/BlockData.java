package plugin.gameStart;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * ゲームステージのブロック情報を扱うオブジェクト
 * ブロックの種類、ブロックの場所などの情報を持つ
 */
public class BlockData {
  private final Material type;
  private final Location location;

  public BlockData(Material type, Location location) {
    this.type = type;
    this.location = location.clone();
  }

  public void restore() {
    location.getBlock().setType(type);
  }
}