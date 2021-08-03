package ethos.model.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethos.Server;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.model.items.GameItem;
import ethos.util.Misc;

public class BrimstoneChest {

	private static final int KEY = 23083;
	private static final int ANIMATION = 881;

	private static final Map<Rarity, List<GameItem>> items = new HashMap<>();

	static {
		items.put(Rarity.COMMON, Arrays.asList(
				new GameItem(1618, 25 + Misc.random(10)), 
				new GameItem(1620, 25 + Misc.random(10)), 
				new GameItem(454, 300 + Misc.random(200)), 
				new GameItem(995, 50000 + Misc.random(100000)), 
				new GameItem(445, 100 + Misc.random(100)),
				new GameItem(11237, 50 + Misc.random(150)),
				new GameItem(441, 350 + Misc.random(150)), 
				new GameItem(1164, 1 + Misc.random(3)), 
				new GameItem(1128, 0 + Misc.random(2)), 
				new GameItem(1080, 0 + Misc.random(6)),
				new GameItem(360, 100 + Misc.random(250)), 
				new GameItem(378, 100 + Misc.random(250)), 
				new GameItem(372, 100 + Misc.random(200)), 
				new GameItem(7945, 100 + Misc.random(200)), 
				new GameItem(384, 100 + Misc.random(150)), 
				new GameItem(396, 80 + Misc.random(80)), 
				new GameItem(390, 80 + Misc.random(80))));
		
		items.put(Rarity.UNCOMMON, Arrays.asList(
				new GameItem(452, 9 + Misc.random(6)), 
				new GameItem(2354, 300 + Misc.random(200)), 
				new GameItem(1514, 120 + Misc.random(40)), 
				new GameItem(11232, 40 + Misc.random(120)), 
				new GameItem(5289, 1 + Misc.random(3)),
				new GameItem(5316, 1 + Misc.random(2)), 
				new GameItem(22869, 1 + Misc.random(3)), 
				new GameItem(22877, 1 + Misc.random(3)), 
				new GameItem(22871, 1), 
				new GameItem(5304, 2 + Misc.random(3)), 
				new GameItem(5300, 2 + Misc.random(3)), 
				new GameItem(5295, 2 + Misc.random(3)),
				new GameItem(7937, 3000 + Misc.random(3000)), 
				new GameItem(11840, 1)));
		
		items.put(Rarity.RARE, Arrays.asList( 
				new GameItem(22963, 1)));
		
		items.put(Rarity.VERY_RARE, Arrays.asList(
				new GameItem(23047, 1), 
				new GameItem(23050, 1), 
				new GameItem(23053, 1), 
				new GameItem(23056, 1),
				new GameItem(23059, 1)));
	}

	private static GameItem randomChestRewards(int chance) {
		int random = Misc.random(chance);
		List<GameItem> itemList = random < chance ? items.get(Rarity.COMMON) : items.get(Rarity.UNCOMMON);
		return Misc.getRandomItem(itemList);
	}

	public static void searchChest(Player c) {
		if (c.getItems().playerHasItem(KEY)) {
			c.getItems().deleteItem(KEY, 1);
			c.startAnimation(ANIMATION);
			GameItem reward = randomChestRewards(1);
			if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
				Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
			}
			c.sendMessage("@blu@You open the chest and receive an item");
		} else {
			c.sendMessage("@blu@The chest is locked, it won't budge!");
		}
	}

	enum Rarity {
		UNCOMMON, COMMON, RARE, VERY_RARE
	}

}