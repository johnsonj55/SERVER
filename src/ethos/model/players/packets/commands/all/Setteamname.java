package ethos.model.players.packets.commands.all;

import ethos.model.content.group_ironman.IronmanTeam;
import ethos.model.content.group_ironman.IronmanTeamHandler;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

import java.util.Optional;
import java.util.UUID;

/**
 * @author optium on 15/05/2020
 */
public class Setteamname extends Command {

    @Override
    public void execute(Player player, String input) {
    	if (input.length() > 12) {
    		player.sendMessage("Team name must be maximum 12 characters long.");
    		return;
    	}
    	if (player.nameSet) {
    	player.sendMessage("You have already set a name for your team.");
		return;
    	}
        Optional<IronmanTeam> team = IronmanTeamHandler.getPlayersTeam(player);
        if(!team.isPresent()) return;
        team.get().setTeamName(player, input);
        player.sendMessage("You have set your teams name to @red@"+input+"@bla@. You may not change this.");
        player.nameSet = true;
    }
}
