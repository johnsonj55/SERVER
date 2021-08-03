package ethos.event.impl;

import ethos.Server;
import ethos.event.Event;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Position;
import ethos.util.Misc;

import javax.swing.text.Utilities;

public class BossEvent extends Event<Object> {
    private static Bosses[] bosses = Bosses.values();
    private static int INTERVAL = 6000; //The interval is the time between bosses, but it is like 120 * 0.6, which is 72 seconds. If you want 15 minutes, you do 15*60 is 900 seconds and then 900 / 0.6 is 1500
    // now it will be 15 mins between bosses.
    //put it to 1 min real quick which is 60 / 0.6 is 100
    public NPC currentBoss = null;
    private static int bossIndex = 0;

    public BossEvent() {
        super(new Object(), INTERVAL);
    }

    public void bossKilled() {
        if (currentBoss.isDead) {
            currentBoss.actionTimer = 0;
            currentBoss.needRespawn = false;
            Server.bossEvent = new BossEvent();
            Server.getEventHandler().submit(Server.bossEvent);
        }
    }

    @Override
    public void update() {
    	 /*     PlayerHandler.nonNullStream().forEach(player -> {
        	if (player.questTab == 0)
       		player.getPA().sendString("@or1@ - Wildy boss: @gre@" + (ticks > 100 ? (ticks / 100 + 1) + " mins" : (int)(ticks * 0.6) + " sec"), 10412);
        });*/
    }

    @Override
    public void execute() {
        currentBoss = NPCHandler.spawnNpc(bosses[bossIndex].npcId, bosses[bossIndex].position.getX(), bosses[bossIndex].position.getY(), bosses[bossIndex].position.getHeight(),
                bosses[bossIndex].walkType, bosses[bossIndex].hp, bosses[bossIndex].maxHit, bosses[bossIndex].attack, bosses[bossIndex].defence);
        currentBoss.doNotRespawn = true;
        PlayerHandler.nonNullStream().forEach(player -> {
            player.sendMessage("[Wildy-Boss]@red@" + Misc.capitalize(bosses[bossIndex].name().toLowerCase().replaceAll("_", " ")) +
                    " has spawned at " + bosses[bossIndex].area + ".");
            if (player.questTab == 0)
            	player.getPA().sendString("@or1@ - Wildy boss: @gre@" + bosses[bossIndex].area, 10412);
        });
        bossIndex++;
        if (bossIndex >= bosses.length) {
            bossIndex = 0;
        }
        stop();
    }

    public enum Bosses { //Here you can add the bosses
    	SKELETON_WARLOCK(84, new Position(3214, 3737, 0), 1, 600, 35, 30, 40, "wildy lvl 28"),
        //let's add one
        ANCIENT_SKELETON_archer(3358, new Position(3095, 3766, 0), 1, 600, 35, 30, 40, "Wildy lvl 29"),
        SKELETON_WARRIOR(1540, new Position(3036, 3686, 0), 1, 600, 35, 30, 40, "wildy lvl 21 ");
    	//SOME_NAME(NPCID, new Position(XCOORD, YCOORD, HEIGHT), WALKTYPE, HP, MAXHIT, ATTACK, DEFENCE, NAME OF THE AREA),

        private int npcId;
        private Position position;
        private int walkType;
        private int hp;
        private int maxHit;
        private int attack;
        private int defence;
        private String area;

        Bosses(int npcId, Position position, int walkType, int hp, int maxHit, int attack, int defence, String area) {
            this.npcId = npcId;
            this.position = position;
            this.walkType = walkType;
            this.hp = hp;
            this.maxHit = maxHit;
            this.attack = attack;
            this.defence = defence;
            this.area = area;
        }

        public int getNpcId() {
            return npcId;
        }

        public Position getPosition() {
            return position;
        }
    }
}
