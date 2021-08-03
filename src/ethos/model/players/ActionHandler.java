package ethos.model.players;

import ethos.model.minigames.CastleWarObjects;
import ethos.model.players.packets.npcoptions.NpcOptionFour;
import ethos.model.players.packets.npcoptions.NpcOptionOne;
import ethos.model.players.packets.npcoptions.NpcOptionThree;
import ethos.model.players.packets.npcoptions.NpcOptionTwo;
import ethos.model.players.packets.objectoptions.ObjectOptionFour;
import ethos.model.players.packets.objectoptions.ObjectOptionOne;
import ethos.model.players.packets.objectoptions.ObjectOptionThree;
import ethos.model.players.packets.objectoptions.ObjectOptionTwo;

public class ActionHandler {

	private Player c;

	public ActionHandler(Player Client) {
		this.c = Client;
	}

	public void firstClickObject(int objectType, int obX, int obY) {
		ObjectOptionOne.handleOption(c, objectType, obX, obY);
		switch (objectType) {
        case 4411:
        case 4415:
        case 4417:
        case 4418:
        case 4419:
        case 4420:
        case 4469:
        case 4470:
        case 4911:
        case 4912:
        case 1747:
        case 1757:
        case 4437:
        case 6281:
        case 6280:
        case 4472:
        case 4471:
        case 4406:
        case 4407:
        case 4458:
        case 4902:
        case 4903:
        case 4900:
        case 4901:
        case 4461:
        case 4463:
        case 4464:
        case 4377:
        case 4378:
            CastleWarObjects.handleObject(c, objectType, obX, obY);
        case 1568:
            if (obX == 3097 && obY == 3468) {
                c.getPA().movePlayer(3097, 9868, 0);
            } else {
                CastleWarObjects.handleObject(c, obY, obY, obY);
            }
            break;
		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		ObjectOptionTwo.handleOption(c, objectType, obX, obY);
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		ObjectOptionThree.handleOption(c, objectType, obX, obY);
	}

	public void fourthClickObject(int objectType, int obX, int obY) {
		ObjectOptionFour.handleOption(c, objectType, obX, obY);
	}

	public void firstClickNpc(int npcType) {
		NpcOptionOne.handleOption(c, npcType);
	}

	public void secondClickNpc(int npcType) {
		NpcOptionTwo.handleOption(c, npcType);
	}

	public void thirdClickNpc(int npcType) {
		NpcOptionThree.handleOption(c, npcType);
	}

	public void fourthClickNpc(int npcType) {
		NpcOptionFour.handleOption(c, npcType);
	}

}