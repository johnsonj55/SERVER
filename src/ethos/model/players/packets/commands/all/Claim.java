package ethos.model.players.packets.commands.all;

import ethos.database.Donations;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Auto Donation System / https://EverythingRS.com
 * @author Genesis
 *
 */

public class Claim extends Command { 
	@Override
	public void execute(Player player, String input) {
		new Donations(player).run();
		
	}

}