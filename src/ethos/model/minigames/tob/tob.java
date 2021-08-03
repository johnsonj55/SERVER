package ethos.model.minigames.tob;

import ethos.clip.doors.Location;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

public class tob {

	/**
	 * The player
	 */
	Player player;

	/**
	 * bosses
	 */

	// public int MaidenOfSugadinti;

	// public int PestilentBloat;

	public static void handleNpcDeath(Player player, NPC npc) {
		switch (npc.npcType) {
		
		case 8361:
			player.sendMessage("@dre@You have killed The Maiden of Sugadinti you will move now to the next boss");
			player.getPA().movePlayer(3288, 4447, 0);// The Maiden of Sugadinti
			break;
			
		case 8359:
			player.sendMessage("@dre@You have killed pestilent bloat you will move now to the next boss");
			player.getPA().movePlayer(3289, 4448, 0);// PestilentBloat
			break;
			
		case 8354:
			player.sendMessage("@dre@You have killed Nylocas Vasilias you will move now to the next boss");
			player.getPA().movePlayer(3280, 4320, 0);// Nylocas Vasilias
			break;
			
		case 8388:
			player.sendMessage("@dre@You have killed Sotetseg you will move now to the next boss");
			player.getPA().movePlayer(3170, 4393, 0);// Sotetseg
			break;
			
		case 8338:
			player.sendMessage("@dre@You have killed Xarpus you will move now to the next boss");
			player.getPA().movePlayer(3168, 4316, 0);// Xarpus
			break;
			
		}

	}

/*	// First thing. This method is never called
	public static void PestilentBloat(Player player) {

		NPC Pestilent_Bloat = NPCHandler.getNpc(8359);
		if (Pestilent_Bloat.isDead = true) {
			Player p = PlayerHandler.getPlayer("username");
			System.out.println("test");
			// player.sendMessage("You have killed pestilent bloat you will move
			// now to the next boss");
			p.getPA().movePlayer(3289, 4448, 0);// PestilentBloat
			p.sendMessage("You have killed pestilent bloat you will move now to the next boss");
			return;
		}

	}

	// if (MaidenOfSugadinti.isDead = true )
	/*
	 * public void killAllSpawns(Player player) {
	 * 
	 * 
	 * NPCHandler.kill(394,
	 * player.getRaids().getHeight(player.getRaids().raidLeader)); // banker
	 * NPCHandler.kill(8361,
	 * player.getRaids().getHeight(player.getRaids().raidLeader),
	 * player.getPA().movePlayer(3181, 4446, 0));
	 * player.getPA().movePlayer(3181, 4446, 0); NPCHandler.kill(8361,
	 * player.getPA().movePlayer(3181, 4446, 0)); // MaidenOfSugadinti
	 * NPCHandler.killtob(8361, .getPA().movePlayer(3289, 4448, 0)); //
	 * MaidenOfSugadinti NPCHandler.kill2(3341, p1.getPA().movePlayer(3289,
	 * 4448, 0)); //PestilentBloat NPCHandler.kill(7563,
	 * player.getPA().movePlayer(3296, 4254, 0)); //Nylocas Vasilias
	 * NPCHandler.kill(7566, player.getPA().movePlayer(3279, 4308, 0)); //
	 * Sotetseg NPCHandler.kill(7585, player.getPA().movePlayer(3170, 4394, 1));
	 * //Xarpus NPCHandler.kill(7544, player.getPA().movePlayer(3168, 4303, 0));
	 * //Verzik Vitur NPCHandler.kill(7573, player.getPA().movePlayer(3237,
	 * 4307, 0)); // chest loot room
	 * 
	 * } }
	 * 
	 * 
	 * /* c.getPA().movePlayer(3181, 4446, 0); // MaidenOfSugadinti
	 * c.getPA().movePlayer(3289, 4448, 0); //PestilentBloat
	 * c.getPA().movePlayer(3296, 4254, 0); //Nylocas Vasilias
	 * c.getPA().movePlayer(3279, 4308, 0); // Sotetseg
	 * c.getPA().movePlayer(3170, 4394, 0); //Xarpus c.getPA().movePlayer(3168,
	 * 4303, 0); //Verzik Vitur c.getPA().movePlayer(3237, 4307, 0); // chest
	 * loot room
	 */

}
