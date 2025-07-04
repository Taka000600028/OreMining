package plugin.gameStart;

import org.bukkit.Location;
import org.bukkit.Material;

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