package ethos.model.players.packets;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.content.group_ironman.IronmanTeam;
import ethos.model.content.group_ironman.IronmanTeamHandler;
import ethos.model.players.PacketType;

public class InviteToGroup implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int playerIndex = c.getInStream().readUnsignedWordBigEndian();
		Optional<Player> other = PlayerHandler.getPlayerByIndex(playerIndex);
		
		if(!other.isPresent()) return;
		
		Optional<IronmanTeam> getTeam = c.getIronmanTeam();
		if(getTeam.isPresent()) {
			if(getTeam.get().canInvite(c, other.get())) {
				getTeam.get().sendInvitation(c, other.get());
			}
		} else {
			
			Optional<IronmanTeam> otherTeam = other.get().getIronmanTeam();
			
			if (otherTeam.isPresent()) {
				c.sendMessage("@red@The person you're trying to invite is in a team already.");
				other.get().sendMessage("@red@You've been invited to a clan by "+c.playerName+" but you are the owner of a team already.");
				other.get().sendMessage("@red@If you wish to be in their team, type ::deleteteam and have them invite you again!");
				return;
			}
			Optional<IronmanTeam> create = IronmanTeamHandler.createIronmanTeam(c);
			if(create.isPresent()) {
				if(create.get().canInvite(c, other.get())) {
					create.get().sendInvitation(c, other.get());
				}
			}
		}
	}

}
