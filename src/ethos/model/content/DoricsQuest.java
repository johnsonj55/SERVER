package ethos.model.content;

import java.util.HashMap;
import java.util.Map;

import ethos.model.players.Player;


public class DoricsQuest {
	/**
	 * Represents the stage each player is on for dorics quest.
	 */
	private Map<String, Integer> doricsQuest = new HashMap<>();

	/**
	 * Returns the stage as a integer value for the quest
	 */
	public int getStage(String doricsquest) {
		if (!doricsQuest.containsKey(doricsquest)) {
			return 0;
		}
		return doricsQuest.get(doricsquest);
	}

	
	public Map<String, Integer> getStages() {
		return doricsQuest;
	}

	
	public void setStage(String doricsquest, int stage) {
		doricsQuest.put(doricsquest, stage);
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
		c.getPA().sendFrame126("@blu@Doric's Quest", 8144);
		c.getPA().sendFrame126("", 8145);
		c.getPA().sendFrame126("@blu@Requirements", 8147);
		c.getPA().sendFrame126("", 8149);
			if (c.playerLevel[c.playerMining] < 15) {
		c.getPA().sendFrame126("@dre@15 Mining", 8148);
		} 		else if (c.playerLevel[c.playerMining] >= 15) {
		c.getPA().sendFrame126("@dre@<str=1>15 Mining</str>", 8148);
	}
		//Send these frames during each stage
		if (c.getDoricsQuest().getStage("DoricsQuest") == 0) {
				c.getPA().sendFrame126("@dre@Speak to Doric north-west of falador to begin!", 8150);
		}
		if (c.getDoricsQuest().getStage("DoricsQuest") == 2) {
			c.getPA().sendFrame126("@dre@<str=1>Speak to Doric north-west of falador to begin!</str>", 8150);
			c.getPA().sendFrame126("@dre@Doric has asked me to bring him 12 clay, 6 copper ore,", 8152);
			c.getPA().sendFrame126("@dre@and 8 iron ore in exchange for access to his anvil.", 8153);
	}
		if (c.getDoricsQuest().getStage("DoricsQuest") == 2
				&& c.getItems().playerHasItem(2371, 10)
				&& c.getItems().playerHasItem(1740, 10)
				&& c.getItems().playerHasItem(2360, 10)) {
			c.getPA().sendFrame126("@dre@<str=1>Speak to Doric north-west of falador to begin!</str>", 8150);
			c.getPA().sendFrame126("@dre@<str=1>Doric has asked me to bring him 12 clay, 6 copper ore,</str>", 8152);
			c.getPA().sendFrame126("@dre@<str=1>and 8 iron ore in exchange for access to his anvil.</str>", 8153);
			c.getPA().sendFrame126("@dre@I have collected the supplies, and should return to", 8155);
			c.getPA().sendFrame126("@dre@Doric now!", 8156);
	}
		if (c.getDoricsQuest().getStage("DoricsQuest") == 4) {
			c.getPA().sendFrame126("@dre@<str=1>Speak to Doric north-west of falador to begin!</str>", 8150);
			c.getPA().sendFrame126("@gre@Quest Complete!", 8153);
	}
		c.getPA().showInterface(8134);
	}

}
