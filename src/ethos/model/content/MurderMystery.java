package ethos.model.content;


import java.util.HashMap;
import java.util.Map;

import ethos.model.players.Player;


public class MurderMystery {
	/**
	 * Represents the stage each player is on for the quest.
	 */
	private Map<String, Integer> murderMystery = new HashMap<>();

	/**
	 * Returns the stage as a integer value for the quest
	 */
	public int getStage(String murdermystery) {
		if (!murderMystery.containsKey(murdermystery)) {
			return 0;
		}
		return murderMystery.get(murdermystery);
	}

	
	public Map<String, Integer> getStages() {
		return murderMystery;
	}

	
	public void setStage(String murdermystery, int stage) {
		murderMystery.put(murdermystery, stage);
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
		c.getPA().sendFrame126("@blu@Murder Mystery", 8144);
		c.getPA().sendFrame126("", 8145);
		c.getPA().sendFrame126("@blu@Requirements", 8147);
		c.getPA().sendFrame126("", 8149);
		c.getPA().sendFrame126("@dre@None", 8148);

		//Send these frames during each stage
		if (c.getMurderMystery().getStage("MurderMystery") == 0) {
				c.getPA().sendFrame126("@dre@Find some gossip in @blu@Camelot @dre@to begin.", 8150);
		}
		if (c.getMurderMystery().getStage("MurderMystery") == 1) {
			c.getPA().sendFrame126("@dre@<str=1>@dre@Find some gossip in @blu@Camelot @dre@to begin.</str>", 8150);
			c.getPA().sendFrame126("@dre@I should head up to the Sinclair Mansion to investigate.", 8152);
			c.getPA().sendFrame126("@dre@", 8153);
		}
		if (c.getMurderMystery().getStage("MurderMystery") == 2) {
			c.getPA().sendFrame126("@dre@<str=1>@dre@Find some gossip in @blu@Camelot @dre@to begin.</str>", 8150);
			c.getPA().sendFrame126("@dre@<str=1>I should head up to the Sinclair Mansion to investigate.</str>", 8152);
			c.getPA().sendFrame126("@dre@The Guards are allowing me to look at the murder scene", 8154);
			c.getPA().sendFrame126("@dre@and investigate the family and staff.", 8155);
			c.getPA().sendFrame126("@dre@", 8156);
		}
		if (c.getMurderMystery().getStage("MurderMystery") == 2 && c.talkElizabeth == true &&
				c.talkMary == true && c.talkPierre == true && c.talkBob == true
				&& c.talkHobbes == true && c.talkStanford == true && c.talkDonovan == true
				&& c.talkCarol == true && c.talkLouisa == true && c.talkDavid == true
				&& c.talkFrank == true) {
			c.getPA().sendFrame126("@dre@<str=1>@dre@Find some gossip in @blu@Camelot @dre@to begin.</str>", 8150);
			c.getPA().sendFrame126("@dre@<str=1>I should head up to the Sinclair Mansion to investigate.</str>", 8152);
			c.getPA().sendFrame126("@dre@<str=1>The Guards are allowing me to look at the murder scene</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>and investigate the family and staff.</str>", 8155);
			c.getPA().sendFrame126("@dre@I've spoken to everyone in the house. Maybe I should", 8156);
			c.getPA().sendFrame126("@dre@Speak with the guard again.", 8157);
		}
		if (c.getMurderMystery().getStage("MurderMystery") == 3) {
			c.getPA().sendFrame126("@dre@<str=1>@dre@Find some gossip in @blu@Camelot @dre@to begin.</str>", 8150);
			c.getPA().sendFrame126("@dre@<str=1>I should head up to the Sinclair Mansion to investigate.</str>", 8152);
			c.getPA().sendFrame126("@dre@<str=1>The Guards are allowing me to look at the murder scene</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>and investigate the family and staff.</str>", 8155);
			c.getPA().sendFrame126("@dre@<str=1>I've spoken to everyone in the house. Maybe I should</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>speak with the @blu@Guard @dre@again.</str>", 8157);
			c.getPA().sendFrame126("@dre@The Guard mentioned that a @blu@Salesman @dre@ visited the house", 8158);
			c.getPA().sendFrame126("@dre@prior to the murder. I should speak to this salesman.", 8159);
		}
		if (c.getMurderMystery().getStage("MurderMystery") == 4) {
			c.getPA().sendFrame126("@dre@<str=1>@dre@Find some gossip in @blu@Camelot @dre@to begin.</str>", 8150);
			c.getPA().sendFrame126("@dre@<str=1>I should head up to the Sinclair Mansion to investigate.</str>", 8152);
			c.getPA().sendFrame126("@dre@<str=1>The Guards are allowing me to look at the murder scene</str>", 8154);
			c.getPA().sendFrame126("@dre@<str=1>and investigate the family and staff.</str>", 8155);
			c.getPA().sendFrame126("@dre@<str=1>I've spoken to everyone in the house. Maybe I should</str>", 8156);
			c.getPA().sendFrame126("@dre@<str=1>speak with the @blu@Guard @dre@again.</str>", 8157);
			c.getPA().sendFrame126("@dre@<str=1>The Guard mentioned that a @blu@Salesman @dre@visited the house</str>", 8158);
			c.getPA().sendFrame126("@dre@<str=1>prior to the murder. I should speak to this salesman.</str>", 8159);
			c.getPA().sendFrame126("@dre@I should report this new information back to the Guard.", 8160);
		}
		if (c.getMurderMystery().getStage("MurderMystery") == 5) {
			c.getPA().sendFrame126("@dre@<str=1>@dre@Find some gossip in @blu@Camelot @dre@to begin.</str>", 8150);
			c.getPA().sendFrame126("@gre@Quest Complete!", 8153);
		}
		c.getPA().showInterface(8134);
	}

}