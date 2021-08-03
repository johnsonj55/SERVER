package ethos.model.players.packets.itemoptions;

import java.util.Objects;
import java.util.Optional;

import ethos.Config;
import ethos.Server;
import ethos.model.content.RunePouch;
import ethos.model.content.teleportation.TeleportTablets;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.model.players.combat.Degrade;
import ethos.model.players.combat.Degrade.DegradableItem;
import ethos.model.players.skills.slayer.Task;
import ethos.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemOptionThree implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId11 = c.getInStream().readSignedWordBigEndianA();
		int itemId1 = c.getInStream().readSignedWordA();
		int itemId = c.getInStream().readSignedWordA();
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (RunePouch.isRunePouch(c, itemId)) {
			c.getRunePouch().withdrawAll();
			return;
		}
		TeleportTablets.operate(c, itemId);
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		Optional<DegradableItem> d = DegradableItem.forId(itemId);
		if (d.isPresent()) {
			Degrade.checkPercentage(c, itemId);
			return;
		}
		switch (itemId) {
		case 9762:
		case 9763:
			c.getDH().sendDialogues(1000022, 5870);
			break;
		case 749:
			Task task = c.getSlayer().getTask().orElse(null);
            if (task == null) {
            	c.sendMessage("You need a slayer task to use this."); 
            	return;  
            }
            if (task.getPrimaryName().contains(task.getNames()[0])) 
            	c.getPA().startTeleport(task.getTeleportLocation()[0],task.getTeleportLocation()[1], task.getTeleportLocation()[2], "modern", false);
			break;
		case 21347:
			c.boltTips = false;
			c.arrowTips = false;
			c.javelinHeads = true;
			c.sendMessage("Your Amethyst method is now Javelin Heads!");
			break;
		case 13125:
		case 13126:
		case 13127:
			if (c.getRunEnergy() < 100) {
				if (c.getRechargeItems().useItem(itemId)) {
					c.getRechargeItems().replenishRun(50);
				}
			} else {
				c.sendMessage("You already have full run energy.");
				return;
			}
			break;
			
		case 22118:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2335, 4828, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Wrath Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1438:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2841, 4828, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Air Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1440:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2655, 4829, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Earth Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1442:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2576, 4846, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Fire Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1444:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2720, 4831, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Water Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1446:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2522, 4833, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Body Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1448:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2793, 4827, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Mind Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1452:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2282, 4837, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Chaos Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1454:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2163, 4833, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Cosmic Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1456:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2208, 4829, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Death Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1458:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2464, 4817, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Law Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 1462:
			if (c.inWild()) {
					if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("@red@You can not use this teleport above 20 wilderness");
						return;
					}
			}
			c.getPA().startTeleport(2400, 4834, 0, "modern", false);
			c.sendMessage("@red@You have teleported to the Nature Rune altar! You can use Pure Essence to craft runes here.");
			break;
		case 13128:
			if (c.getRunEnergy() < 100) {
				if (c.getRechargeItems().useItem(itemId)) {
					c.getRechargeItems().replenishRun(100);
				}
			} else {
				c.sendMessage("You already have full run energy.");
				return;
			}
			break;
			
		case 13226:
			c.getHerbSack().check();
			break;
			
		case 12020:
			c.getGemBag().withdrawAll();
			break;
		
		case 12902: //Toxic staff dismantle
			if (!c.getItems().playerHasItem(12902))
				return;
			if (c.getItems().freeSlots() < 2)
				return;
			
			c.getItems().deleteItem(12902, 1);
			c.getItems().addItem(12932, 1);
			c.getItems().addItem(11791, 1);
			c.sendMessage("You dismantle your toxic staff of the dead.");
			break;
			
		case 12900: //Toxic trident dismantle
			if (!c.getItems().playerHasItem(12900))
				return;
			if (c.getItems().freeSlots() < 2)
				return;
			
			c.getItems().deleteItem(12900, 1);
			c.getItems().addItem(12932, 1);
			c.getItems().addItem(11907, 1);
			c.sendMessage("You dismantle your toxic trident.");
			break;
			
		case 11283:
			if (c.getDragonfireShieldCharge() == 0) {
				c.sendMessage("Your dragonfire shield has no charge.");
				return;
			}
			c.setDragonfireShieldCharge(0);
			c.sendMessage("Your dragonfire shield has been emptied.");
			break;
		case 13196:
		case 13198:
			c.getItems().deleteItem2(itemId, 1);
			c.getItems().addItem(12929, 1);
			c.getItems().addItem(itemId == 13196 ? 13200 : 13201, 1);
			c.sendMessage("You revoke the mutagen from the helmet.");
			break;
		case 11907:
		case 12899:
			int charge = itemId == 11907 ? c.getTridentCharge() : c.getToxicTridentCharge();
			if (charge <= 0) {
				if (itemId == 12899) {
					if (c.getToxicTridentCharge() == 0) {
						if (c.getItems().freeSlots() > 1) {
							c.getItems().deleteItem(12899, 1);
							c.getItems().addItem(12932, 1);
							c.getItems().addItem(11907, 1);
							c.sendMessage("You dismantle your Trident of the swamp.");
							return;
						} else {
							c.sendMessage("You need at least 2 inventory spaces to dismantle the trident.");
							return;
						}
					}
				} else {
					c.sendMessage("Your trident currently has no charge.");
					return;
				}
			}
			
			if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots for this.");
				return;
			}
			c.getItems().addItem(554, 5 * charge);
			c.getItems().addItem(560, 1 * charge);
			c.getItems().addItem(562, 1 * charge);
			
			if (itemId == 12899) {
				c.getItems().addItem(12934, 10 * charge);
			}
			
			if (itemId == 11907) {
				c.setTridentCharge(0);
			} else {
				c.setToxicTridentCharge(0);
			}
			c.sendMessage("You revoke " + charge + " charges from the trident.");
			break;

		case 12926:
			if (c.getToxicBlowpipeAmmo() == 0 || c.getToxicBlowpipeAmmoAmount() == 0) {
				c.sendMessage("You have no ammo in the pipe.");
				return;
			}
			if (c.getItems().addItem(c.getToxicBlowpipeAmmo(), c.getToxicBlowpipeAmmoAmount())) {
				c.setToxicBlowpipeAmmoAmount(0);
				c.sendMessage("You unload the pipe.");
			}
			break;
		case 2552:
		case 2554:
		case 2556:
		case 2558:
		case 2560:
		case 2562:
		case 2564:
		case 2566:
			 //c.getPA().ROD();
			c.getPA().spellTeleport(3304, 3130, 0, false);
			break;
		case 11968:
		case 11970:
		case 11105:
		case 11107:
		case 11109:
		case 11111:
			c.getPA().handleSkills(itemId);
			c.isOperate = true;
			c.itemUsing = itemId;
			break;
		case 1712:
			c.getPA().handleGlory(itemId);
			c.isOperate = true;
			c.itemUsing = itemId;
			break;

		case 1710:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;

		case 1708:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;

		case 1706:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;
			
		case 19639:
		case 19641:
		case 19643:
		case 19645:
		case 19647:
		case 19649:
			c.getSlayer().revertHelmet(itemId);
			break;
			
		case 13280:
		case 113238:
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
				for (int i = 8144; i < 8195; i++) {
					c.getPA().sendFrame126("", i);
				}
				c.getPA().sendFrame126("               @dre@Max Cape Features", 8144);
				
				c.getPA().sendFrame126("While wielding the cape you will:", 8145);
				
				c.getPA().sendFrame126("Have a chance of saving ammo.", 8148);
				c.getPA().sendFrame126("Deplete run energy slower.", 8149);
				c.getPA().sendFrame126("Get more herbs & faster growth time.", 8150);
				c.getPA().sendFrame126("Have less chance of burning food", 8151);
				c.getPA().sendFrame126("Have 20% of saving a bar while smithing.", 8152);
				c.getPA().sendFrame126("Have 20% of saving a herb while mixing potions.", 8153);
				c.getPA().sendFrame126("Regenerate 2 hitpoints instead of 1 at a time.", 8154);
				c.getPA().sendFrame126("Get 5+ restore per prayer/super restore sip.", 8155);
				c.getPA().sendFrame126("Get double seeds while thieving the master farmer.", 8156);
				c.getPA().sendFrame126("Be able to operate for additional options.", 8157);

				c.getPA().showInterface(8134);
			break;

		default:
			if (c.getRights().isOrInherits(Right.OWNER)) {
				Misc.println("[DEBUG] Item Option #3-> Item id: " + itemId);
			}
			break;
		}

	}

}
