package ethos.model.content;

import java.util.HashMap;
import java.util.Map;

import ethos.model.players.Player;


public class CooksAssistant {
	/**
	 * Represents the stage each player is on for the quest.
	 */
	private Map<String, Integer> cooksAssistant = new HashMap<>();

	/**
	 * Returns the stage as a integer value for the quest
	 */
	public int getStage(String cooksassistant) {
		if (!cooksAssistant.containsKey(cooksassistant)) {
			return 0;
		}
		return cooksAssistant.get(cooksassistant);
	}

	
	public Map<String, Integer> getStages() {
		return cooksAssistant;
	}

	
	public void setStage(String cooksassistant, int stage) {
		cooksAssistant.put(cooksassistant, stage);
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
				c.getPA().sendFrame126("@blu@Cook's Assistant", 8144);
				c.getPA().sendFrame126("", 8145);
				c.getPA().sendFrame126("@blu@Requirements", 8147);
				c.getPA().sendFrame126("", 8149);
					if (c.playerLevel[c.playerCooking] < 10) {
				c.getPA().sendFrame126("@dre@10 Cooking", 8148);
				} 		else if (c.playerLevel[c.playerCooking] >= 10) {
				c.getPA().sendFrame126("@dre@<str=1>10 Cooking</str>", 8148);
			}
				//Send these frames during each stage
				if (c.getCooksAssistant().getStage("CooksAssistant") == 0) {
						c.getPA().sendFrame126("@dre@Speak to the @blu@Cook @dre@in the kitchen of", 8150);
						c.getPA().sendFrame126("@dre@Lumbridge Castle.", 8151);
				}
				if (c.getCooksAssistant().getStage("CooksAssistant") == 1) {
					c.getPA().sendFrame126("@dre@<str=1>Speak to the @blu@Cook @dre@in the kitchen of</str>", 8150);
					c.getPA().sendFrame126("@dre@<str=1>Lumbridge Castle.</str>", 8151);
					c.getPA().sendFrame126("@dre@The Cook needs me to gather a bucket of milk,", 8152);
					c.getPA().sendFrame126("@dre@a pot of flour, and an egg.", 8153);
			}
				if (c.getCooksAssistant().getStage("CooksAssistant") == 1
						&& c.getItems().playerHasItem(1927, 1)
						&& c.getItems().playerHasItem(1933, 1)
						&& c.getItems().playerHasItem(1944, 1)) {
					c.getPA().sendFrame126("@dre@<str=1>Speak to the @blu@Cook @dre@in the kitchen of</str>", 8150);
					c.getPA().sendFrame126("@dre@<str=1>Lumbridge Castle.</str>", 8151);
					c.getPA().sendFrame126("@dre@<str=1>The Cook needs me to gather a bucket of milk,</str>", 8152);
					c.getPA().sendFrame126("@dre@<str=1>a pot of flour, and an egg.</str>", 8153);
					c.getPA().sendFrame126("@dre@I should return to the @blu@Cook @dre@quickly!", 8154);
			}
				if (c.getCooksAssistant().getStage("CooksAssistant") == 2) {
					c.getPA().sendFrame126("@dre@<str=1>Speak to the @blu@Cook @dre@in the kitchen of</str>", 8150);
					c.getPA().sendFrame126("@dre@<str=1>Lumbridge Castle.</str>", 8151);
					c.getPA().sendFrame126("@gre@Quest Complete!", 8153);
			}
				c.getPA().showInterface(8134);
			}

		}
