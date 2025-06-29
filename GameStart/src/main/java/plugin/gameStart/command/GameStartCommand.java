package plugin.gameStart.command;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.gameStart.PlayerScoreData;
import plugin.gameStart.Main;
import plugin.gameStart.mapper.data.PlayerScore;

/**
 * 制限時間内に出現するブロックを壊して、スコアを獲得するゲームを起動するコマンドです。 スコアはブロックの種類によって変わり、合計点数が変動します。
 * 結果はプレイヤー名、点数、日時などで保存されます。
 **/
public class GameStartCommand extends BaseCommand implements Listener {

  public static final int GAME_TIME = 40;
  private final Main main;
  private int gameTime = 40;
  private static final String LIST = "list";
  private int currentScore = 0;
  private final PlayerScoreData playerScoreData = new PlayerScoreData();

  public GameStartCommand(Main main) {
    this.main = main;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player) {
    return false;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player, CommandSender commandSender, String s,
      String[] strings) {

    gameTime = GAME_TIME;
    Main main = this.main;
    currentScore = 0;

    // 最初の引数が「list」だったら、スコアを一覧で表示して処理を終了する。
    if (strings.length == 1 && LIST.equals(strings[0])) {
      sendPlayerScoreList(player);
      return true;
    }

    //　ゲーム開始前の装備を記録しておく。
    PlayerInventory inventory = player.getInventory();
    ItemStack helmet = inventory.getHelmet();
    ItemStack chestPlate = inventory.getChestplate();
    ItemStack leggings = inventory.getLeggings();
    ItemStack boots = inventory.getBoots();
    ItemStack itemInMainHand = inventory.getItemInMainHand();

    //ゲーム開始後、指定された場所に移動する。
    Location fromLocation = player.getLocation().clone();

    Location teleportLocation = new Location(player.getWorld(), 155, 64, -39);
    teleportLocation.setYaw(180);
    player.teleport(teleportLocation);
    player.sendTitle("ゲームを開始します！", "洞窟内の鉱石を採掘して下さい！", 20, 20, 20);

    //コマンドを実行の間、装備する。
    inventory.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
    inventory.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
    inventory.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
    inventory.setBoots(new ItemStack(Material.NETHERITE_BOOTS));
    inventory.setItemInMainHand(new ItemStack(Material.NETHERITE_PICKAXE));

    gamePlay(player, main, fromLocation, inventory, helmet, chestPlate, leggings, boots,
        itemInMainHand);
    return true;
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender commandSender) {
    return false;
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Material blockType = e.getBlock().getType();
    int score = getOreScore(blockType);

    if (score > 0) {
      currentScore += score;
      e.getPlayer().sendMessage("§a+" + score + "点！　現在のスコア：" + currentScore);
    }
  }

  /**
   * 現在登録されているスコアの一覧をメッセージに送る。
   *
   * @param player 　プレイヤー
   */
  private void sendPlayerScoreList(Player player) {
    List<PlayerScore> playerScoreList = playerScoreData.selectList();
    for (PlayerScore playerScore : playerScoreList) {
      player.sendMessage(playerScore.getId() + "　|　"
          + playerScore.getPlayerName() + "　|　"
          + playerScore.getScore() + "点　|　"
          + playerScore.getRegisteredAt()
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
      case NETHER_GOLD_ORE -> 100;
      case GOLD_ORE -> 30;
      case DIAMOND_ORE -> 10;
      case EMERALD_ORE -> 20;
      case LAPIS_ORE -> 5;
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
    if (chance < 1) {
      return Material.NETHER_GOLD_ORE;
    } else if (chance < 20) {
      return Material.GOLD_ORE;
    } else if (chance < 30) {
      return Material.DIAMOND_ORE;
    } else if (chance < 40) {
      return Material.EMERALD_ORE;
    } else if (chance < 80) {
      return Material.LAPIS_ORE;
    } else {
      return Material.STONE;
    }
  }

  //1のエリアに4*4*4のブロックを出現させる。
  private static void spawnOre1(World world, Player player, SplittableRandom splittableRandom) {
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
  private static void spawnOre2(World world, Player player, SplittableRandom splittableRandom) {
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

  /**
   * ゲームを実行します。規定時間内にブロックを壊すとスコアが加算されます。合計スコアを時間経過後に表示します。 持ち物はゲームを実行すると設定された持ち物に変更されます。
   *
   * @param player         　コマンドを実行したプレイヤー
   * @param main           　クラス名
   * @param fromLocation   　コマンドを実行する時にいる場所の情報
   * @param inventory      　プレイヤーの持ち物
   * @param helmet         　コマンドを実行する時に装着しているヘルメット
   * @param chestPlate     　コマンドを実行する時に装着しているチェストプレート
   * @param leggings       　コマンドを実行する時に装着しているレギンス
   * @param boots          　コマンドを実行する時に装着しているブーツ
   * @param itemInMainHand 　コマンドを実行する時に装着している武器
   */

  private void gamePlay(Player player, Main main, Location fromLocation, PlayerInventory inventory,
      ItemStack helmet, ItemStack chestPlate, ItemStack leggings, ItemStack boots,
      ItemStack itemInMainHand) {
    new BukkitRunnable() {
      int timeLeft = gameTime;

      @Override
      public void run() {
        if (timeLeft <= 20) {
          cancel();

          clearOreArea1(player.getWorld());
          clearOreArea2(player.getWorld());

          int finalScore = currentScore;
          player.sendTitle("ゲームが終了しました！",
              player.getName() + "のスコアは" + finalScore + "点！",
              30, 30, 30);

          Bukkit.getScheduler().runTaskLater(main, () -> {
            player.teleport(fromLocation);

            //ゲーム開始前の装備に戻す。
            inventory.setHelmet(helmet);
            inventory.setChestplate(chestPlate);
            inventory.setLeggings(leggings);
            inventory.setBoots(boots);
            inventory.setItemInMainHand(itemInMainHand);

            player.sendMessage("元の位置に戻りました。");
          }, 20 * 10);

          playerScoreData.insert(
              new PlayerScore(player.getName(), finalScore));

          return;
        }

        spawnOre1(player.getWorld(), player, new SplittableRandom());
        spawnOre2(player.getWorld(), player, new SplittableRandom());

        timeLeft -= 50;
      }
    }.runTaskTimer(main, 0, 20 * 50);
  }
}

