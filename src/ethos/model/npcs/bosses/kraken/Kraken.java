package ethos.model.npcs.bosses.kraken;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.instances.InstancedArea;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.items.ItemDefinition;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.kraken.impl.SpawnKrakenStageZero;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.CombatType;
import ethos.util.Misc;

import com.google.common.base.Stopwatch;


public class Kraken {
	
	private static int[] DROP_LIST_RARE = {11905, 12007, 12006
			
	};
	
	public static int RARE_DROP() {
		return DROP_LIST_RARE[(int)(Math.random()*DROP_LIST_RARE.length - 1)];
	}
	
	public static void yell(String msg) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = (Player) PlayerHandler.players[j];
				c2.sendMessage(msg);
			}
		}
	}
	
	public final static void dropLoot(Player c) {
		final int logs = Misc.random(200);
		final int coins = Misc.random(2000);
		final int food = Misc.random(20);
		if (Misc.random(100) >= 0 && Misc.random(100) <= 25) {
			Server.itemHandler.createGroundItem(c, 1514, 3696, 5806, c.heightLevel, logs, c.getIndex());
		} else 
		if (Misc.random(100) >= 26 && Misc.random(100) <= 36) {
				Server.itemHandler.createGroundItem(c, 1516, 3696, 5806, c.heightLevel, logs, c.getIndex());
		} else 
		if (Misc.random(100) >= 37 && Misc.random(100) <= 49) {
				Server.itemHandler.createGroundItem(c, 13307, 3696, 5806, c.heightLevel, coins, c.getIndex());
		} else 
		if (Misc.random(100) >= 50 && Misc.random(100) <= 97) {
				Server.itemHandler.createGroundItem(c, 11937, 3696, 5806, c.heightLevel, food, c.getIndex());
		} else
		if(Misc.random(100) >= 98 && Misc.random(100) <= 100) {
				Server.itemHandler.createGroundItem(c, RARE_DROP(), 3696, 5806, c.heightLevel, 1, c.getIndex());
		}
	}
	
	public final static void RARE_MESSAGE(Player c, final int itemId) {
		//yell("@cr10@[@blu@DROP@bla@] @bla@" + c.playerName + " received a @blu@RARE @bla@drop: @blu@1x "
			//	+ ItemDefinition.forId(itemId).getName());
	}
	
	private final Object EVENT_LOCK = new Object();

	private final Player player;
	
	private SingleInstancedArea krakenInstance;
	
	public static final Boundary BOUNDARY = new Boundary(/*(X)*/3685, /*(Y)*/5798,
														/*(X)*/3707, /*(Y)*/5823);
	private NPC npc;

	private Stopwatch stopwatch = Stopwatch.createUnstarted();

	private Map<Integer, KrakenStage> stages = new HashMap<>();

	public Kraken(Player player) {
		this.player = player;
		stages.put(0, new SpawnKrakenStageZero(this, player));
	}
	
	public void initialize() {
		if (krakenInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(krakenInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		krakenInstance = new SingleInstancedKraken(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, krakenInstance);
		if (krakenInstance == null) {
			player.sendMessage("An error occured while trying to enter Kraken. Please try again.");
			return;
		}
		stopwatch = Stopwatch.createStarted();
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Kraken's Cave...", 1, 5);
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}

	public void stop() {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		stopwatch.stop();
		krakenInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(krakenInstance);
		krakenInstance = null;
	}

	public InstancedArea getInstancedKraken() {
		return krakenInstance;
	}

	public NPC getNpc() {
		return npc;
	}
	
	public void setNpc(NPC npc) {
		this.npc = npc;
	}
}
