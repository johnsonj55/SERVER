package ethos.model.players.packets.commands.all;

import ethos.model.content.group_ironman.IronmanTeamHandler;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Deleteteam extends Command {
	

		@Override
		public void execute(Player c, String input) {
			if (c.totalLevel > 50) {
				c.getDH().sendStatement("You may not delete your clan and form a new one, your total level is over 50!");
				return;
			}
			IronmanTeamHandler.deleteClan(c);
			c.getDH().sendStatement("You have succesfully deleted your clan.", "Form a new one by accepting or sending an invite.");
		}


}
