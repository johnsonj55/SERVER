package ethos.model.content;

import java.util.*;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.skills.hunter.impling.ItemRarity;
import ethos.model.items.GameItem;
import ethos.model.items.ItemAssistant;
import ethos.util.Misc;

/**
 * Revamped a simple means of receiving a random item based on chance.
 */
public class RandomPetBox extends CycleEvent {

    /**
     * The item id of the mystery box required to trigger the event
     */
    private static final int PET_BOX = 12789;

    /**
     * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
     */
    private static Map<Rarity, List<GameItem>> items = new HashMap<>();

    /**
     * Stores an array of items into each map with the corresponding rarity to the list
     */
    static {
        items.put(Rarity.COMMON,
                Arrays.asList(
                		new GameItem(13320),
                		new GameItem(20663),
                		new GameItem(13262),
                		new GameItem(13321),
                		new GameItem(13322),
                		new GameItem(13323),
                		new GameItem(13324),
                		new GameItem(13325),
                		new GameItem(13326),
                        new GameItem(12644),
                        new GameItem(12643),
                        new GameItem(12645),
                        new GameItem(12653),
                        new GameItem(12647),
                        new GameItem(13181),
                        new GameItem(12648),
                        new GameItem(19730),
                        new GameItem(12655))
        );

        items.put(Rarity.UNCOMMON,
                Arrays.asList(
                        new GameItem(12649),
                        new GameItem(12650),
                        new GameItem(12651),
                        new GameItem(12652),
                        new GameItem(13178),
                        new GameItem(12646))
        );

        items.put(Rarity.RARE,
                Arrays.asList(
                        new GameItem(12939),
                        new GameItem(12940),
                        new GameItem(21992),
                        new GameItem(11995),
                        new GameItem(13177),
                        new GameItem(12921),
                        new GameItem(12816),
                        new GameItem(22382),
                        new GameItem(22378),
                        new GameItem(22380),
                        new GameItem(22473),
                        new GameItem(21273),
                        new GameItem(13179)));
    }

    /**
     * The player object that will be triggering this event
     */
    private Player player;

    /**
     * Constructs a new pet box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public RandomPetBox(Player player) {
        this.player = player;
    }

    /**
     * Opens a pet box if possible, and ultimately triggers and event, if possible.
     */
    public void open() {
        if (System.currentTimeMillis() - player.lastMysteryBox < 150 * 4) {
            return;
        }
        if (player.getItems().freeSlots() < 2) {
            player.sendMessage("You need atleast two free slots to open a pet box.");
            return;
        }
        if (!player.getItems().playerHasItem(PET_BOX)) {
            player.sendMessage("You need a random pet box to do this.");
            return;
        }
        player.getItems().deleteItem(PET_BOX, 1);
        player.lastMysteryBox = System.currentTimeMillis();
        CycleEventHandler.getSingleton().stopEvents(this);
        CycleEventHandler.getSingleton().addEvent(this, this, 2);
    }

    /**
     * Executes the event for receiving the mystery box
     */
    @Override
    public void execute(CycleEventContainer container) {
        if (player.disconnected || Objects.isNull(player)) {
            container.stop();
            return;
        }
        int coins = 200_000 + Misc.random(1_500_000);
        int coinsDouble = 200_000 + Misc.random(1_500_000);
        int random = Misc.random(200);
        List<GameItem> itemList = random < 105 ? items.get(Rarity.COMMON) : random >= 105 && random <= 190 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
        GameItem item = Misc.getRandomItem(itemList);
        GameItem itemDouble = Misc.getRandomItem(itemList);


        if (Misc.random(10) == 0) {
            player.getItems().addItem(995, coins + coinsDouble);
            player.getItems().addItem(item.getId(), item.getAmount());
            player.getItems().addItem(itemDouble.getId(), itemDouble.getAmount());
            player.sendMessage("You receive " + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + ", and "
                    + Misc.insertCommas(Integer.toString(coins)) + " coins.");
            player.sendMessage("You receive " + itemDouble.getAmount() + " x " + ItemAssistant.getItemName(itemDouble.getId()) + ", and "
                    + Misc.insertCommas(Integer.toString(coins)) + " coins.");
           
        } else {
            player.getItems().addItem(995, coins);
            player.getItems().addItem(item.getId(), item.getAmount());
            player.sendMessage("You receive " + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + ", and "
                    + Misc.insertCommas(Integer.toString(coins)) + " coins.");
            for(Map.Entry<Rarity, List<GameItem>> gift : items.entrySet())
                for(GameItem gift_item : gift.getValue())
                    if(gift_item == item)
                        if(gift.getKey() == Rarity.RARE)
                            PlayerHandler.executeGlobalMessage(
                                    "[@blu@Pet Box@bla@] " + Misc.capitalize(player.playerName) + " received a rare pet: @pur@"
                                            + (item.getAmount() > 1 ? (item.getAmount() + "x ") : ItemAssistant.getItemName(item.getId()) + " @bla@from a Random Pet Box!"));
        }
        container.stop();
    }

    /**
     * Represents the rarity of a certain list of items
     */
    enum Rarity {
        UNCOMMON, COMMON, RARE
    }

}