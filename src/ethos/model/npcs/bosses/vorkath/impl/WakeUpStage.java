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
public class WakeUpStage extends Stage {

    private boolean alreadyAlive;

    public WakeUpStage(Vorkath vorkath, Player player) {
        super(vorkath, player);
    }

    public WakeUpStage(Vorkath vorkath, Player player, boolean alreadyAlive) {
        super(vorkath, player);
        this.alreadyAlive = alreadyAlive;
    }

    @Override
    public void execute(CycleEventContainer container) {
        if (container.getOwner() == null || vorkath == null || player == null || player.isDead) {
            container.stop();
            return;
        }
        int cycle = container.getTotalTicks();
        //reset both combat until stage ends
        if(vorkath != null) {
        	vorkath.resetCombat();
        }
        if(alreadyAlive) {
            if(cycle == 3) {
                vorkath.getNpc().getHealth().setAmount(VorkathConstants.VORKATH_LIFE_POINTS);
            }
            if(cycle == 6) {
                vorkath.getNpc().startAnimation(7950);
            }
            if(cycle == 9) {
                vorkath.setAttacks(0);
                vorkath.getNpc().setFacePlayer(true);
                vorkath.getNpc().requestTransform(VorkathConstants.AWAKENED_VORKATH_ID);
                vorkath.setVorkath(Server.npcHandler.getNpc(VorkathConstants.AWAKENED_VORKATH_ID, VorkathConstants.X_COORDINATE, VorkathConstants.Y_COORDINATE, player.getHeight()));
                vorkath.setVorkathState(VorkathState.AWAKE);
            }
            if(cycle >= 12) {
                container.stop();
            }
        } else {
            if(cycle == 1) {
                player.startAnimation(2292);
                player.sendMessage("You poke the dragon..");
            }
            if(cycle == 2) {
                vorkath.getNpc().startAnimation(7950);
                //player.turnPlayerTo(vorkath.getNpc().getX(), vorkath.getNpc().getY() - 3);
                vorkath.getNpc().getHealth().setAmount(VorkathConstants.VORKATH_LIFE_POINTS);
            }
            if(cycle == 9) {
            	vorkath.getNpc().setFacePlayer(true);
                vorkath.getNpc().requestTransform(VorkathConstants.AWAKENED_VORKATH_ID);
                vorkath.setVorkathState(VorkathState.AWAKE);
                container.stop();
            }
        }


    }
}
