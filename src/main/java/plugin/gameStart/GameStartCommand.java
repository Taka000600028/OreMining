package plugin.gameStart;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class GameStartCommand extends BaseCommand implements Listener {

  public static final String EASY = "easy";
  public static final String NORMAL = "normal";
  public static final String HARD = "hard";
  private Main main;
  public static int NUMBER_OF_PAIRS = 5;
  private int gameTime = 20;

  @Override
  public boolean onExecutePlayerCommand(Player player, CommandSender commandSender, Command command,
      String s, String[] strings) {
    World world = player.getWorld();
    gameTime = 20;

    String difficulty = getDifficulty(player, strings);

    Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
      if (gameTime <= 0) {
        Runnable.cancel();
        player.sendMessage("ゲームが終了しました");
        List<Entity> nearbyEnemies = player.getNearbyEntities(50, 0, 50);
        for (Entity enemy : nearbyEnemies) {
          switch (enemy.getType()) {
            case PIG, ZOMBIE, CHICKEN -> enemy.remove();
          }
        }
        return;
      }
      spawnEntityPairs(player, world, difficulty);
      gameTime -= 20;
    }, 0, 20 * 20);

    return true;
  }

  /**
   * 難易度をコマンド引数から取得します。
   *
   * @param player  　コマンドを実行したプレイヤー
   * @param strings 　コマンド引数
   * @return 難易度
   */
  private String getDifficulty(Player player, String[] strings) {
    if (strings.length == 1 && EASY.equals(strings[0]) || NORMAL.equals(strings[0]) || HARD.equals(
        strings[0])) {
      return strings[0];
    }
    player.sendMessage(ChatColor.RED
        + "実行できません。コマンド引数に難易度を指定してください。[easy,normal,hard]");
    return EASY;
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender commandSender, Command command, String s,
      String[] strings) {
    return false;
  }

  /**
   * コマンドを実行すると豚が10匹出現する。
   *
   * @param player
   * @param world
   * @param difficulty 難易度
   */
  List<EntityType> enemyList = List.of(EntityType.PIG, EntityType.CHICKEN, EntityType.ZOMBIE);

  private void spawnEntityPairs(Player player, World world, String difficulty) {
    List<EntityType> enemyList = new ArrayList<>();
    switch (difficulty) {
      case NORMAL -> enemyList.get(1);
      case HARD -> enemyList.get(2);
      default -> enemyList.get(0);
    }

    for (int i = 0; i < GameStartCommand.NUMBER_OF_PAIRS; i++) {
      Entity spawnFirstEntity = player.getWorld()
          .spawnEntity(FirstEntitySpawnLocation(player, world, difficulty), enemyList.get(0));
      spawnFirstEntity.setInvulnerable(true);
      Entity spawnSecondEntity = player.getWorld()
          .spawnEntity(SecondEntitySpawnLocation(player, world, difficulty), enemyList.get(0));
      spawnSecondEntity.setInvulnerable(true);
    }
  }

  /**
   * ペアとなる片方のEntityの出現場所を取得します。 出現エリアはX軸とZ軸は自分の位置からプラス、-10～9の値が設定される。 Y軸はプレイヤーと同じ位置になります。
   *
   * @param player     　コマンドを実行したプレイヤー
   * @param world      　コマンドを実行したプレイヤーが所属するワールド
   * @param difficulty 難易度
   * @return　Entityの出現場所
   */
  private Location FirstEntitySpawnLocation(Player player, World world, String difficulty) {
    Location playerLocation = player.getLocation();
    int randomX = new SplittableRandom().nextInt(10) - 5;
    int randomZ = new SplittableRandom().nextInt(10) - 5;

    double x = playerLocation.getX() + randomX;
    double y = playerLocation.getY();
    double z = playerLocation.getZ() + randomZ;

    return new Location(world, x, y, z);

  }

  /**
   * もう一方のペアとなるEntityの出現場所を取得します。 出現エリアはX軸とZ軸は自分の位置からプラス、-10～9の値が設定される。 Y軸はプレイヤーと同じ位置になります。
   *
   * @param player 　コマンドを実行したプレイヤー
   * @param world  　コマンドを実行したプレイヤーが所属するワールド
   * @return　Entityの出現場所
   */
  private Location SecondEntitySpawnLocation(Player player, World world, String difficulty) {
    Location playerLocation = player.getLocation();
    int randomX = new SplittableRandom().nextInt(10) - 5;
    int randomZ = new SplittableRandom().nextInt(10) - 5;

    double x = playerLocation.getX() + randomX;
    double y = playerLocation.getY();
    double z = playerLocation.getZ() + randomZ;

    return new Location(world, x, y, z);

  }
}