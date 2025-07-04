package plugin.gameStart;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.gameStart.command.GameStartCommand;

public final class Main extends JavaPlugin{

  @Override
  public void onEnable() {
    GameStartCommand gameStartCommand = new GameStartCommand(this);
    Bukkit.getPluginManager().registerEvents(gameStartCommand, this);
    Objects.requireNonNull(getCommand("gamestart")).setExecutor(gameStartCommand);
  }
}