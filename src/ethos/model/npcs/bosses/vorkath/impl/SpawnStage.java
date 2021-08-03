package ethos.model.npcs.bosses.vorkath.impl;

import ethos.Server;
import ethos.event.CycleEventContainer;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.npcs.bosses.vorkath.VorkathConstants;
import ethos.model.players.Player;

/**
 * @author Yasin
 */
public class SpawnStage extends Stage {

    public SpawnStage(Vorkath vorkath, Player player) {
        super(vorkath, player);
    }

    @Override
    public void execute(CycleEventContainer container) {
        if (container.getOwner() == null || vorkath == null || player == null || player.isDead || player.disconnected || vorkath.getVorkathInstance() == null) {
            container.stop();
            return;
        }
        int cycle = container.getTotalTicks();
        int height = vorkath.getVorkathInstance().getHeight();
        if(cycle == 1) {
           // player.sendMessage("Height before:" + player.heightLevel);
            player.setHeight(height);
            player.heightLevel = height;
            Server.getGlobalObjects().remove(32000, height);
            //player.sendMessage("Height after: " + player.getHeight());
            vorkath.setVorkath(Server.npcHandler.spawnNpc(player, VorkathConstants.SLEEPING_VORKATH_ID, VorkathConstants.X_COORDINATE, VorkathConstants.Y_COORDINATE, height, -1, VorkathConstants.VORKATH_LIFE_POINTS, 1, 500, 500, false, false));
            vorkath.getNpc().setFacePlayer(false);
            //player.sendMessage("On height: " + height);
        }
        if(cycle == 2) {
            player.sendMessage("You find yourself in a cave, with a dragon sleeping..");
        }
        if(cycle >= 3) {
            container.stop();
        }
    }
}
