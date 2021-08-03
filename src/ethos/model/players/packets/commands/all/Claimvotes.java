package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.database.Votes;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Opens the vote page in the default web browser.
 *
 * @author Emiel
 */
public class Claimvotes extends Command { 


    @Override
    public void execute(Player player, String input) {
    	new Votes(player).run();;
    } 
}

