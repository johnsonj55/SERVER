package ethos.model.players.packets.commands.all;

import ethos.model.content.group_ironman.IronmanTeam;
import ethos.model.content.group_ironman.IronmanTeamHandler;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

import java.util.Optional;

/**
 * @author optium on 15/05/2020
 */
public class Checkteam extends Command {

    @Override
    public void execute(Player player, String input) {
        Optional<IronmanTeam> team = IronmanTeamHandler.getPlayersTeam(player);
        if(!team.isPresent()) return;
        player.getDH().sendStatement("Your team consists of: "+ team.get().getMembersAsString());
    }
}
