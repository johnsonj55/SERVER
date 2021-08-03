/*package ethos.model.npcs.bosses.hydra;

import java.util.stream.Stream;

import ethos.model.players.Boundary;
//import lombok.Getter;


public enum HydraStage {

	POISON(8615, 8616, 8237, -1, 1100, new Boundary(1369, 10261, 1373, 10265)),

	LIGHTNING(8619, 8617, 8244, 8238, 825, new Boundary(1369, 10270, 1373, 10274)),

	FLAME(8620, 8618, 8251, 8245, 550, new Boundary(1360, 10270, 1364, 10274)),

	ENRAGED(8621, 8622, 8257, 8252, 275, AlchemicalHydra.AREA);
	
	

	private final int npcId, health, deathID, deathAnimation;
	private final int transformation;
	private final Boundary boundary;

	HydraStage(int npcId, int deathID, int deathAnimation, int transformation, int health, Boundary boundary) {
		this.npcId = npcId;
		this.transformation = transformation;
		this.health = health;
		this.boundary = boundary;
		this.deathID = deathID;
		this.deathAnimation = deathAnimation;
	}

	public static Stream<HydraStage> stream() {
		return Stream.of(values());
	}

}*/