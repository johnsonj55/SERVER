package ethos.model.players.packets.commands.all;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Train extends Command {

	@Override
	public void execute(Player player, String input) {
		if (player.inWild()) {
			player.sendMessage("You must not be in the wilderness to use this.");
			return;
		}
		if (!player.getPA().canTeleport("modern")) {
			player.sendMessage("You may not teleport at the moment");
			return;
		}
		player.getPA().movePlayerUnconditionally(3130, 3482, 0);
		
	}

}
