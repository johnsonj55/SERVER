package ethos.model.players.packets.commands.all;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Pure extends Command {

	@Override
	public void execute(Player player, String input) { 
		if (player.PK_RESTRICTED == true) {
			if (player.getItems().isWearingItems() == true) {
				player.sendMessage("You cannot use this command when wearing items.");
				return;
			}
		player.playerLevel[0] = 60;
		player.playerLevel[3] = 99;
		player.playerLevel[4] = 99;
		player.playerLevel[2] = 80;
		player.playerLevel[5] = 43;
		player.playerLevel[6] = 99;
		player.playerXP[0] = 275000;
		player.playerXP[2] = 2000000;
		player.playerXP[3] = 14000000;
		player.playerXP[4] = 14000000;
		player.playerXP[5] = 52000;
		player.playerXP[6] = 14000000;
			player.getPA().setSkillLevel(3, 99, 14000000);
			player.getPA().setSkillLevel(2, 80, 2000000);
			player.getPA().setSkillLevel(4, 99, 14000000);
			player.getPA().setSkillLevel(5, 43, 52000);
			player.getPA().setSkillLevel(6, 99, 14000000);
			player.getPA().setSkillLevel(0, 60, 275000);
			player.getPA().levelUp(1);
			player.getPA().levelUp(0);
			player.getPA().levelUp(2);
			player.getPA().levelUp(3);
			player.getPA().levelUp(4);
			player.getPA().levelUp(5);
			player.getPA().levelUp(6);
			player.getPA().refreshSkill(0);
			player.getPA().refreshSkill(1);
			player.getPA().refreshSkill(2);
			player.getPA().refreshSkill(3);
			player.getPA().refreshSkill(4);
			player.getPA().refreshSkill(5);
			player.getPA().refreshSkill(6);
		} else { 
			player.sendMessage("Please make a pk account to do this.");
		}
 
	}
	

}
