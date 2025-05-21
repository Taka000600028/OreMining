package plugin.gameStart2;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.gameStart2.command.GameStartCommand;

public final class Main extends JavaPlugin{

  GameStartCommand gameStartCommand = new GameStartCommand();
  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(gameStartCommand, this);
    getCommand("gameStart").setExecutor(gameStartCommand);
  }

}