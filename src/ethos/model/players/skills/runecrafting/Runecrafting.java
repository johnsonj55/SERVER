package ethos.model.players.skills.runecrafting;

import java.util.stream.IntStream;

import ethos.Config;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.karamja.KaramjaDiaryEntry;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.SkillHandler;
import ethos.util.Misc;

public class Runecrafting extends SkillHandler {
	
	private enum RUNECRAFTING_DATA {
		AIR(25378, 556, 7.5, new int[] { 1, 11, 22, 33, 44, 55, 66, 77, 88, 99 }, 15000, 20667), 
		AIRTALISMAN(34760, 556, 7.5, new int[] { 1, 11, 22, 33, 44, 55, 66, 77, 88, 99 }, 15000, 20667), 
		MIND(25379, 558, 7.8, new int[] { 2, 14, 28, 42, 56, 70, 84, 98 }, 14500, 20669), 
		WATER(25376, 555, 9, new int[] { 5, 19, 38, 57, 76, 88, 99 }, 14000, 20671), 
		WATERTALISMAN(34762, 555, 9, new int[] { 5, 19, 38, 57, 76, 88, 99 }, 14000, 20671), 
		MIST(-1, -1, 12, new int[] { 6 }, 15, -1), 
		EARTH(24972, 557, 9.3, new int[] { 9, 26, 52, 78, 88, 96 }, 13500, 20673), 
		EARTHTALISMAN(34763, 557, 9.3, new int[] { 9, 26, 52, 78, 88, 96 }, 13500, 20673), 
		DUST(-1, -1, 12.2, new int[] { 10 }, 15, -1), 
		MUD(-1, -1, 13, new int[] { 13 }, 15, -1), 
		FIRE(24971, 554, 10.5, new int[] { 14, 35, 70, 85, 95 }, 13000, 20665), 
		FIRETALISMAN(34764, 554, 10.5, new int[] { 14, 35, 70, 85, 95 }, 13000, 20665), 
		SMOKE(-1, -1, 12.5, new int[] { 15 }, 15, -1), 
		STEAM(-1, -1, 14, new int[] { 19 }, 15, -1), 
		BODY(24973, 559, 11, new int[] { 20, 46, 75, 92 }, 12500, 20675), 
	//	BODYTALISMAN(24973, 559, 11, new int[] { 20, 46, 75, 92 }, 12500, 20675), 
		LAVA(-1, -1, 15, new int[] { 23 }, 15, -1), 
		COSMIC(24974, 564, 12, new int[] { 27, 59, 78 }, 12000, 20677), 
		CHAOS(24976, 562, 12.3, new int[] { 35, 74, 86 }, 11500, 20679), 
		CHAOSTALISMAN(34769, 562, 12.3, new int[] { 35, 74, 86 }, 11500, 20679), 
		ASTRAL(14911, 9075, 14, new int[] { 40, 82 }, 11000, 20689), 
		NATURE(24975, 561, 13, new int[] { 44, 91, 95 }, 10000, 20681), 
		NATURETALISMAN(34768, 561, 13, new int[] { 44, 91, 95 }, 10000, 20681), 
		LAW(25034, 563, 13.6, new int[] { 54, 79, 93 }, 9500, 20683), 
		LAWTALISMAN(34767, 563, 13.6, new int[] { 54, 79, 93 }, 9500, 20683), 
		DEATH(25035, 560, 15, new int[] { 65, 84, 96 }, 9000, 20685), 
		DEATHTALISMAN(34770, 560, 15, new int[] { 65, 84, 96 }, 9000, 20685), 
		BLOOD(25380, 565, 16.5, new int[] { 77, 89 }, 8800, 20691), 
		WRATH(34772, 21880, 15, new int[] { 95 }, 17450, 21990), 
		SOUL(25377, 566, 18, new int[] { 90 }, 8000, 20687);

		private int objectId, runeId, petChance, petId;
		private double experience;
		private int[] multiplier;

		public int getObjectId() {
			return objectId;
		}

		public int getRuneId() {
			return runeId;
		}

		public int getPetChance() {
			return petChance;
		}

		public int getPetId() {
			return petId;
		}
		
		public double getExperience() {
			return experience;
		}
		
		public int getLevelRequirement() {
			return multiplier[0];
		}

		RUNECRAFTING_DATA(int objectId, int runeId, double experience, int[] multiplier, int petChance, int petId) {
			this.objectId = objectId;
			this.runeId = runeId;
			this.experience = experience;
			this.multiplier = multiplier;
			this.petChance = petChance;
			this.petId = petId;
		}
	};
	
	
	public static void execute(Player player, int objectId) {
		for (RUNECRAFTING_DATA data : RUNECRAFTING_DATA.values()) {
			
			String name = data.name().toLowerCase().replaceAll("_", " ");
			if (data.getObjectId() == objectId) {
				if (!hasRequiredLevel(player, 20, data.getLevelRequirement(), "runecrafting", "craft these runes")) {
					return;
				}
				if (!player.getItems().playerHasItem(1436) && !player.getItems().playerHasItem(7936)) {
					player.sendMessage("You need some essence to craft runes!");
					return;
				}
				int multiplier = 1;
				for (int multiply = 0; multiply < data.multiplier.length; multiply++) {
					if (player.playerLevel[20] >= data.multiplier[multiply]) {
						multiplier = multiply + 1;
					}
				}
				int count = player.getItems().getItemAmount(7936) + player.getItems().getItemAmount(1436);
				int essence = player.getItems().getItemAmount(7936) + player.getItems().getItemAmount(1436);
				int multiply = essence *= multiplier;
				if (name.equals("water")) {
					player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.CRAFT_WATER);
				}
				if (name.equals("death")) {
					player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.CRAFT_DEATH);
				}
				if (name.equals("mind")) {
					if (multiply > 100) {
						player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.CRAFT_MIND);
					}
				}
				if (name.equals("cosmic")) {
					if (multiply > 56) {
						player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.CRAFT_COSMIC);
					}
				}
				if (name.equals("earth")) {
					if (multiply > 150) {
						player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.ALOT_OF_EARTH);
					}
					player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.EARTH_RUNES);
				}
				if (name.equals("nature")) {
					if (multiply > 50) {
						player.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.CRAFT_NATURES);
					}
					DailyTasks.increase(player, PossibleTasks.NATURE_RUNES);
				}
				if (name.equals("law"))
					DailyTasks.increase(player, PossibleTasks.LAW_RUNES);
				/*if (name.equals("astral")) {
					if (multiply > 50) {
						player.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.CRAFT_ASTRAL);
					}
				}*/
				player.getItems().deleteItem2(7936, essence);
				player.getItems().deleteItem2(1436, essence);
				player.gfx100(186);
				player.startAnimation(791);
				double percentOfXp = data.getExperience() * count * 2.5;
				player.getPA().addSkillXP((int) (((double) (data.getExperience()) * count) * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.RUNECRAFTING_EXPERIENCE) + (player.getItems().isWearingItem(20008) ? percentOfXp : 0)), 20, true);
				player.getItems().addItem(data.getRuneId(), multiply);
				player.sendMessage("You bind the temple's power into " + essence + " " + name + " runes.");
				player.getPA().requestUpdates();
				
					boolean hasGuardian = IntStream.range(20665, 20691).anyMatch(id -> player.getItems().getItemCount(id) > 0);
					boolean hasGuardianItem = IntStream.range(20665, 20691).anyMatch(id -> player.getItems().playerHasItem(id));
					if (hasGuardianItem) {
						IntStream.range(20665, 20691).forEach(id -> player.getItems().deleteItem(id, 1));
						player.getItems().addItem(data.getPetId(), 1);
					}
					if (!hasGuardian) {
					if (Misc.random(data.getPetChance()) == 18) {
						PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] @cr20@ <col=255>" + player.playerName + "</col> successfully crafted a <col=CC0000>Rift guardian</col> pet!");
						player.getItems().addItemUnderAnyCircumstance(data.getPetId(), 1);
					}
				}
			}
		}
	}
}