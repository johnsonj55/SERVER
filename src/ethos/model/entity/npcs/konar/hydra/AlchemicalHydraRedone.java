/*package ethos.model.entity.npcs.konar.hydra;
import com.google.common.collect.Lists;
import ethos.clip.Region;
import ethos.clip.doors.Location;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.entity.HealthStatus;
import ethos.model.entity.Projectile;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDumbPathFinder;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Hitmark;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.util.Misc;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlchemicalHydraRedone extends SingleInstancedArea {

    public AlchemicalHydraRedone(Player player, Boundary boundary, int height) {
        super(player,
                boundary,
                height);


        AlchemicalHydraRedone alchemicalHydraInstance = this;

        if (!Boundary.isIn(player, AlchemicalHydraConstants.AREA)) {

            CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    if (container.getTotalTicks() == 1) {
                        player.getPA().sendScreenFade("", 1, 2);
                    } else if (container.getTotalTicks() >= 4) {
                        player.getPA().movePlayerUnconditionally(player.getX() == 1355 ? player.getX() + 1 : 1356, player.getY(), height);
                        player.getPA().sendScreenFade("The doors slam shut and lock behind you!", -1, 3);

                        if (Boundary.isIn(player, AlchemicalHydraConstants.AREA)) {
                            currentStage = HydraStage.POISON;
                            previousStage = currentStage;
                            isDamageReducted = true;
                            alchemicalHydra = NPCHandler.spawnNpc(currentStage.getNpcId(), AlchemicalHydraConstants.CENTER.getX(), AlchemicalHydraConstants.CENTER.getY(), height, 0, currentStage.getHealth(), isBuffed ? 26 : 17, 260, 100);
                            alchemicalHydra.getHealth().resolveStatus(HealthStatus.POISON, Integer.MAX_VALUE);
                            alchemicalHydra.getHealth().resolveStatus(HealthStatus.VENOM, Integer.MAX_VALUE);
                            setTickCount(0);
                            normalAttacksPerformed = 0;
                            afterTransformationAttackCount = 0;
                            alchemicalHydra.setInstance(alchemicalHydraInstance);
                            player.setInstance(alchemicalHydraInstance);
                            container.stop();
                        }
                    }
                }
            }, 1);
        }

    }

    private NPC alchemicalHydra;
    private HydraStage currentStage;
    private HydraStage previousStage;

    private Location playerLocation;

    private List<Location> poisonTiles;
    private List<Location> lightningTiles;

    private boolean isDamageReducted;
    private boolean isEmpowered;
    private boolean isBuffed;
    private boolean isSprayed;
    private boolean canTransform;
    private boolean isFireAttackExecuting;
    private boolean isWalkingToCenter;
    private boolean isStandingOnCorrectVent;
    private boolean isStandingOnWrongVent;
    private boolean isDoubleProjectile;

    private CombatType previousCombatStyle;

    private String message;

    private int absorbedMessageCount = 0;
    private int neutraliseMessageCount = 0;
    private int afterTransformationAttackCount = 0;
    private int normalAttacksPerformed = 0;
    private int enragedStageAttacks = 0;
    private int tickCount = 0;
    private int incrementableModulus = 3;
    private int previousDamage = 0;


    public void onTick() {
        tickCount++;
        checkHydraTransformation();
        isHydraPositionOnCorrectVent();

        if (currentStage == HydraStage.ENRAGED && isDamageReducted) {
            isDamageReducted = false;
        }

        if (alchemicalHydra != null && player.getHydra() != null) {
            if (tickCount % 4 == 0) {
                sendObjectAnimations();

                if (currentStage != HydraStage.ENRAGED) {
                    if (isStandingOnCorrectVent) {
                        if (neutraliseMessageCount == 0) {
                            alchemicalHydra.forceChat("Rooooaaaaaaaaaaaaaar!");
                            player.getPA().sendMessage("@gre@The chemicals neutralise the Alchemical Hydra's defences!");
                            setNeutraliseMessageCount(1);
                            isSprayed = true;
                            if (absorbedMessageCount == 1) {
                                setAbsorbedMessageCount(0);
                            }
                        }
                    } else if (isStandingOnWrongVent){
                        if (absorbedMessageCount == 0) {
                            isBuffed = true;
                            player.getPA().sendMessage("The chemicals are absorbed by the Alchemical Hydra; empowering it further!");
                            absorbedMessageCount = 1;
                            if (neutraliseMessageCount == 1) {
                                setNeutraliseMessageCount(0);
                            }
                        }
                    }
                }
            }
        }

        if (canTransform) {
            applyTransformation();
        }


        if (isSprayed) {
            isBuffed = false;
            isDamageReducted = false;
        }

        if (isDamageReducted) {
            applyDamageReduction();
        }
    }

    public void doAttack() {
        if (tickCount % 4 == 0) {
            if (Boundary.isIn(player, AlchemicalHydraConstants.AREA)) {
                afterTransformationAttackCount++;
                normalAttacksPerformed++;

                if (isFireAttackExecuting) {
                    return;
                }

                if (normalAttacksPerformed == 0) {
                    alchemicalHydra.attackType = CombatType.getRandom(CombatType.MAGE, CombatType.RANGE);
                    previousCombatStyle = alchemicalHydra.attackType;
                }

                if (currentStage == HydraStage.ENRAGED && Misc.random(1, 5) == 5) {
                    performSinglePoisonAttack();
                }

                if (currentStage == HydraStage.ENRAGED) {
                    enragedStageAttacks++;

                    if (incrementableModulus > 9) {
                        incrementableModulus = 3;
                    }

                    if (enragedStageAttacks % incrementableModulus == 0) {
                        incrementableModulus = incrementableModulus * 3;
                        performPoisonAttack();
                    }
                }

                if (afterTransformationAttackCount == 3) {
                    switch (currentStage) {
                        case POISON:
                            performPoisonAttack();
                            break;
                        case LIGHTNING:
                            performLightningAttack();
                            break;
                        case FLAME:
                            performFlameAttack();
                            break;
                        case ENRAGED:
                            performPoisonAttack();
                            break;
                    }
                } else {
                    alchemicalHydra.startAnimation(AlchemicalHydraConstants.ATTACK_ANIMS[currentStage.ordinal()]
                            [(alchemicalHydra.attackType == CombatType.MAGE ? AlchemicalHydraConstants.RIGHT_HEAD : AlchemicalHydraConstants.LEFT_HEAD)]);

                    if (currentStage != HydraStage.ENRAGED && normalAttacksPerformed % 3 == 0) {
                        alchemicalHydra.attackType = alchemicalHydra.attackType == CombatType.MAGE ? CombatType.RANGE : CombatType.MAGE;
                    } else if (currentStage == HydraStage.ENRAGED) {
                        alchemicalHydra.attackType = alchemicalHydra.attackType == CombatType.MAGE ? CombatType.RANGE : CombatType.MAGE;
                    }

                    if (alchemicalHydra.attackType == CombatType.MAGE) {
                        if (currentStage == HydraStage.POISON || (currentStage == HydraStage.LIGHTNING && isBuffed)) {
                            sendProjectileToTile(AlchemicalHydraConstants.MAGIC_PROJECTILE);
                            isDoubleProjectile = true;
                        }
                        Projectile magicProjectileOffset = Projectile.copy(AlchemicalHydraConstants.MAGIC_PROJECTILE)
                                .setSpeed(105)
                                .setStartHeight(55);
                        sendProjectileToTile(magicProjectileOffset);
                        projectileDamage();
                        player.sendMessage(String.valueOf(alchemicalHydra.attackType));
                    } else {
                        if (currentStage == HydraStage.POISON) {
                            sendProjectileToTile(AlchemicalHydraConstants.RANGED_PROJECTILE);
                            isDoubleProjectile = true;
                        }
                        Projectile rangeProjectileOffset = Projectile.copy(AlchemicalHydraConstants.RANGED_PROJECTILE)
                                .setSpeed(105)
                                .setStartHeight(55);
                        sendProjectileToTile(rangeProjectileOffset);
                        projectileDamage();
                        player.sendMessage(String.valueOf(alchemicalHydra.attackType));
                    }
                }
            }
        }
    }

    private void performPoisonAttack() {
        if (alchemicalHydra.isDead || player.isDead()) {
            return;
        }

        playerLocation = player.getLocation();

        List<Location> poisonTiles = Misc.shuffle(getPlayerLocation().getSurrounding(3).stream()
                .limit(Misc.random(2, 5)).collect(Collectors.toList()));
        poisonTiles.add(getPlayerLocation());

        alchemicalHydra.startAnimation(AlchemicalHydraConstants.ATTACK_ANIMS[currentStage.ordinal()]
                [(AlchemicalHydraConstants.MIDDLE_HEAD)]);

        poisonTiles.forEach(poisonTile -> CycleEventHandler.getSingleton().addEvent(getAlchemicalHydra(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                sendPoisonProjectileOnTick(container, poisonTile);

            }
        }, 1));
    }

    private void performSinglePoisonAttack() {
        if (alchemicalHydra.isDead || player.isDead()) {
            return;
        }

        Location poisonTileLocation = player.getLocation();

        CycleEventHandler.getSingleton().addEvent(getAlchemicalHydra(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (alchemicalHydra.isDead || player.isDead()) {
                    container.stop();
                }

                sendPoisonProjectileOnTick(container, poisonTileLocation);
            }
        }, 1);
    }

    private void sendPoisonProjectileOnTick(CycleEventContainer container, Location poisonTile) {
        if (container.getTotalTicks() == 0) {
            alchemicalHydra.startAnimation(AlchemicalHydraConstants.ATTACK_ANIMS[currentStage.ordinal()]
                    [(AlchemicalHydraConstants.MIDDLE_HEAD)]);
            sendProjectileToTile(poisonTile, AlchemicalHydraConstants.POISON_PROJECTILE);
        } else if (container.getTotalTicks() == 4) {
            player.getPA().stillGfx(AlchemicalHydraConstants.POISON_SPLASH, poisonTile.getX(), poisonTile.getY(), 0, 0);
        } else if (container.getTotalTicks() == 5) {
            player.getPA().stillGfx(Misc.random(AlchemicalHydraConstants.POISON_POOL_MIN, AlchemicalHydraConstants.POISON_POOL_MAX), poisonTile.getX(), poisonTile.getY(), 0, 0);
        }

        onTickPoisonDamage(container, poisonTile);
    }

    private void onTickPoisonDamage(CycleEventContainer container, Location poisonTileLocation) {
        if (container.getTotalTicks() >= 6) {
            if (poisonTileLocation.equalsIgnoreHeight(player.getLocation())) {
                player.appendDamage(Misc.random(1, 12), Hitmark.HIT);
                player.getHealth().proposeStatus(HealthStatus.POISON, 6, Optional.of(alchemicalHydra));
            }
        }

        if (container.getTotalTicks() >= 18) {
            container.stop();
        }
    }

    private void projectileDamage() {
        alchemicalHydra.attackType = alchemicalHydra.attackType == CombatType.MAGE ? CombatType.MAGE : CombatType.RANGE;

        CycleEventHandler.getSingleton().addEvent(alchemicalHydra, new CycleEvent() {

            CombatType combatType = alchemicalHydra.attackType;
            int damage = currentStage == HydraStage.POISON ? getRandomMaxHit(combatType, isBuffed ? 26 : 17)
                    : currentStage == HydraStage.LIGHTNING ? getRandomMaxHit(combatType, isBuffed ? 35 : 26)
                    : currentStage == HydraStage.FLAME ? getRandomMaxHit(combatType, isBuffed ? 52 : 35)
                    : getRandomMaxHit(combatType, 52);

            @Override
            public void execute(CycleEventContainer container) {
                if (player.getHealth().getAmount() - damage < 0) {
                    damage = player.getHealth().getAmount();
                }
                player.logoutDelay = System.currentTimeMillis();
                if (((player.protectingMagic() && combatType == CombatType.MAGE) ||
                        (player.protectingRange() && combatType == CombatType.RANGE))) {
                    damage = 0;
                }

                previousDamage = damage;

                if (isDoubleProjectile) {
                    player.addDamageTaken(alchemicalHydra, damage);
                    player.addDamageTaken(alchemicalHydra, previousDamage);
                    player.appendDamage(damage, (damage > 0 ? Hitmark.HIT : Hitmark.MISS));
                    player.appendDamage(previousDamage, (previousDamage > 0 ? Hitmark.HIT : Hitmark.MISS));
                    isDoubleProjectile = false;
                } else {
                    player.addDamageTaken(alchemicalHydra, damage);
                    player.appendDamage(damage, (damage > 0 ? Hitmark.HIT : Hitmark.MISS));
                }

                container.stop();

            }

        }, 3);
    }

    public int getRandomMaxHit(CombatType type, int maxhit) {
        double attack = alchemicalHydra.attack; //npc.getStats().getAttackForStyle(npc, type);
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

    private void performFlameAttack() {
        isWalkingToCenter = true;

        if (alchemicalHydra.isDead || player.isDead()) {
            return;
        }


        CycleEventHandler.getSingleton().addEvent(alchemicalHydra, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (alchemicalHydra.isDead || player.isDead()) {
                    container.stop();
                    return;
                }


                alchemicalHydra.killerId = 0;
                alchemicalHydra.underAttack = false;
                alchemicalHydra.underAttackBy = 0;
                alchemicalHydra.lastRandomlySelectedPlayer = 0;
                alchemicalHydra.setFacePlayer(false);
                player.getCombat().resetPlayerAttack();
                alchemicalHydra.turnNpc(AlchemicalHydraConstants.CENTER.getX(), AlchemicalHydraConstants.CENTER.getY());


                if (!alchemicalHydra.getLocation().equalsIgnoreHeight(AlchemicalHydraConstants.CENTER)) {
                    alchemicalHydra.walkingHome = false;
                    NPCDumbPathFinder.walkTowards(alchemicalHydra, AlchemicalHydraConstants.CENTER.getX(), AlchemicalHydraConstants.CENTER.getY());
                } else {
                    alchemicalHydra.teleport(AlchemicalHydraConstants.CENTER.getX(), AlchemicalHydraConstants.CENTER.getY(), getHeight());
                }

                if (alchemicalHydra.getLocation().equalsIgnoreHeight(AlchemicalHydraConstants.CENTER)) {
                    player.freezeTimer = 12;
                    performFireSpecial();
                    container.stop();
                }

                if (container.getTotalTicks() >= 16 && !alchemicalHydra.getLocation()
                        .equalsIgnoreHeight(AlchemicalHydraConstants.CENTER)) {

                    alchemicalHydra.teleport(AlchemicalHydraConstants.CENTER.getX(), AlchemicalHydraConstants.CENTER.getY(), getHeight());

                    if (!isFireAttackExecuting) {
                        player.freezeTimer = 12;
                        performFireSpecial();
                        container.stop();
                    }
                }
            }
        }, 1);
    }

    private void performFireSpecial() {
        Boundary[] fireZones = calculateFireBoundaries();
        NPC flameAlchemicalHydra = getAlchemicalHydra();

        CycleEventHandler.getSingleton().addEvent(getAlchemicalHydra(), new CycleEvent() {
            int flameAttackCount = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (container.getTotalTicks() == 0) {
                    isFireAttackExecuting = true;
                    flameAlchemicalHydra.turnNpc(Boundary.centre(fireZones[0]).getX(), Boundary.centre(fireZones[0]).getY());
                } else if (container.getTotalTicks() == 2) {
                    flameAlchemicalHydra.startAnimation(8248);
                } else if (container.getTotalTicks() == 4) {
                    int xLocation = fireZones[0] == AlchemicalHydraConstants.WEST ? fireZones[0].getMaximumX() : fireZones[0].getMinimumX();
                    int yLocation = fireZones[0] == AlchemicalHydraConstants.SOUTH ? fireZones[0].getMaximumY() : fireZones[0].getMinimumY();

                    for (int height = 0; height < 8; height++) {
                        for (int width = 0; width < 4; width++) {
                            int xOff = fireZones[0] == AlchemicalHydraConstants.EAST || fireZones[0] == AlchemicalHydraConstants.WEST ? height : width;
                            int yOff = fireZones[0] == AlchemicalHydraConstants.NORTH || fireZones[0] == AlchemicalHydraConstants.SOUTH ? height : width;
                            if (fireZones[0] == AlchemicalHydraConstants.SOUTH) {
                                yOff *= -1;
                            }
                            if (fireZones[0] == AlchemicalHydraConstants.WEST) {
                                xOff *= -1;
                            }
                            player.getPA().stillGfx(AlchemicalHydraConstants.FLOOR_FIRE, xLocation + xOff, yLocation + yOff, 0, 0);
                        }
                    }
                } else if (container.getTotalTicks() == 6) {
                    flameAlchemicalHydra.turnNpc(Boundary.centre(fireZones[1]).getX(), Boundary.centre(fireZones[1]).getY());
                } else if (container.getTotalTicks() == 8) {
                    flameAlchemicalHydra.startAnimation(8248);
                } else if (container.getTotalTicks() == 10) {
                    int xLocation = fireZones[1] == AlchemicalHydraConstants.WEST ? fireZones[1].getMaximumX() : fireZones[1].getMinimumX();
                    int yLocation = fireZones[1] == AlchemicalHydraConstants.SOUTH || fireZones[1] == AlchemicalHydraConstants.WEST ? fireZones[1].getMaximumY() : fireZones[1].getMinimumY();
                    for (int height = 0; height < 8; height++) {
                        for (int width = 0; width < 4; width++) {
                            int xOff = fireZones[1] == AlchemicalHydraConstants.EAST || fireZones[1] == AlchemicalHydraConstants.WEST ? height : width;
                            int yOff = fireZones[1] == AlchemicalHydraConstants.NORTH || fireZones[1] == AlchemicalHydraConstants.SOUTH ? height : width;
                            if (fireZones[1] == AlchemicalHydraConstants.SOUTH || fireZones[1] == AlchemicalHydraConstants.WEST)
                                yOff *= -1;
                            if (fireZones[1] == AlchemicalHydraConstants.WEST)
                                xOff *= -1;

                            player.getPA().stillGfx(AlchemicalHydraConstants.FLOOR_FIRE, xLocation + xOff, yLocation + yOff, 0, 0);
                        }
                    }
                } else if (container.getTotalTicks() == 12) {
                    sendFireProjectile(fireZones[2]);
                } else if (container.getTotalTicks() >= 61) {
                    container.stop();
                    return;
                }

                if (Boundary.isIn(player, fireZones[0],
                        (container.getTotalTicks() >= 10 ? fireZones[1] : Boundary.EMPTY))
                        && container.getTotalTicks() >= 4
                        && container.getTotalTicks() <= 60) {

                    if (flameAttackCount == 0) {
                        player.appendDamage(Misc.random(1, 20), Hitmark.HIT);
                    }

                    if (flameAttackCount < 5) {
                        CycleEventHandler.getSingleton().addEvent(alchemicalHydra, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                if (container.getTotalTicks() == 2) {
                                    player.appendDamage(5, Hitmark.HIT);
                                    flameAttackCount++;
                                } else if (container.getTotalTicks() == 12 || flameAttackCount > 5) {
                                    flameAttackCount = 0;
                                    container.stop();
                                }
                            }
                        }, 1);
                    }
                }
            }
        }, 1);
    }

    private void sendFireProjectile(Boundary fireZone) {
        List<Location> spawnedFireLocation = Lists.newArrayList();

        CycleEventHandler.getSingleton().addEvent(2, alchemicalHydra, new CycleEvent() {

            Location fireFollow = player.getLocation();

            @Override
            public void execute(CycleEventContainer container) {
                if (alchemicalHydra.isDead || player.isDead()) {
                    container.stop();
                    return;
                }

                if (container.getTotalTicks() == 0) {
                    alchemicalHydra.facePlayer(player.getIndex());
                    alchemicalHydra.startAnimation(8248);
                    sendProjectileToTile(fireFollow, AlchemicalHydraConstants.FIRE_PROJECTILE);
                } else if (container.getTotalTicks() >= 1 && container.getTotalTicks() <= 14) {
                    fireFollow = Region.getNextStepLocation(fireFollow.getX(), fireFollow.getY(), getPlayer().getX(), getPlayer().getY(), getPlayer().getHeight(), 1, 1);

                    if (container.getTotalTicks() == 6) {
                        isFireAttackExecuting = false;
                    }

                    if (!spawnedFireLocation.contains(fireFollow)) {
                        player.getPA().stillGfx(AlchemicalHydraConstants.FLOOR_FIRE, fireFollow.getX(), fireFollow.getY(), 0, 0);
                        spawnedFireLocation.add(fireFollow);
                    }
                } else if (container.getTotalTicks() >= 60) {
                    container.stop();
                }
            }
        }, 1);
    }

    private void performLightningAttack() {
        List<Location> lightningTiles = Stream.of(AlchemicalHydraConstants.LIGHTNING_SPAWNS)
                .map(lightningTile -> new Location(lightningTile.getX(), lightningTile.getY(), getHeight()))
                .collect(Collectors.toList());
        setLightningTiles(lightningTiles);

        CycleEventHandler.getSingleton().addEvent(alchemicalHydra, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (alchemicalHydra.isDead || player.isDead()) {
                    container.stop();
                    return;
                }

                if (container.getTotalTicks() == 0) {
                    alchemicalHydra.startAnimation(AlchemicalHydraConstants.ATTACK_ANIMS[currentStage.ordinal()][AlchemicalHydraConstants.MIDDLE_HEAD]);
                    lightningTiles.forEach(location -> sendProjectileToTile(location, AlchemicalHydraConstants.LIGHTNING_PROJECTILE));
                } else if (container.getTotalTicks() >= 2 && container.getTotalTicks() <= 15) {
                    lightningTiles.forEach(location -> {
                        Location nextLightningTileLocation = Region.getNextStepLocation(location.getX(), location.getY(), player.getX(), player.getY(), getHeight(), 1, 1);
                        location.setX(nextLightningTileLocation.getX());
                        location.setY(nextLightningTileLocation.getY());

                        player.getPA().stillGfx(AlchemicalHydraConstants.LIGHTNING_GFX, location.getX(), location.getY(), 0, 0);

                        if (player.getLocation().equalsIgnoreHeight(location)) {
                            player.freezeTimer = 5;
                            player.appendDamage(Misc.random(5, 20), Hitmark.HIT);
                        }
                    });
                } else if (container.getTotalTicks() >= 15 || currentStage != HydraStage.LIGHTNING) {
                    container.stop();
                }
            }
        }, 1);
    }

    private void applyDamageReduction() {
        player.getDamageQueue().getQueue().forEach(damage -> damage.setAmount(damage.getAmount() - (damage.getAmount() * 75 / 100)));
    }

    private void sendObjectAnimations() {
        player.getPA().objectAnim(1371, 10263, 8280, 10, 45);
        player.getPA().objectAnim(1370, 10272, 8280, 10, 45);
        player.getPA().objectAnim(1362, 10272, 8280, 10, 45);
    }

    private void checkHydraTransformation() {
        if (alchemicalHydra != null) {
            if (alchemicalHydra.getHealth().getAmount() <= currentStage.getHealth() - 275) {
                if (currentStage == HydraStage.ENRAGED) {
                    return;
                }

                canTransform = true;
            }
        }
    }

    private Boundary[] calculateFireBoundaries() {
        Location center = Boundary.centerAsLocation(AlchemicalHydraConstants.AREA);
        Boundary[] zones = new Boundary[]{AlchemicalHydraConstants.NORTH, AlchemicalHydraConstants.EAST, AlchemicalHydraConstants.NORTH_EAST}; //defaults
        if (player.getX() >= center.getX() && player.getY() >= center.getY()) { //NORTH_EAST
            zones = new Boundary[]{AlchemicalHydraConstants.NORTH, AlchemicalHydraConstants.EAST, AlchemicalHydraConstants.NORTH_EAST};
        } else if (player.getX() >= center.getX() && player.getY() <= center.getY()) { //SOUTH_EAST
            zones = new Boundary[]{AlchemicalHydraConstants.SOUTH, AlchemicalHydraConstants.EAST, AlchemicalHydraConstants.SOUTH_EAST};
        } else if (player.getX() <= center.getX() && player.getY() >= center.getY()) { //NORTH_WEST
            zones = new Boundary[]{AlchemicalHydraConstants.NORTH, AlchemicalHydraConstants.WEST, AlchemicalHydraConstants.NORTH_WEST};
        } else if (player.getX() <= center.getX() && player.getY() <= center.getY()) { //SOUTH_WEST
            zones = new Boundary[]{AlchemicalHydraConstants.SOUTH, AlchemicalHydraConstants.WEST, AlchemicalHydraConstants.SOUTH_WEST};
        }
        return zones;
    }

    private void applyTransformation() {
        if (currentStage == HydraStage.ENRAGED) {
            return;
        }

        previousStage = currentStage;
        setCurrentStage(HydraStage.values()[currentStage.ordinal() + 1]);

        CycleEventHandler.getSingleton().addEvent(getAlchemicalHydra(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getTotalTicks() == 0) {
                    alchemicalHydra.requestTransform(currentStage.getDeathID());
                    alchemicalHydra.startAnimation(currentStage.getDeathAnimation());
                } else if (container.getTotalTicks() == 2) {
                    alchemicalHydra.requestTransform(currentStage.getNpcId());
                    alchemicalHydra.startAnimation(currentStage.getTransformation());
                } else if (container.getTotalTicks() == 4) {
                    container.stop();
                }
            }
        }, 1);

        resetHydraVariablesOnPhaseStart();
    }

    private void resetHydraVariablesOnPhaseStart() {
        isSprayed = false;
        isBuffed = false;
        absorbedMessageCount = 0;
        neutraliseMessageCount = 0;
        afterTransformationAttackCount = 0;
        isStandingOnCorrectVent = false;
        isStandingOnWrongVent = false;
        canTransform = false;
        if (currentStage != HydraStage.ENRAGED) {
            isDamageReducted = true;
        }
    }

    private void isHydraPositionOnCorrectVent() {
        if (!currentStage.equals(HydraStage.ENRAGED)) {
            Predicate<HydraStage> hydraStageCorrectVentPredicate = filteredStage -> Boundary.isIn(alchemicalHydra, filteredStage.getBoundary());
            Predicate<HydraStage> hydraStageIncorrectVentPredicate = filteredStage -> Boundary.isIn(alchemicalHydra, filteredStage.getBoundary());

            if (HydraStage.stream().filter(currentStage -> currentStage.getNpcId() == this.currentStage.getNpcId())
                    .anyMatch(hydraStageCorrectVentPredicate)) {
                isStandingOnWrongVent = false;
                isStandingOnCorrectVent = true;

            }

            if (HydraStage.stream().filter(stage -> stage.getNpcId() != this.currentStage.getNpcId())
                    .filter(filteredStages -> filteredStages.getBoundary() != AlchemicalHydraConstants.AREA)
                    .anyMatch(hydraStageIncorrectVentPredicate)) {
                isStandingOnCorrectVent = false;
                isStandingOnWrongVent = true;
            }
        }
    }

    public void sendProjectileToTile(Location target, Projectile projectile) {
        int size = (int) Math.ceil((double) getAlchemicalHydra().getSize() / 2.0);

        int centerX = getAlchemicalHydra().getX() + size;
        int centerY = getAlchemicalHydra().getY() + size;
        int offsetX = (centerY - target.getY()) * -1;
        int offsetY = (centerX - target.getX()) * -1;
        player.getPA().createPlayersProjectile(centerX, centerY, offsetX, offsetY, projectile.getAngle(), projectile.getSpeed(), projectile.getGfx(), projectile.getStartHeight(), projectile.getEndHeight(), -1, 65, projectile.getDelay());
    }

    private void sendProjectileToTile(Projectile rangedProjectile) {
        int size = (int) Math.ceil((double) getAlchemicalHydra().getSize() / 2.0);

        int centerX = getAlchemicalHydra().getX() + size;
        int centerY = getAlchemicalHydra().getY() + size;
        int offsetX = (centerY - getPlayer().getY()) * -1;
        int offsetY = (centerX - getPlayer().getX()) * -1;
        player.getPA().createPlayersProjectile(centerX, centerY, offsetX, offsetY, rangedProjectile.getAngle(), rangedProjectile.getSpeed(), rangedProjectile.getGfx(), rangedProjectile.getStartHeight(), rangedProjectile.getEndHeight(), -1, 65, rangedProjectile.getDelay());
    }

    public final void endInstance(DisposeTypes disposeTypes) {
        if (player == null) {
            return;
        }

        if (disposeTypes == DisposeTypes.INCOMPLETE) {
            NPCHandler.kill(alchemicalHydra.npcType, getHeight());

            if (player.getHydra() != null) {
                InstancedAreaManager.getSingleton().disposeOf(player.getHydra());
            }


//            CycleEventHandler.getSingleton().addEvent(alchemicalHydra, new CycleEvent() {
//                @Override
//                public void execute(CycleEventContainer container) {
//
//                    if (container.getTotalTicks() == 0) {
//                        NPCHandler.kill(alchemicalHydra.npcType, getHeight());
//                        NPCHandler.kill(alreadySpawnedHydra.npcType, alreadySpawnedHydra.getHeight());
//                    } else if (container.getTotalTicks() == 5) {
////                        NPCHandler.deregister(alchemicalHydra);
//                        NPCHandler.deregister(alreadySpawnedHydra);
//                        container.stop();
//                    }
//                }
//            } ,1);
        }
    }

    @Override
    public void onDispose() {
        endInstance(DisposeTypes.INCOMPLETE);
        NPCHandler.kill(alchemicalHydra.npcType, getHeight());
        alchemicalHydra.needRespawn = false;
        alchemicalHydra = null;
        InstancedAreaManager.getSingleton().disposeOf(player.getHydra());
    }

    private boolean isHydraEligibleForTransformation() {
        return canTransform;
    }

    public NPC getAlchemicalHydra() {
        return alchemicalHydra;
    }

    public void setAlchemicalHydra(NPC alchemicalHydra) {
        this.alchemicalHydra = alchemicalHydra;
    }

    public HydraStage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(HydraStage currentStage) {
        this.currentStage = currentStage;
    }

    public boolean isEmpowered() {
        return isEmpowered;
    }

    public void setEmpowered(boolean empowered) {
        isEmpowered = empowered;
    }

    public boolean isBuffed() {
        return isBuffed;
    }

    public void setBuffed(boolean buffed) {
        isBuffed = buffed;
    }

    public boolean isSprayed() {
        return isSprayed;
    }

    public void setSprayed(boolean sprayed) {
        isSprayed = sprayed;
    }

    public boolean isCanTransform() {
        return canTransform;
    }

    public void setCanTransform(boolean canTransform) {
        this.canTransform = canTransform;
    }

    public boolean isDamageReducted() {
        return isDamageReducted;
    }

    public void setDamageReducted(boolean damageReducted) {
        isDamageReducted = damageReducted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAbsorbedMessageCount() {
        return absorbedMessageCount;
    }

    public void setAbsorbedMessageCount(int absorbedMessageCount) {
        this.absorbedMessageCount = absorbedMessageCount;
    }

    public CombatType getPreviousCombatStyle() {
        return previousCombatStyle;
    }

    public void setPreviousCombatStyle(CombatType previousCombatStyle) {
        this.previousCombatStyle = previousCombatStyle;
    }

    public int getAfterTransformationAttackCount() {
        return afterTransformationAttackCount;
    }

    public void setAfterTransformationAttackCount(int afterTransformationAttackCount) {
        this.afterTransformationAttackCount = afterTransformationAttackCount;
    }

    public Location getPlayerLocation() {
        return playerLocation;
    }

    public void setPlayerLocation(Location playerLocation) {
        this.playerLocation = playerLocation;
    }

    public List<Location> getPoisonTiles() {
        return poisonTiles;
    }

    public void setPoisonTiles(List<Location> poisonTiles) {
        this.poisonTiles = poisonTiles;
    }

    public List<Location> getLightningTiles() {
        return lightningTiles;
    }

    public void setLightningTiles(List<Location> lightningTiles) {
        this.lightningTiles = lightningTiles;
    }

    public HydraStage getPreviousStage() {
        return previousStage;
    }

    public void setPreviousStage(HydraStage previousStage) {
        this.previousStage = previousStage;
    }

    public boolean isFireAttackExecuting() {
        return isFireAttackExecuting;
    }

    public void setFireAttackExecuting(boolean fireAttackExecuting) {
        isFireAttackExecuting = fireAttackExecuting;
    }

    public boolean isWalkingToCenter() {
        return isWalkingToCenter;
    }

    public void setWalkingToCenter(boolean walkingToCenter) {
        isWalkingToCenter = walkingToCenter;
    }

    public int getNeutraliseMessageCount() {
        return neutraliseMessageCount;
    }

    public void setNeutraliseMessageCount(int neutraliseMessageCount) {
        this.neutraliseMessageCount = neutraliseMessageCount;
    }

    public int getTickCount() {
        return tickCount;
    }

    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    public int getNormalAttacksPerformed() {
        return normalAttacksPerformed;
    }

    public void setNormalAttacksPerformed(int normalAttacksPerformed) {
        this.normalAttacksPerformed = normalAttacksPerformed;
    }

    public int getEnragedStageAttacks() {
        return enragedStageAttacks;
    }

    public void setEnragedStageAttacks(int enragedStageAttacks) {
        this.enragedStageAttacks = enragedStageAttacks;
    }

    public int getIncrementableModulus() {
        return incrementableModulus;
    }

    public void setIncrementableModulus(int incrementableModulus) {
        this.incrementableModulus = incrementableModulus;
    }
}*/
