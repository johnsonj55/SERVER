/*package ethos.model.entity.npcs.konar.hydra;

import ethos.model.players.Boundary;

import java.util.stream.Stream;

public enum HydraStage {

        POISON(8615, 8616, 8237,-1, 1100, AlchemicalHydraConstants.RED_VENT),

        LIGHTNING(8619, 8617, 8244, 8238, 825, AlchemicalHydraConstants.GREEN_VENT),

        FLAME(8620, 8618, 8251, 8245, 550, AlchemicalHydraConstants.BLUE_VENT),

        ENRAGED(8621, 8622, 8257, 8252, 275, AlchemicalHydraConstants.AREA);

        private int npcId, health, deathID, deathAnimation;
        private int transformation;
        private Boundary boundary;


    HydraStage(int npcId, int deathID, int deathAnimation, int transformation, int health, Boundary boundary) {
            this.npcId = npcId;
            this.transformation = transformation;
            this.health = health;
            this.boundary = boundary;
            this.deathID = deathID;
            this.deathAnimation = deathAnimation;
        }

        public static Stream<HydraStage> stream(){
            return Stream.of(values());
        }

        public int getNpcId() {
            return npcId;
        }

        public int getHealth() {
            return health;
        }

        public int getTransformation() {
            return transformation;
        }

        public Boundary getBoundary() {
            return boundary;
        }

    public int getDeathID() {
        return deathID;
    }

    public int getDeathAnimation() {
        return deathAnimation;
    }
}*/
