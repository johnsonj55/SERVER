package ethos.model.content.bonus;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ethos.Config;

import ethos.model.players.Player;

public class SkillOfTheDay {
	
	
	private static boolean skillActive = false;
	
	public static int[] BONUS_SKILLS = { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 };
	
	public static int randomSkill() {
		return BONUS_SKILLS[(int) (Math.random() * BONUS_SKILLS.length)];
	}
	
	public static void setStatus(boolean status) {
		skillActive = status;
	}
	
	public static boolean getStatus() {
		return SkillOfTheDay.skillActive;
	}
	
	public static int SKILL_OF_THE_DAY;
	public static int lastSkill;
	public static long currentTime;
	public static boolean DEBUG = false;
	public static int goal = 100;
	public static void appendSkillOfTheDay(Player player) {
		Calendar cal = new GregorianCalendar();
		long time = cal.getTimeInMillis();
		long hour = (time / (1000 * 60 * 60)) % 24;
		 
		currentTime = hour;
		if (SKILL_OF_THE_DAY < 1
				&& !getStatus()) {
			SKILL_OF_THE_DAY = randomSkill();
			setStatus(true);
		}
		if (hour == 12
				&& !getStatus()) {
			SKILL_OF_THE_DAY = randomSkill();
			if (SKILL_OF_THE_DAY == lastSkill) {
				appendSkillOfTheDay(player);
				return;
			}
			lastSkill = SKILL_OF_THE_DAY;
			setStatus(true);
			player.getPA().yell("The daily bonus skill has changed! It is now @pur@" + Config.SKILL_NAME[SKILL_OF_THE_DAY] + "@bla@");
			if (Config.totalAmountDonated > goal )  {
				if (Config.SPAWNED_BOSS == false) {
					player.getPA().yell("[GOAL] We've reached over $" + goal + " in donations, the Almighty Mistex is Live!");
					Config.SPAWNED_BOSS = true;
					
				}
			} else { 
				Config.SPAWNED_BOSS = false;
				Config.totalAmountDonated = 0;
				player.getPA().yell("[GOAL] We still haven't reached our $" + goal + " goal for Almighty Mistex!");
			}
			return;
		} else if (hour != 12
				&& getStatus()) {
			setStatus(false);
			return;
		}
	}	

	
	public static void skillOfTheDayMessage(Player player) {
		if (Config.totalAmountDonated < goal) {
			player.sendMessage("[GOAL] We haven't reached our donation goal of $100, we will unlock the Almighty Mistex");
		} else {
			player.sendMessage("[GOAL] The donation boss is spawned. Thank you all for donating.");
		}
		if (SKILL_OF_THE_DAY > 6) {
		player.sendMessage("The daily bonus skill is: @pur@" + Config.SKILL_NAME[SKILL_OF_THE_DAY] + "@bla@");
		player.getPA().sendFrame126("<col=C27E3A> Skill Bonus:</col> @gre@" + Config.SKILL_NAME[SKILL_OF_THE_DAY] + "",10226);
		} else {
			appendSkillOfTheDay(player);
			player.sendMessage("The daily bonus skill is: @pur@" + Config.SKILL_NAME[SKILL_OF_THE_DAY] + "@bla@");
			player.getPA().sendFrame126("<col=C27E3A> Skill Bonus:</col> @gre@" + Config.SKILL_NAME[SKILL_OF_THE_DAY] + "",10226);
		}
	}
}