package ethos.model.npcs.bosses;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ethos.Server;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.util.Misc;

public class Nightmare {
	
	private final Player player;

	/**
	 * Player variables, start coordinates.
	 */
	private static final int START_X = 4048, START_Y = 9123;
	
	/**
	 * Npc variables, start coordinates.
	 */
	public static final int SPAWN_X = 4048, SPAWN_Y = 9133, Nightmare_ID = 9426, AWAKENED_ALTAR_NORTH = 9442, AWAKENED_ALTAR_SOUTH = 9442, AWAKENED_ALTAR_WEST = 9442, AWAKENED_ALTAR_EAST = 9442, REANIMATED_DEMON = 7287, DARK_ANKOU = 7296;
	
	public boolean northAltar, southAltar, eastAltar, westAltar, ankouSpawned, demonsSpawned = false;
	
	public boolean firstHit = true;
	
    public Map<Integer, String> altarMap = Collections.synchronizedMap(new HashMap<Integer, String>());
    
    private String altarMapDirection[] = {"NORTH", "SOUTH", "WEST", "EAST"}; 
    
    public int altarCount = 0;
	
    private int getAltar() { return (Misc.random(3) + 1); }
    
	public Nightmare(Player player) {
		this.player = player;
	}
	
	public int calculateNightmareHit(Player attacker, int damage) {
		if (altarCount == 0) {
			if (attacker.debugMessage)
				player.sendMessage("full hit");
		} else if (attacker.getNightmare().altarCount == 1) {
			if (attacker.debugMessage)
		//		attacker.sendMessage("3/4 hit");
				damage = (int)(damage * 0);
		} else if (attacker.getNightmare().altarCount == 2) {
			if (attacker.debugMessage)
			//	attacker.sendMessage("1/2 hit");
				damage = (int)(damage * 0);
		} else if (attacker.getNightmare().altarCount == 3) {
			if (attacker.debugMessage)
			//	attacker.sendMessage("1/4 hit");
				damage = (int)(damage * 0);
		} else if (attacker.getNightmare().altarCount == 4) {
			if (attacker.debugMessage)
			//	attacker.sendMessage("0 hit");
				damage = 0;
		}
		return damage;
	}
	
	public void NightmareSpecials() {
		NPC Nightmare = NPCHandler.getNpc(Nightmare_ID);
		
		if (Nightmare.isDead) {
			return;
		}
		
		int random = Misc.random(11);
		
		if (random == 1) {
			
			int altarNumber = getAltar();
			boolean unique = false;
			
			while (!unique) {
				if ((altarMap.get(1) == "NORTH") && (altarMap.get(2)== "SOUTH") && (altarMap.get(3) == "WEST") && (altarMap.get(4) == "EAST")) {
					break;
				}
				String altar = altarMap.get(altarNumber);
				if(altar == null) {
					altarMap.put(altarNumber, altarMapDirection[altarNumber-1]);
					unique = true;
					if (altarMapDirection[altarNumber-1] == "NORTH") {
						player.sendMessage("@or2@The north altar has just awakened!");
						player.getPA().sendChangeSprite(29232, (byte) 1);
						Server.npcHandler.spawnNpc(player, AWAKENED_ALTAR_NORTH, 1694, 9904, 0, 0, 100, 10, 200, 200, false, false);
						altarCount++;
						northAltar = true;
					} else if (altarMapDirection[altarNumber-1] == "SOUTH") {
						player.sendMessage("@or2@The south altar has just awakened!");
						player.getPA().sendChangeSprite(29233, (byte) 1);
						Server.npcHandler.spawnNpc(player, AWAKENED_ALTAR_SOUTH, 1696, 9871, 0, 0, 100, 10, 200, 200, false, false);
						altarCount++;
						southAltar = true;
					} else if (altarMapDirection[altarNumber-1] == "WEST") {
						player.sendMessage("@or2@The west altar has just awakened!");
						player.getPA().sendChangeSprite(29234, (byte) 1);
						Server.npcHandler.spawnNpc(player, AWAKENED_ALTAR_WEST, 1678, 9888, 0, 0, 100, 10, 200, 200, false, false);
						altarCount++;
						westAltar = true;
					} else if (altarMapDirection[altarNumber-1] == "EAST") {
						Server.npcHandler.spawnNpc(player, AWAKENED_ALTAR_EAST, 1714, 9888, 0, 0, 100, 10, 200, 200, false, false);
						altarCount++;
						eastAltar = true;
					}
				} else {
					altarNumber = getAltar();
				}
			}
		} else if (random == 2 || random == 3) {
			if (Nightmare.getHealth().getAmount() < 225 && !demonsSpawned) {
				NPCHandler.npcs[Nightmare.getIndex()].forceChat("Gar mulno ful taglo!");
				Server.npcHandler.spawnNpc(player, REANIMATED_DEMON, player.absX + 1, player.absY, 0, 0, 85, 8, 350, 300, true, false);
				Server.npcHandler.spawnNpc(player, REANIMATED_DEMON, player.absX - 1, player.absY, 0, 0, 85, 8, 350, 300, true, false);
				Server.npcHandler.spawnNpc(player, REANIMATED_DEMON, player.absX, player.absY + 1, 0, 0, 85, 8, 350, 300, true, false);
				demonsSpawned = true;
			}
		} else if (random == 4 && Misc.random(5) == 0) {
			if (Nightmare.getHealth().getAmount() < 150 && !ankouSpawned) {
				Server.npcHandler.spawnNpc(player, DARK_ANKOU, player.absX, player.absY - 1, 0, 0, 60, 8, 350, 300, true, false);
				ankouSpawned = true;
			}
		}
	}

	/**
	 * Constructs the content by creating an event
	 */
	public void init() {
		Server.npcHandler.spawnNpc(player, Nightmare_ID, SPAWN_X, SPAWN_Y, 0, 0, 450, 38, 500, 600, true, false);
		
		player.getPA().movePlayer(START_X, START_Y, 0);
		
		player.getPA().sendChangeSprite(29232, (byte) 0);
		player.getPA().sendChangeSprite(29233, (byte) 0);
		player.getPA().sendChangeSprite(29234, (byte) 0);
		player.getPA().sendChangeSprite(29235, (byte) 0);
		
	}

	/**
	 * Disposes of the content by moving the player and finalizing and or removing any left over content.
	 * 
	 * @param dispose the type of dispose
	 */
	public final void end(DisposeTypes dispose) {
		if (player == null) {
			return;
		}
		if (dispose == DisposeTypes.COMPLETE) {
			//player.sendMessage("You killed Nightmare.");
			if (demonsSpawned) {
				NPCHandler.kill(REANIMATED_DEMON, 0);
				NPCHandler.kill(REANIMATED_DEMON, 0);
				NPCHandler.kill(REANIMATED_DEMON, 0);
			}
			if (ankouSpawned)
				NPCHandler.kill(DARK_ANKOU, 0);
			if (northAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_NORTH, 0);
				player.getPA().sendChangeSprite(29232, (byte) 0);
				altarMap.remove(1);
				northAltar = false;
			} else {

			}
			if (southAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_SOUTH, 0);
				player.getPA().sendChangeSprite(29233, (byte) 0);
				altarMap.remove(2);
				southAltar = false;
			} else {
			}
			if (westAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_WEST, 0);
				player.getPA().sendChangeSprite(29234, (byte) 0);
				altarMap.remove(3);
				westAltar = false;
			} else {

			}
			if (eastAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_EAST, 0);
				player.getPA().sendChangeSprite(29235, (byte) 0);
				altarMap.remove(4);
				eastAltar = false;
			} else {
			}

			
		} else if (dispose == DisposeTypes.INCOMPLETE) {			
			NPCHandler.kill(Nightmare_ID, 0);
			if (northAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_NORTH, 0);
				player.getPA().sendChangeSprite(29232, (byte) 0);
				altarMap.remove(1);
				northAltar = false;
			} else {
			}
			if (southAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_SOUTH, 0);
				player.getPA().sendChangeSprite(29233, (byte) 0);
				altarMap.remove(2);
				southAltar = false;
			} else {
			}
			if (westAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_WEST, 0);
				player.getPA().sendChangeSprite(29234, (byte) 0);
				altarMap.remove(3);
				westAltar = false;
			} else {
			}
			if (eastAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_EAST, 0);
				player.getPA().sendChangeSprite(29235, (byte) 0);
				altarMap.remove(4);
				eastAltar = false;
			} else {
			}
			if (demonsSpawned) {
				NPCHandler.kill(REANIMATED_DEMON, 0);
				NPCHandler.kill(REANIMATED_DEMON, 0);
				NPCHandler.kill(REANIMATED_DEMON, 0);
			}
			if (ankouSpawned)
				NPCHandler.kill(DARK_ANKOU, 0);

		}
	}
	
	public int getHeight() {
		return 0;
	}
}
