package ethos.model.npcs.bosses.kraken.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;

public class SpawnEntity {
	
	public static final Map<NPC, Player> DISTURBED_POOLS = new ConcurrentHashMap<>();
	
	public static void spawnEntity(Player c, int i) {
		final NPC npc = NPCHandler.npcs[i];
		if (npc == null || npc.npcType != 493 && npc.npcType != 496)
			return;
		final Player player = DISTURBED_POOLS.get(npc);
		if (player != null && player == c) {
			c.sendMessage("You've already disturbed this pool!");
			return;
		}
		final boolean head = npc.npcType == 496;
		c.getCombat().resetPlayerAttack();
		NPCHandler.KILL_POOLS(c, head ? 496 : 493, npc.absX, npc.absY, npc.heightLevel);
		DISTURBED_POOLS.put(npc,c);
		if (head) {
			for (NPC n : DISTURBED_POOLS.keySet()) {
				if (n == null)
					continue;
				Player p = DISTURBED_POOLS.get(n);
				if (p == null)
					continue;
				if (p == c)
					DISTURBED_POOLS.remove(p);
				p.krakenTent = 0;
			}
		}
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Server.npcHandler.spawnNpc(c, head ? 494 : 5535, npc.absX, npc.absY, c.getKraken().getInstancedKraken().getHeight(), -1, head ? 255 : 80, 10, 500, 500, true, false);
				c.krakenTent++;
				if (head) {
					npc.startAnimation(7135);
					npc.facePlayer(c.getIndex());
				}
				container.stop();
			}
		}, 3);
	} 
}
