package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Kraken extends Command {

	@Override
	public void execute(Player player, String input) {
		player.getKraken().initialize();
		
	}

}
