package plugin.gameStart2.command;

import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GameStartCommand implements CommandExecutor {

  World world;

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s,
      String[] strings) {
    if (commandSender instanceof Player player) {
      world = player.getWorld();
      player.getWorld();

      //　プレイヤーの状態を初期化する。
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

      List<Material> BlockList = List.of(Material.DIAMOND_ORE, Material.LAPIS_ORE,
          Material.EMERALD_ORE, Material.GOLD_ORE);
      int random = new SplittableRandom().nextInt(4);
      world.spawnFallingBlock(getBlockSpawnLocation1(player, world),
          BlockList.get(random).createBlockData());

//      ゲーム終了　(ゲーム時間を設定後、実装する。)
//      player.getHealth();
//      inventory.setHelmet(helmet);
//      inventory.setChestPlate(chestPlate);
//      inventory.setLeggings(leggings);
//      inventory.setBoots(boots);
//      inventory.setItemInMainHand(itemInMainHand);

    }
    return false;
  }

  /**
   * Blockの出現エリアを取得します。 出現エリアはX、Z軸は指定した場所-1から4の値が設定されます。 Y軸は指定した場所の1から4の値が設定されます。
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

}