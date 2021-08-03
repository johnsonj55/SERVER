package ethos.model.npcs.bosses.kraken.impl;

import java.util.Arrays;

import ethos.Server;
import ethos.event.CycleEventContainer;
import ethos.model.npcs.bosses.kraken.Kraken;
import ethos.model.npcs.bosses.kraken.KrakenStage;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.combat.CombatType;


public class SpawnKrakenStageZero extends KrakenStage {

	public SpawnKrakenStageZero(Kraken kraken, Player player) {
		super(kraken, player);
	}
		
	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || kraken == null || player == null || player.isDead || kraken.getInstancedKraken() == null) {
			container.stop();
			return;
		}
		int cycle = container.getTotalTicks();
		if (cycle == 8) {
			player.stopMovement();
			player.getPA().sendScreenFade("Welcome to Kraken's Cave...", -1, 4);
			player.getPA().movePlayer(2279, 10022, kraken.getInstancedKraken().getHeight());
		}
		else if (cycle == 13) {
			Server.npcHandler.spawnNpc(player, 496, 2278, 10035, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 2275, 10034, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 2284, 10034, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 2284, 10038, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 2275, 10038, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			//player.KRAKEN_CLICKS = 0;
			container.stop();
			}
			stop();
		}
	}
