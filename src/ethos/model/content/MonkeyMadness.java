package ethos.model.content;

import java.util.HashMap;
import java.util.Map;

import ethos.model.players.Player;


public class MonkeyMadness {
	/**
	 * Represents the stage each player is on for monkey madness quest.
	 */
	private Map<String, Integer> monkeyMadness = new HashMap<>();

	/**
	 * Returns the stage as a integer value for the quest
	 */
	public int getStage(String monkeymadness) {
		if (!monkeyMadness.containsKey(monkeymadness)) {
			return 0;
		}
		return monkeyMadness.get(monkeymadness);
	}

	
	public Map<String, Integer> getStages() {
		return monkeyMadness;
	}

	
	public void setStage(String monkeymadness, int stage) {
		monkeyMadness.put(monkeymadness, stage);
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
		c.getPA().sendFrame126("@blu@Monkey Madness", 8144);
		c.getPA().sendFrame126("", 8145);
		c.getPA().sendFrame126("@blu@Requirements", 8147);
		c.getPA().sendFrame126("", 8149);
		if (c.playerLevel[c.playerAttack] < 60) {
			c.getPA().sendFrame126("@dre@60 Attack", 8148);
		} else if (c.playerLevel[c.playerAttack] >= 60) {
			c.getPA().sendFrame126("@dre@<str=1>60 Attack</str>", 8148);
		}
		//Send these frames during each stage
		if (c.getMonkeyMadness().getStage("MonkeyMadness") == 0) {
			c.getPA().sendFrame126("@dre@I should find King Narnode at the Grand Tree to", 8150);
			c.getPA().sendFrame126("@dre@begin this journey.", 8151);
		}
		if (c.getMonkeyMadness().getStage("MonkeyMadness") == 1) {
			c.getPA().sendFrame126("@dre@<str=1>I should find King Narnode at the Grand Tree to</str>", 8150);
			c.getPA().sendFrame126("@dre@<str=1>begin this journey.</str>", 8151);
			c.getPA().sendFrame126("@dre@The King has asked me to slay the demon dwelling", 8153);
			c.getPA().sendFrame126("@dre@underneath the Grand Tree.", 8154);
		}
		if (c.getMonkeyMadness().getStage("MonkeyMadness") == 2) {
			c.getPA().sendFrame126("@dre@<str=1>I should find King Narnode at the Grand Tree to</str>", 8150);
			c.getPA().sendFrame126("@dre@<str=1>begin this journey.</str>", 8151);
			c.getPA().sendFrame126("@dre@<str=1>The King has asked me to slay the demon dwelling</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>underneath the Grand Tree.</str>", 8154);
			c.getPA().sendFrame126("@dre@I have slayed the demon. I should see if the King needs", 8156);
			c.getPA().sendFrame126("@dre@anymore help.", 8157);
		}
		if (c.getMonkeyMadness().getStage("MonkeyMadness") == 3) {
			c.getPA().sendFrame126("@dre@<str=1>I should find King Narnode at the Grand Tree to</str>", 8150);
			c.getPA().sendFrame126("@dre@<str=1>begin this journey.</str>", 8151);
			c.getPA().sendFrame126("@dre@<str=1>The King has asked me to slay the demon dwelling</str>", 8153);
			c.getPA().sendFrame126("@dre@<str=1>underneath the Grand Tree.</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>I have slayed the demon. I should see if the King needs</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>anymore help.</str>", 8157);
			c.getPA().sendFrame126("@gre@Quest Complete!", 8159);
	}
		c.getPA().showInterface(8134);
	}

}