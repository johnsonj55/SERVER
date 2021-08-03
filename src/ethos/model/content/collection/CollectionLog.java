package ethos.model.content.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ethos.model.content.barrows.Barrows;
import ethos.model.content.trails.RewardItem;
import ethos.model.items.GameItem;
 
public class CollectionLog implements Serializable{
 
	private static final long serialVersionUID = -2477021978626050498L;
	
	public String name;
	public int amount;
	public long best_time;
	public HashMap<Integer, Integer> loots = new HashMap<Integer, Integer>();

	public CollectionLog() { 
	}
	
	public void addLoot(int id, int amount) {
        addLoot(new GameItem(id, amount));
    }
	
	public CollectionLog(String name, int amount, int best_time, HashMap<Integer, Integer> loots) {
		this.name = name;
		this.amount = amount;
		this.best_time = best_time;
		this.loots = loots;
	}

	public void combine(CollectionLog log) { 
		if (log.best_time < best_time) 
			best_time = log.best_time;
		amount += log.amount;
		addLoot(log.loots);
	}

	public void addLoot(List<ethos.model.content.barrows.RewardItem> rewards) {
		for (ethos.model.content.barrows.RewardItem item : rewards)
			addLoot(item);
	}

	public void addLoot(List<RewardItem> rewards, boolean fix) {
		for (RewardItem item : rewards)
			addLoot(item);
	}
	 
	public void addLoot(RewardItem item) {
		if (loots.containsKey(item.getId()))
			loots.replace(item.getId(), item.getAmount() + loots.get(item.getId()));
		else
			loots.put(item.getId(), item.getAmount());
	}
	
	public void addLoot(GameItem item) {
		if (loots.containsKey(item.getId()))
			loots.replace(item.getId(), item.getAmount() + loots.get(item.getId()));
		else
			loots.put(item.getId(), item.getAmount());
	}
	 
	public void addLoot(Entry<Integer, Integer> item) {
		if (loots.containsKey(item.getKey()))
			loots.replace(item.getKey(), item.getValue() + loots.get(item.getKey()));
		else
			loots.put(item.getKey(), item.getValue());
	}

	public void addLoot(ArrayList<RewardItem> loot) {
		for (GameItem item : loot)
			addLoot(item);
	}
	 
	
	public void addLoot(Set<GameItem> loot) {
		for (GameItem item : loot)
			addLoot(item);
	}
	
	public void addLoot(GameItem[] loot) {
		for (GameItem item : loot)
			addLoot(item);
	}

	public void addLoot(HashMap<Integer, Integer> loot) {
		for (Entry<Integer, Integer> item : loot.entrySet())
			addLoot(item);
	} 
	
}
