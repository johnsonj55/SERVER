package ethos.model.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethos.Server;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.items.GameItem;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.util.Misc;

public class SlayerChest {

	private static final int KEY = 3465;
	private static final int KEY1 = 3466;
	private static final int KEY2 = 3467;
	private static final int KEY3 = 3468;
	private static final int DRAGONSTONE = 1631; 
	private static final int ANIMATION = 881;

	private static final Map<Rarity, List<GameItem>> items = new HashMap<>();

	static {
		items.put(Rarity.COMMON, Arrays.asList(
				new GameItem(140, 10), 
				new GameItem(374, 50), 
				new GameItem(380, 100), 
				new GameItem(995, 100000), 
				new GameItem(1127, 1),
				new GameItem(2435, 2),
				new GameItem(1163, 1), 
				new GameItem(1201, 1), 
				new GameItem(1303, 1), 
				new GameItem(1712, 1),
				new GameItem(2677, 1), 
				new GameItem(441, 25), 
				new GameItem(454, 25), 
				new GameItem(1516, 20), 
				new GameItem(1512, 35), 
				new GameItem(208, 15), 
				new GameItem(565, 250), 
				new GameItem(560, 250), 
				new GameItem(71, 25), 
				new GameItem(1632, 5), 
				new GameItem(537, 10), 
				new GameItem(384, 15), 
				new GameItem(4131, 1)));
		
		items.put(Rarity.UNCOMMON, Arrays.asList(
				new GameItem(386, 20), 
				new GameItem(990, 3), 
				new GameItem(995, 500000), 
				new GameItem(1305, 1), 
				new GameItem(1377, 1),
				new GameItem(2368, 1), 
				new GameItem(2801, 1), 
				new GameItem(3027, 10), 
				new GameItem(3145, 15), 
				new GameItem(4587, 1), 
				new GameItem(6688, 10), 
				new GameItem(11840, 1)));
		
		items.put(Rarity.RARE, Arrays.asList(
				new GameItem(11286, 1), 
				new GameItem(4151, 1), 
				new GameItem(11235, 1), 
				new GameItem(4716, 1), 
				new GameItem(4718, 1), 
				new GameItem(4720, 1), 
				new GameItem(4722, 1),
				new GameItem(13227, 1), 
				new GameItem(4724, 1), 
				new GameItem(4726, 1), 
				new GameItem(4728, 1), 
				new GameItem(4730, 1),
				new GameItem(4753, 1), 
				new GameItem(4755, 1), 
				new GameItem(4757, 1), 
				new GameItem(4759, 1), 
				new GameItem(4708, 1), 
				new GameItem(4710, 1), 
				new GameItem(4712, 1), 
				new GameItem(4714, 1), 
				new GameItem(4732, 1), 
				new GameItem(4734, 1), 
				new GameItem(4736, 1), 
				new GameItem(4738, 1)
				));
	}

	private static GameItem randomChestRewards(int chance) {
		int random = Misc.random(chance);
		List<GameItem> itemList = (random > 8 ? items.get(Rarity.COMMON) : random >= 2 && random < 8  ?  items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE) );
		return Misc.getRandomItem(itemList);
	} 

	public int keyId;
	public static void searchChest(Player c) {
		
		if (c.getItems().playerHasItem(KEY)) {
			c.getItems().deleteItem(KEY, 1);
			c.startAnimation(ANIMATION);
			c.getItems().addItem(DRAGONSTONE, 1);
			GameItem reward = (c.getSlayerChest().keyId == c.getSlayerChest().KEY ? randomChestRewards(20) : c.getSlayerChest().keyId == c.getSlayerChest().KEY1 ? randomChestRewards(18) : c.getSlayerChest().keyId == c.getSlayerChest().KEY2 ? randomChestRewards(15) : randomChestRewards(13));
			if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
				Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
			}
			Achievements.increase(c, AchievementType.OPEN_SLAYER_CHEST, 1);
			c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
		} else if (c.getItems().playerHasItem(KEY1)) {
			c.getItems().deleteItem(KEY1, 1);
			c.startAnimation(ANIMATION);
			c.getItems().addItem(DRAGONSTONE, 1);
			GameItem reward = (c.getSlayerChest().keyId == c.getSlayerChest().KEY ? randomChestRewards(20) : c.getSlayerChest().keyId == c.getSlayerChest().KEY1 ? randomChestRewards(18) : c.getSlayerChest().keyId == c.getSlayerChest().KEY2 ? randomChestRewards(15) : randomChestRewards(13));
			if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
				Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
			}
			Achievements.increase(c, AchievementType.OPEN_SLAYER_CHEST, 1);
			c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
		} else if (c.getItems().playerHasItem(KEY2)) {
			c.getItems().deleteItem(KEY2, 1);
			c.startAnimation(ANIMATION);
			c.getItems().addItem(DRAGONSTONE, 1);
			GameItem reward = (c.getSlayerChest().keyId == c.getSlayerChest().KEY ? randomChestRewards(20) : c.getSlayerChest().keyId == c.getSlayerChest().KEY1 ? randomChestRewards(18) : c.getSlayerChest().keyId == c.getSlayerChest().KEY2 ? randomChestRewards(15) : randomChestRewards(13));
			if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
				Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
			}
			Achievements.increase(c, AchievementType.OPEN_SLAYER_CHEST, 1);
			c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
		} else if (c.getItems().playerHasItem(KEY3)) {
			c.getItems().deleteItem(KEY3, 1);
			c.startAnimation(ANIMATION);
			c.getItems().addItem(DRAGONSTONE, 1);
			GameItem reward = (c.getSlayerChest().keyId == c.getSlayerChest().KEY ? randomChestRewards(20) : c.getSlayerChest().keyId == c.getSlayerChest().KEY1 ? randomChestRewards(18) : c.getSlayerChest().keyId == c.getSlayerChest().KEY2 ? randomChestRewards(15) : randomChestRewards(13));
			if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
				Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
			}
			Achievements.increase(c, AchievementType.OPEN_SLAYER_CHEST, 1);
			c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
			
		} else {
			c.sendMessage("@blu@The chest is locked, it won't budge!");
		}
		c.getSlayerChest().keyId = -1;
	}

	enum Rarity {
		UNCOMMON, COMMON, RARE
	}

}