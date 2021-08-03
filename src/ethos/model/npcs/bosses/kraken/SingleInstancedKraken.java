package ethos.model.npcs.bosses.kraken;

import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;

public class SingleInstancedKraken extends SingleInstancedArea {
	
	public SingleInstancedKraken(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Kraken kraken = player.getKraken();
		if (player.getKraken().getNpc() != null) {
			NPCHandler.kill(player.getKraken().getNpc().npcType, height);
		}
		//Server.getGlobalObjects().remove(17000, height);
		//NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
