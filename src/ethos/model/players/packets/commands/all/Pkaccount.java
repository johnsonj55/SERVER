
package ethos.model.players.packets.commands.all;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Pkaccount extends Command {

	@Override
	public void execute(Player player, String input) {
		player.getDH().sendDialogues(99999, 311);
		
	}

}
