package ethos.model.npcs.bosses.vorkath;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.event.DelayEvent;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.content.instances.impl.SingleInstancedVorkath;
import ethos.model.entity.HealthStatus;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDumbPathFinder;
import ethos.model.npcs.bosses.vorkath.impl.DeathStage;
import ethos.model.npcs.bosses.vorkath.impl.SpawnStage;
import ethos.model.npcs.bosses.vorkath.impl.WakeUpStage;
import ethos.model.players.Player;
import ethos.model.players.Position;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Yasin
 */
public class Vorkath {

    private final Player player;

    private AttackType specialAttackType;

    private AttackType attackType;

    private VorkathState vorkathState;

    private final Object eventLock;

    private int attacks;

    private NPC vorkath;

    private boolean zombieSpawned;
    

    private boolean forceDeath;

    private SingleInstancedVorkath vorkathInstance;

    public Vorkath(Player player) {
        this.player = player;
        this.specialAttackType = Arrays.stream(AttackType.values()).filter(type -> type.name().toLowerCase().contains("special")).collect(Collectors.toList()).get(Misc.random(1));//only 0 or 1
        this.vorkathState = VorkathState.SLEEPING;
        this.eventLock = new Object();
    }

    public void start() {
        if (vorkathInstance != null) {
            disposeInstance();
            //player.sendMessage("Vorkath instance is not null, fyi! Disposing..");
        }
        int height = InstancedAreaManager.getSingleton().getNextOpenHeight(VorkathConstants.VORKATH_BOUNDARY);
        vorkathInstance = new SingleInstancedVorkath(player, VorkathConstants.VORKATH_BOUNDARY, height);
        InstancedAreaManager.getSingleton().add(height, vorkathInstance);
        if (vorkathInstance == null) {
            player.getDH().sendStatement("Vorkath can't be fought right now.", "Please try again shortly.");
            player.nextChat = -1;
            return;
        }
        CycleEventHandler.getSingleton().addEvent(eventLock, new SpawnStage(this, player), 1);
    }

    public void disposeInstance() {
        forceDeath = true;
        if (vorkath != null) {
            setVorkathState(VorkathState.SLEEPING);
            Server.npcHandler.kill(vorkath.npcType, vorkathInstance.getHeight());
        }
        attacks = 0;
        InstancedAreaManager.getSingleton().disposeOf(vorkathInstance);
        Server.getGlobalObjects().remove(32000, vorkathInstance.getHeight());
        vorkathInstance = null;
        forceDeath = false;
    }

    public void resetCombat() {
    	if (vorkath == null) {
    		return;
    	}
        player.getCombat().resetPlayerAttack();
        vorkath.underAttack = false;
        vorkath.face = 0;
        if (vorkathState == VorkathState.RESTING) {
            vorkath.attackTimer += 1;
        }
        vorkath.underAttackBy = 0;
    }


    public boolean canSpecial() {
        return attacks % 7 == 0 && attacks > 0;
    }

    public void wakeUp() {
        if(vorkathState == VorkathState.AWAKE) {
            return;
        }
        if (vorkathState != VorkathState.RESTING) { //just to make sure
            vorkathState = VorkathState.AWAKE;
            CycleEventHandler.getSingleton().addEvent(eventLock, new WakeUpStage(this, player, false), 1);
        }
        if (vorkathState == VorkathState.RESTING) { //just to make sure
            vorkathState = VorkathState.AWAKE;
            CycleEventHandler.getSingleton().addEvent(eventLock, new WakeUpStage(this, player, true), 1);
        }
    }

    public void handleAttack() {
        if (player != null && vorkath != null && !vorkath.isDead && !player.isDead && !zombieSpawned) {
            int distance = player.distanceToPoint(vorkath.getX(), vorkath.getY());
            if (vorkath.actionTimer > 0) {
                return;
            }
            if(distance >= 15) {
                return;
            }
            if(distance <= 3) {
                vorkath.actionTimer += 2;
            }
            //vorkath.facePlayer(player.getIndex());
            vorkath.actionTimer += 5;
            attacks += 1;
            if (canSpecial()) { //Every 7 attacks
                specialAttackType = specialAttackType == AttackType.SPECIAL_1 ? AttackType.SPECIAL_2 : AttackType.SPECIAL_1;
                vorkath.startAnimation(specialAttackType.getAnimationId());
                if (specialAttackType == AttackType.SPECIAL_1) { //acid
                    CycleEventHandler.getSingleton().addEvent(eventLock, handleAcidSpecial(), 1);
                    vorkath.actionTimer += 22;
                } else if (specialAttackType == AttackType.SPECIAL_2) { //jihad
                    fireTargetedProjectile(specialAttackType.getProjectileId());
                    CycleEventHandler.getSingleton().addEvent(eventLock, handleJihadSpecial(), 1);
                    player.stopMovement();
                    vorkath.actionTimer += 7;
                }
            } else {
                attackType = Arrays.stream(AttackType.values()).filter(type ->
                        !type.name().toLowerCase().contains("special")).
                        collect(Collectors.toList()).get(Misc.random(5));
                vorkath.actionTimer += 1;
                vorkath.startAnimation(attackType.getAnimationId());
                if (attackType != AttackType.ONE_HIT) {
                    CycleEventHandler.getSingleton().addEvent(eventLock, handleAttackType(), 1);
                    fireTargetedProjectile(attackType.getProjectileId());
                } else {
                    CycleEventHandler.getSingleton().addEvent(eventLock, handleOneHit(), 1);
                    fireOneshit(attackType.getProjectileId(), 110, player.getX(), player.getY(),
                            50, 50); //50 -> current angle, 50 -> current start height

                }
            }
        }
    }

    public void jump() {
        CycleEventHandler.getSingleton().addEvent(eventLock, new CycleEvent() {
            boolean north;

            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || player == null || player.isDead || player.animationRequest != -1) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                if (cycle == 1) {
                    if (player.getX() <= 2273 && player.getX() >= 2271 && player.getY() == 4052) {
                        north = true;
                    }
                    player.startAnimation(1115);
                    player.sendMessage("You jump over the ice bricks..");
                }
                if (cycle == 3) {
                    player.stopAnimation();
                    player.sendMessage("and reach the other side..");
                    if(north && vorkathInstance == null) {
                        start();
                    }
                    if(vorkathInstance == null) { //height will be set in spawnstage
                        player.getPA().movePlayer(player.getX(), north ? player.getY() + 2 : player.getY() - 2);
                    } else { //so the player can continue
                        player.getPA().movePlayer(player.getX(), north ? player.getY() + 2 : player.getY() - 2, vorkathInstance.getHeight());
                    }
                    container.stop();
                }

            }
        }, 1);
    }

    private CycleEvent handleJihadSpecial() {
        return new CycleEvent() {
            int SPAWN_X;
            int SPAWN_Y;
            NPC spawn;

            private void killSpawn(boolean explode) {
                spawn.gfx0(542);
                zombieSpawned = false;
                spawn.needRespawn = false;
                spawn.isDead = true;
                if(explode) {
                    player.appendDamage(Misc.random(20) + 10, Hitmark.HIT); //So it hits a minimum of 10 and maximum of 70, random number between those. let's make it like 10 to 30 or?
                } //You understand how this misc ra// its give random numbers right ? yeah that's right, the number after it in the () is the max it can be like 20 is a random number between 0 to 20 and (40) is a random number between 0 and 40, so if you do 
                //Misc.random(40) + 10 then it will take a random number between 0 and 40 and then it does + 10, so you get a random number between 10 and 50, see?yea i get it now thanks ;) so what I did should give between 10 and 30.30. Misc.random(20) + 10 random from 0 to 20 and then plus 10, so 10 to 30 yea this will be good alright
                player.freezeTimer = 0;
            }
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkathInstance == null) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                if (cycle == 4) {
                    player.gfx0(specialAttackType.getEndGfx());
                    player.freezeTimer = 500;
                    player.sendMessage("You've been frozen.");
                }
                if (cycle == 5) {
                    SPAWN_X = vorkath.getX() + Misc.random(7) + 3;
                    SPAWN_Y = vorkath.getY() - 2;
                    player.sendMessage("The dragon throws a creature towards you..");
                    zombieSpawned = true;
                    fireProjectileToLocation(1484, 130, SPAWN_X, SPAWN_Y, 50);
                }
                if (cycle == 9) {
                    spawn = Server.npcHandler.spawnNpc(player, VorkathConstants.ZOMBIE_SPAWN, SPAWN_X, SPAWN_Y, vorkathInstance.getHeight(), 1, VorkathConstants.ZOMBIE_SPAWN_LIFE_POINTS, 1, 1, 1, false, false);
                }
                if (cycle >= 10) {
                    int distance = player.distanceToPoint(spawn.getX(), spawn.getY());
                    if(zombieSpawned && spawn.isDead) {
                        killSpawn(false);
                        container.stop();
                    }
                    if (distance <= 1 && zombieSpawned) {
                        killSpawn(true);
                        container.stop();
                    }
                }
                if (zombieSpawned && cycle >= 10) {
                    if (spawn.getX() != player.absX - 1 && spawn.getY() != player.absY - 1) {
                        NPCDumbPathFinder.walkTowards(spawn, player.getX(), player.getY());
                    }
                }

                if (!zombieSpawned && cycle >= 20) {
                    container.stop();
                }


                if (zombieSpawned && cycle >= 20 && player.distanceToPoint(spawn.getX(), spawn.getY()) >= 5) { 
                    if (player.distanceToPoint(spawn.getX(), spawn.getY()) < 40) { 
                       killSpawn(false);
                        player.sendMessage("The spawn lost its orientation and blew up..");
                    }
                }
            }
        };
    }

    private CycleEvent handleAcidSpecial() {
        return new CycleEvent() {
            int x;
            int y;

            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkathInstance == null) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                if(Server.getGlobalObjects().exists(32000, player.getX(), player.getY(), player.getHeight())) {
                    int randomDamage = Misc.random(10) + 7;
                    vorkath.getHealth().increase(randomDamage);
                    vorkath.updateRequired = true;
                    player.appendDamage(randomDamage, Hitmark.HIT);
                    player.sendMessage("You step on the acid and take some damage");
                }
                if (cycle == 1) {
                    int minX = VorkathConstants.VORKATH_BOUNDARY.getMinimumX();
                    int maxX = VorkathConstants.VORKATH_BOUNDARY.getMaximumX();
                    int minY = 4054; //it's bugged in the boundaries
                    int maxY = VorkathConstants.VORKATH_BOUNDARY.getMaximumY();
                    for (int i = 0; i < 40; i++) {
                        int randomX = minX + Misc.random(maxX - minX);
                        int randomY = minY + Misc.random(maxY - minY);
                        if ((randomX <= 2276 && randomX >= 2268 && randomY <= 4069 && randomY >= 4061)) {
                            continue;
                        }
                        fireProjectileToLocation(1486, 100,
                                randomX,
                                randomY, 60);
                        Server.getEventHandler().submit(new DelayEvent(5) {
                            @Override
                            public void onExecute() {
                                Server.getGlobalObjects().add(new GlobalObject(32000, randomX, randomY, vorkathInstance.getHeight(), Misc.random(3) + 1, 10, -1, -1));
                            }
                        });
                    }
                }
                if (cycle >= 3 && cycle <= 25) {
                	if (cycle >= 5) {
                     	if (x == player.getX() && y == player.getY()) {
                          	player.appendDamage(30, Hitmark.HIT);
                      	}
                    }
                    x = player.getX();
                    y = player.getY();
                    fireProjectileToLocation(1482, 95, x, y, 35);
                    player.getPA().stillGfx(131, x, y, 15, 95);
                    fireProjectileToLocation(1482, 70, x, y, 35);
                    player.getPA().stillGfx(131, x, y, 15, 70);
                }
                if (cycle == 30) {
                    Server.getGlobalObjects().remove(32000, vorkathInstance.getHeight());
                    container.stop();
                }
            }
        };
    }

    private CycleEvent handleOneHit() {
        return new CycleEvent() {
            Position arrival;

            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkathInstance == null) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                if (cycle == 1) {
                    arrival = new Position(player.getX(), player.getY());
                }
                if (cycle == 6) {
                    int arrivalX = arrival.getX();
                    int arrivalY = arrival.getY();
                    int playerX = player.getX();
                    int playerY = player.getY();
                    if (playerX == arrivalX && playerY == arrivalY) {
                        applyRandomDamage(player.getHealth().getMaximum());
                        player.getPA().stillGfx(attackType.getEndGfx(), arrival.getX(), arrival.getY(), 100, 0);
                    } if(playerX == (arrivalX + 1)
                            || playerX == (arrivalX - 1)
                            || playerY == (arrivalY + 1)
                            || arrivalY == (arrivalY - 1)) {
                        applyRandomDamage(player.getHealth().getMaximum() / 2);
                        player.getPA().stillGfx(attackType.getEndGfx(), arrivalX, arrivalY, 100, 0);
                    } else {
                        player.getPA().stillGfx(attackType.getEndGfx(), arrivalX, arrivalY, 20, 0);
                    }
                    container.stop();
                }
            }
        };
    }
    
    public Position gfxStartPosition() {
        double theta = Math.atan2((double) player.absY - (vorkath.absY + 3), (double) player.absX - (vorkath.absX + 3));
        int x = (int) Math.round(Math.cos(theta) * 3) + vorkath.absX + 3;
        int y = (int) Math.round(Math.sin(theta) * 3) + vorkath.absY + 3;
        return new Position(x, y);
    }

    private void fireTargetedProjectile(int projectileId) {
        int offY = (vorkath.getX() - player.getX()) * -1;
        int offX = (vorkath.getY() - player.getY()) * -1;
        int delay = 0;
        Position gfxStartPosition = gfxStartPosition();
        player.getPA().createPlayersProjectile(gfxStartPosition.getX(), gfxStartPosition.getY(), offX, offY, 50, 110, projectileId, 35, 31, -player.getIndex() - 1, 65, delay);
    }

    private void fireProjectileToLocation(int projectileId, int projectileSpeed, int targetX, int targetY, int startHeight) {
        int offY = (vorkath.getX() - targetX) * -1;
        int offX = (vorkath.getY() - targetY) * -1;
        int delay = 0;
        Position gfxStartPosition = gfxStartPosition();
        player.getPA().createPlayersProjectile(gfxStartPosition.getX(), gfxStartPosition.getY(), offX, offY - 3, 50, projectileSpeed, projectileId, startHeight, 31, 0, 65, delay);
    }

    private void fireOneshit(int projectileId, int projectileSpeed, int targetX, int targetY, int angle, int startHeight) {
        int offY = (vorkath.getX() - targetX) * -1;
        int offX = (vorkath.getY() - targetY) * -1;
        int delay = 0;
        Position gfxStartPosition = gfxStartPosition();
        player.getPA().createPlayersProjectile(gfxStartPosition.getX(), gfxStartPosition.getY(), offX, offY - 3, 50, projectileSpeed, projectileId, startHeight, 31, 0, 65, delay);
    }


    private CycleEvent handleAttackType() {
        return new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkathInstance == null) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                //player.getPA().stillGfx(attackType.getEndGfx(), player.getX(), player.getY(), 100, 110);
                if (cycle == 4) {
                    handleEffect();
                }
                if (cycle == 5) {
                    player.getPA().stillGfx(attackType.getEndGfx(), player.getX(), player.getY(), 100, 0);
                    container.stop();
                }
            }
        };
    }

    private void applyRandomDamage(int amount) {
        player.appendDamage(Misc.random(amount) + 1, Hitmark.HIT);
    }

    private void handleEffect() {
        switch (attackType) {
            case MAGIC:
                if (player.prayerActive[16]) {
                    player.appendDamage(0, Hitmark.MISS);
                    return;
                } else {
                    applyRandomDamage(35);
                }
                break;
            case POISON:
                applyRandomDamage(3);
                player.getHealth().proposeStatus(HealthStatus.POISON, Misc.random(12), Optional.of(vorkath));
                break;
            case RANGED:
                if (player.prayerActive[17]) {
                    player.appendDamage(0, Hitmark.MISS);
                    return;
                } else {
                    applyRandomDamage(30);
                }
                break;
            case DRAGON_FIRE:
                boolean isResistent =
                        player.getItems().isWearingItem(1540)
                                || player.getItems().isWearingItem(11283)
                                || player.getItems().isWearingItem(11284)
                                || (System.currentTimeMillis() - player.lastAntifirePotion < player.antifireDelay);
                if (isResistent) {
                    player.sendMessage("Your resistance reflects the dragons fire!");
                    player.appendDamage(0, Hitmark.MISS);
                    return;
                } else {
                    applyRandomDamage(35);
                    player.sendMessage("You got horribly burned by the dragons fire.");
                }
                break;
            case PRAYER_SNIPE:
                for (int i = 0; i < player.prayerActive.length - 1; i++) {
                    if (!player.prayerActive[i])
                        continue;
                    player.prayerActive[i] = false;
                    player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
                }
                player.headIcon = -1;
                player.getPA().requestUpdates();
                applyRandomDamage(3);
                break;
		default:
			break;
        }
    }

    private static int[][] commonDrops = { { 1303, 2 + Misc.random(1) }, { 1201, 2 + Misc.random(1) },
            { 562, 700 + Misc.random(300) }, { 1754, 14 + Misc.random(18) }, { 1752, 11 + Misc.random(19) },
            { 450, 10 + Misc.random(20) }, { 1602, 10 + Misc.random(20) }, { 6694, 10 + Misc.random(20) },
            { 1988, 250 + Misc.random(51) }, { 1514, 50 }, { 995, 37000 + Misc.random(44000) },
            { 392, 25 + Misc.random(30) } };

    private static int[][] uncommonDrops = { { 21338, 27 + Misc.random(3) }, { 21930, 55 + Misc.random(45) },
            { 21486, 1 }, { 22118, 1 }, { 1377, 1 }, { 1305, 1 }, { 4087, 1 }, { 4585, 1 },
            { 560, 300 + Misc.random(200) }, { 1750, 10 + Misc.random(15) }, { 1748, 5 + Misc.random(20) },
            { 9189, 25 + Misc.random(10) }, { 9190, 25 + Misc.random(10) }, { 9191, 31 }, { 9192, 30 },
            { 9193, 7 + Misc.random(21) }, { 9194, 5 + Misc.random(5) }, { 9194, 26 + Misc.random(4) },
            { 824, 86 + Misc.random(14) }, { 11232, 10 + Misc.random(40) }, { 11237, 27 + Misc.random(23) },
            { 1616, 1 + Misc.random(2) }, { 5288, 1 }, { 5321, 15 }, { 5295, 1 }, { 5300, 1 }, { 5304, 1 }, { 5313, 1 },
            { 5314, 1 }, { 5290, 1 }, { 537, 7 + Misc.random(21) } };

    private static int[][] rareDrops = { { 1249, 1 }, { 2366, 1 }, { 1247, 1 }, { 1201, 1 }, { 1149, 1 }, { 1617, 1 },
            { 1615, 1 }, { 443, 100 }, { 1185, 1 }, { 1319, 1 }, { 1373, 1 }, { 560, 45 }, { 563, 45 }, { 561, 67 },
            { 2363, 1 }, { 829, 20 }, { 830, 5 }, { 892, 42 }, { 886, 150 }, { 1462, 1 }, { 1619, 1 }, { 1621, 1 },
            { 1623, 1 }, { 1452, 1 }, { 985, 1 }, { 987, 1 }, { 995, 3000 }, { 21880, 30 + Misc.random(70) },
            { 21488, 1 }, { 1392, 5 + Misc.random(10) }, { 5316, 1 }, { 5317, 1 }, { 5289, 1 }, { 5315, 1 } };

    private static int[][] veryRareDrops = { { 11286, 1 }, { 22006, 1 } };

    private static int[] lootCoordinates = { 2268, 4061 };

    public void drop() {
        Server.itemHandler.createGroundItem(player, 22124, lootCoordinates[0], lootCoordinates[1], player.heightLevel,
                1, player.getIndex());
        Server.itemHandler.createGroundItem(player, 1751, lootCoordinates[0], lootCoordinates[1], player.heightLevel, 1,
                player.getIndex());
        int roll = Misc.random(2500);
        int roll2 = Misc.random(500);
        int roll3 = Misc.random(1500);
        if (roll2 == 1) {
            Server.itemHandler.createGroundItem(player, 22111, lootCoordinates[0], lootCoordinates[1],
                    player.heightLevel, 1, player.getIndex());
            return;
        }
        if (roll3 == 1) {
            Server.itemHandler.createGroundItem(player, 22106, lootCoordinates[0], lootCoordinates[1],
                    player.heightLevel, 1, player.getIndex());
            return;
        }
        if (roll == 1) {
            int veryRareItemRoll = Misc.random(veryRareDrops.length - 1);
            Server.itemHandler.createGroundItem(player, veryRareDrops[veryRareItemRoll][0], lootCoordinates[0],
                    lootCoordinates[1], player.heightLevel, veryRareDrops[veryRareItemRoll][1], player.getIndex());
        } else if (roll >= 2 && roll <= 250) {
            int rareItemRoll = Misc.random(rareDrops.length - 1);
            Server.itemHandler.createGroundItem(player, rareDrops[rareItemRoll][0], lootCoordinates[0],
                    lootCoordinates[1], player.heightLevel, rareDrops[rareItemRoll][1], player.getIndex());
        } else if (roll > 250 && roll <= 1000) {
            int uncommonItemRoll = Misc.random(uncommonDrops.length - 1);
            Server.itemHandler.createGroundItem(player, uncommonDrops[uncommonItemRoll][0], lootCoordinates[0],
                    lootCoordinates[1], player.heightLevel, uncommonDrops[uncommonItemRoll][1], player.getIndex());
        } else {
            int commonItemRoll = Misc.random(commonDrops.length - 1);
            Server.itemHandler.createGroundItem(player, commonDrops[commonItemRoll][0], lootCoordinates[0],
                    lootCoordinates[1], player.heightLevel, commonDrops[commonItemRoll][1], player.getIndex());
        }
    }


    public void handleDeath() {
        vorkathState = VorkathState.RESTING;
        vorkath.setFacePlayer(false);
        CycleEventHandler.getSingleton().addEvent(eventLock, new DeathStage(this, player), 1);
    }

    public void setVorkathInstance(SingleInstancedVorkath vorkathInstance) {
        this.vorkathInstance = vorkathInstance;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public VorkathState getVorkathState() {
        return vorkathState;
    }

    public SingleInstancedArea getVorkathInstance() {
        return vorkathInstance;
    }

    public Object getEventLock() {
        return eventLock;
    }

    public AttackType getSpecialAttackType() {
        return specialAttackType;
    }

    public void setVorkathState(VorkathState vorkathState) {
        this.vorkathState = vorkathState;
    }

    public NPC getNpc() {
        return vorkath;
    }

    public void setVorkath(NPC vorkath) {
        this.vorkath = vorkath;
    }

    public int getAttacks() {
        return attacks;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
    }

    public boolean isForceDeath() {
        return forceDeath;
    }

    public void setForceDeath(boolean forceDeath) {
        this.forceDeath = forceDeath;
    }

    public boolean isZombieSpawned() {
    	return zombieSpawned;
    }
}
