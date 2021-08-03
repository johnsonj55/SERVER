package ethos.model.npcs.bosses.vorkath.impl;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.players.Player;

/**
 * @author Yasin
 */
public class Stage extends CycleEvent {

    protected Vorkath vorkath;

    protected Player player;

    public Stage(Vorkath vorkath, Player player) {
        this.vorkath = vorkath;
        this.player = player;
    }

    @Override
    public void execute(CycleEventContainer container) {

    }
}
