package plugin.gameStart;

import static org.bukkit.Bukkit.getLogger;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.gameStart.command.BaseCommand;

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