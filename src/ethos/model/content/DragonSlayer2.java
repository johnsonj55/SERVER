package ethos.model.content;

import java.util.HashMap;
import java.util.Map;

import ethos.model.players.Player;


public class DragonSlayer2 {
	/**
	 * Represents the stage each player is on for dragon slayer 2 quest.
	 */
	private Map<String, Integer> dragonSlayer2 = new HashMap<>();

	/**
	 * Returns the stage as a integer value for the quest
	 */
	public int getStage(String dragonslayer2) {
		if (!dragonSlayer2.containsKey(dragonslayer2)) {
			return 0;
		}
		return dragonSlayer2.get(dragonslayer2);
	}

	
	public Map<String, Integer> getStages() {
		return dragonSlayer2;
	}

	
	public void setStage(String dragonslayer2, int stage) {
		dragonSlayer2.put(dragonslayer2, stage);
	}
	
	int frameIndex = 0;
	int amount = frameIndex == 10 || frameIndex == 16 || frameIndex == 20 ? 2 : 1;
	public final void display(Player c) {
		int[] frames = { 8148, 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8158, 8159, 8160, 8161, 8162, 8163,
				8165, 8166, 8167, 8168, 8170, 8171, 8172, 8173, 8174, 8175, 8176, 8178, 8179, 8180, 8181,
				8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194 };
		
		for (int i = 8144; i < 8195; i++) {
			c.getPA().sendFrame126("", i);
		}
		frameIndex = 0;
		
		//Always send these frames
		c.getPA().sendFrame126("@blu@Dragon Slayer II", 8144);
		c.getPA().sendFrame126("@blu@Requirements", 8147);
		if (c.combatLevel < 85) {
			c.getPA().sendFrame126("@dre@Combat Level 85+", 8148);
		} else if (c.combatLevel >= 85) {
			c.getPA().sendFrame126("@dre@<str=1>Combat Level 85+</str>", 8148);
		}
		if (c.playerLevel[c.playerHerblore] < 73) {
		c.getPA().sendFrame126("@dre@73 Herblore", 8149);
		} else if (c.playerLevel[c.playerHerblore] >= 73) {
		c.getPA().sendFrame126("@dre@<str=1>73 Herblore</str>", 8149);
		}
		if (c.getMonkeyMadness().getStage("MonkeyMadness") < 3) {
		c.getPA().sendFrame126("@dre@Completion of Monkey Madness", 8150);
		} else if (c.getMonkeyMadness().getStage("MonkeyMadness") == 3) {
		c.getPA().sendFrame126("@dre@<str=1>Completion of Monkey Madness</str>", 8150);
		}
		if (c.rfdGloves < 6) {
		c.getPA().sendFrame126("@dre@Completion of Recipe for Disaster", 8151);
		} else if (c.rfdGloves == 6) {
		c.getPA().sendFrame126("@dre@<str=1>Completion of Recipe for Disaster</str>", 8151);
		}
		
		//Send these frames during each stage
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 0) {
			c.getPA().sendFrame126("@dre@I can start this quest by speaking to @blu@Alec Kincade,", 8153);
			c.getPA().sendFrame126("@dre@who can be found at the Myths' Guild.", 8154);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 1) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@Alec suggested I speak with Dallas Jones in Karamja to", 8156);
			c.getPA().sendFrame126("@dre@help me find the missing historian.", 8157);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 2) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with Dallas Jones in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@I need to obtain 3 keys in order to enter the dragon lair.", 8159);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 3) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with Dallas Jones in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@The Wise Old Man needs the other two keys before he", 8161);
			c.getPA().sendFrame126("@dre@can enchant all three.", 8162);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 4) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with @blu@Dallas Jones @dre@in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@The @blu@Wise Old Man @dre@needs the other two keys before he", 8161);
			c.getPA().sendFrame126("@dre@can enchant all three.", 8162);
			c.getPA().sendFrame126("@dre@Doric will give me the Black Key in exchange for", 8164);
			c.getPA().sendFrame126("@blu@50 @dre@weapon poison+ potions.", 8165);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 5) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with @blu@Dallas Jones @dre@in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@The @blu@Wise Old Man @dre@needs the other two keys before he", 8161);
			c.getPA().sendFrame126("@dre@can enchant all three.", 8162);
			c.getPA().sendFrame126("@dre@<str=1>Doric will give me the Black Key in exchange for</str>", 8164);
			c.getPA().sendFrame126("@blu@<str=1>50 @dre@weapon poison+ potions.</str>", 8165);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 6) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with @blu@Dallas Jones @dre@in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@The @blu@Wise Old Man @dre@needs the other two keys before he", 8161);
			c.getPA().sendFrame126("@dre@can enchant all three.", 8162);
			c.getPA().sendFrame126("@dre@<str=1>Doric will give me the Black Key in exchange for</str>", 8164);
			c.getPA().sendFrame126("@blu@<str=1>50 @dre@weapon poison+ potions.</str>", 8165);
			c.getPA().sendFrame126("@dre@I need to kill dragons in the @blu@Metal Dragon Vault", 8167);
			c.getPA().sendFrame126("@dre@until I retrieve the Kings key.", 8168);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 7) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with @blu@Dallas Jones @dre@in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@<str=1>The @blu@Wise Old Man @dre@needs the other two keys before he</str>", 8161);
			c.getPA().sendFrame126("@dre@<str=1>can enchant all three.</str>", 8162);
			c.getPA().sendFrame126("@dre@<str=1>Doric will give me the Black Key in exchange for</str>", 8164);
			c.getPA().sendFrame126("@blu@<str=1>50 @dre@weapon poison+ potions.</str>", 8165);
			c.getPA().sendFrame126("@dre@<str=1>I need to kill dragons in the @blu@Metal Dragon Vault", 8167);
			c.getPA().sendFrame126("@dre@<str=1>until I retrieve the Kings key.", 8168);
			c.getPA().sendFrame126("@dre@I've given both keys to the Wise Old Man", 8170);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 8) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with @blu@Dallas Jones @dre@in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@<str=1>The @blu@Wise Old Man @dre@needs the other two keys before he</str>", 8161);
			c.getPA().sendFrame126("@dre@<str=1>can enchant all three.</str>", 8162);
			c.getPA().sendFrame126("@dre@<str=1>Doric will give me the Black Key in exchange for</str>", 8164);
			c.getPA().sendFrame126("@blu@<str=1>50 @dre@weapon poison+ potions.</str>", 8165);
			c.getPA().sendFrame126("@dre@<str=1>I need to kill dragons in the @blu@Metal Dragon Vault", 8167);
			c.getPA().sendFrame126("@dre@<str=1>until I retrieve the Kings key.</str>", 8168);
			c.getPA().sendFrame126("@dre@<str=1>I've given both keys to the Wise Old Man</str>", 8170);
			c.getPA().sendFrame126("@dre@I should speak with Dallas to travel to the lair", 8172);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 9) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with @blu@Dallas Jones @dre@in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@<str=1>The @blu@Wise Old Man @dre@needs the other two keys before he</str>", 8161);
			c.getPA().sendFrame126("@dre@<str=1>can enchant all three.</str>", 8162);
			c.getPA().sendFrame126("@dre@<str=1>Doric will give me the Black Key in exchange for</str>", 8164);
			c.getPA().sendFrame126("@blu@<str=1>50 @dre@weapon poison+ potions.</str>", 8165);
			c.getPA().sendFrame126("@dre@<str=1>I need to kill dragons in the @blu@Metal Dragon Vault", 8167);
			c.getPA().sendFrame126("@dre@<str=1>until I retrieve the Kings key.</str>", 8168);
			c.getPA().sendFrame126("@dre@<str=1>I've given both keys to the Wise Old Man</str>", 8170);
			c.getPA().sendFrame126("@dre@I should speak with Dallas to travel to the lair", 8172);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 10) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with @blu@Dallas Jones @dre@in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@<str=1>The @blu@Wise Old Man @dre@needs the other two keys before he</str>", 8161);
			c.getPA().sendFrame126("@dre@<str=1>can enchant all three.</str>", 8162);
			c.getPA().sendFrame126("@dre@<str=1>Doric will give me the Black Key in exchange for</str>", 8164);
			c.getPA().sendFrame126("@blu@<str=1>50 @dre@weapon poison+ potions.</str>", 8165);
			c.getPA().sendFrame126("@dre@<str=1>I need to kill dragons in the @blu@Metal Dragon Vault", 8167);
			c.getPA().sendFrame126("@dre@<str=1>until I retrieve the Kings key.</str>", 8168);
			c.getPA().sendFrame126("@dre@<str=1>I've given both keys to the Wise Old Man</str>", 8170);
			c.getPA().sendFrame126("@dre@<str=1>I should speak with Dallas to travel to the lair</str>", 8172);
			c.getPA().sendFrame126("@dre@We have located the historian, and should leave", 8174);
			c.getPA().sendFrame126("@dre@the lair!", 8175);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 11) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with @blu@Dallas Jones @dre@in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@<str=1>The @blu@Wise Old Man @dre@needs the other two keys before he</str>", 8161);
			c.getPA().sendFrame126("@dre@<str=1>can enchant all three.</str>", 8162);
			c.getPA().sendFrame126("@dre@<str=1>Doric will give me the Black Key in exchange for</str>", 8164);
			c.getPA().sendFrame126("@blu@<str=1>50 @dre@weapon poison+ potions.</str>", 8165);
			c.getPA().sendFrame126("@dre@<str=1>I need to kill dragons in the @blue@Metal Dragon Vault", 8167);
			c.getPA().sendFrame126("@dre@<str=1>until I retrieve the Kings key.</str>", 8168);
			c.getPA().sendFrame126("@dre@<str=1>I've given both keys to the Wise Old Man</str>", 8170);
			c.getPA().sendFrame126("@dre@<str=1>I should speak with Dallas to travel to the lair</str>", 8172);
			c.getPA().sendFrame126("@dre@<str=1>We have located the historian, and should return</str>", 8174);
			c.getPA().sendFrame126("@dre@<str=1>to the Myths Guild.</str>", 8175);
			c.getPA().sendFrame126("@dre@Defeat Vorkath", 8177);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 12) {
			c.getPA().sendFrame126("@dre@<str=1>I can start this quest by speaking to @blu@Alec Kincade,</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>who can be found at the Myths' Guild.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>Alec suggested I speak with @blu@Dallas Jones @dre@in Karamja to</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>help me find the missing historian.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>I need to obtain 3 keys in order to enter the dragon lair.</str>", 8159);
			c.getPA().sendFrame126("@dre@<str=1>The @blu@Wise Old Man @dre@needs the other two keys before he</str>", 8161);
			c.getPA().sendFrame126("@dre@<str=1>can enchant all three.</str>", 8162);
			c.getPA().sendFrame126("@dre@<str=1>Doric will give me the Black Key in exchange for</str>", 8164);
			c.getPA().sendFrame126("@blu@<str=1>50 @dre@weapon poison+ potions.</str>", 8165);
			c.getPA().sendFrame126("@dre@<str=1>I need to kill dragons in the @blue@Metal Dragon Vault", 8167);
			c.getPA().sendFrame126("@dre@<str=1>until I retrieve the Kings key.</str>", 8168);
			c.getPA().sendFrame126("@dre@<str=1>I've given both keys to the Wise Old Man</str>", 8170);
			c.getPA().sendFrame126("@dre@<str=1>I should speak with Dallas to travel to the lair</str>", 8172);
			c.getPA().sendFrame126("@dre@<str=1>We have located the historian, and should return</str>", 8174);
			c.getPA().sendFrame126("@dre@<str=1>to the Myths Guild.</str>", 8175);
			c.getPA().sendFrame126("@dre@<str=1>Defeat Vorkath</str>", 8177);
			c.getPA().sendFrame126("@dre@I should speak with @blu@Alec Kincade @dre@again", 8179);
		}
		if (c.getDragonSlayer2().getStage("DragonSlayer2") == 13) {
			c.getPA().sendFrame126("@gre@Quest Complete!", 8153);
		}
		c.getPA().showInterface(8134);
	}

}