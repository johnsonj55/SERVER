package ethos.model.npcs.bosses.vorkath.impl;

import ethos.Server;
import ethos.event.CycleEventContainer;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.npcs.bosses.vorkath.VorkathConstants;
import ethos.model.npcs.bosses.vorkath.VorkathState;
import ethos.model.players.Player;

/**
 * @author Yasin
 */
public class DeathStage extends Stage {
    public DeathStage(Vorkath vorkath, Player player) {
        super(vorkath, player);
    }

    @Override
    public void execute(CycleEventContainer container) {
        if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkath.getVorkathInstance() == null) {
            container.stop();
            return;
        }
        int cycle = container.getTotalTicks();
        int height = vorkath.getVorkathInstance().getHeight();
        //reset both combat until stage ends
        vorkath.resetCombat();
        player.getCombat().resetPlayerAttack();
       // player.sendMessage("Death stage Cycle: " + cycle);
        if (cycle == 1) {
            vorkath.setVorkathState(VorkathState.RESTING);
            vorkath.getNpc().isDead = false;
            vorkath.getNpc().spawnedMinions = false;
            player.sendMessage("The dragon died..");
            vorkath.getNpc().startAnimation(7949);//death animation
            vorkath.getNpc().requestTransform(VorkathConstants.SLEEPING_VORKATH_ID);
            vorkath.setVorkath(Server.npcHandler.getNpc(VorkathConstants.SLEEPING_VORKATH_ID, VorkathConstants.X_COORDINATE, VorkathConstants.Y_COORDINATE, height));
            container.stop();
        }
        /*if (cycle == 10) {
            CycleEventHandler.getSingleton().addEvent(vorkath.getEventLock(), new WakeUpStage(vorkath, player, true), 1);
            container.stop();
        }*/
    }
}
