package ethos.model.content;

import ethos.Config;
import ethos.model.players.Player;
import ethos.model.players.Position;
import ethos.util.Misc;

public class TeleportTab {

    private static int firstCategoryButton = 195083;
    private static int firstTeleportString = 50029;
    private static int firstTeleportButton = 195109;
    private static Category currentCategory = Category.MONSTERS;
    
  //Here you can add the teleports, make sure you look good at the rest. You see that they all end with , and the last one ends with ;
	//When you want to add a new one, you just copy one of these
    
    public enum Teleport {
        //MONSTERS
        ROCK_CRABS(new Position(2673, 3710, 0)),
        COWS(new Position(3260, 3272, 0)),
        YAKS(new Position(2326, 3801, 0)),
        BOBS_ISLAND(new Position(2524, 4775, 0), "Bob's Island"),
        DESERT_BANDITS(new Position(3176, 2987, 0)),
        ELF_WARRIORS(new Position(2897, 2725, 0)),
        DAGANNOTHS(new Position(2442, 10147, 0)),
        MITHRIL_DRAGONS(new Position(1740, 5342, 0)),
        SLAYER_TOWER(new Position(3428, 3538, 0)),
        DEMONIC_GORILLAS(new Position(2124, 5660, 0)),
        SMOKE_DEVILS(new Position(2808, 10002, 0)),
        EDGEVILLE_DUNGEON(new Position(3096, 9867, 0)),

        //WILDERNESS
        WEST_DRAGONS(new Position(2976, 3591, 0)),
        MAGE_BANK(new Position(2539, 4716, 0)),
        DARK_CASTLE(new Position(3020, 3632, 0)),
        HILL_GIANTS(new Position(3304, 3657, 0)),
        WILDERNESS_AGILITY_COURSE(new Position(3003, 3934, 0)),
        VETION(new Position(3200, 3794, 0), "Vet'ion"),
        CALLISTO(new Position(3325, 3845, 0)),
        SCORPIA(new Position(3233, 3945, 0)),
        VENENATIS(new Position(3345, 3754, 0)),
        CHAOS_ELEMENTAL(new Position(3281, 3910, 0)),
        CHAOS_FANATIC(new Position(2981, 3836, 0)),
        CRAZY_ARCHAELOGOGIST(new Position(2984, 3713, 0)),
        REVENANT_CAVES(new Position(3130, 3831, 0)),
        //BOSSING
        BARRELCHEST(new Position(1229, 3497, 0)),
        DAGANNOTH_KINGS(new Position(1913, 4367, 0)),
        KING_BLACK_DRAGON(new Position(3005, 3849, 0)),
        GAINT_MOLE(new Position(3078, 3924, 0)),
        KALPHITE_QUEEN(new Position(3508, 9494, 0)),
        GOD_WARS_DUNGEON(new Position(2881, 5310, 2)),
        CORPOREAL_BEAST(new Position(2964, 4382, 2)),
        DAGANNOTH_MOTHER(new Position(2508, 3643, 0)),
        KRAKEN(new Position(2280, 10016, 0)),
        ZULRAH(new Position(2202, 3056, 0)),
        CERBERUS(new Position(1310, 1237, 0)),
        THERMONUCLEAR_SMOKE_DEVILS(new Position(2404, 9415, 0)),
        ABYSSAL_SIRE(new Position(3037, 4765, 0)),
        LIZARDMAN_SHAMAN(new Position(1558, 3696, 0)),
        VORKATH(new Position(2272, 4050, 0)),
        SKOTIZO(new Position(1665, 10046, 0)),
        NIGHTMARE(new Position(3808, 9747, 1)),
        CHAOTIC_DEATH_SPAWN(new Position(3512, 9834, 0)),

        //SKILLING
        LANDS_END(new Position(1504, 3419, 0)),
        SKILLING_AREA(new Position(1721, 3464, 0)),
        WOODCUTTING(new Position(1658, 3505, 0)),
        FARMING(new Position(3003, 3376, 0)),
        AGILITY(new Position(2480, 3437, 0)),
        HUNTER(new Position(1580, 3437, 0)),
        PURO_PURO(new Position(2595, 4321, 0)),
        MINING(new Position(3046, 9756, 0)),
        //AREAS
        NEW_TRAINING_AREA(new Position(1817, 3857, 0)),
        RAIDS(new Position(1255, 3562, 0)),
        WARRIORS_GUILD(new Position(2874, 3546, 0)),
        PEST_CONTROL(new Position(2660, 2648, 0)),
        FIGHT_CAVES(new Position(2444, 5179, 0)),
        BARROWS(new Position(3565, 3316, 0)),
        CLAN_WARS(new Position(3387, 3158, 0)),
        SHAYZIEN_ASSAULT(new Position(1461, 3689, 0)),
        MAGE_ARENA(new Position(2541, 4716, 0)),
        DUEL_ARENA(new Position(3365, 3266, 0)),
        INFERNO(new Position(2495, 5110, 0)),
        CATACOMBS(new Position(1661, 10049, 0)),
        FREMENNIK_SLAYER_DUNGEON(new Position(2807, 10003, 0)),
        TAVERLEY_DUNGEON(new Position(2883, 9800, 0)),
        KARUULM_SLAYER_DUNGEON(new Position(1311, 3817, 0)),
        SKELETAL_WYVERNS(new Position(3056, 9555, 0)),
        WYVERNS(new Position(3604, 10231, 0)),
        ASGARNIAN_ICE_DUNGEON(new Position(3048, 9582, 0)),
        BRIMHAVEN_DUNGEON(new Position(2710, 9466, 0)),
        SMOKE_DUNGEON(new Position(3303, 9375, 0)),
        TAVERLY_DUNGEON(new Position(2884, 9796, 0)),
        STRONGHOLD_CAVE(new Position(2432, 3423, 0)),
        
    	 //TOWNS   	
        VARROCK(new Position(3210, 3424, 0)),
        YANILLE(new Position(2606, 3093, 0)),
        LUMBRIDGE(new Position(3222, 3218, 0)),
        ARDOUGNE(new Position(2662, 3305, 0)),
        NEITIZNOT(new Position(2321, 3804, 0)),
        KARAMJA(new Position(2948, 3147, 0)),
        FALADOR(new Position(2964, 3378, 0)),
        TAVERLEY(new Position(2928, 3451, 0)),
        CAMELOT(new Position(2757, 3477, 0)),
        CATHERBY(new Position(2804, 3432, 0)),
        AL_KHARID(new Position(3293, 3179, 0)),
        BARBARIAN(new Position(3087, 3415, 0)),
    	EDGEVILLE(new Position(3086, 3496, 0));
    	
    	
    	//We get an error because of the ; and , and beacuse of the name, it's the same
    	//now we added a teleport. Now we need to add it to the category
    	//Also the position is just x y and height, just like before with the npc

        Position position;
        String name = null;

        Teleport(Position position) {
            this.position = position;
        }

        Teleport(Position position, String name) {
            this(position);
            this.name = name;
        }

        public String getName() {
            if (name == null) {
                return Misc.capitalize(this.name().replaceAll("_", " ").toLowerCase());
            }
            return name;
        }
    }

    public enum Category {
        MONSTERS(new Teleport[]{Teleport.ROCK_CRABS, Teleport.COWS, Teleport.YAKS, Teleport.BOBS_ISLAND, Teleport.DESERT_BANDITS, Teleport.ELF_WARRIORS, Teleport.DAGANNOTHS,
                Teleport.MITHRIL_DRAGONS, Teleport.DEMONIC_GORILLAS, Teleport.EDGEVILLE_DUNGEON}),
        AREAS(new Teleport[]{Teleport.NEW_TRAINING_AREA, Teleport.RAIDS, Teleport.WARRIORS_GUILD, Teleport.PEST_CONTROL, Teleport.FIGHT_CAVES, Teleport.BARROWS, Teleport.CLAN_WARS, Teleport.SHAYZIEN_ASSAULT,Teleport.MAGE_ARENA
        		, Teleport.DUEL_ARENA, Teleport.INFERNO, Teleport.CATACOMBS,Teleport.FREMENNIK_SLAYER_DUNGEON, Teleport.KARUULM_SLAYER_DUNGEON, Teleport.TAVERLEY_DUNGEON, Teleport.SKELETAL_WYVERNS, Teleport.WYVERNS,
       		 Teleport.ASGARNIAN_ICE_DUNGEON, Teleport.BRIMHAVEN_DUNGEON, Teleport.TAVERLY_DUNGEON, Teleport.STRONGHOLD_CAVE, Teleport.SMOKE_DEVILS, Teleport.SLAYER_TOWER}),
        WILDERNESS(new Teleport[]{Teleport.WEST_DRAGONS, Teleport.MAGE_BANK, Teleport.DARK_CASTLE, Teleport.HILL_GIANTS, Teleport.WILDERNESS_AGILITY_COURSE, Teleport.VETION, Teleport.CALLISTO,
                Teleport.SCORPIA, Teleport.VENENATIS, Teleport.CHAOS_ELEMENTAL, Teleport.CHAOS_FANATIC, Teleport.CRAZY_ARCHAELOGOGIST, Teleport.REVENANT_CAVES}),
        BOSSING(new Teleport[]{Teleport.BARRELCHEST, Teleport.DAGANNOTH_KINGS, Teleport.KING_BLACK_DRAGON, Teleport.KALPHITE_QUEEN, Teleport.GOD_WARS_DUNGEON, Teleport.CORPOREAL_BEAST,
        		 Teleport.DAGANNOTH_MOTHER, Teleport.KRAKEN, Teleport.ZULRAH, Teleport.CERBERUS, Teleport.THERMONUCLEAR_SMOKE_DEVILS, Teleport.ABYSSAL_SIRE, Teleport.LIZARDMAN_SHAMAN, Teleport.VORKATH, Teleport.SKOTIZO, Teleport.NIGHTMARE, Teleport.CHAOTIC_DEATH_SPAWN}),
        SKILLING(new Teleport[]{Teleport.LANDS_END, Teleport.SKILLING_AREA, Teleport.WOODCUTTING, Teleport.FARMING, Teleport.AGILITY, Teleport.HUNTER, Teleport.PURO_PURO, Teleport.MINING}),
        TOWNS(new Teleport[]{Teleport.EDGEVILLE, Teleport.VARROCK, Teleport.KARAMJA, Teleport.LUMBRIDGE, Teleport.ARDOUGNE, Teleport.NEITIZNOT, Teleport.YANILLE, Teleport.FALADOR,
        		 Teleport.TAVERLEY, Teleport.CAMELOT, Teleport.CATHERBY, Teleport.AL_KHARID, Teleport.BARBARIAN});
    	
    	//Here are the categories, you can just add it like this. Edgeville is a town, so we add it to the towns
    	//This should be it

        Teleport[] teleports;

        Category(Teleport[] teleports) {
            this.teleports = teleports;
        }

    }

    public static boolean handleButtons(Player player, int buttonId) {
        if (buttonId >= firstTeleportButton && buttonId < firstTeleportButton + 20) {
            if (currentCategory.teleports.length > buttonId - firstTeleportButton) {
            	if(player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
            		player.sendMessage("You can't teleport above " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
    				return true;
    			}
                Teleport teleport = currentCategory.teleports[buttonId - firstTeleportButton];
                player.getPA().startTeleport(teleport.position.getX(), teleport.position.getY(), teleport.position.getHeight(),
                        player.playerMagicBook == 1 ? "ancient" : "modern", false);
                return true;
            }
        }

        if (buttonId >= firstCategoryButton && buttonId < firstCategoryButton + 24) {
            if ((buttonId - firstCategoryButton) % 4 == 0) {
                int index = (buttonId - firstCategoryButton) / 4;
                updateInterface(player, Category.values()[index]);
                return true;
            }
        }
        return false;
    }

    public static void updateInterface(Player player, Category category) {
        currentCategory = category;
        int i = 0;
        for (; i < currentCategory.teleports.length; i++) {
            Teleport teleport = currentCategory.teleports[i];
            player.getPA().sendString(teleport.getName(), firstTeleportString + i);
        }
        for (; i < 20; i++) {
            player.getPA().sendString("", firstTeleportString + i);
        }
    }

}
