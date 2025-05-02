package plugin.gameStart;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    GameStartCommand gameStartCommand = new GameStartCommand() {
      @Override
      public boolean onExecutePlayerCommand(Player player) {
        return false;
      }

      @Override
      public boolean onExecuteNPCCommand(CommandSender commandSender) {
        return false;
      }
    };
    Bukkit.getPluginManager().registerEvents(this, this);
    getCommand("gameStart").setExecutor(gameStartCommand);
  }
}
