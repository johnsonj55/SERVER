/*package ethos.model.npcs.bosses.hydra;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import ethos.clip.Region;
import ethos.clip.doors.Location;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.entity.HealthStatus;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDumbPathFinder;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Hitmark;
import ethos.model.npcs.bosses.hydra.HydraStage;
import ethos.util.Misc;

public class AlchemicalHydra extends SingleInstancedArea {
	

	private final Player player;

    public AlchemicalHydra(Player player) {
        super(player, AREA, InstancedAreaManager.getSingleton().getNextOpenHeight(AREA));
      
        InstancedAreaManager.getSingleton().add(getHeight(), this);
    	this.player = player;
        if (Boundary.isIn(player, AREA)) {
            player.getPA().movePlayerUnconditionally(player.getX(), player.getY(), getHeight());
        } else {
            player.getPA().movePlayerUnconditionally(CENTER.getX(), CENTER.getY(), getHeight());
        }
        player.setInstance(this);
        currentStage = HydraStage.POISON;
        npc = NPCHandler.spawnNpc(this, currentStage.getNpcId(), CENTER.getX(), CENTER.getY(), getHeight(), 0, currentStage.getHealth(), 1, 1, 1);
        npc.getHealth().resolveStatus(HealthStatus.POISON, Integer.MAX_VALUE);
        npc.getHealth().resolveStatus(HealthStatus.VENOM, Integer.MAX_VALUE);
        setupVents();
        
    }

    //Projectiles
    private static CombatProjectile POISON_PROJECTILE = new CombatProjectile(1644, 50, 25, 0, 120, 0, 50);
    private static CombatProjectile LIGHTNING_PROJECTILE = new CombatProjectile(1665, 50, 0, 0, 100, 0, 50);
    private static CombatProjectile RANGED_PROJECTILE = new CombatProjectile(1662, 50, 25, 0, 100, 0, 50);
    private static CombatProjectile MAGIC_PROJECTILE = new CombatProjectile(1663, 50, 25, 0, 100, 0, 50);
    private static CombatProjectile FIRE_PROJECTILE = new CombatProjectile(1667, 50, 25, 0, 100, 0, 50);

    //Boundarys used for fires
    private static final Boundary SOUTH = new Boundary(1365, 10257, 1368, 10264);
    private static final Boundary NORTH = new Boundary(1365, 10271, 1368, 10271);
    private static final Boundary EAST = new Boundary(1370, 10266, 1377, 10269);
    private static final Boundary WEST = new Boundary(1356, 10266, 1363, 10269);

    //Arena boundary
    public static final Boundary AREA = new Boundary(1356, 10257, 1377, 10278);
    private static final Location CENTER = new Location(1364, 10265);

    //Where firewalk ball goes
    private static final Boundary NORTH_WEST = new Boundary(1364, 10270, 1364, 10270);
    private static final Boundary NORTH_EAST = new Boundary(1369, 10270, 1369, 10270);
    private static final Boundary SOUTH_WEST = new Boundary(1364, 10265, 1364, 10265);
    private static final Boundary SOUTH_EAST = new Boundary(1369, 10265, 1369, 10265);

    //Lightning npc spawn locs
    private static final Location[] LIGHTNING_SPAWNS = {
            new Location(1362, 10274),
            new Location(1373, 10273),
            new Location(1372, 10261),
            new Location(1360, 10262),
    };

    private static final int POISON_SPLASH = 1645;
    private static final int POISON_POOL_MIN = 1654, POISON_POOL_MAX = 1661;
    private static final int FLOOR_FIRE = 1668;
    private static final int LIGHTNING_GFX = 1666;
    private static final int MIDDLE_HEAD = 0;
    private static final int LEFT_HEAD = 1;
    private static final int RIGHT_HEAD = 2;

    private static final int[][] ATTACK_ANIMS = {
            //Poison phase
            {
                    8234,
                    8235,
                    8236
            },
            //Lightning phase
            {
                    8241,
                    8242,
                    8243
            },
            //Flame phase
            {
                    8248,
                    8249,
                    8250
            },
            //Enranged phase
            {
                    8255,
                    8255,
                    8256
            }
    };

    private NPC npc;
    private HydraStage currentStage;

    private boolean buffed, magic, fireAttackExecuting;
    private int performedAttacks;

    private int tickCount = 0;
    private boolean sprayed;
    private int ventTicks = 0;


    public void onTick() {
        tickCount++;
        ventTicks++;


        if(!Boundary.isIn(player, AREA)) {
            player.setInstance(null);
        }
        if (player == null || npc == null || player.isDead() || !npc.sameInstance(player)) {
            InstancedAreaManager.getSingleton().disposeOf(this);
            System.out.println("DESTROYING INSTANCE");
            NPCHandler.deregister(npc);
            return;
        }
        if(transforming)
        	return;
        if(!fireAttackExecuting) {
	        npc.killerId = player.getIndex();
			npc.facePlayer(player.getIndex());
			npc.underAttack = true;
			npc.walkingHome = false;
        } else {
        	npc.killerId = 0;
            npc.facePlayer(0);
            npc.underAttack = false;
            npc.lastRandomlySelectedPlayer = 0;
        }
		npc.lastDamageTaken = System.currentTimeMillis();
        checkTransform();
        if (sprayed) {
            buffed = false;
        }
        
        if(ventTicks >= 4 && ventTicks <= 11) {
        	  if (currentStage != HydraStage.ENRAGED) {
                  if (Boundary.isIn(npc, currentStage.getBoundary())) {
                      sprayed = true;
                  } else if (!sprayed && HydraStage.stream()
                          .filter(hydraStage -> hydraStage != currentStage)
                          .anyMatch(hydraStage -> Boundary.isIn(npc, hydraStage.getBoundary()))) {
                  
                      buffed = true;
                  }

              }
        } else if(ventTicks == 26) {
        	ventTicks = 0;
        }


        


        if (!sprayed) {
            player.getDamageQueue().getQueue()
                    .forEach(damage -> damage.setAmount(damage.getAmount() - (damage.getAmount() * 75 / 100)));
        }

        //Check for being on the wrong vent
      
    }

    public void reset() {
        fireAttackExecuting = false;
        performedAttacks = 0;
    }

    public void doAttack() {
    	if(transforming)
    		return;
        if (tickCount % 6 == 0) {
            if (Boundary.isIn(player, AREA)) {
                if (fireAttackExecuting)
                    return;

                if (performedAttacks == 3) {

                    switch (currentStage) {
                        case FLAME:
                            walkToCenter();
                            break;
                        case LIGHTNING:
                            sendLightningProjectile();
                            break;
                        case POISON:
                        case ENRAGED:
                            sendPoisonBlob();
                            break;
                        default:
                            break;

                    }
                } else {
                    if (performedAttacks != 0 && performedAttacks % 3 == 0 || currentStage == HydraStage.ENRAGED)
                        magic = !magic;

                    npc.startAnimation(ATTACK_ANIMS[currentStage.ordinal()][(magic ? RIGHT_HEAD : LEFT_HEAD)]);
                    if (magic) {
                        if (currentStage == HydraStage.POISON || currentStage == HydraStage.LIGHTNING) {
                            sendProjectile(RANGED_PROJECTILE);
                            projectileDamage();
                        }
                        CombatProjectile rangedProjOffset = CombatProjectile.copy(RANGED_PROJECTILE).setSpeed(105).setStartHeight(55);
                        sendProjectile(rangedProjOffset);
                        projectileDamage();
                    } else {
                        if (currentStage == HydraStage.POISON) {
                            sendProjectile(MAGIC_PROJECTILE);
                            projectileDamage();
                        }
                        CombatProjectile magicProjOffset = CombatProjectile.copy(MAGIC_PROJECTILE).setSpeed(105).setStartHeight(55);
                        sendProjectile(magicProjOffset);
                        projectileDamage();
                    }
                }
                performedAttacks++;

            }
        }
    }

    private void projectileDamage() {
        npc.attackType = magic ? CombatType.MAGE : CombatType.RANGE;

        CycleEventHandler.getSingleton().addEvent(npc, new CycleEvent() {

            CombatType type = npc.attackType;
            int damage = getRandomMaxHit(npc.attackType, buffed ? 26 : 17);

            @Override
            public void execute(CycleEventContainer container) {
                if (player.getHealth().getAmount() - damage < 0) {
                    damage = player.getHealth().getAmount();
                }
                player.logoutDelay = System.currentTimeMillis();
                if (((player.protectingMagic() && type == CombatType.MAGE) ||
                        (player.protectingRange() && type == CombatType.RANGE))) {
                    damage = 0;
                }

                player.appendDamage(damage, (damage > 0 ? Hitmark.HIT : Hitmark.MISS));
                player.addDamageTaken(npc, damage);

                container.stop();

            }

        }, 3);
    }

    public int getRandomMaxHit(CombatType type, int maxhit) {
        double attack = npc.attack; //npc.getStats().getAttackForStyle(npc, type);
        double defence;
        int def = 0;
        switch (type) {
            case MAGE:
                def = player.getCombat().mageDef();
                break;
            case MELEE:
                def = player.getCombat().calculateMeleeDefence();
                break;
            case RANGE:
                def = player.getCombat().calculateRangeDefence();
                break;
            default:
                break;
        }
        defence = player.playerLevel[player.playerDefence] + (2 * def);

        double probability = (attack / defence) * 100;
        if (probability > 0.90)
            probability = 0.90;
        else if (probability < 0.05)
            probability = 0.05;
        if (probability < Math.random())
            return 0;
        return Misc.random(maxhit);
    }


    private void sendLightningProjectile() {
        List<Location> steps = Stream
                .of(LIGHTNING_SPAWNS)
                .map(loc -> new Location(loc.getX(), loc.getY(), height))
                .collect(Collectors.toList());//Create copy
        CycleEventHandler.getSingleton().addEvent(npc, new CycleEvent() {

            int tick = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (npc.isDead || player.isDead) {
                    container.stop();
                    return;
                }

                if (tick == 0) {
                    npc.startAnimation(ATTACK_ANIMS[currentStage.ordinal()][MIDDLE_HEAD]);
                    steps.stream().forEach(loc -> sendProjectileToTile(loc, LIGHTNING_PROJECTILE));
                } else if (tick >= 2 && tick <= 15) {
                    steps.stream().forEach(loc -> {
                    	Location nextStep = Region.getNextStepLocation(loc.getX(), loc.getY(), player.getX(), player.getY(), height, 1, 1);
                    	loc.setX(nextStep.getX());
                    	loc.setY(nextStep.getY());
                        player.getPA().stillGfx(LIGHTNING_GFX, loc.getX(), loc.getY(), 0, 0);
                        if (player.getLocation().equalsIgnoreHeight(loc)) {
                            player.freezeTimer = 5;
                            player.appendDamage(Misc.random(5, 20), Hitmark.HIT);
                        }
                    });
                } else if (tick >= 15) {
                    container.stop();
                }

                tick++;
            }

        }, 1);
    }

    private void sendFireProjectile(Boundary boundary) {
        List<Location> spawnedFire = Lists.newArrayList();
        CycleEventHandler.getSingleton().addEvent(2, npc, new CycleEvent() {

            Location fireFollow = boundary.getMinLocation();
            int tick = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (npc.isDead || player.isDead) {
                    container.stop();
                    return;
                }
                if (!spawnedFire.isEmpty()) {
                    if (tick >= 30)
                        spawnedFire.remove(0);

                    boolean inFire = spawnedFire.stream().anyMatch(loc -> loc.equalsIgnoreHeight(player.getLocation()));
                    if (inFire) {
                        player.appendDamage(Misc.random(1, 20), Hitmark.HIT);
                    }
                }
                if (tick == 0) {
                    npc.facePlayer(player.getIndex());
                    npc.startAnimation(ATTACK_ANIMS[currentStage.ordinal()][MIDDLE_HEAD]);
                    sendProjectileToTile(fireFollow, FIRE_PROJECTILE);
                } else if (tick >= 1 && tick <= 14) {
                    fireFollow = Region.getNextStepLocation(fireFollow.getX(), fireFollow.getY(), player.getX(), player.getY(), player.getHeight(), 1, 1);
                    if (!spawnedFire.contains(fireFollow)) {
                        player.getPA().stillGfx(FLOOR_FIRE, fireFollow.getX(), fireFollow.getY(), 0, 0);
                        spawnedFire.add(fireFollow);
                    }
                } else if (tick == 15) {
                    fireAttackExecuting = false;
                } else if (tick >= 60) {
                    container.stop();
                }

                tick++;
            }

        }, 1);
    }

    private void fireSpecial() {
        Boundary[] fireZones = calculateFireBoundaries();

        CycleEventHandler.getSingleton().addEvent(1, npc, new CycleEvent() {

            int tick = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (npc.isDead || player.isDead) {
                    container.stop();
                    return;
                }
                if (tick == 0) {
                    player.freezeTimer = 12;
                    npc.turnNpc(Boundary.centre(fireZones[0]).getX(), Boundary.centre(fireZones[0]).getY());
                } else if (tick == 2) {
                    npc.startAnimation(ATTACK_ANIMS[currentStage.ordinal()][MIDDLE_HEAD]);
                } else if (tick == 4) {
                    int xLoc = fireZones[0] == WEST ? fireZones[0].getMaximumX() : fireZones[0].getMinimumX();
                    int yLoc = fireZones[0] == SOUTH ? fireZones[0].getMaximumY() : fireZones[0].getMinimumY();
                    for (int height = 0; height < 8; height++) {
                        for (int width = 0; width < 4; width++) {
                            int xOff = fireZones[0] == EAST || fireZones[0] == WEST ? height : width;
                            int yOff = fireZones[0] == NORTH || fireZones[0] == SOUTH ? height : width;
                            if (fireZones[0] == SOUTH)
                                yOff *= -1;
                            if (fireZones[0] == WEST)
                                xOff *= -1;
                            player.getPA().stillGfx(FLOOR_FIRE, xLoc + xOff, yLoc + yOff, 0, 0);
                        }
                    }
                } else if (tick == 6) {
                    npc.turnNpc(Boundary.centre(fireZones[1]).getX(), Boundary.centre(fireZones[1]).getY());
                } else if (tick == 8) {
                    npc.startAnimation(ATTACK_ANIMS[currentStage.ordinal()][MIDDLE_HEAD]);
                } else if (tick == 10) {
                    int xLoc = fireZones[1] == WEST ? fireZones[1].getMaximumX() : fireZones[1].getMinimumX();
                    int yLoc = fireZones[1] == SOUTH || fireZones[1] == WEST ? fireZones[1].getMaximumY() : fireZones[1].getMinimumY();
                    for (int height = 0; height < 8; height++) {
                        for (int width = 0; width < 4; width++) {
                            int xOff = fireZones[1] == EAST || fireZones[1] == WEST ? height : width;
                            int yOff = fireZones[1] == NORTH || fireZones[1] == SOUTH ? height : width;
                            if (fireZones[1] == SOUTH || fireZones[1] == WEST)
                                yOff *= -1;
                            if (fireZones[1] == WEST)
                                xOff *= -1;

                            player.getPA().stillGfx(FLOOR_FIRE, xLoc + xOff, yLoc + yOff, 0, 0);
                        }
                    }
                } else if (tick == 12) {
                    sendFireProjectile(fireZones[2]);
                } else if (tick >= 61) {
                    container.stop();
                    return;
                }
                if (Boundary.isIn(player, fireZones[0], (tick >= 10 ? fireZones[1] : Boundary.EMPTY)) && tick >= 4 && tick <= 60) {
                    player.appendDamage(Misc.random(1, 20), Hitmark.HIT);
                }
                tick++;
            }

        }, 1);
    }

    private Boundary[] calculateFireBoundaries() {
        Location center = Boundary.centerAsLocation(AREA);
        Boundary[] zones = new Boundary[]{NORTH, EAST, NORTH_EAST}; //defaults
        if (player.getX() >= center.getX() && player.getY() >= center.getY()) { //NORTH_EAST
            zones = new Boundary[]{NORTH, EAST, NORTH_EAST};
        } else if (player.getX() >= center.getX() && player.getY() <= center.getY()) { //SOUTH_EAST
            zones = new Boundary[]{SOUTH, EAST, SOUTH_EAST};
        } else if (player.getX() <= center.getX() && player.getY() >= center.getY()) { //NORTH_WEST
            zones = new Boundary[]{NORTH, WEST, NORTH_WEST};
        } else if (player.getX() <= center.getX() && player.getY() <= center.getY()) { //SOUTH_WEST
            zones = new Boundary[]{SOUTH, WEST, SOUTH_WEST};
        }
        return zones;
    }

    private void walkToCenter() {
        fireAttackExecuting = true;
        CycleEventHandler.getSingleton().addEvent(0, npc, new CycleEvent() {

            int cycle = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (npc.isDead || player.isDead) {
                    container.stop();
                    return;
                }

                if (cycle == 0) {
                    npc.killerId = 0;
                    npc.facePlayer(0);
                    npc.underAttack = false;
                    npc.lastRandomlySelectedPlayer = 0;
                    npc.turnNpc(CENTER.getX(), CENTER.getY());
                }
                if (!npc.getLocation().equalsIgnoreHeight(CENTER)) {
                    npc.walkingHome = false;
                    NPCDumbPathFinder.walkTowards(npc, CENTER.getX(), CENTER.getY());
                } else {//Sometimes bugs out visually
                    npc.teleport(CENTER.getX(), CENTER.getY(), height);
                    fireSpecial();
                    container.stop();
                }
                if (cycle >= 16) {
                    npc.teleport(CENTER.getX(), CENTER.getY(), height);
                    fireSpecial();
                    container.stop();
                }
                cycle++;
            }

        }, 1);
    }

    private void sendPoisonBlob() {
        Location targetLoc = player.getLocation();
        if (currentStage == HydraStage.ENRAGED && Misc.random(2) == 0) {
            CycleEventHandler.getSingleton().addEvent(npc, new CycleEvent() {

                int tick = 0;

                @Override
                public void execute(CycleEventContainer container) {
                    if (npc.isDead || player.isDead) {
                        container.stop();
                        return;
                    }
                    if (tick >= 16) {
                        container.stop();
                        return;
                    }
                    if (tick == 0) {
                        npc.startAnimation(ATTACK_ANIMS[currentStage.ordinal()][MIDDLE_HEAD]);
                        sendProjectileToTile(targetLoc, POISON_PROJECTILE);
                    } else if (tick == 1) {
                        player.getPA().stillGfx(POISON_SPLASH, targetLoc.getX(), targetLoc.getY(), 0, 0);
                    } else if (tick == 2) {

                        player.getPA().stillGfx(Misc.random(POISON_POOL_MIN, POISON_POOL_MAX), targetLoc.getX(), targetLoc.getY(), 0, 0);

                    }
                    if (tick >= 3) {

                        boolean targetInLocation = targetLoc.equalsIgnoreHeight(player.getLocation());
                        if (targetInLocation) {
                            player.appendDamage(Misc.random(1, 12), Hitmark.HIT);
                            player.getHealth().proposeStatus(HealthStatus.POISON, 6, Optional.of(npc));
                        }
                    }
                    tick++;
                }

            }, 1);
        } else if (currentStage == HydraStage.POISON) {
            List<Location> tiles = Misc.shuffle(targetLoc.getSurrounding(3)).stream().limit(Misc.random(2, 5)).collect(Collectors.toList());

            CycleEventHandler.getSingleton().addEvent(npc, new CycleEvent() {

                int tick = 0;

                @Override
                public void execute(CycleEventContainer container) {
                    if (npc.isDead || player.isDead) {
                        container.stop();
                        return;
                    }
                    if (tick >= 18) {
                        container.stop();
                        return;
                    }
                    if (tick == 0) {
                        npc.startAnimation(ATTACK_ANIMS[currentStage.ordinal()][MIDDLE_HEAD]);
                    }
                    tiles.forEach(tile -> {
                        if (tick == 0) {

                            sendProjectileToTile(tile, POISON_PROJECTILE);
                        } else if (tick == 4) {
                            player.getPA().stillGfx(POISON_SPLASH, tile.getX(), tile.getY(), 0, 0);
                        } else if (tick == 5) {

                            player.getPA().stillGfx(Misc.random(POISON_POOL_MIN, POISON_POOL_MAX), tile.getX(), tile.getY(), 0, 0);

                        }
                        if (tick >= 6) {

                            boolean targetInLocation = tile.equals(player.getLocation());
                            if (targetInLocation) {
                                player.appendDamage(Misc.random(1, 12), Hitmark.HIT);
                                player.getHealth().proposeStatus(HealthStatus.POISON, 6, Optional.of(npc));
                            }
                        }
                    });

                    container.setTick(1);
                    tick++;
                }

            }, 1);
        }

    }

    public void sendProjectile(CombatProjectile projectile) {
        int size = (int) Math.ceil((double) npc.getSize() / 2.0);

        int centerX = npc.getX() + size;
        int centerY = npc.getY() + size;
        int offsetX = (centerY - player.getY()) * -1;
        int offsetY = (centerX - player.getX()) * -1;
        player.getPA().createPlayersProjectile(centerX, centerY, offsetX, offsetY, projectile.getAngle(), projectile.getSpeed(), projectile.getGfx(), projectile.getStartHeight(), projectile.getEndHeight(), -player.getIndex() - 1, 65, projectile.getDelay());
    }

    public void sendProjectileToTile(Location target, CombatProjectile projectile) {
        int size = (int) Math.ceil((double) npc.getSize() / 2.0);

        int centerX = npc.getX() + size;
        int centerY = npc.getY() + size;
        int offsetX = (centerY - target.getY()) * -1;
        int offsetY = (centerX - target.getX()) * -1;
        player.getPA().createPlayersProjectile(centerX, centerY, offsetX, offsetY, projectile.getAngle(), projectile.getSpeed(), projectile.getGfx(), projectile.getStartHeight(), projectile.getEndHeight(), -1, 65, projectile.getDelay());
    }


    private boolean transforming;
    
    public void checkTransform() {
    	
        if (npc != null) {
            if (npc.getHealth().getAmount() <= currentStage.getHealth() - 275) {
            	transforming = true;
                npc.freezeTimer = 5;
                if (currentStage == HydraStage.ENRAGED) {
					npc.startAnimation(currentStage.getDeathAnimation());
                	 CycleEventHandler.getSingleton().addEvent(npc, new CycleEvent() {
                         @Override
						public void execute(CycleEventContainer container) {
                        	 if (container.getTotalTicks() == 4) {
								npc.startAnimation(8258);
								npc.requestTransform(currentStage.getDeathID());
							} else if(container.getTotalTicks() == 10) {
								transforming = false;
								container.stop();
							}

						}
                     }, 1);
                    return;
                }
                
                HydraStage lastStage = currentStage;
                npc.requestTransform(lastStage.getDeathID());
                
                currentStage = HydraStage.values()[currentStage.ordinal() + 1];
                sprayed = currentStage == HydraStage.ENRAGED;
                buffed = false;
                performedAttacks = 0;

                CycleEventHandler.getSingleton().addEvent(npc, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                    	if(container.getTotalTicks() == 1) {
                            npc.startAnimation(lastStage.getDeathAnimation());
                    	}
						if (container.getTotalTicks() == (lastStage == HydraStage.POISON ? 4 : 3)) {
							npc.startAnimation(currentStage.getTransformation());
							npc.requestTransform(currentStage.getNpcId());
							transforming = false;
							tickCount = 0;
							container.stop();
						}

                    }
                }, 1);

            }
        }
    }
    
    public void setupVents() {
    	player.getPA().sendPlayerObjectAnimation(player, 1371, 10263, 5771, 10, 0, 0);
    	player.getPA().sendPlayerObjectAnimation(player, 1371, 10272, 5771, 10, 0, 0);
    	player.getPA().sendPlayerObjectAnimation(player, 1362, 10272, 5771, 10, 0, 0);
    }
    

	public void respawn() {
		tickCount = 0;
		performedAttacks = 0;
		buffed = false;
		fireAttackExecuting = false;
		sprayed = false;
		currentStage = HydraStage.POISON;
		transforming = false;
        npc = NPCHandler.spawnNpc(this, currentStage.getNpcId(), CENTER.getX(), CENTER.getY(), getHeight(), 0, currentStage.getHealth(), 1, 1, 1);
        npc.getHealth().resolveStatus(HealthStatus.POISON, Integer.MAX_VALUE);
        npc.getHealth().resolveStatus(HealthStatus.VENOM, Integer.MAX_VALUE);
	}

	public void movePhase() {
		npc.appendDamage(275, Hitmark.HIT);
	}
}*/
