package ethos.model.players.packets.commands.all;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Song extends Command {
	
	@Override
	public void execute(Player player, String input) {
		try {
			player.getActionSender().sendSong(Integer.parseInt(input));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

}
