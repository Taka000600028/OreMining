package plugin.OreMining;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.OreMining.command.OreMining;

public final class Main extends JavaPlugin{

  @Override
  public void onEnable() {
    OreMining oreMining = new OreMining(this);
    Bukkit.getPluginManager().registerEvents(oreMining, this);
    Objects.requireNonNull(getCommand("oremining")).setExecutor(oreMining);
  }
}