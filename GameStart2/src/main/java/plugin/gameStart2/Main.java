package plugin.gameStart2;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.gameStart2.command.GameStartCommand;

public final class Main extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
    getCommand("gameStart").setExecutor(new GameStartCommand());
  }
}
