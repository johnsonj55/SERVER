package ethos.model.content;

import ethos.world.objects.GlobalObject;
import ethos.util.Misc;
import ethos.util.Stopwatch;

import java.util.Arrays;
import java.util.List;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Position;

/**
 * 
 * @author Robbie
 *
 */

public class ShootingStar {

	private static final int TIME = 1800000;
	public static final int MAXIMUM_MINING_AMOUNT = 250;
	
	private static Stopwatch timer = new Stopwatch().reset();
	public static CrashedStar CRASHED_STAR = null;
	private static LocationData LAST_LOCATION = null;
	static final int STAR_ID = 5895;
	static GlobalObject star;
	
	public static class CrashedStar {
		
		public CrashedStar (GlobalObject starObject, LocationData starLocation) {
			this.starObject = starObject;
			this.starLocation = starLocation;
		}
		
		private GlobalObject starObject;
		private LocationData starLocation;
		
		public GlobalObject getStarObject() {
			return starObject;
		}
		
		public LocationData getStarLocation() {
			return starLocation;
		}
		
	}
	
	
	public static enum LocationData {
		LOCATION_1(new Position(3053, 3301), "south of the Falador Farming patches", "Farming"),
		LOCATION_2(new Position(3094, 3484), "south of the Edgeville bank", "Edgeville"),
		LOCATION_3(new Position(2480, 3433), "at the Gnome Agility Course", "Gnome Course"),
		LOCATION_4(new Position(2745, 3445), "in the middle of the Flax field", "Flax Field"),
		LOCATION_5(new Position(2322, 3796), "in the yak field", "Yak Field"),
		LOCATION_6(new Position(2481, 2867), "outside the Myths Guild", "Myths Guild"),
		LOCATION_7(new Position(3368, 3269), "in the Duel Arena", "Duel Arena"),
		LOCATION_8(new Position(1746, 5327), "in the Ancient cavern", "Ancient Cavern"),
		LOCATION_9(new Position(2882, 9800), "in the Taverly dungeon", "Taverly Dung."),
		LOCATION_10(new Position(2666, 2648), "at the Void knight island", "Pest Control"),
		LOCATION_11(new Position(3566, 3297), "on the Barrows hills", "Barrows"),
		LOCATION_12(new Position(2986, 3599), "in the Wilderness (western dragons)", "West Dragons"),
		LOCATION_13(new Position(3091, 3528), "in the Wilderness (Edgeville)", "Edgeville Wild"),
		LOCATION_14(new Position(2995, 3911), "outside the Wilderness Agility Course", "Wild. Course");

		private LocationData (Position spawnPos, String clue, String playerPanelFrame) {
			this.spawnPos = spawnPos;
			this.clue = clue;
			this.playerPanelFrame = playerPanelFrame;
		}

		private Position spawnPos;
		private String clue;
		public String playerPanelFrame;

	}
	public static LocationData getLocation() {
		return LAST_LOCATION;
	}
	
	public static LocationData getRandom() {
		LocationData star = LocationData.values()[Misc.random(LocationData.values().length - 1)];
		return star;
	}

	public static void spawnStar() {
		if(CRASHED_STAR == null) {
			//PlayerHandler.getPlayers().forEach(p ->p.getPA().sendFrame126("@or1@ -Crashed Star: @gre@None", 10410));
			if(timer.elapsed(TIME)) {
				LocationData locationData = getRandom();
				if(LAST_LOCATION != null) {
					if(locationData == LAST_LOCATION) {
						locationData = getRandom();
					}
				}
				LAST_LOCATION = locationData;
				CRASHED_STAR = new CrashedStar(new GlobalObject(5895, locationData.spawnPos), locationData);
				Server.getGlobalObjects().add(CRASHED_STAR.starObject);
				PlayerHandler.executeGlobalMessage("@pur@A star has just crashed " +locationData.clue+ "!");
				//PlayerHandler.getPlayers().forEach(p -> p.getPA().sendFrame126("@or1@ -Crashed star: @gre@"+ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame+"", 10410));
				timer.reset();
			}
		} else {
			if(CRASHED_STAR.starObject.getPickAmount() >= MAXIMUM_MINING_AMOUNT) {
				despawn(true);
				timer.reset();
		}
	}
}

	public static void despawn(boolean respawn) {
		if(respawn) {
			timer.reset();
		} else {
			timer.reset();
		}
		if(CRASHED_STAR != null) {
			for(Player p : PlayerHandler.getPlayers()) {
				if(p == null) {
					continue;
				}
				//p.getPA().sendFrame126("@or1@ -Crashed Star: @gre@None", 10410);
				if(CRASHED_STAR.starObject != null) {
					p.getSkilling().stop();
					p.sendMessage("The star has been fully mined.");
				}
			}
			PlayerHandler.executeGlobalMessage("@pur@The star is forcasted to crash again in one hour!");
			Server.getGlobalObjects().remove(CRASHED_STAR.starObject);
			CRASHED_STAR = null;
		}
	}
}


