package ethos.model.npcs.bosses.kraken;

import ethos.event.CycleEvent;
import ethos.model.players.Player;

public abstract class KrakenStage extends CycleEvent {
	
	protected Kraken kraken;
	
	protected Player player;
	
	public KrakenStage(Kraken kraken, Player player) {
		this.kraken = kraken;
		this.player = player;
	}

}
