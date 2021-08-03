package ethos.model.players.packets.commands.all;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Resetstats extends Command {

	@Override
	public void execute(Player player, String input) {
		for (int i = 0; i < 7; i++) { 
			player.getPA().refreshSkill(i);
			player.getPA().setSkillLevel(2, 1, player.getPA().getXPForLevel(1) + 1);
			player.getPA().setSkillLevel(3, 10, player.getPA().getXPForLevel(10) + 1);
			player.getPA().setSkillLevel(4, 1, player.getPA().getXPForLevel(1) + 1);
			player.getPA().setSkillLevel(5, 1, player.getPA().getXPForLevel(1) + 1);
			player.getPA().setSkillLevel(6, 1, player.getPA().getXPForLevel(1) + 1);
			player.getPA().setSkillLevel(0, 1, player.getPA().getXPForLevel(1) + 1);
			player.getPA().levelUp(i);
		}
		player.claimedPurePackage = false;
		
	}

}
