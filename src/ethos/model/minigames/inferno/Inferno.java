package ethos.model.minigames.inferno;


import java.util.ArrayList;
import java.util.List;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDumbPathFinder;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

/**
 * 
 * Written By @TutusFrutus 
 * Services: https://bit.ly/317ZqoS
 *
 */

public class Inferno extends Tzkalzuk {

    private int killsRemaining;

    private NPC wall1;

    private NPC wall2;

    private NPC wall3;

    private NPC nib1;

    private NPC nib2;

    private NPC nib3;

    private NPC nib4;

    private NPC nib5;

    private NPC nib6;

    private boolean started;

    public Inferno(Player player, Boundary boundary, int height) {
        super(player, boundary, height);

    }

    public void spawn() {
        final int[][] type = InfernoWave.LEVEL_WIKI;
        if (player.infernoWaveId >= type.length && player.infernoWaveType > 0 && Boundary.isIn(player, Boundary.INFERNO)) {
            if (player.getInferno().zukDead)
                stop();
            return;
        }
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer event) {
                if (player == null) {
                    event.stop();
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
                    event.stop();
                    return;
                }
                if (player.infernoWaveId >= type.length && player.infernoWaveType > 0) {
                    stop();
                    event.stop();
                    return;
                }
                if (player.infernoWaveId != 66 && player.infernoWaveId != 67 && player.infernoWaveId != 68) {
                    createWalls();
                }
                started = true;
                if (player.infernoWaveId != 0 && player.infernoWaveId < type.length)
                    player.sendMessage("@red@You are now on wave " + (player.infernoWaveId + 1) + " of " + type.length + ".", 255);
                if (player.infernoWaveId == 68) {
                    initiateTzkalzuk();
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height));
                    Server.getGlobalObjects().remove(30354, 2270, 5333, height);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height));
                    Server.getGlobalObjects().remove(30355, 2259, 5349, height);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height));
                    Server.getGlobalObjects().remove(30355, 2278, 5350, height);
                    if (wall1 != null) {
                        wall1.isDead = true;
                    }
                    if (wall2 != null) {
                        wall2.isDead = true;
                    }
                    if (wall3 != null) {
                        wall3.isDead = true;
                    }
                }
                if (player.infernoWaveId == 66) {
                    setKillsRemaining(1);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height));
                    Server.getGlobalObjects().remove(30354, 2270, 5333, height);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height));
                    Server.getGlobalObjects().remove(30355, 2259, 5349, height);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height));
                    Server.getGlobalObjects().remove(30355, 2278, 5350, height);
                    if (wall1 != null) {
                        wall1.isDead = true;
                    }
                    if (wall2 != null) {
                        wall2.isDead = true;
                    }
                    if (wall3 != null) {
                        wall3.isDead = true;
                    }
                    NPC jad = Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD, 2271 + Misc.random(-4, 4), 5342 + Misc.random(-4, 4), height, 1, InfernoWave.getHp(InfernoWave.JALTOK_JAD), InfernoWave.getMax(InfernoWave.JALTOK_JAD), InfernoWave.getAtk(InfernoWave.JALTOK_JAD), InfernoWave.getDef(InfernoWave.JALTOK_JAD), false, false, true);
                    jadCombat(jad);
                }
                if (player.infernoWaveId == 67) {
                    setKillsRemaining(3);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height));
                    Server.getGlobalObjects().remove(30354, 2270, 5333, height);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height));
                    Server.getGlobalObjects().remove(30355, 2259, 5349, height);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height));
                    Server.getGlobalObjects().remove(30355, 2278, 5350, height);
                    if (wall1 != null) {
                        wall1.isDead = true;
                    }
                    if (wall2 != null) {
                        wall2.isDead = true;
                    }
                    if (wall3 != null) {
                        wall3.isDead = true;
                    }
                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        NPC jad1;
                        NPC jad2;
                        NPC jad3;
                        int ticks = 0;

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (ticks == 0) {
                                jad1 = Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD, 2263, 5341, height, 1, InfernoWave.getHp(InfernoWave.JALTOK_JAD), InfernoWave.getMax(InfernoWave.JALTOK_JAD), InfernoWave.getAtk(InfernoWave.JALTOK_JAD), InfernoWave.getDef(InfernoWave.JALTOK_JAD), false, false, true);
                                jadCombat(jad1);
                            }
                            if (ticks == 2) {
                                jad2 = Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD, 2269, 5347, height, 1, InfernoWave.getHp(InfernoWave.JALTOK_JAD), InfernoWave.getMax(InfernoWave.JALTOK_JAD), InfernoWave.getAtk(InfernoWave.JALTOK_JAD), InfernoWave.getDef(InfernoWave.JALTOK_JAD), false, false, true);
                                jadCombat(jad2);
                            }
                            if (ticks == 4) {
                                jad3 = Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD, 2276, 5341, height, 1, InfernoWave.getHp(InfernoWave.JALTOK_JAD), InfernoWave.getMax(InfernoWave.JALTOK_JAD), InfernoWave.getAtk(InfernoWave.JALTOK_JAD), InfernoWave.getDef(InfernoWave.JALTOK_JAD), false, false, true);
                                jadCombat(jad3);
                                container.stop();
                            }
                            ticks++;
                        }
                    }, 1);
                }
                if (player.infernoWaveId < 66) {
                    final int count = type[player.infernoWaveId].length + 3;
                    final List<Integer> exceptions = new ArrayList<>();
                    exceptions.add(2);
                    exceptions.add(7);
                    exceptions.add(16);
                    exceptions.add(33);
                    if (exceptions.contains(player.infernoWaveId)) {
                        setKillsRemaining(count * 2);
                    } else {
                        setKillsRemaining(count);
                    }
                }

                if (player.infernoWaveId < 66) {
                    for (int i = 0; i < type[player.infernoWaveId].length; i++) {
                        final int npcType = type[player.infernoWaveId][i];
                        //int index = Misc.random(InfernoWave.SPAWN_DATA.length - 1);

                        //int random = Misc.random(10);

                        final int startX = 2271 + Misc.random(-4, 4); //InfernoWave.SPAWN_DATA[index][0]
                        final int startY = 5342 + Misc.random(-4, 4); // InfernoWave.SPAWN_DATA[index][1]

                        final int hp = InfernoWave.getHp(npcType);
                        final int maxhit = InfernoWave.getMax(npcType);
                        final int atk = InfernoWave.getAtk(npcType);
                        final int def = InfernoWave.getDef(npcType);


                        //NPCHandler.spawnNpc(npcType, x, y, height, 1, hp, maxhit, atk, def);
                        Server.npcHandler.spawnNpc(player, npcType, startX, startY, height, 1, hp, maxhit, atk, def, true, false, true);

                    }
                }
                event.stop();
            }

            @Override
            public void stop() {


            }
        }, 16);

        //Wave 3 is right? It is now, moved it to 3 and then wave 3 nothing spawned.
        if(player.infernoWaveId < 66) { //TODO EDITED 
        if (player.infernoWaveId == 2 || player.infernoWaveId == 7 || player.infernoWaveId == 16 || player.infernoWaveId == 33) {
        	System.out.println("SPECIAL WAVE STARTING: " + player.infernoWaveId);
        	//if (player.infernoWaveId == 2 || player.infernoWaveId == 8 || player.infernoWaveId == 17 || player.infernoWaveId == 34) {
           // System.out.println("wave2");
            CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                int ticks = 0;
                @Override
                public void execute(CycleEventContainer container) {
                    if (player == null) {
                        container.stop();
                        return;
                    }
                    
                    if (player.infernoWaveId >= type.length && player.infernoWaveType > 0) {
                        stop();
                        container.stop();
                        return;
                    }
                    
                    if (started) {
                        if (ticks == 0) {
                            nib1 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2270, 5342, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false, true);
                            nib2 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2269, 5343, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false, true);
                            nib3 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2271, 5343, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false, true);
                            nib4 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2270, 5342, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false, true);
                            nib5 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2269, 5343, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false, true);
                            nib6 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2271, 5343, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false, true);

                        }
                        if (wall1 != null) {
                            if (!wall1.isDead) {
                               // System.out.println("not nulled");
                                NPCDumbPathFinder.walkTowards(nib1, 2270, 5336);
                                NPCDumbPathFinder.walkTowards(nib2, 2271, 5336);
                                NPCDumbPathFinder.walkTowards(nib3, 2272, 5336);
                                NPCDumbPathFinder.walkTowards(nib4, 2272, 5336);
                                NPCDumbPathFinder.walkTowards(nib5, 2272, 5336);
                                NPCDumbPathFinder.walkTowards(nib6, 2272, 5336);


                                if (ticks % 4 == 0) {
                                    if (!nib1.isDead) {
                                        if (wall1.getDistance(nib1.getX(), nib1.getY()) <= 1) {
                                            nib1.turnNpc(wall1.getX(), wall1.getY());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib2.isDead) {
                                        if (wall1.getDistance(nib2.getX(), nib2.getY()) <= 1) {
                                            nib2.turnNpc(wall1.getX() + 1, wall1.getY());
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib3.isDead) {
                                        if (wall1.getDistance(nib3.getX(), nib3.getY()) <= 1) {
                                            nib3.turnNpc(wall1.getX() + 2, wall1.getY());
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib4.isDead) {
                                        if (wall1.getDistance(nib4.getX(), nib4.getY()) <= 1) {
                                            nib4.turnNpc(wall1.getX() + 2, wall1.getY());
                                            nib4.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib5.isDead) {
                                        if (wall1.getDistance(nib5.getX(), nib5.getY()) <= 1) {
                                            nib5.turnNpc(wall1.getX() + 2, wall1.getY());
                                            nib5.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }

                                    if (!nib6.isDead) {
                                        if (wall1.getDistance(nib6.getX(), nib6.getY()) <= 1) {
                                            nib6.turnNpc(wall1.getX() + 2, wall1.getY());
                                            nib6.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (wall2 != null) {
                            if (!wall2.isDead && wall1.isDead && !wall3.isDead) {
                                NPCDumbPathFinder.walkTowards(nib1, 2261, 5348);
                                NPCDumbPathFinder.walkTowards(nib2, 2260, 5348);
                                NPCDumbPathFinder.walkTowards(nib3, 2259, 5348);
                                if (ticks % 4 == 0) {
                                    if (!nib1.isDead) {
                                        if (wall2.getDistance(nib1.getX(), nib1.getY()) <= 1) {
                                            nib1.turnNpc(wall2.getX() + 2, wall2.getY());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall2Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib2.isDead) {
                                        if (wall2.getDistance(nib2.getX(), nib2.getY()) <= 1) {
                                            nib2.turnNpc(wall2.getX() + 1, wall2.getY());
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall2Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib3.isDead) {
                                        if (wall2.getDistance(nib3.getX(), nib3.getY()) <= 1) {
                                            nib3.turnNpc(wall2.getX(), wall2.getY());
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall2Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }

                                    if (!nib4.isDead) {
                                        if (wall2.getDistance(nib4.getX(), nib4.getY()) <= 1) {
                                            nib4.turnNpc(wall2.getX() + 2, wall2.getY());
                                            nib4.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib5.isDead) {
                                        if (wall2.getDistance(nib5.getX(), nib5.getY()) <= 1) {
                                            nib5.turnNpc(wall2.getX() + 2, wall2.getY());
                                            nib5.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }

                                    if (!nib6.isDead) {
                                        if (wall2.getDistance(nib6.getX(), nib6.getY()) <= 1) {
                                            nib6.turnNpc(wall2.getX() + 2, wall2.getY());
                                            nib6.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (wall3 != null) {
                            if (!wall3.isDead && wall1.isDead && wall2.isDead) {
                                NPCDumbPathFinder.walkTowards(nib1, 2277, 5350);
                                NPCDumbPathFinder.walkTowards(nib2, 2277, 5351);
                                NPCDumbPathFinder.walkTowards(nib3, 2277, 5352);
                                if (ticks % 4 == 0) {
                                    if (!nib1.isDead) {
                                        if (wall3.getDistance(nib1.getX(), nib1.getY()) <= 1) {
                                            nib1.turnNpc(wall3.getX(), wall3.getY());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall3Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib2.isDead) {
                                        if (wall3.getDistance(nib2.getX(), nib2.getY()) <= 1) {
                                            nib2.turnNpc(wall3.getX(), wall3.getY() + 1);
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall3Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib3.isDead) {
                                        if (wall3.getDistance(nib3.getX(), nib3.getY()) <= 1) {
                                            nib3.turnNpc(wall3.getX(), wall3.getY() + 2);
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall3Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }

                                    if (!nib4.isDead) {
                                        if (wall3.getDistance(nib4.getX(), nib4.getY()) <= 1) {
                                            nib4.turnNpc(wall3.getX() + 2, wall3.getY());
                                            nib4.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib5.isDead) {
                                        if (wall3.getDistance(nib5.getX(), nib5.getY()) <= 1) {
                                            nib5.turnNpc(wall3.getX() + 2, wall3.getY());
                                            nib5.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }

                                    if (!nib6.isDead) {
                                        if (wall3.getDistance(nib6.getX(), nib6.getY()) <= 1) {
                                            nib6.turnNpc(wall3.getX() + 2, wall3.getY());
                                            nib6.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (wall3 != null) {
                            if (wall3.getHealth().getAmount() == 0) {
                                wall3.isDead = true;
                            }
                        }
                        if (wall1 != null) {
                            if (wall1.isDead) {
                                Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height));
                                Server.getGlobalObjects().remove(30354, 2270, 5333, height);
                                //Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height, 1, 10, -1, -1));
                            }
                        }
                        if (wall2 != null) {
                            if (wall2.isDead) {
                                Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height));
                                Server.getGlobalObjects().remove(30355, 2259, 5349, height);
                                //Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height, 1, 10, -1, -1));
                            }
                        }
                        if (wall3 != null) {
                            if (wall3.isDead) {
                                Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height));
                                Server.getGlobalObjects().remove(30355, 2278, 5350, height);
                                //Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height, 1, 10, -1, -1));
                            }
                        }
                        if (wall1 != null && wall2 != null && wall3 != null) {
                            if (wall1.isDead && wall2.isDead && wall3.isDead) {
                                if (!nib1.isDead) {
                                    NPCDumbPathFinder.follow(nib1, player);
                                    if (ticks % 4 == 0) {
                                        if (nib1.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib1.facePlayer(player.getIndex());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                                if (!nib2.isDead) {
                                    NPCDumbPathFinder.follow(nib2, player);
                                    if (ticks % 4 == 0) {
                                        if (nib2.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib2.facePlayer(player.getIndex());
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                                if (!nib3.isDead) {
                                    NPCDumbPathFinder.follow(nib3, player);
                                    if (ticks % 4 == 0) {
                                        if (nib3.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib3.facePlayer(player.getIndex());
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }

                                if (!nib4.isDead) {
                                    NPCDumbPathFinder.follow(nib4, player);
                                    if (ticks % 4 == 0) {
                                        if (nib4.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib4.facePlayer(player.getIndex());
                                            nib4.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }

                                if (!nib5.isDead) {
                                    NPCDumbPathFinder.follow(nib5, player);
                                    if (ticks % 4 == 0) {
                                        if (nib5.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib5.facePlayer(player.getIndex());
                                            nib5.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }

                                if (!nib6.isDead) {
                                    NPCDumbPathFinder.follow(nib6, player);
                                    if (ticks % 4 == 0) {
                                        if (nib6.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib6.facePlayer(player.getIndex());
                                            nib6.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }


                            }
                        }
                        if (wall1 == null && wall2 == null && wall3 == null) {
                            if (!nib1.isDead) {
                                NPCDumbPathFinder.follow(nib1, player);
                                if (ticks % 4 == 0) {
                                    if (nib1.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib1.facePlayer(player.getIndex());
                                        nib1.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }
                            if (!nib2.isDead) {
                                NPCDumbPathFinder.follow(nib2, player);
                                if (ticks % 4 == 0) {
                                    if (nib2.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib2.facePlayer(player.getIndex());
                                        nib2.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }
                            if (!nib3.isDead) {
                                NPCDumbPathFinder.follow(nib3, player);
                                if (ticks % 4 == 0) {
                                    if (nib3.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib3.facePlayer(player.getIndex());
                                        nib3.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }

                            if (!nib4.isDead) {
                                NPCDumbPathFinder.follow(nib4, player);
                                if (ticks % 4 == 0) {
                                    if (nib4.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib4.facePlayer(player.getIndex());
                                        nib4.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }

                            if (!nib5.isDead) {
                                NPCDumbPathFinder.follow(nib5, player);
                                if (ticks % 4 == 0) {
                                    if (nib5.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib5.facePlayer(player.getIndex());
                                        nib5.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }

                            if (!nib6.isDead) {
                                NPCDumbPathFinder.follow(nib6, player);
                                if (ticks % 4 == 0) {
                                    if (nib6.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib6.facePlayer(player.getIndex());
                                        nib6.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }

                        }
                        if (nib1 != null && nib2 != null && nib3 != null && nib4 != null && nib5 != null && nib6 != null) {
                            if (nib1.isDead && nib2.isDead && nib3.isDead && nib4.isDead && nib5.isDead && nib6.isDead ) {
                            	System.out.println("ALL 6 DEAD NOT NULL");
                                started = false;
                                container.stop();
                            }
                        }
                        if (nib1 == null && nib2 == null && nib3 == null && nib4 == null && nib5 == null && nib6 == null ) {
                        	System.out.println("ONE OF SIX NULL");
                            started = false;
                            container.stop();
                        }
                        ticks++;

                    }
                }
            }, 1);
        }
        else  { //TODO ELSE
        //else if (player.infernoWaveId < 66 && player.infernoWaveId != 3 && player.infernoWaveId != 7 && player.infernoWaveId != 17 && player.infernoWaveId != 34 ) {
//            System.out.println("Startin wave " + player.infernoWaveId);
        	System.out.println("NORMAL WAVE STARTING: " + player.infernoWaveId);
            CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                int ticks = 0;

                @Override
                public void execute(CycleEventContainer container) {
                    if (player == null) {
                        container.stop();
                        return;
                    }
                    if (!Boundary.isIn(player, Boundary.INFERNO)) {
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
                    if (player.infernoWaveId >= type.length && player.infernoWaveType > 0) {
                        stop();
                        container.stop();
                        return;
                    }
                    if (started) {
                        if (ticks == 0) {
                            nib1 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2270, 5342, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false, true);
                            nib2 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2269, 5343, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false, true);
                            nib3 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2271, 5343, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false, true);
                        }
                        if (wall1 != null) {
                            if (!wall1.isDead) {
                                NPCDumbPathFinder.walkTowards(nib1, 2270, 5336);
                                NPCDumbPathFinder.walkTowards(nib2, 2271, 5336);
                                NPCDumbPathFinder.walkTowards(nib3, 2272, 5336);
                                if (ticks % 4 == 0) {
                                    if (!nib1.isDead) {
                                        if (wall1.getDistance(nib1.getX(), nib1.getY()) <= 1) {
                                            nib1.turnNpc(wall1.getX(), wall1.getY());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib2.isDead) {
                                        if (wall1.getDistance(nib2.getX(), nib2.getY()) <= 1) {
                                            nib2.turnNpc(wall1.getX() + 1, wall1.getY());
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib3.isDead) {
                                        if (wall1.getDistance(nib3.getX(), nib3.getY()) <= 1) {
                                            nib3.turnNpc(wall1.getX() + 2, wall1.getY());
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (wall2 != null) {
                            if (!wall2.isDead && wall1.isDead && !wall3.isDead) {
                                NPCDumbPathFinder.walkTowards(nib1, 2261, 5348);
                                NPCDumbPathFinder.walkTowards(nib2, 2260, 5348);
                                NPCDumbPathFinder.walkTowards(nib3, 2259, 5348);
                                if (ticks % 4 == 0) {
                                    if (!nib1.isDead) {
                                        if (wall2.getDistance(nib1.getX(), nib1.getY()) <= 1) {
                                            nib1.turnNpc(wall2.getX() + 2, wall2.getY());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall2Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib2.isDead) {
                                        if (wall2.getDistance(nib2.getX(), nib2.getY()) <= 1) {
                                            nib2.turnNpc(wall2.getX() + 1, wall2.getY());
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall2Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib3.isDead) {
                                        if (wall2.getDistance(nib3.getX(), nib3.getY()) <= 1) {
                                            nib3.turnNpc(wall2.getX(), wall2.getY());
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall2Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (wall3 != null) {
                            if (!wall3.isDead && wall1.isDead && wall2.isDead) {
                                NPCDumbPathFinder.walkTowards(nib1, 2277, 5350);
                                NPCDumbPathFinder.walkTowards(nib2, 2277, 5351);
                                NPCDumbPathFinder.walkTowards(nib3, 2277, 5352);
                                if (ticks % 4 == 0) {
                                    if (!nib1.isDead) {
                                        if (wall3.getDistance(nib1.getX(), nib1.getY()) <= 1) {
                                            nib1.turnNpc(wall3.getX(), wall3.getY());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall3Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib2.isDead) {
                                        if (wall3.getDistance(nib2.getX(), nib2.getY()) <= 1) {
                                            nib2.turnNpc(wall3.getX(), wall3.getY() + 1);
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall3Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib3.isDead) {
                                        if (wall3.getDistance(nib3.getX(), nib3.getY()) <= 1) {
                                            nib3.turnNpc(wall3.getX(), wall3.getY() + 2);
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            player.wall3Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (wall3 != null) {
                            if (wall3.getHealth().getAmount() == 0) {
                                wall3.isDead = true;
                            }
                        }
                        if (wall1 != null) {
                            if (wall1.isDead) {
                                Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height));
                                Server.getGlobalObjects().remove(30354, 2270, 5333, height);
                                //Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height, 1, 10, -1, -1));
                            }
                        }
                        if (wall2 != null) {
                            if (wall2.isDead) {
                                Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height));
                                Server.getGlobalObjects().remove(30355, 2259, 5349, height);
                                //Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height, 1, 10, -1, -1));
                            }
                        }
                        if (wall3 != null) {
                            if (wall3.isDead) {
                                Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height));
                                Server.getGlobalObjects().remove(30355, 2278, 5350, height);
                                //Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height, 1, 10, -1, -1));
                            }
                        }
                        if (wall1 != null && wall2 != null && wall3 != null) {
                            if (wall1.isDead && wall2.isDead && wall3.isDead) {
                                if (!nib1.isDead) {
                                    NPCDumbPathFinder.follow(nib1, player);
                                    if (ticks % 4 == 0) {
                                        if (nib1.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib1.facePlayer(player.getIndex());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                                if (!nib2.isDead) {
                                    NPCDumbPathFinder.follow(nib2, player);
                                    if (ticks % 4 == 0) {
                                        if (nib2.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib2.facePlayer(player.getIndex());
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                                if (!nib3.isDead) {
                                    NPCDumbPathFinder.follow(nib3, player);
                                    if (ticks % 4 == 0) {
                                        if (nib3.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib3.facePlayer(player.getIndex());
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (wall1 == null && wall2 == null && wall3 == null) {
                            if (!nib1.isDead) {
                                NPCDumbPathFinder.follow(nib1, player);
                                if (ticks % 4 == 0) {
                                    if (nib1.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib1.facePlayer(player.getIndex());
                                        nib1.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }
                            if (!nib2.isDead) {
                                NPCDumbPathFinder.follow(nib2, player);
                                if (ticks % 4 == 0) {
                                    if (nib2.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib2.facePlayer(player.getIndex());
                                        nib2.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }
                            if (!nib3.isDead) {
                                NPCDumbPathFinder.follow(nib3, player);
                                if (ticks % 4 == 0) {
                                    if (nib3.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib3.facePlayer(player.getIndex());
                                        nib3.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }
                        }
                        if (nib1 != null && nib2 != null && nib3 != null) {
                            if (nib1.isDead && nib2.isDead && nib3.isDead) {
                            	System.out.println("ALL 3 DEAD");
                                started = false;
                                container.stop();
                            }
                        }
                        if (nib1 == null && nib2 == null && nib3 == null) {
                        	System.out.println("ONE OF THREE NULL");
                            started = false;
                            container.stop();
                        }
                        ticks++;

                    }
                }
            }, 1);
        }
        }
    }

    public void leaveGame() {
        /*if (System.currentTimeMillis() - player.infernoLeaveTimer < 15000) {
            player.sendMessage("You cannot leave yet, wait a couple of seconds and try again.");
            return;
        }*/
        killAllSpawns();
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height));
        Server.getGlobalObjects().remove(30354, 2270, 5333, height);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height));
        Server.getGlobalObjects().remove(30355, 2259, 5349, height);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height));
        Server.getGlobalObjects().remove(30355, 2278, 5350, height);
        if (wall1 != null) {
            wall1.isDead = true;
        }
        if (wall2 != null) {
            wall2.isDead = true;
        }
        if (wall3 != null) {
            wall3.isDead = true;
        }
        player.sendMessage("You have left the Inferno minigame.");
        player.getPA().movePlayer(2497, 5116, 0);
        player.infernoWaveType = 0;
        player.infernoWaveId = 0;
        player.wall1Alive = 0;
        player.wall1Hp = 0;
        player.wall2Alive = 0;
        player.wall2Hp = 0;
        player.wall3Alive = 0;
        player.wall3Hp = 0;
    }

    public void create(int type) {
        if (player.infernoWaveType > 0) {
            System.out.println("Okay?");
            return;
        }
        player.getPA().removeAllWindows();
        player.getPA().movePlayer(2271, 5329, height);
        player.infernoWaveType = 1 ;
        //player.resetInfernoInstance();
        player.sendMessage("Welcome to the Inferno. Your first wave will start soon.", 255);
        player.infernoWaveId = 0 ;
        player.wall1Alive = 0;
        player.wall1Hp = 1000;
        player.wall2Alive = 0;
        player.wall2Hp = 1000;
        player.wall3Alive = 0;
        player.wall3Hp = 1000;
        player.infernoLeaveTimer = System.currentTimeMillis();
        createWalls();
        spawn();
    }

    public void stop() {
        player.getItems().addItemUnderAnyCircumstance(TOKKUL, (10000 * player.infernoWaveType) + Misc.random(5000));
        player.getPA().movePlayer(2497, 5116, 0);
        player.getDH().sendStatement("Congratulations for finishing the Inferno!");
        player.waveInfo[player.infernoWaveType - 1] += 1;
        player.resetInfernoInstance();
        player.infernoWaveType = 0;
        player.infernoWaveId = 0;
        player.wall1Alive = 0;
        player.wall1Hp = 0;
        player.wall2Alive = 0;
        player.wall2Hp = 0;
        player.wall3Alive = 0;
        player.wall3Hp = 0;
        player.nextChat = 0;
        player.setRunEnergy(100);
        killAllSpawns();
        player.getInferno().zukDead = false;
    }

    private void createWalls() {
        if (player.wall1Alive == 0 || player.wall1Hp > 0) {
            Server.getGlobalObjects().add(new GlobalObject(30354, 2270, 5333, height, 1, 10, -1, -1));
            wall1 = Server.npcHandler.spawnNpc(player, 7709, 2270, 5333, height, 0, 1000, 0, 0, 0, false, false, true);
            wall1.getHealth().setAmount(player.wall1Hp);
            player.wall1Alive = 1;
        } else {
            wall1 = Server.npcHandler.spawnNpc(player, 7709, 2270, 5333, height, 0, 0, 0, 0, 0, false, false, true);
            wall1.isDead = true;
        }
        if (player.wall2Alive == 0 || player.wall2Hp > 0) {
            Server.getGlobalObjects().add(new GlobalObject(30355, 2259, 5349, height, 1, 10, -1, -1));
            wall2 = Server.npcHandler.spawnNpc(player, 7709, 2259, 5349, height, 0, 1000, 0, 0, 0, false, false, true);
            wall2.getHealth().setAmount(player.wall2Hp);
            player.wall2Alive = 1;
        } else {
            wall2 = Server.npcHandler.spawnNpc(player, 7709, 2259, 5349, height, 0, 0, 0, 0, 0, false, false, true);
            wall2.isDead = true;
        }
        if (player.wall3Alive == 0 || player.wall3Hp > 0) {
            Server.getGlobalObjects().add(new GlobalObject(30355, 2278, 5350, height, 1, 10, -1, -1));
            wall3 = Server.npcHandler.spawnNpc(player, 7709, 2278, 5350, height, 0, 1000, 0, 0, 0, false, false, true);
            wall3.getHealth().setAmount(player.wall3Hp);
            player.wall3Alive = 1;
        } else {
            wall3 = Server.npcHandler.spawnNpc(player, 7709, 2278, 5350, height, 0, 0, 0, 0, 0, false, false, true);
            wall3.isDead = true;
        }
    }

    public void handleDeath() {
        int wave = player.infernoWaveId + 1;
        player.getPA().movePlayer(2497, 5116, 0);
        player.getDH().sendStatement("Unfortunately you died on wave " + wave + ". Better luck next time.");
        player.nextChat = 0;
        player.infernoWaveType = 0;
        player.infernoWaveId = 0;
        player.wall1Alive = 0;
        player.wall1Hp = 0;
        player.wall2Alive = 0;
        player.wall2Hp = 0;
        player.wall3Alive = 0;
        player.wall3Hp = 0;
        player.resetInfernoInstance();
        killAllSpawns(); //you placed 6 at dead 5
    }

    public void killAllSpawns() {
        for (int i = 0; i < NPCHandler.npcs.length; i++) {
            if (NPCHandler.npcs[i] != null) {
                if (NPCHandler.isInfernoNpc(i)) {
                    if (NPCHandler.isSpawnedBy(player, NPCHandler.npcs[i])) {
                        NPCHandler.npcs[i] = null;
                    }
                }
            }
        }
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height));
        Server.getGlobalObjects().remove(30354, 2270, 5333, height);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height));
        Server.getGlobalObjects().remove(30355, 2259, 5349, height);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height));
        Server.getGlobalObjects().remove(30355, 2278, 5350, height);
        if (wall1 != null) {
            wall1.isDead = true;
        }
        if (wall2 != null) {
            wall2.isDead = true;
        }
        if (wall3 != null) {
            wall3.isDead = true;
        }
        if (nib1 != null) {
            nib1.isDead = true;
        }
        if (nib2 != null) {
            nib2.isDead = true;
        }
        if (nib3 != null) {
            nib3.isDead = true;
        }
        if (nib4 != null) {
            nib4.isDead = true;
        }
        if (nib5 != null) { //mb 
            nib5.isDead = true;
        }
        if (nib6 != null) {
            nib6.isDead = true;
        }
        
    }

    public void gamble() {
        if (!player.getItems().playerHasItem(21295)) {
            player.sendMessage("You do not have an infernal cape.");
            return;
        }

        player.getItems().deleteItem(21295, 1);

        if (Misc.random(50) == 0) {
            PlayerHandler.executeGlobalMessage("[@red@PET@bla@] @cr20@<col=255> " + player.playerName + "</col> received a pet from <col=255>TzKal-Zuk</col>.");
            player.getItems().addItemUnderAnyCircumstance(21291, 1);
            player.getDH().sendDialogues(74, 7690);
        } else {
            player.getDH().sendDialogues(73, 7690);
            return;
        }
    }

    private void jadCombat(NPC jad) {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            int ticks = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (player == null) {
                    container.stop();
                    return;
                }
                if (player.isDead()) {
                    container.stop();
                    return;
                }
                if (jad != null) {
                    if (!jad.isDead) {
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
                        jad.facePlayer(player.getIndex());
                    }
                }
                int type;
                if (jad.getDistance(player.getX(), player.getY()) <= 2) {
                    type = Misc.random(0, 2);
                } else {
                    type = Misc.random(0, 1);
                }
                if (ticks % 10 == 0 && ticks != 0) {
                    if (jad != null) {
                        if (!jad.isDead) {
                            if (type == 0) {
                                jad.startAnimation(7593);
                            } else if (type == 1) {
                                jad.startAnimation(7592);
                            } else if (type == 2) {
                                jad.startAnimation(7590);
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
                                        } else if (type == 1) {
                                            if (player.protectingMagic()) {
                                                amount = 0;
                                            }
                                            player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 448, startHeight, endHeight, -player.getIndex() - 1, 65, delay);
                                        } else if (type == 2) {
                                            if (player.protectingMelee()) {
                                                amount = 0;
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
                                } else if (type == 1) {
                                    if (player.protectingMagic()) {
                                        amount = 0;
                                    }
                                } else if (type == 2) {
                                    if (player.protectingMelee()) {
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
                                        if (player == null) {
                                            container.stop();
                                            return;
                                        }
                                        if (player.isDead()) {
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
                                        } else if (type == 1) {
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
                                        } else if (type == 2) {
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (Misc.random(500) + 200 > Misc.random(player.getCombat().calculateMeleeDefence())) {
                                                if (amount == 0) {
                                                    player.appendDamage(amount, Hitmark.MISS);
                                                } else {
                                                    player.appendDamage(amount, Hitmark.HIT);
                                                }
                                            } else {
                                                player.appendDamage(0, Hitmark.MISS);
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
                            tick++;
                        }
                    }, 1);
                }
                ticks++;
            }
        }, 1);
    }

    public int getKillsRemaining() {
        return killsRemaining;
    }

    public void setKillsRemaining(int remaining) {
        this.killsRemaining = remaining;
    }

}
