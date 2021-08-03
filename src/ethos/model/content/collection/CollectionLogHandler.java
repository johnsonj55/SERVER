package ethos.model.content.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap; 

import ethos.model.content.barrows.RewardItem;
import ethos.model.content.barrows.brothers.Ahrim;
import ethos.model.content.barrows.brothers.Dharok;
import ethos.model.content.barrows.brothers.Guthan;
import ethos.model.content.barrows.brothers.Karil;
import ethos.model.content.barrows.brothers.Torag;
import ethos.model.content.barrows.brothers.Verac;
import ethos.model.items.GameItem; 
import ethos.model.players.Player;
import ethos.util.Misc;
  
public class CollectionLogHandler implements Serializable {
	
	private static final long serialVersionUID = 3800427572469676057L;
	
	private Player player;
	
	public CollectionLogType selectedType = CollectionLogType.KILL;
	public HashMap<String, CollectionLog> kill_logs = new HashMap<String, CollectionLog>();
	public HashMap<String, CollectionLog> minigame_logs = new HashMap<String, CollectionLog>(); 
	public HashMap<String, CollectionLog> clue_logs = new HashMap<String, CollectionLog>(); 
	public HashMap<String, CollectionLog> other_logs = new HashMap<String, CollectionLog>(); 

	public static HashMap<String, ArrayList<Integer>> dropReqs = new HashMap<String, ArrayList<Integer>>();
	public static HashMap<String, ArrayList<Integer>> clueReqs = new HashMap<String, ArrayList<Integer>>();
	public static HashMap<String, ArrayList<Integer>> minigameReqs = new HashMap<String, ArrayList<Integer>>();
	public static HashMap<String, ArrayList<Integer>> otherReqs = new HashMap<String, ArrayList<Integer>>();

	static {
		ArrayList<Integer> pcItems = new ArrayList<Integer>(Arrays.asList(new Integer[] {
				11663, 11664, 11665, 8839, 8840, 8841, 8842, 13072, 13073
		}));
		minigameReqs.put("pest control", pcItems);

		ArrayList<Integer> fc = new ArrayList<Integer>(Arrays.asList(new Integer[] {
				6570, 13225
		}));
		minigameReqs.put("fight caves", fc);

		ArrayList<Integer> inferno = new ArrayList<Integer>(Arrays.asList(new Integer[] {
				21295, 22319
		}));
		minigameReqs.put("inferno", inferno);

		ArrayList<Integer> barrows = new ArrayList<Integer>();
		for (RewardItem piece : new Ahrim(null).getRewards())
			barrows.add(piece.getId());
		for (RewardItem piece : new Dharok(null).getRewards())
			barrows.add(piece.getId());
		for (RewardItem piece : new Guthan(null).getRewards())
			barrows.add(piece.getId());
		for (RewardItem piece : new Verac(null).getRewards())
			barrows.add(piece.getId());
		for (RewardItem piece : new Torag(null).getRewards())
			barrows.add(piece.getId()); 
		for (RewardItem piece : new Karil(null).getRewards())
			barrows.add(piece.getId());
		minigameReqs.put("barrows", barrows);

		ArrayList<Integer> wg = new ArrayList<Integer>(Arrays.asList(new Integer[] {
				8844, 8845, 8846, 8847, 8848, 8849, 8850, 12954
		}));
		minigameReqs.put("warrior's guild", wg);
		
//basically just make a new copy of these for each selection
		ArrayList<Integer> chambersOfXericReqs = new ArrayList<Integer>(Arrays.asList(new Integer[] {
				//list of item ids for required items
		}));
		otherReqs.put("chambers of xeric", chambersOfXericReqs);
		
		//and then you go where you receive items from whatever it is you're adding, and copy 
		/*
		 get it? yea more on thing does the saving works ?
		 oh fuck lol i forgot
		 uhhhhhhhhh... one sec
		CollectionLog log = new CollectionLog(); 
		log.amount = 1;  
		log.name = "chambers of xeric";  
		log.addLoot(new GameItem(itemId, amount));// or log.addLoot(rewardItem);// or log.addLoot(rewardItemList);
		player.collectionLog.addLog(log, CollectionLogType.OTHER);
		
		/*
		ArrayList<Integer> cox = new ArrayList<Integer>();
		for (Entry<Rarity, List<Item>> entry : GreatOlmRewardHandler.items.entrySet())
			if (entry.getKey().ordinal() > 0)
				for (Item item : entry.getValue())
					if (!cox.contains(item.getId()))
						cox.add(item.getId());
		otherReqs.put("chambers of xeric", cox);*/
	}
	
	public static ArrayList<String> npcs = new ArrayList<String>(Arrays.asList(new String[] {
			"king black dragon", 
			"general graardor",//here is where to add more npcs, etc you will need to also have them defined in that file for it to work Aa okay just add the correct name and working?yep!
			"kree arra",
			"corporeal beast",
			"kraken",
			"zulrah",
			"cerberus",
			"abyssal sire",
			"lizardman shaman",
			"vorkath",
			"nightmare",
			"skotizo",
			"kalphite queen",
			"barrelchest",
			"vetion",
			"callisto",
			"scorpia",
			"venenatis ",
			"chaos elemental",
			"chaos fanatic",
			"crazy archaeologist",
			"dagannoth mother",
			"dagannoth supreme",
			"dagannoth prime",
			"dagannoth rex",
	}));

	public static ArrayList<String> clues = new ArrayList<String>(Arrays.asList(new String[] {
			"easy", 
			"medium",
			"hard",
			"master"   
	}));
	
	public static ArrayList<String> minigames = new ArrayList<String>(Arrays.asList(new String[] {
			"pest control",
			"fight caves", 
			"inferno",
			"barrows",
			"warrior's guild"   
	}));

	public static ArrayList<String> other = new ArrayList<String>(Arrays.asList(new String[] {
			"chambers of xeric"
	}));
	
	public CollectionLogHandler(Player player) {
		this.player = player;
	} 
	 
	//didn't add chamber of xeric loading yet cuz it's different on mine,
	//but basically to add anything, the simplest way is
	
	public boolean select(int index) {
		if (index < 0)
			return false;
		switch (selectedType) { 
		case CLUE:
			if (index >= clues.size())
				return false;
			if (index < clues.size()) {
				String name = clues.get(index);
				player.getPA().sendString(54302, Misc.capitalize(name));
				int obtained = clue_logs.containsKey(name) ? clue_logs.get(name).loots.size(): 0;
				int required = clueReqs.get(name).size();
				String color = obtained == required ? "<col=00ff00>" : "<col=ff0000>";
				player.getPA().sendString(54303, "Obtained: " + color + obtained + "/" + required);
				int kills = clue_logs.containsKey(name) ? clue_logs.get(name).amount : 0;
				player.getPA().sendString(54304, "Opened: <col=ffffff>" + kills);
				GameItem[] items = new GameItem[clueReqs.get(name).size()];
				for (int i = 0; i < items.length; i++) {
					items[i] = new GameItem(clueReqs.get(name).get(i), 0);
					int amount = clue_logs.containsKey(name) && clue_logs.get(name).loots.containsKey(items[i].getId()) ? 
							clue_logs.get(name).loots.get(items[i].getId()) : 0;
					items[i].setAmount(amount);
				}
				player.getPA().sendItems(54301, Arrays.asList(items)); 
			}
			return true;
		case KILL:
			if (index >= npcs.size())
				return false;
			if (index < npcs.size()) {
				String name = npcs.get(index);
				player.getPA().sendString(54302, Misc.capitalize(name));
				int obtained = kill_logs.containsKey(name) ? kill_logs.get(name).loots.size(): 0;
				int required = dropReqs.get(name).size();
				String color = obtained == required ? "<col=00ff00>" : "<col=ff0000>";
				player.getPA().sendString(54303, "Obtained: " + color + obtained + "/" + required);
				int kills = kill_logs.containsKey(name) ? kill_logs.get(name).amount : 0;
				player.getPA().sendString(54304, "kills: <col=ffffff>" + kills);
				GameItem[] items = new GameItem[dropReqs.get(name).size()];
				for (int i = 0; i < items.length; i++) {
					items[i] = new GameItem(dropReqs.get(name).get(i), 0);
					int amount = kill_logs.containsKey(name) && kill_logs.get(name).loots.containsKey(items[i].getId()) ? kill_logs.get(name).loots.get(items[i].getId()) : 0;
					items[i].setAmount(amount);
				}
				player.getPA().sendItems(54301, Arrays.asList(items));  
			}
			return true;
		case MINIGAMES:
			if (index >= minigames.size())
				return false;
			if (index < minigames.size()) {
				String name = minigames.get(index);
				player.getPA().sendString(54302, Misc.capitalize(name));
				int obtained = minigame_logs.containsKey(name) ? minigame_logs.get(name).loots.size(): 0;
				int required = minigameReqs.get(name).size();
				String color = obtained == required ? "<col=00ff00>" : "<col=ff0000>";
				player.getPA().sendString(54303, "Obtained: " + color + obtained + "/" + required);
				int kills = minigame_logs.containsKey(name) ? minigame_logs.get(name).amount : 0;
				player.getPA().sendString(54304, "kills: <col=ffffff>" + kills);
				GameItem[] items = new GameItem[minigameReqs.get(name).size()];
				for (int i = 0; i < items.length; i++) {
					items[i] = new GameItem(minigameReqs.get(name).get(i), 0);
					int amount = minigame_logs.containsKey(name) && minigame_logs.get(name).loots.containsKey(items[i].getId()) ? minigame_logs.get(name).loots.get(items[i].getId()) : 0;
					items[i].setAmount(amount);
				}
				player.getPA().sendItems(54301, Arrays.asList(items));  
			}
			return true;
		case OTHER:
			if (index >= other.size())
				return false;
			if (index < other.size()) {
				String name = other.get(index);
				player.getPA().sendString(54302, Misc.capitalize(name));
				int obtained = other_logs.containsKey(name) ? other_logs.get(name).loots.size(): 0;
				int required = otherReqs.get(name).size();
				String color = obtained == required ? "<col=00ff00>" : "<col=ff0000>";
				player.getPA().sendString(54303, "Obtained: " + color + obtained + "/" + required);
				int kills = other_logs.containsKey(name) ? other_logs.get(name).amount : 0;
				player.getPA().sendString(54304, "kills: <col=ffffff>" + kills);
				GameItem[] items = new GameItem[otherReqs.get(name).size()];
				for (int i = 0; i < items.length; i++) {
					items[i] = new GameItem(otherReqs.get(name).get(i), 0);
					int amount = other_logs.containsKey(name) && other_logs.get(name).loots.containsKey(items[i].getId()) ? other_logs.get(name).loots.get(items[i].getId()) : 0;
					items[i].setAmount(amount);
				}
				player.getPA().sendItems(54301, Arrays.asList(items));  
			}
			return true;
		} 
		return false;
	}
	
	public boolean clickButton(int button) {
		switch (button) {
		case 212033:
			selectedType = CollectionLogType.KILL;
			open();
			return true;
		case 211087:
			selectedType = CollectionLogType.CLUE;
			open();
			return true;
		case 211090:
			selectedType = CollectionLogType.MINIGAMES;
			open();
			return true;
		case 211036:
			selectedType = CollectionLogType.OTHER;
			open();
			return true;
		}
		if (select((button+1)/2 - 211136 / 2))
			return true;
		return false;
	}
	
	public void open() { 
		int idx = 0; 
		switch (selectedType) { 
		case CLUE:
			for (; idx < clues.size(); idx++)
				player.getPA().sendString(54152+(idx*2), Misc.capitalize(clues.get(idx)));
			while (idx < 17+8)
				player.getPA().sendString(54152+(idx++*2), "");
			//player.send(new SendScrollbar(54150, idx * 15)); 
			break;
		case KILL:
			for (; idx < npcs.size(); idx++)
				player.getPA().sendString(54152+(idx*2), Misc.capitalize(npcs.get(idx)));
			while (idx < 17+8)
				player.getPA().sendString(54152+(idx++*2), "");
		//	player.send(new SendScrollbar(54150, idx * 15)); 
			break;
		case MINIGAMES:
			for (; idx < minigames.size(); idx++)
				player.getPA().sendString(54152+(idx*2), Misc.capitalize(minigames.get(idx)));
			while (idx < 17+8)
				player.getPA().sendString(54152+(idx++*2), "");
			//player.send(new SendScrollbar(54150, idx * 15));
			break; 
		case OTHER:
			for (; idx < other.size(); idx++)
				player.getPA().sendString(54152+(idx*2), Misc.capitalize(other.get(idx)));
			while (idx < 17+8)
				player.getPA().sendString(54152+(idx++*2), "");
			//player.send(new SendScrollbar(54150, idx * 15));
			break;
		}
		select(0);
		player.getPA().showInterface(54100);
	}
	
	public void addLog(CollectionLog log, CollectionLogType type) { 
		switch (type) {
		case CLUE:
			if (!clues.contains(log.name.toLowerCase()))
				return;
			break;
		case KILL:
			if (!npcs.contains(log.name.toLowerCase()))
				return;
			break;
		case MINIGAMES:
			if (!minigames.contains(log.name.toLowerCase()))
				return;
			break; 
		case OTHER:
			break;
		}
		HashMap<String, CollectionLog> logs = getLogs(type);
		int amount = 0;
		if (logs.containsKey(log.name)) {
			amount = logs.get(log.name).amount;
			logs.get(log.name).combine(log);
		} else
			logs.put(log.name, log);
		log = getLogs(type).get(log.name);
		if (log.amount > amount)
			player.sendMessage("[Collection Log] You have " + type.actionWord +" "+ log.name + " " + log.amount + " times.");		
	}
	
	public HashMap<String, CollectionLog> getLogs(CollectionLogType type) {
		switch (type) { 
		case CLUE:
			return clue_logs; 
		case KILL: 
			return kill_logs;
		case MINIGAMES:
			return minigame_logs; 
		case OTHER:
			return other_logs;
		default:
			return kill_logs;
		}
	}
}
