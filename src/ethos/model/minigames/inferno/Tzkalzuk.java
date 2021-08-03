package ethos.model.minigames.inferno;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ethos.Server;
import ethos.model.content.collection.CollectionLog;
import ethos.model.content.collection.CollectionLogType;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.items.GameItem;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.Hitmark;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

public class Tzkalzuk extends SingleInstancedArea {

    /**
     * Npc variables, start coordinates.
     */
    public static final int SPAWN_X = 2268, SPAWN_Y = 5365;
    public static final int GLYPH_SPAWN_X = 2270, GLYPH_SPAWN_Y = 5363;

    public static final int TOKKUL = 6529;

    public int glyphCurrentX;
    public int glyphCurrentY;

    public NPC glyphNPC;

    public boolean started;

    public boolean zukDead = false;

    /**
     * Player variables, start coordinates.
     */
    private static final int START_X = 2271, START_Y = 5358;
    public boolean glyphCanMove, glyphMoveLeft, glyphMoveRight, cutsceneWalkBlock = false;

    public boolean spawnedJad;

    public boolean spawnedHealers;

    public List<NPC> kill = new ArrayList<>();

    public Tzkalzuk(Player player, Boundary boundary, int height) {
        super(player, boundary, height);
    }

    public void tzkalzukSpecials() {
        final NPC npc = NPCHandler.getNpc(InfernoWave.TZKAL_ZUK, height);

        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (npc.isDead) {
                    container.stop();
                    return;
                }
                if (player == null) {
                    container.stop();
                    return;
                }
                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                    player.resetInfernoInstance();
                    player.infernoWaveId = 0;
                    player.infernoWaveType = 0;
                    player.wall1Alive = 0;
                    player.wall1Hp = 0;
                    player.wall2Alive = 0;
                    player.wall2Hp = 0;
                    player.wall3Alive = 0;
                    player.wall3Hp = 0;
                    container.stop();
                    return;
                }
                if (container.getTotalTicks() % 200 == 0) {
                    spawnRangerAndMage();
                }

                if (npc.getHealth().getAmount() <= 480 && !player.getInferno().spawnedJad) {
                    player.getInferno().spawnedJad = true;
                    spawnJad();
                }
                if (npc.getHealth().getAmount() <= 240 && !player.getInferno().spawnedHealers) {
                    player.getInferno().spawnedHealers = true;
                    spawnHealers();
                }
                if (container.getTotalTicks() % 8 == 0) {
                    if (!Boundary.isIn(player, Boundary.INFERNO)) {
                        player.resetInfernoInstance();
                        player.infernoWaveId = 0;
                        player.infernoWaveType = 0;
                        player.wall1Alive = 0;
                        player.wall1Hp = 0;
                        player.wall2Alive = 0;
                        player.wall2Hp = 0;
                        player.wall3Alive = 0;
                        player.wall3Hp = 0;
                        container.stop();
                        return;
                    }
                    if (npc.isDead) {
                        container.stop();
                        return;
                    }
                    if (player == null) {
                        container.stop();
                        return;
                    }
                    npc.startAnimation(7566);
                    if (player.getInferno().glyphNPC.getDistance(player.getX(), player.getY()) >= 3.5 || player.getInferno().glyphNPC.isDead) {
                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                            int ticks = 0;
                            int amount = Misc.random(0, 255);


                            @Override
                            public void execute(CycleEventContainer container) {
                                if (npc.isDead) {
                                    container.stop();
                                    return;
                                }
                                if (player == null) {
                                    container.stop();
                                    return;
                                }
                                if (player.isDead()) {
                                    container.stop();
                                    return;
                                }
                                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                    player.resetInfernoInstance();
                                    player.infernoWaveId = 0;
                                    player.infernoWaveType = 0;
                                    player.wall1Alive = 0;
                                    player.wall1Hp = 0;
                                    player.wall2Alive = 0;
                                    player.wall2Hp = 0;
                                    player.wall3Alive = 0;
                                    player.wall3Hp = 0;
                                    container.stop();
                                    return;
                                }
                                if (ticks == 0) {
                                    if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                        player.resetInfernoInstance();
                                        player.infernoWaveId = 0;
                                        player.infernoWaveType = 0;
                                        player.wall1Alive = 0;
                                        player.wall1Hp = 0;
                                        player.wall2Alive = 0;
                                        player.wall2Hp = 0;
                                        player.wall3Alive = 0;
                                        player.wall3Hp = 0;
                                        container.stop();
                                        return;
                                    }
                                    if (npc.isDead) {
                                        container.stop();
                                        return;
                                    }
                                    if (player == null) {
                                        container.stop();
                                        return;
                                    }
                                    if (player.isDead()) {
                                        container.stop();
                                        return;
                                    }
                                    int nX = npc.getX();
                                    int nY = npc.getY();
                                    int pX = player.getX();
                                    int pY = player.getY();
                                    int offX = (nX - pX) * -1;
                                    int offY = (nY - pY) * -1;
                                    int centerX = nX + npc.getSize() / 2;
                                    int centerY = nY + npc.getSize() / 2;

                                    int speed = 130;
                                    int startHeight = 110;
                                    int endHeight = 31;
                                    int delay = 0;
                                    player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1375, startHeight, endHeight, -player.getIndex() - 1, 65, delay);
                                }
                                if (ticks == 3) {
                                    if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                        player.resetInfernoInstance();
                                        player.infernoWaveId = 0;
                                        player.infernoWaveType = 0;
                                        player.wall1Alive = 0;
                                        player.wall1Hp = 0;
                                        player.wall2Alive = 0;
                                        player.wall2Hp = 0;
                                        player.wall3Alive = 0;
                                        player.wall3Hp = 0;
                                        container.stop();
                                        return;
                                    }
                                    if (npc.isDead) {
                                        container.stop();
                                        return;
                                    }
                                    if (player == null) {
                                        container.stop();
                                        return;
                                    }
                                    if (player.isDead()) {
                                        container.stop();
                                        return;
                                    }
                                    if (Misc.random(500) + 200 > Misc.random(player.getCombat().mageDef())) {
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    } else {
                                        player.appendDamage(0, Hitmark.MISS);
                                    }
                                    container.stop();
                                }
                                ticks++;
                            }
                        }, 1);
                    } else {
                        if (npc.isDead) {
                            container.stop();
                            return;
                        }
                        if (player.getInferno().glyphNPC.isDead) {
                            container.stop();
                            return;
                        }
                        if (!Boundary.isIn(player, Boundary.INFERNO)) {
                            player.resetInfernoInstance();
                            player.infernoWaveId = 0;
                            player.infernoWaveType = 0;
                            player.wall1Alive = 0;
                            player.wall1Hp = 0;
                            player.wall2Alive = 0;
                            player.wall2Hp = 0;
                            player.wall3Alive = 0;
                            player.wall3Hp = 0;
                            container.stop();
                            return;
                        }
                        if (player == null) {
                            container.stop();
                            return;
                        }
                        if (player.isDead()) {
                            container.stop();
                            return;
                        }
                        int nX = npc.getX();
                        int nY = npc.getY();
                        int pX = player.getInferno().glyphNPC.getX();
                        int pY = player.getInferno().glyphNPC.getY();
                        int offX = (nX - pX) * -1;
                        int offY = (nY - pY) * -1;
                        int centerX = nX + npc.getSize() / 2;
                        int centerY = nY + npc.getSize() / 2;

                        int speed = 130;
                        int startHeight = 110;
                        int endHeight = 31;
                        int delay = 0;
                        player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1375, startHeight, endHeight, player.getInferno().glyphNPC.getIndex() + 1,
                                65, delay);
                    }
                }
            }
        }, 1);
    }

    private void spawnHealers() {
        final NPC npc = NPCHandler.getNpc(InfernoWave.TZKAL_ZUK, height);
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            NPC healer1;
            NPC healer2;
            NPC healer3;
            NPC healer4;
            int tick = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (npc.isDead) {
                    container.stop();
                    return;
                }
                if (player == null) {
                    container.stop();
                    return;
                }
                if (player.isDead()) {
                    container.stop();
                    return;
                }
                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                    player.resetInfernoInstance();
                    player.infernoWaveId = 0;
                    player.infernoWaveType = 0;
                    player.wall1Alive = 0;
                    player.wall1Hp = 0;
                    player.wall2Alive = 0;
                    player.wall2Hp = 0;
                    player.wall3Alive = 0;
                    player.wall3Hp = 0;
                    container.stop();
                    return;
                }
                if (tick == 0) {
                    healer1 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_MEJJAK, 2281, 5363, height, 0, InfernoWave.getHp(InfernoWave.JAL_MEJJAK), InfernoWave.getMax(InfernoWave.JAL_MEJJAK), InfernoWave.getAtk(InfernoWave.JAL_MEJJAK), InfernoWave.getDef(InfernoWave.JAL_MEJJAK), false, false);
                    healer2 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_MEJJAK, 2275, 5363, height, 0, InfernoWave.getHp(InfernoWave.JAL_MEJJAK), InfernoWave.getMax(InfernoWave.JAL_MEJJAK), InfernoWave.getAtk(InfernoWave.JAL_MEJJAK), InfernoWave.getDef(InfernoWave.JAL_MEJJAK), false, false);
                    healer3 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_MEJJAK, 2267, 5363, height, 0, InfernoWave.getHp(InfernoWave.JAL_MEJJAK), InfernoWave.getMax(InfernoWave.JAL_MEJJAK), InfernoWave.getAtk(InfernoWave.JAL_MEJJAK), InfernoWave.getDef(InfernoWave.JAL_MEJJAK), false, false);
                    healer4 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_MEJJAK, 2261, 5363, height, 0, InfernoWave.getHp(InfernoWave.JAL_MEJJAK), InfernoWave.getMax(InfernoWave.JAL_MEJJAK), InfernoWave.getAtk(InfernoWave.JAL_MEJJAK), InfernoWave.getDef(InfernoWave.JAL_MEJJAK), false, false);
                    player.getInferno().kill.add(healer1);
                    player.getInferno().kill.add(healer2);
                    player.getInferno().kill.add(healer3);
                    player.getInferno().kill.add(healer4);
                }
                if (healer1 != null && !healer1.isDead) {
                    faceNpc(healer1, npc, container);
                }
                if (healer2 != null && !healer2.isDead) {
                    faceNpc(healer2, npc, container);
                }
                if (healer3 != null && !healer3.isDead) {
                    faceNpc(healer3, npc, container);
                }
                if (healer4 != null && !healer4.isDead) {
                    faceNpc(healer4, npc, container);
                }

                if (tick % 6 == 0) {
                    if (healer1 != null && !healer1.isDead) {
                        heal(healer1, npc, container);
                    }
                    if (healer2 != null && !healer2.isDead) {
                        heal(healer2, npc, container);
                    }
                    if (healer3 != null && !healer3.isDead) {
                        heal(healer3, npc, container);
                    }
                    if (healer4 != null && !healer4.isDead) {
                        heal(healer4, npc, container);
                    }
                }
                if (healer1 == null && healer2 == null && healer3 == null && healer4 == null) {
                    container.stop();
                    return;
                }
                if (healer1 != null && healer2 != null && healer3 != null && healer4 != null) {
                    if (healer1.isDead && healer2.isDead && healer3.isDead && healer4.isDead) {
                        container.stop();
                        return;
                    }
                }
                tick++;
            }

            private void faceNpc(NPC npc, NPC turnTo, CycleEventContainer container) {
                if (npc == null) {
                    container.stop();
                    return;
                }
                if (turnTo == null) {
                    container.stop();
                    return;
                }
                if (npc.isDead) {
                    container.stop();
                    return;
                }
                if (turnTo.isDead) {
                    container.stop();
                    return;
                }
                if (npc != null) {
                    if (!npc.isDead) {
                        npc.turnNpc(turnTo.getX(), turnTo.getY());
                    }
                }
            }

            private void heal(NPC npc, NPC heal, CycleEventContainer container) {
                if (npc == null) {
                    container.stop();
                    return;
                }
                if (heal == null) {
                    container.stop();
                    return;
                }
                if (npc.isDead) {
                    container.stop();
                    return;
                }
                if (heal.isDead) {
                    container.stop();
                    return;
                }
                npc.startAnimation(2868);
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                    int tick = 0;

                    @Override
                    public void execute(CycleEventContainer container) {
                        if (npc == null) {
                            container.stop();
                            return;
                        }
                        if (heal == null) {
                            container.stop();
                            return;
                        }
                        if (npc.isDead) {
                            container.stop();
                            return;
                        }
                        if (heal.isDead) {
                            container.stop();
                            return;
                        }
                        if (tick == 0) {
                            if (npc == null) {
                                container.stop();
                                return;
                            }
                            if (heal == null) {
                                container.stop();
                                return;
                            }
                            if (npc.isDead) {
                                container.stop();
                                return;
                            }
                            if (heal.isDead) {
                                container.stop();
                                return;
                            }
                            int nX = npc.getX();
                            int nY = npc.getY();
                            int pX = player.getInferno().glyphNPC.getX();
                            int pY = player.getInferno().glyphNPC.getY();
                            int offX = (nX - pX) * -1;
                            int offY = (nY - pY) * -1;
                            int centerX = nX + npc.getSize() / 2;
                            int centerY = nY + npc.getSize() / 2;

                            int speed = 85;
                            int startHeight = 43;
                            int endHeight = 31;
                            int delay = 0;
                            player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 660, startHeight, endHeight, heal.getIndex() + 1,
                                    65, delay);
                        }

                        if (tick == 2) {
                            if (npc == null) {
                                container.stop();
                                return;
                            }
                            if (heal == null) {
                                container.stop();
                                return;
                            }
                            if (npc.isDead) {
                                container.stop();
                                return;
                            }
                            if (heal.isDead) {
                                container.stop();
                                return;
                            }
                            if (heal.getHealth().getAmount() < heal.getHealth().getMaximum()) {
                                heal.getHealth().increase(25);
                            }
                            container.stop();
                        }
                        tick++;
                    }
                }, 1);
            }
        }, 1);
    }

    private void spawnJad() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            NPC jad;
            int tick = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (tick == 0) {
                    jad = Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD, 2270, 5348, height, 0, InfernoWave.getHp(InfernoWave.JALTOK_JAD), InfernoWave.getMax(InfernoWave.JALTOK_JAD), InfernoWave.getAtk(InfernoWave.JALTOK_JAD), InfernoWave.getDef(InfernoWave.JALTOK_JAD), false, false);
                    player.getInferno().kill.add(jad);
                }
                if (player == null) {
                    container.stop();
                    return;
                }
                if (player.getInferno().zukDead) {
                    container.stop();
                    return;
                }
                if (player.isDead()) {
                    container.stop();
                    return;
                }
                if (jad != null) {
                    if (!jad.isDead) {
                        if (jad.lastDamageTaken > 0 || player.getInferno().glyphNPC.isDead) {
                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                player.resetInfernoInstance();
                                player.infernoWaveId = 0;
                                player.infernoWaveType = 0;
                                player.wall1Alive = 0;
                                player.wall1Hp = 0;
                                player.wall2Alive = 0;
                                player.wall2Hp = 0;
                                player.wall3Alive = 0;
                                player.wall3Hp = 0;
                                container.stop();
                                return;
                            }
                            if (player == null) {
                                container.stop();
                                return;
                            }
                            if (player.isDead()) {
                                container.stop();
                                return;
                            }
                            if (player.getInferno().zukDead) {
                                container.stop();
                                return;
                            }
                            jad.facePlayer(player.getIndex());
                        } else {
                            jad.turnNpc(player.getInferno().glyphNPC.getX(), player.getInferno().glyphNPC.getY());
                        }
                    }
                }
                final int type = Misc.random(0, 1);
                if (tick % 10 == 0 && tick != 0) {
                    if (jad != null) {
                        if (!jad.isDead) {
                            if (type == 0) {
                                jad.startAnimation(7593);
                            } else {
                                jad.startAnimation(7592);
                            }
                        }
                    }
                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        int tick = 0;
                        int amount = Misc.random(0, 113);

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (tick == 0) {
                                if (jad != null) {
                                    if (!jad.isDead) {
                                        if (jad.lastDamageTaken > 0 || player.getInferno().glyphNPC.isDead) {
                                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                                player.resetInfernoInstance();
                                                player.infernoWaveId = 0;
                                                player.infernoWaveType = 0;
                                                player.wall1Alive = 0;
                                                player.wall1Hp = 0;
                                                player.wall2Alive = 0;
                                                player.wall2Hp = 0;
                                                player.wall3Alive = 0;
                                                player.wall3Hp = 0;
                                                container.stop();
                                                return;
                                            }
                                            if (player == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.isDead()) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            int nX = jad.getX();
                                            int nY = jad.getY();
                                            int pX = player.getX();
                                            int pY = player.getY();
                                            int offX = (nX - pX) * -1;
                                            int offY = (nY - pY) * -1;
                                            int centerX = nX + jad.getSize() / 2;
                                            int centerY = nY + jad.getSize() / 2;

                                            int speed = 130;
                                            int startHeight = 110;
                                            int endHeight = 31;
                                            int delay = 0;
                                            if (type == 0) {
                                                player.gfx100(451);
                                                if (player.protectingRange()) {
                                                    amount = 0;
                                                }
                                            } else {
                                                if (player.protectingMagic()) {
                                                    amount = 0;
                                                }
                                                player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 448, startHeight, endHeight, -player.getIndex() - 1, 65, delay);
                                            }
                                        } else {
                                            if (player.getInferno().glyphNPC == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().glyphNPC.isDead) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            int nX = jad.getX();
                                            int nY = jad.getY();
                                            int pX = player.getInferno().glyphNPC.getX();
                                            int pY = player.getInferno().glyphNPC.getY();
                                            int offX = (nX - pX) * -1;
                                            int offY = (nY - pY) * -1;
                                            int centerX = nX + jad.getSize() / 2;
                                            int centerY = nY + jad.getSize() / 2;

                                            int speed = 130;
                                            int startHeight = 110;
                                            int endHeight = 31;
                                            int delay = 0;
                                            if (type == 0) {
                                                player.getInferno().glyphNPC.gfx100(451);
                                            } else {
                                                player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 448, startHeight, endHeight, player.getInferno().glyphNPC.getIndex() + 1,
                                                        65, delay);
                                            }

                                        }
                                    }
                                }
                            }

                            if (tick == 1 || tick == 2) {
                                if (type == 0) {
                                    if (player.protectingRange()) {
                                        amount = 0;
                                    }
                                } else {
                                    if (player.protectingMagic()) {
                                        amount = 0;
                                    }
                                }
                            }

                            if (tick == 3) {
                                if (jad != null) {
                                    if (jad.isDead) {
                                        container.stop();
                                        return;
                                    }
                                }
                                if (jad == null) {
                                    container.stop();
                                    return;
                                }

                                if (player == null) {
                                    container.stop();
                                    return;
                                }
                                if (player.getInferno().zukDead) {
                                    container.stop();
                                    return;
                                }
                                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                    player.resetInfernoInstance();
                                    player.infernoWaveId = 0;
                                    player.infernoWaveType = 0;
                                    player.wall1Alive = 0;
                                    player.wall1Hp = 0;
                                    player.wall2Alive = 0;
                                    player.wall2Hp = 0;
                                    player.wall3Alive = 0;
                                    player.wall3Hp = 0;
                                    container.stop();
                                    return;
                                }
                                if (jad != null) {
                                    if (!jad.isDead) {
                                        if (jad.lastDamageTaken > 0 || player.getInferno().glyphNPC.isDead) {
                                            if (player == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.isDead()) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            if (type == 0) {
                                                if (player.protectingRange()) {
                                                    amount = 0;
                                                }
                                                if (Misc.random(500) + 200 > Misc.random(player.getCombat().calculateRangeDefence())) {
                                                    if (amount == 0) {
                                                        player.appendDamage(amount, Hitmark.MISS);
                                                    } else {
                                                        player.appendDamage(amount, Hitmark.HIT);
                                                    }
                                                } else {
                                                    player.appendDamage(0, Hitmark.MISS);
                                                }
                                            } else {
                                                if (player.protectingMagic()) {
                                                    amount = 0;
                                                }
                                                if (Misc.random(500) + 200 > Misc.random(player.getCombat().mageDef())) {
                                                    if (amount == 0) {
                                                        player.appendDamage(amount, Hitmark.MISS);
                                                    } else {
                                                        player.appendDamage(amount, Hitmark.HIT);
                                                    }
                                                } else {
                                                    player.appendDamage(0, Hitmark.MISS);
                                                }

                                            }
                                        } else {
                                            if (player.getInferno().glyphNPC == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().glyphNPC.isDead) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            amount = Misc.random(0, 113);
                                            if (amount == 0) {
                                                player.getInferno().glyphNPC.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.getInferno().glyphNPC.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                                container.stop();
                            }

                            if (jad != null) {
                                if (jad.isDead) {
                                    container.stop();
                                    return;
                                }
                            }
                            if (jad == null) {
                                container.stop();
                                return;
                            }

                            if (player == null) {
                                container.stop();
                                return;
                            }

                            if (player.isDead()) {
                                container.stop();
                                return;
                            }
                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                player.resetInfernoInstance();
                                player.infernoWaveId = 0;
                                player.infernoWaveType = 0;
                                player.wall1Alive = 0;
                                player.wall1Hp = 0;
                                player.wall2Alive = 0;
                                player.wall2Hp = 0;
                                player.wall3Alive = 0;
                                player.wall3Hp = 0;
                                container.stop();
                                return;
                            }

                            if (player.getInferno().zukDead) {
                                container.stop();
                                return;
                            }

                            tick++;
                        }
                    }, 1);
                }
                tick++;
            }
        }, 1);
    }

    private void spawnRangerAndMage() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            NPC ranger;
            NPC mager;
            int tick = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (tick == 0) {
                    ranger = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_XIL, 2278, 5351, height, 0, InfernoWave.getHp(InfernoWave.JAL_XIL), InfernoWave.getMax(InfernoWave.JAL_XIL), InfernoWave.getAtk(InfernoWave.JAL_XIL), InfernoWave.getDef(InfernoWave.JAL_XIL), false, false);
                    mager = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_ZEK, 2264, 5351, height, 0, InfernoWave.getHp(InfernoWave.JAL_ZEK), InfernoWave.getMax(InfernoWave.JAL_ZEK), InfernoWave.getAtk(InfernoWave.JAL_ZEK), InfernoWave.getDef(InfernoWave.JAL_ZEK), false, false);
                    player.getInferno().kill.add(ranger);
                    player.getInferno().kill.add(mager);
                }
                if (player.getInferno().zukDead) {
                    container.stop();
                    return;
                }
                if (ranger != null) {
                    if (!ranger.isDead) {
                        if (ranger.lastDamageTaken > 0 || player.getInferno().glyphNPC.isDead) {
                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                player.resetInfernoInstance();
                                player.infernoWaveId = 0;
                                player.infernoWaveType = 0;
                                player.wall1Alive = 0;
                                player.wall1Hp = 0;
                                player.wall2Alive = 0;
                                player.wall2Hp = 0;
                                player.wall3Alive = 0;
                                player.wall3Hp = 0;
                                container.stop();
                                return;
                            }
                            if (player == null) {
                                container.stop();
                                return;
                            }
                            if (player.isDead()) {
                                container.stop();
                                return;
                            }
                            if (player.getInferno().zukDead) {
                                container.stop();
                                return;
                            }
                            ranger.facePlayer(player.getIndex());
                        } else {
                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                player.resetInfernoInstance();
                                player.infernoWaveId = 0;
                                player.infernoWaveType = 0;
                                player.wall1Alive = 0;
                                player.wall1Hp = 0;
                                player.wall2Alive = 0;
                                player.wall2Hp = 0;
                                player.wall3Alive = 0;
                                player.wall3Hp = 0;
                                container.stop();
                                return;
                            }
                            if (player == null) {
                                container.stop();
                                return;
                            }
                            if (player.getInferno().glyphNPC == null) {
                                container.stop();
                                return;
                            }
                            if (player.getInferno().glyphNPC.isDead) {
                                container.stop();
                                return;
                            }
                            if (player.getInferno().zukDead) {
                                container.stop();
                                return;
                            }
                            ranger.turnNpc(player.getInferno().glyphNPC.getX(), player.getInferno().glyphNPC.getY());
                        }
                    }
                }
                if (mager != null) {
                    if (!mager.isDead) {
                        if (mager.lastDamageTaken > 0 || player.getInferno().glyphNPC.isDead) {
                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                player.resetInfernoInstance();
                                player.infernoWaveId = 0;
                                player.infernoWaveType = 0;
                                player.wall1Alive = 0;
                                player.wall1Hp = 0;
                                player.wall2Alive = 0;
                                player.wall2Hp = 0;
                                player.wall3Alive = 0;
                                player.wall3Hp = 0;
                                container.stop();
                                return;
                            }
                            if (player == null) {
                                container.stop();
                                return;
                            }
                            if (player.isDead()) {
                                container.stop();
                                return;
                            }
                            if (player.getInferno().zukDead) {
                                container.stop();
                                return;
                            }
                            mager.facePlayer(player.getIndex());
                        } else {
                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                player.resetInfernoInstance();
                                player.infernoWaveId = 0;
                                player.infernoWaveType = 0;
                                player.wall1Alive = 0;
                                player.wall1Hp = 0;
                                player.wall2Alive = 0;
                                player.wall2Hp = 0;
                                player.wall3Alive = 0;
                                player.wall3Hp = 0;
                                container.stop();
                                return;
                            }
                            if (player == null) {
                                container.stop();
                                return;
                            }
                            if (player.getInferno().glyphNPC == null) {
                                container.stop();
                                return;
                            }
                            if (player.getInferno().glyphNPC.isDead) {
                                container.stop();
                                return;
                            }
                            if (player.getInferno().zukDead) {
                                container.stop();
                                return;
                            }
                            mager.turnNpc(player.getInferno().glyphNPC.getX(), player.getInferno().glyphNPC.getY());
                        }
                    }
                }
                if (tick % 8 == 0) {
                    if (mager != null) {
                        if (!mager.isDead) {
                            mager.startAnimation(7610);
                        }
                    }
                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        int tick = 0;
                        int amount = Misc.random(0, 70);

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (tick == 0) {
                                if (mager != null) {
                                    if (!mager.isDead) {
                                        if (mager.lastDamageTaken > 0 || player.getInferno().glyphNPC.isDead) {
                                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                                player.resetInfernoInstance();
                                                player.infernoWaveId = 0;
                                                player.infernoWaveType = 0;
                                                player.wall1Alive = 0;
                                                player.wall1Hp = 0;
                                                player.wall2Alive = 0;
                                                player.wall2Hp = 0;
                                                player.wall3Alive = 0;
                                                player.wall3Hp = 0;
                                                container.stop();
                                                return;
                                            }
                                            if (player == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.isDead()) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            int nX = mager.getX();
                                            int nY = mager.getY();
                                            int pX = player.getX();
                                            int pY = player.getY();
                                            int offX = (nX - pX) * -1;
                                            int offY = (nY - pY) * -1;
                                            int centerX = nX + mager.getSize() / 2;
                                            int centerY = nY + mager.getSize() / 2;

                                            int speed = 85;
                                            int startHeight = 43;
                                            int endHeight = 31;
                                            int delay = 0;
                                            if (player.protectingMagic()) {
                                                amount = 0;
                                            }
                                            player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1378, startHeight, endHeight, -player.getIndex() - 1, 65, delay);
                                        } else {
                                            if (player.getInferno().glyphNPC == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().glyphNPC.isDead) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            int nX = mager.getX();
                                            int nY = mager.getY();
                                            int pX = player.getInferno().glyphNPC.getX();
                                            int pY = player.getInferno().glyphNPC.getY();
                                            int offX = (nX - pX) * -1;
                                            int offY = (nY - pY) * -1;
                                            int centerX = nX + mager.getSize() / 2;
                                            int centerY = nY + mager.getSize() / 2;

                                            int speed = 85;
                                            int startHeight = 43;
                                            int endHeight = 31;
                                            int delay = 0;
                                            player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1378, startHeight, endHeight, player.getInferno().glyphNPC.getIndex() + 1,
                                                    65, delay);
                                        }
                                    }
                                }
                            }

                            if (tick == 1) {
                                if (player.protectingMagic()) {
                                    amount = 0;
                                }
                            }

                            if (tick == 2) {
                                if (mager != null) {
                                    if (mager.isDead) {
                                        container.stop();
                                        return;
                                    }
                                }
                                if (mager == null) {
                                    container.stop();
                                    return;
                                }

                                if (player == null) {
                                    container.stop();
                                    return;
                                }
                                if (player.getInferno().zukDead) {
                                    container.stop();
                                    return;
                                }
                                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                    player.resetInfernoInstance();
                                    player.infernoWaveId = 0;
                                    player.infernoWaveType = 0;
                                    player.wall1Alive = 0;
                                    player.wall1Hp = 0;
                                    player.wall2Alive = 0;
                                    player.wall2Hp = 0;
                                    player.wall3Alive = 0;
                                    player.wall3Hp = 0;
                                    container.stop();
                                    return;
                                }
                                if (mager != null) {
                                    if (!mager.isDead) {
                                        if (mager.lastDamageTaken > 0 || player.getInferno().glyphNPC.isDead) {
                                            if (player == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.isDead()) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.protectingMagic()) {
                                                amount = 0;
                                            }
                                            if (Misc.random(500) + 200 > Misc.random(player.getCombat().mageDef())) {
                                                if (amount == 0) {
                                                    player.appendDamage(amount, Hitmark.MISS);
                                                } else {
                                                    player.appendDamage(amount, Hitmark.HIT);
                                                }
                                            } else {
                                                player.appendDamage(0, Hitmark.MISS);
                                            }
                                        } else {
                                            if (player.getInferno().glyphNPC == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().glyphNPC.isDead) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            amount = Misc.random(0, 70);
                                            if (amount == 0) {
                                                player.getInferno().glyphNPC.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.getInferno().glyphNPC.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                                container.stop();
                            }

                            if (mager != null) {
                                if (mager.isDead) {
                                    container.stop();
                                    return;
                                }
                            }
                            if (mager == null) {
                                container.stop();
                                return;
                            }

                            if (player == null) {
                                container.stop();
                                return;
                            }

                            if (player.isDead()) {
                                container.stop();
                                return;
                            }

                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                player.resetInfernoInstance();
                                player.infernoWaveId = 0;
                                player.infernoWaveType = 0;
                                player.wall1Alive = 0;
                                player.wall1Hp = 0;
                                player.wall2Alive = 0;
                                player.wall2Hp = 0;
                                player.wall3Alive = 0;
                                player.wall3Hp = 0;
                                container.stop();
                                return;
                            }

                            tick++;
                        }
                    }, 1);
                }
                if (tick % 6 == 0) {
                    if (ranger != null) {
                        if (!ranger.isDead) {
                            ranger.startAnimation(7605);
                        }
                    }
                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        int tick = 0;
                        int amount = Misc.random(0, 70);

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (tick == 0) {
                                if (ranger != null) {
                                    if (!ranger.isDead) {
                                        if (ranger.lastDamageTaken > 0 || player.getInferno().glyphNPC.isDead) {
                                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                                player.resetInfernoInstance();
                                                player.infernoWaveId = 0;
                                                player.infernoWaveType = 0;
                                                player.wall1Alive = 0;
                                                player.wall1Hp = 0;
                                                player.wall2Alive = 0;
                                                player.wall2Hp = 0;
                                                player.wall3Alive = 0;
                                                player.wall3Hp = 0;
                                                container.stop();
                                                return;
                                            }
                                            if (player == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.isDead()) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            int nX = ranger.getX();
                                            int nY = ranger.getY();
                                            int pX = player.getX();
                                            int pY = player.getY();
                                            int offX = (nX - pX) * -1;
                                            int offY = (nY - pY) * -1;
                                            int centerX = nX + ranger.getSize() / 2;
                                            int centerY = nY + ranger.getSize() / 2;

                                            int speed = 85;
                                            int startHeight = 43;
                                            int endHeight = 31;
                                            int delay = 0;
                                            if (player.protectingRange()) {
                                                amount = 0;
                                            }
                                            player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1376, startHeight, endHeight, -player.getIndex() - 1, 65, delay);
                                        } else {
                                            if (player.getInferno().glyphNPC == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().glyphNPC.isDead) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            int nX = ranger.getX();
                                            int nY = ranger.getY();
                                            int pX = player.getInferno().glyphNPC.getX();
                                            int pY = player.getInferno().glyphNPC.getY();
                                            int offX = (nX - pX) * -1;
                                            int offY = (nY - pY) * -1;
                                            int centerX = nX + ranger.getSize() / 2;
                                            int centerY = nY + ranger.getSize() / 2;

                                            int speed = 85;
                                            int startHeight = 43;
                                            int endHeight = 31;
                                            int delay = 0;
                                            player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1376, startHeight, endHeight, player.getInferno().glyphNPC.getIndex() + 1,
                                                    65, delay);
                                        }
                                    }
                                }
                            }

                            if (tick == 1) {
                                if (player.protectingRange()) {
                                    amount = 0;
                                }
                            }

                            if (tick == 2) {
                                if (ranger != null) {
                                    if (ranger.isDead) {
                                        container.stop();
                                        return;
                                    }
                                }
                                if (ranger == null) {
                                    container.stop();
                                    return;
                                }

                                if (player == null) {
                                    container.stop();
                                    return;
                                }
                                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                    player.resetInfernoInstance();
                                    player.infernoWaveId = 0;
                                    player.infernoWaveType = 0;
                                    player.wall1Alive = 0;
                                    player.wall1Hp = 0;
                                    player.wall2Alive = 0;
                                    player.wall2Hp = 0;
                                    player.wall3Alive = 0;
                                    player.wall3Hp = 0;
                                    container.stop();
                                    return;
                                }
                                if (ranger != null) {
                                    if (!ranger.isDead) {
                                        if (ranger.lastDamageTaken > 0 || player.getInferno().glyphNPC.isDead) {
                                            if (player == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.isDead()) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.protectingRange()) {
                                                amount = 0;
                                            }

                                            if (Misc.random(500) + 200 > Misc.random(player.getCombat().mageDef())) {
                                                if (amount == 0) {
                                                    player.appendDamage(amount, Hitmark.MISS);
                                                } else {
                                                    player.appendDamage(amount, Hitmark.HIT);
                                                }
                                            } else {
                                                player.appendDamage(0, Hitmark.MISS);
                                            }
                                        } else {
                                            if (player.getInferno().glyphNPC == null) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().glyphNPC.isDead) {
                                                container.stop();
                                                return;
                                            }
                                            if (player.getInferno().zukDead) {
                                                container.stop();
                                                return;
                                            }
                                            amount = Misc.random(0, 70);
                                            if (amount == 0) {
                                                player.getInferno().glyphNPC.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.getInferno().glyphNPC.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                                container.stop();
                            }

                            if (ranger != null) {
                                if (ranger.isDead) {
                                    container.stop();
                                    return;
                                }
                            }
                            if (ranger == null) {
                                container.stop();
                                return;
                            }

                            if (player == null) {
                                container.stop();
                                return;
                            }
                            if (player.isDead()) {
                                container.stop();
                                return;
                            }
                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                player.resetInfernoInstance();
                                player.infernoWaveId = 0;
                                player.infernoWaveType = 0;
                                player.wall1Alive = 0;
                                player.wall1Hp = 0;
                                player.wall2Alive = 0;
                                player.wall2Hp = 0;
                                player.wall3Alive = 0;
                                player.wall3Hp = 0;
                                container.stop();
                                return;
                            }

                            tick++;
                        }
                    }, 1);
                }
                if (mager != null && ranger != null) {
                    if (mager.isDead && ranger.isDead) {
                        container.stop();
                        return;
                    }
                }
                if (mager == null && ranger == null) {
                    container.stop();
                    return;
                }

                if (player == null) {
                    container.stop();
                    return;
                }
                if (player.isDead()) {
                    container.stop();
                    return;
                }
                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                    player.resetInfernoInstance();
                    player.infernoWaveId = 0;
                    player.infernoWaveType = 0;
                    player.wall1Alive = 0;
                    player.wall1Hp = 0;
                    player.wall2Alive = 0;
                    player.wall2Hp = 0;
                    player.wall3Alive = 0;
                    player.wall3Hp = 0;
                    container.stop();
                    return;
                }
                tick++;
            }
        }, 1);
    }

    public void spawnNpcs() {
        Server.npcHandler.spawnNpc(player, InfernoWave.TZKAL_ZUK, SPAWN_X, SPAWN_Y, height, 0, 1200, 251, 1000, 1000, false, false);
    }


    public void initiateTzkalzuk() {
        player.getInferno().started = true;
        System.out.println("1");
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (player == null) {
                    container.stop();
                    System.out.println("2");

                    return;
                }
                System.out.println("3");

                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                    player.resetInfernoInstance();
                    player.infernoWaveId = 0;
                    player.infernoWaveType = 0;
                    player.wall1Alive = 0;
                    player.wall1Hp = 0;
                    player.wall2Alive = 0;
                    player.wall2Hp = 0;
                    player.wall3Alive = 0;
                    player.wall3Hp = 0;
                    container.stop();
                    System.out.println("4");

                    return;
                }
                int cycle = container.getTotalTicks();
                if (cycle == 1) {//TODO FIX CAMERA ANGLE SOMTIMES? (MAYBE)
                    player.getInferno().cutsceneWalkBlock = true;
                    player.getPA().sendScreenFade("TzKal-Zuk instance loading...", -1, 5);
                    player.getPA().movePlayer(START_X, START_Y, height + 1);
                    System.out.println("5");

                } else if (cycle == 2) {
                    player.turnPlayerTo(START_X, SPAWN_Y);
                } else if (cycle == 3) {
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2267, 5368, (height + 1), 0, 10, -1, -1));  // Delete ceiling
                    player.getPA().movePlayer(START_X, START_Y, height);
                    player.getInferno().glyphNPC = Server.npcHandler.spawnNpc(player, InfernoWave.ANCESTRAL_GLYPH, GLYPH_SPAWN_X, GLYPH_SPAWN_Y, height, 0, 600, 38, 500, 700, false, false);
                } else if (cycle == 4) {
                    Server.getGlobalObjects().add(new GlobalObject(30342, 2267, 5366, height, 1, 10, -1, -1));  // West Wall
                    Server.getGlobalObjects().add(new GlobalObject(30341, 2275, 5366, height, 3, 10, -1, -1));  // East Wall
                    Server.getGlobalObjects().add(new GlobalObject(30340, 2267, 5364, height, 1, 10, -1, -1));  // West Corner
                    Server.getGlobalObjects().add(new GlobalObject(30339, 2275, 5364, height, 3, 10, -1, -1));  // East Corner
                    Server.getGlobalObjects().add(new GlobalObject(30344, 2268, 5364, height, 3, 10, -1, -1)); // Set falling rocks - west
                    Server.getGlobalObjects().add(new GlobalObject(30343, 2273, 5364, height, 3, 10, -1, -1)); // Set falling rocks - east
                    deleteGlyph();
                    player.getPA().stillCamera(2271, 5365, 2, 0, 10);
                } else if (cycle == 10) {
                    player.getPA().sendPlayerObjectAnimation(player, 2268, 5364, 7560, 10, 3, height); // Set falling rocks animation - west
                    player.getPA().sendPlayerObjectAnimation(player, 2273, 5364, 7559, 10, 3, height); // Set falling rocks animation - east
                    //player.getPA().sendPlayerObjectAnimation(player, 2270, 5363, 7560, 10, 3, height); // Set falling rocks animation - middle
                    spawnNpcs();
                } else if (cycle >= 14) {
                    player.getPA().resetCamera();
                    player.getInferno().cutsceneWalkBlock = false;
                    player.getInferno().glyphCanMove = true;
                    tzkalzukSpecials();
                    container.stop();
                    System.out.println("6");

                }

                System.out.println("7");

            }
        }, 1);
    }

    private void deleteGlyph() {
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5363, height, 3, 10, -1, -1)); // Delete ancestral glyph
        /*Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5363, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5363, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5363, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5362, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5362, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5362, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5362, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5363, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5363, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5363, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5363, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5363, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5363, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5363, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5363, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5362, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5362, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5362, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5362, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5363, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5363, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5363, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5363, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5363, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5363, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5363, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5363, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5362, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5362, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5362, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5362, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5363, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5363, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5363, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5363, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5363, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5363, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5363, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5363, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5362, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5362, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5362, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5362, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5363, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5363, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 3, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 2, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 1, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2269, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2271, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph
        Server.getGlobalObjects().add(new GlobalObject(-1, 2272, 5364, height, 0, 10, -1, -1)); // Delete ancestral glyph*/
    }

    /**
     * Disposes of the content by moving the player and finalizing and or removing any left over content.
     *
     * @param dispose the type of dispose
     */
    public final void end(DisposeTypes dispose) {
        if (player == null) {
            return;
        }
        if (dispose == DisposeTypes.COMPLETE) {
            NPCHandler.kill(InfernoWave.TZKAL_ZUK, height);
            NPCHandler.kill(InfernoWave.ANCESTRAL_GLYPH, height);
            for (NPC kill : player.getInferno().kill) {
                if (kill != null) {
                    kill.isDead = true;
                }
            }
            player.getItems().addItemUnderAnyCircumstance(TOKKUL, (10000 * player.infernoWaveType) + Misc.random(5000));
            player.getPA().movePlayer(2497, 5116, 0);
            //PetHandler.receive(player, "TZKAL_ZUK"); //TODO
            player.getItems().addItemUnderAnyCircumstance(21295, 1);
    		CollectionLog log = new CollectionLog(); 
    		log.amount = 1; 
    		log.name = "inferno"; 
    		log.addLoot(new GameItem(21295));
    		player.collectionLog.addLog(log, CollectionLogType.MINIGAMES);
            PlayerHandler.executeGlobalMessage("@cr10@@red@" + Misc.capitalize(player.getName()) + " has defeated the Inferno.");
            try {
                PrintWriter writer = new PrintWriter("./Data/infernowins.txt", "UTF-8");
                writer.println(Misc.capitalize(player.getName()) + " has defeated the Inferno on: " + new Date().toString());
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.getInferno().zukDead = true;
        } else if (dispose == DisposeTypes.INCOMPLETE) {
            NPCHandler.kill(InfernoWave.TZKAL_ZUK, height);
            NPCHandler.kill(InfernoWave.ANCESTRAL_GLYPH, height);
            for (NPC kill : player.getInferno().kill) {
                if (kill != null) {
                    kill.isDead = true;
                }
            }
            player.getPA().movePlayer(2497, 5116, 0);
        }
    }

    @Override
    public void onDispose() {
        end(DisposeTypes.INCOMPLETE);
    }

    public int getHeight() {
        return height;
    }
}
