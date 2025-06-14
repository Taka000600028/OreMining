package plugin.gameStart.command;

import java.util.HashMap;
import java.util.SplittableRandom;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import plugin.gameStart.Main;

public class GameStartCommand implements CommandExecutor, Listener {

  World world;
  private Player player;
  private Main main;
  private int gameTime = 60;
  private final HashMap<UUID, Integer> playerScores = new HashMap<>();

  public GameStartCommand(Main main) {
    this.main = main;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s,
      String[] strings) {
    if (commandSender instanceof Player player) {
      world = player.getWorld();
      this.player = player;
      gameTime = 60;
      //スコアをリセットする。
      playerScores.put(player.getUniqueId(), 0);

      //　ゲーム開始前の装備を記録しておく。
      player.getHealth();
      PlayerInventory inventory = player.getInventory();
      ItemStack helmet = inventory.getHelmet();
      ItemStack chestPlate = inventory.getChestplate();
      ItemStack leggings = inventory.getLeggings();
      ItemStack boots = inventory.getBoots();
      ItemStack itemInMainHand = inventory.getItemInMainHand();

      //コマンドを実行の間、装備する。
      player.setHealth(20);
      inventory.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
      inventory.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
      inventory.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
      inventory.setBoots(new ItemStack(Material.NETHERITE_BOOTS));
      inventory.setItemInMainHand(new ItemStack(Material.NETHERITE_PICKAXE));

      Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
        if (gameTime <= 0) {
          Runnable.cancel();

          clearOreArea1(player.getWorld());
          clearOreArea2(player.getWorld());

          int finalScore = playerScores.getOrDefault(player.getUniqueId(), 0);
          player.sendTitle("ゲームが終了しました！",
              player.getName() + "のスコアは" + finalScore + "点",
              30, 30, 30);

          //ゲーム開始前の装備に戻す。
          player.getHealth();
          inventory.setHelmet(helmet);
          inventory.setChestplate(chestPlate);
          inventory.setLeggings(leggings);
          inventory.setBoots(boots);
          inventory.setItemInMainHand(itemInMainHand);

          return;
        }

        spawnOre1(player.getWorld(), player, new SplittableRandom(), 140, 58, -49);
        spawnOre2(player.getWorld(), player, new SplittableRandom(), 168, 63, -75);

        gameTime -= 60;
      }, 0, 20 * 60);
    }
    return false;
  }


  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Material blockType = e.getBlock().getType();
    int score = getOreScore(blockType);
    if (score > 0) {
      UUID playerId = e.getPlayer().getUniqueId();
      playerScores.put(playerId, playerScores.getOrDefault(playerId, 0) + score);
      e.getPlayer().sendMessage("§a+" + score + "点！　現在のスコア：" + playerScores.get(playerId));
    }
  }

  /**
   * 鉱石の種類に応じてスコアを設定する。
   *
   * @param oreType 　ブロックの種類
   * @return ブロックのスコア情報
   */
  private int getOreScore(Material oreType) {
    return switch (oreType) {
      case DIAMOND_ORE -> 10;
      case LAPIS_ORE -> 5;
      case EMERALD_ORE -> 20;
      case GOLD_ORE -> 30;
      default -> 0;
    };
  }

  /**
   * 鉱石の出現率を設定します。
   *
   * @param chance 　出現率
   * @return 鉱石の種類
   */
  private static Material getMaterial(int chance) {
    Material oreType = null;
    if (chance < 10) {
      return Material.GOLD_ORE;
    } else if (chance < 20) {
      return Material.DIAMOND_ORE;
    } else if (chance < 30) {
      return Material.EMERALD_ORE;
    } else if (chance < 40) {
      return Material.LAPIS_ORE;
    } else {
      return Material.STONE;
    }
  }

  //1のエリアに4*4*4のブロックを出現させる。
  private static void spawnOre1(World world, Player player, SplittableRandom splittableRandom,
      double x, double y, double z) {
    for (int a = 0; a <= 3; a++) {
      for (int b = 0; b <= 3; b++) {
        for (int c = 0; c <= 3; c++) {
          int chance = splittableRandom.nextInt(100);
          Material oreType = getMaterial(chance);
          Location oreLocation = new Location(world, 140 + a, 58 + b, -49 + c);
          world.getBlockAt(oreLocation).setType(oreType);
        }
      }
    }
  }

  //１のエリアの4*4*4のブロックを消去する。
  private void clearOreArea1(World world) {
    for (int a = 0; a <= 3; a++) {
      for (int b = 0; b <= 3; b++) {
        for (int c = 0; c <= 3; c++) {
          Location location = new Location(world, 140 + a, 58 + b, -49 + c);
          world.getBlockAt(location).setType(Material.AIR);
        }
      }
    }
  }

  //２のエリアの4*4*4のブロックを出現させる。
  private static void spawnOre2(World world, Player player, SplittableRandom splittableRandom,
      double x, double y, double z) {
    for (int a = 0; a <= 3; a++) {
      for (int b = 0; b <= 3; b++) {
        for (int c = 0; c <= 3; c++) {
          int chance = splittableRandom.nextInt(100);
          Material oreType = getMaterial(chance);
          Location oreLocation = new Location(world, 168 + a, 63 + b, -75 + c);
          world.getBlockAt(oreLocation).setType(oreType);
        }
      }
    }
  }

  //２のエリアの4*4*4のブロックを消去する。
  private void clearOreArea2(World world) {
    for (int a = 0; a <= 3; a++) {
      for (int b = 0; b <= 3; b++) {
        for (int c = 0; c <= 3; c++) {
          Location location = new Location(world, 168 + a, 63 + b, -75 + c);
          world.getBlockAt(location).setType(Material.AIR);
        }
      }
    }
  }
}

