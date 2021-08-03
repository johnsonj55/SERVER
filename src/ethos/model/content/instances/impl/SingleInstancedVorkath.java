package ethos.model.content.instances.impl;

import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.players.Boundary;
import ethos.model.players.Player;

public class SingleInstancedVorkath extends SingleInstancedArea {

    public SingleInstancedVorkath(Boundary boundary, int height) {
        super(boundary, height);
    }

    public SingleInstancedVorkath(Player player, Boundary boundary, int height) {
        super(player, boundary, height);
    }

    @Override
    public void onDispose() {
        Vorkath vorkath = player.getVorkath();
        if (vorkath != null) {
            vorkath.disposeInstance();
        }
    }

}
