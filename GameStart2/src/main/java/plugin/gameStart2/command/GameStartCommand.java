package plugin.gameStart2.command;

import java.util.List;
import java.util.SplittableRandom;
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
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import plugin.gameStart2.Main;

public class GameStartCommand implements CommandExecutor, Listener {

  World world;
  int count;
  private Player player;
  private int score;
  private Main main;
  private int gameTime = 20;

  public GameStartCommand(Main main) {
    this.main = main;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s,
      String[] strings) {
    if (commandSender instanceof Player player) {
      world = player.getWorld();
      this.player = player;
      gameTime = 20;
      score = 0;

      //　プレイヤーの状態を初期化する。
      player.getHealth();
      player.getFoodLevel();
      PlayerInventory inventory = player.getInventory();
      ItemStack helmet = inventory.getHelmet();
      ItemStack chestPlate = inventory.getChestplate();
      ItemStack leggings = inventory.getLeggings();
      ItemStack boots = inventory.getBoots();
      ItemStack itemInMainHand = inventory.getItemInMainHand();

      //コマンドを実行の間、装備する。
      player.setHealth(20);
      player.setFoodLevel(20);
      inventory.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
      inventory.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
      inventory.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
      inventory.setBoots(new ItemStack(Material.NETHERITE_BOOTS));
      inventory.setItemInMainHand(new ItemStack(Material.NETHERITE_PICKAXE));

      Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
        if (gameTime <= 0) {
          Runnable.cancel();
          player.sendTitle("ゲームが終了しました！",player.getName()+"のスコアは" + score + "点",30,30,30);
          return;
        }

        if (count % 20 == 0) {
          for (int i = 0; i < 21; i++) {

            List<Material> BlockList = List.of(Material.DIAMOND_ORE, Material.LAPIS_ORE,
                Material.EMERALD_ORE, Material.GOLD_ORE);
            int random = new SplittableRandom().nextInt(4);
            world.spawnFallingBlock(getBlockSpawnLocation1(player, world),
                BlockList.get(random).createBlockData());
          }
        }

        if (count % 20 == 0) {
          for (int i = 0; i < 21; i++) {

            List<Material> BlockList = List.of(Material.DIAMOND_ORE, Material.LAPIS_ORE,
                Material.EMERALD_ORE, Material.GOLD_ORE);
            int random = new SplittableRandom().nextInt(4);
            world.spawnFallingBlock(getBlockSpawnLocation2(player, world),
                BlockList.get(random).createBlockData());
          }
        }

//      ゲーム終了　(ゲーム時間を設定後、実装する。)
//      player.getHealth();
//      inventory.setHelmet(helmet);
//      inventory.setChestPlate(chestPlate);
//      inventory.setLeggings(leggings);
//      inventory.setBoots(boots);
//      inventory.setItemInMainHand(itemInMainHand);

        gameTime -= 20;
      }, 0, 20 * 20);
    }
    return false;
  }

  @EventHandler
  public void onGetCursor(BlockDropItemEvent e) {
    List<Material> getItems;
    score += 10;
    e.getPlayer().sendMessage("鉱石を見つけた！" + score + "点！");
  }

  /**
   * Blockの出現エリア1を取得します。 出現エリアはX、Z軸は指定した場所-1から4の値が設定されます。 Y軸は指定した場所の1から4の値が設定されます。
   *
   * @param player 　プレイヤー
   * @param world  　プレイヤーが所属するワールド
   * @return ブロックの出現場所
   */
  private Location getBlockSpawnLocation1(Player player, World world) {
    Location playerLocation = player.getLocation();
    //random１はX,Z軸に使用する。random2はY軸に使用する。
    int randomX = new SplittableRandom().nextInt(6) - 1;
    int randomY = new SplittableRandom().nextInt(3) + 1;
    int randomZ = new SplittableRandom().nextInt(6) - 1;
    return new Location
        (world, 168 + randomX, 63 + randomY, -75 + randomZ);
  }

  /**
   * Blockの出現エリア2を取得します。 出現エリアはX、Z軸は指定した場所-1から4の値が設定されます。 Y軸は指定した場所の1から4の値が設定されます。
   *
   * @param player 　プレイヤー
   * @param world  　プレイヤーが所属するワールド
   * @return ブロックの出現場所
   */
  private Location getBlockSpawnLocation2(Player player, World world) {
    Location playerLocation = player.getLocation();
    //random１はX,Z軸に使用する。random2はY軸に使用する。
    int randomX = new SplittableRandom().nextInt(6) - 1;
    int randomY = new SplittableRandom().nextInt(3) + 1;
    int randomZ = new SplittableRandom().nextInt(6) - 1;
    return new Location
        (world, 140 + randomX, 58 + randomY, -49 + randomZ);
  }

}