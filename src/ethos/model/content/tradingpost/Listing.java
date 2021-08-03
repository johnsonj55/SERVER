package ethos.model.content.tradingpost;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ethos.Config;
import ethos.Server;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.items.ItemDefinition;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.util.Misc;

/**
 * @author Nighel, Nicholas
 */
public class Listing {

    // make true to preload all sales and keep them all in the cache
    private static final boolean PRELOAD_ALL = false;

    // the next ID to be assigned to a sale, increment every time someone makes a new sale
    private static int NEXT_SALE_ID = 1;

    // Number of sales to keep in memory, irrelevant if PRELOAD_SALES is true
    private static int CACHE_SIZE = 100;

    // recently read sales kept in memory for faster access
    private static LinkedList<Sale> cache = new LinkedList<>();

    public enum Category {
        MAIN, RECENT, PLAYER, ITEM
    }

    /**
     * Updates the sale id for a new sale
     */
    public static void updateSaleId() {
        File f = new File("./data/tradingpost/sales/");
        File[] files = f.listFiles();
        if (files != null)
            NEXT_SALE_ID = files.length + 1;
    }

    /**
     * Loads the sales via player name
     *
     * @param playerName - player his username
     * @return List that contains the sales of the player with playerName
     */
    private static List<Sale> getSales(String playerName) {
        String line;
        LinkedList<Sale> sales = new LinkedList<>();
        File file = new File("./data/tradingpost/players/" + playerName + ".txt");
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                System.out.println("Could not create directory: " + file.getParentFile().getAbsolutePath());
            }
        }
        try {
            if (!file.exists()) {
                return sales;
            }
            BufferedReader br = new BufferedReader(new FileReader("./data/tradingpost/players/" + playerName + ".txt"));

            while ((line = br.readLine()) != null) {
                int id = Integer.parseInt(line);
                sales.add(getSale(id));
            }

            br.close();

            return sales;
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    /**
     * Loads the sales via item id
     *
     * @param itemId sales with item id
     * @return list with sales with itemId
     */

    private static List<Sale> getSales(int itemId) {
        String line;
        LinkedList<Sale> sales = new LinkedList<>();
        // read text file at /players/playerName.txt
        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/tradingpost/items/" + itemId + ".txt"));

            while ((line = br.readLine()) != null) {
                int id = Integer.parseInt(line);
                sales.add(getSale(id));
            }

            br.close();

            return sales;
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    /**
     * Gets the sale via the id
     *
     * @param saleId - id of the sale
     * @return Sale with saleId
     */
    public static Sale getSale(int saleId) {
        String[] split;
        // Check cache for this sale
        for (Sale sale : cache)
            if (sale.getSaleId() == saleId)
                return sale;

        // read text file at /sale/saleId.txt
        try {
            BufferedReader br = new BufferedReader(new FileReader("./data/tradingpost/sales/" + saleId + ".txt"));

            // read information
            split = br.readLine().split("\t");
            Sale sale = new Sale(saleId, split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Boolean.parseBoolean(split[6]));

            // If the cache is full, remove the last Sale. Add this one to the beginning either way.
            if (!PRELOAD_ALL && cache.size() == CACHE_SIZE)
                cache.removeLast();
            cache.addFirst(sale);

            br.close();

            return sale;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Opens up the first interface for the trading post.
     * And then loading all the data thats needed.
     *
     * @param c         Player to open post for
     * @param listItem  true if listed an item, false if not
     * @param openFirst true if lines has to be emptied, else not
     */
    
    public static boolean open = false;
    public static void openPost(Player c, boolean listItem, boolean openFirst) {
    	if (open == false) {
    		c.sendMessage("Currently disabled.");
    		return;
    	}
        if (!c.getMode().isTradingPermitted(null, null)) { 
            c.sendMessage("You are not permitted to make use of this.");
            return;
        }
        resetEverything(c);
        emptyInterface(c, openFirst);
        if (listItem) {
            String each = c.quantity > 1 ? "each" : "";
            c.sendMessage("[@red@Trading Post@bla@] You successfully list " + c.quantity + "x " + ItemAssistant.getItemName(c.item) + " for " + Misc.format(c.price) + " GP " + each);
            c.item = -1;
            c.uneditItem = -1;
            c.quantity = -1;
            c.price = -1;
        }
        loadPlayersListings(c);
        c.getPA().showInterface(48600);
        c.insidePost = true;
        loadHistory(c);
    }

    /**
     * Makes all the listings show up for the player
     *
     * @param c Player to load the listings for
     */
    private static void loadPlayersListings(Player c) {
        int start = 48788, id = 0, moneyCollectible = 0;

        LinkedList<Sale> sales = (LinkedList<Sale>) getSales(c.playerName);

        for (Sale sale : sales) {
            c.getPA().sendTradingPost(48847, sale.getId(), id, sale.getQuantity());
            id++;
            c.getPA().sendFrame126(ItemAssistant.getItemName(sale.getId()), start);
            start++;
            c.getPA().sendFrame126("" + zerosIntoMills(sale.getPrice()), start);
            start++;
            c.getPA().updateProgressBar(start, (byte)(100 * (sale.getTotalSold() / (double)sale.getQuantity())), sale.getTotalSold() + " / " + sale.getQuantity());
            //c.getPA().sendFrame126(sale.getTotalSold() + " / " + sale.getQuantity(), start);
            start += 2;
            moneyCollectible += (sale.getPrice() * sale.getLastCollectedAmount());
        }
        c.getPA().sendFrame126(Misc.format(moneyCollectible) + " GP", 48610);
        for (int k = id; k < 15; k++) {
            c.getPA().sendTradingPost(48847, -1, k, -1);
        }
        for (int i = start; i < 48848; i++) {
            if ((i - start + 2) % 4 == 0) {
                c.getPA().updateProgressBar(i, (byte)-1, "");
            }
        }
        for (int i = start; i < 48848; i++) {
            c.getPA().sendFrame126("", i);
        }
    }

    /**
     * Shows the last 10 latest sales you have done.
     *
     * @param player load history for this player
     */
    private static void loadHistory(Player player) {
        for (int i = 0, start1 = 48636, start2 = 48637; i < player.saleItems.size(); i++) {
            //System.out.println("salesItems - " + player.saleItems.get(i).intValue());
            //System.out.println("saleAmount - " + player.saleAmount.get(i).intValue());
            //System.out.println("salePrice - " + player.salePrice.get(i).intValue());
            if (player.saleItems.get(i) > 0 && player.saleAmount.get(i) > 0 && player.salePrice.get(i) > 0) {
                String each = player.saleAmount.get(i) > 1 ? "each" : "coins";
                player.getPA().sendFrame126(player.saleAmount.get(i) + " x " + ItemAssistant.getItemName(player.saleItems.get(i)), start1);
                player.getPA().sendFrame126("sold for " + zerosIntoMills(player.salePrice.get(i)) + " " + each, start2);
                start1 += 2;
                start2 += 2;
            }
        }
    }

    /**
     * Opens up the selected item using offer 1/5/10/all/x
     *
     * @param c      player to open the selected item for
     * @param itemId of the item to open
     * @param amount of the item to open
     * @param price  of the item to open
     */
    public static void openSelectedItem(Player c, int itemId, int amount, int price) {
        //System.out.println("");
        if (!c.getItems().playerHasItem(itemId, amount)) {
            c.sendMessage("[@red@Trading Post@bla@] You don't have that many " + ItemAssistant.getItemName(itemId) + (amount > 1 ? "s" : "") + ".");
            return;
        }
        if (ItemDefinition.forId(itemId) != null) {
            if (!ItemDefinition.forId(itemId).isTradable()) {
                c.sendMessage("[@red@Trading Post@bla@] You can't sell that item");
                return;
            }
        }
        for (int item : Config.NOT_SHAREABLE) {
            if (item == itemId) {
                c.sendMessage("[@red@Trading Post@bla@] You can't sell that item");
                return;
            }
        }
        if (itemId == 995) {
            c.sendMessage("[@red@Trading Post@bla@] You can't sell that item");
            return;
        }
        //if(c.uneditItem <= 0) - Caused a dupe if you changed items
        c.uneditItem = itemId;
        //Config.trade
        c.item = -1;

        c.inSelecting = false;
        c.isListing = true;
        boolean noted = Item.itemIsNote[itemId];
        //boolean noted = ItemDefinition.forId(itemId).isNoteable();
        if (noted)
            itemId--;

        c.item = itemId;
        c.quantity = amount;
        //c.price = price >= 1 ? price : (int) itemList.ShopValue; //c.getInventory().getItemshopValue(c.item);
        c.price = price >= 1 ? price : ItemDefinition.forId(itemId).getValue();
        c.getPA().showInterface(48598);
        c.getPA().sendTradingPost(48962, itemId, 0, amount);
        c.getPA().sendFrame126(ItemAssistant.getItemName(itemId), 48963); //item name
        c.getPA().sendFrame126("Price (each): " + Misc.format(c.price) + "", 48964); //price each
        c.getPA().sendFrame126("Quantity: " + amount, 48965); //quantity
        //c.getPA().sendFrame(s, 48966); //guide
        //c.getPA().sendFrame(s, 48967); //listings
    }

    /**
     * Writes every thing the the proper files.
     *
     * @param c player that lists an item
     */
    private static void confirmListing(Player c) {

        if (c.uneditItem == -1) {
            if (c.debugMessage)
                c.sendMessage("Stopped");
            return;
        }
        File salesDir = new File("./data/tradingpost/sales/");
        File itemsDir = new File("./data/tradingpost/items/");
        File playersDir = new File("./data/tradingpost/players/");
        if (!salesDir.exists())
            if (!salesDir.mkdirs())
                System.out.println("Couldn't make dir: " + salesDir.getAbsolutePath());
        if (!itemsDir.exists())
            if (!itemsDir.mkdirs())
                System.out.println("Couldn't make dir: " + itemsDir.getAbsolutePath());
        if (!playersDir.exists())
            if (!playersDir.mkdirs())
                System.out.println("Couldn't make dir: " + playersDir.getAbsolutePath());

        BufferedWriter sale_id;
        BufferedWriter item_id;
        BufferedWriter name;
        try {
            sale_id = new BufferedWriter(new FileWriter("./data/tradingpost/sales/" + NEXT_SALE_ID + ".txt", true));
            item_id = new BufferedWriter(new FileWriter("./data/tradingpost/items/" + c.item + ".txt", true));
            name = new BufferedWriter(new FileWriter("./data/tradingpost/players/" + c.playerName + ".txt", true));

            sale_id.write(c.playerName + "\t" + c.item + "\t" + c.quantity + "\t0\t" + c.price + "\t0\t" + "false");
            sale_id.newLine();

            item_id.write("" + NEXT_SALE_ID);
            item_id.newLine();

            name.write("" + NEXT_SALE_ID);
            name.newLine();

            //try {
            //CreateListing.getSingleton().createListing(NEXT_SALE_ID, c.item, c.getPA().getItemName(c.item), c.quantity, c.price, c.playerName, 0);
            //} catch (Exception e) {
            //	e.printStackTrace();
            //}
            Sale sale = new Sale(NEXT_SALE_ID, c.playerName, c.item, c.quantity, 0, c.price, 0, false);

            ++NEXT_SALE_ID;

            if (!PRELOAD_ALL && cache.size() == CACHE_SIZE)
                cache.removeLast();
            cache.addFirst(sale);

            sale_id.close();
            item_id.close();
            name.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (c.debugMessage)
            c.sendMessage("uneditItem " + c.uneditItem + " - c.item " + c.item + " - quanity: " + c.quantity);
        c.getItems().deleteItem2(c.uneditItem, c.quantity);
        for (Player player : PlayerHandler.getPlayers()) {
            if (player.searchCategory != null) {
                refreshInterface(player, c.item, c.playerName);
            }
        }
        openPost(c, true, false);
        PlayerSave.save(c);
    }

    /**
     * Refresh the interface only when the player is in the recent category, is looking up the player or is looking up the item
     * @param player to update the interface for
     * @param item id that the player might be searching for
     * @param playerName that the player might be looking up
     */
    private static void refreshInterface(Player player, int item, String playerName) {
        System.out.println(player.playerName + " " + player.lookup + " " + player.searchCategory.name() + " " + ItemAssistant.getItemName(item).toLowerCase() + " " + playerName + " " + item);
        switch (player.searchCategory) {
            case RECENT:
                loadRecent(player);
                break;
            case PLAYER:
                if (playerName.toLowerCase().contains(player.lookup.toLowerCase()))
                    loadPlayerName(player, player.lookup);
                break;
            case ITEM:
                if (ItemAssistant.getItemName(item).toLowerCase().contains(player.lookup.toLowerCase()))
                    loadItemName(player, player.lookup);
                break;
        }
    }

    /**
     * Cancel a listing via its sale id
     *
     * @param player that cancels the listing
     * @param id     of the sale that is cancelled
     * @param itemId of the item that is cancelled
     */
    public static void cancelListing(Player player, int id, int itemId) {
        if (id < 0 || itemId < 0)
            return;
        Sale sales = getSales(player.playerName).get(id);
        int leftOver = sales.getQuantity() - sales.getTotalSold(), saleItem = sales.getId();
        boolean stackable = Item.itemStackable[saleItem];
        boolean isNoted = Item.itemIsNote[saleItem];
        if (!stackable && !isNoted && leftOver > 1) {
            saleItem++;
        }
        if (sales.getId() == itemId) { //gg
            sales.setHasSold(true);
            save(sales);
            updateHistory(player, sales.getId(), sales.getTotalSold(), sales.getPrice());
            if (leftOver > 0) {
                if ((((player.getItems().freeSlots() >= 1) || player.getItems().playerHasItem(saleItem, 1)) && Item.itemIsNote[saleItem]) || ((player.getItems().freeSlots() > 0) && !Item.itemStackable[saleItem])) {
                    player.getItems().addItem(saleItem, leftOver);
                    player.sendMessage("[@red@Trading Post@bla@] You succesfully cancel the offer for " + leftOver + "x " + ItemAssistant.getItemName(sales.getId()) + ".");
                } else {// If inventory is full!
                    player.getItems().addItemToBank(saleItem, leftOver);
                    player.sendMessage("[@red@Trading Post@bla@] You succesfully cancel the offer for " + leftOver + "x " + ItemAssistant.getItemName(sales.getId()) + ".");
                    player.sendMessage("[@red@Trading Post@bla@] You had no room so your " + leftOver + "x " + ItemAssistant.getItemName(sales.getId()) + " was sent to your bank.");
                }
            }
            loadPlayersListings(player);
            PlayerSave.save(player);
        }
        for (Player otherPlayer : PlayerHandler.getPlayers()) {
            if (otherPlayer.searchCategory != null) {
                refreshInterface(otherPlayer, itemId, player.playerName);
            }
        }
    }

    /**
     * Collecting your money via the button
     *
     * @param player that collects the money
     */
    private static void collectMoney(Player player) {
        LinkedList<Sale> sales = (LinkedList<Sale>) getSales(player.playerName);
        int moneyCollectable = 0;
        for (Sale sale : sales) {
            moneyCollectable += (sale.getPrice() * sale.getLastCollectedAmount());
            sale.setLastCollectedSold(0);
            save(sale);
        }
        player.getItems().addItem(995, moneyCollectable);
        player.sendMessage("[@red@Trading Post@bla@] You successfully collect " + Misc.format(moneyCollectable) + " coins from your coffer.");
        moneyCollectable = 0;
        player.getPA().sendFrame126(Misc.format(moneyCollectable) + " GP", 48610);
        PlayerSave.save(player);
    }

    /**
     * Save the sale to files
     *
     * @param sale to save
     */
    public static void save(Sale sale) {
        String line;
        StringBuilder newLine = new StringBuilder();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./data/tradingpost/sales/" + sale.getSaleId() + ".txt"));
            writer.write(sale.getName() + "\t" + sale.getId() + "\t" + sale.getQuantity() + "\t" + sale.getTotalSold() + "\t" + sale.getPrice() + "\t" + sale.getLastCollectedAmount() + "\t" + sale.hasSold());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (sale.hasSold()) {
            if (sale.getLastCollectedAmount() > 0) {
                Player c = PlayerHandler.players[PlayerHandler.getPlayerID(sale.getName())];
                c.getItems().addItem(995, sale.getLastCollectedAmount() * sale.getPrice());
                sale.setLastCollectedSold(0);
            }
            try {
				/*try {
					if(sale.getTotalSold() != sale.getQuantity())
						CreateListing.getSingleton().updateListing(sale.getSaleId(), sale.getQuantity(), false);
					else
						CreateListing.getSingleton().updateListing(sale.getSaleId(), sale.getQuantity(), true);
				} catch (SQLException e) {
					e.printStackTrace();
				}*/
                BufferedReader read = new BufferedReader(new FileReader("./data/tradingpost/players/" + sale.getName() + ".txt"));
                while ((line = read.readLine()) != null) {
                    if (line.equals(Integer.toString(sale.getSaleId()))) continue;
                    newLine.append(line).append(System.getProperty("line.separator"));
                }
                read.close();
                BufferedWriter write = new BufferedWriter(new FileWriter("./data/tradingpost/players/" + sale.getName() + ".txt"));
                write.write(newLine.toString());
                write.close();
                newLine = new StringBuilder();
                read = new BufferedReader(new FileReader("./data/tradingpost/items/" + sale.getId() + ".txt"));
                while ((line = read.readLine()) != null) {
                    if (line.equals(Integer.toString(sale.getSaleId()))) continue;
                    newLine.append(line).append(System.getProperty("line.separator"));
                }
                read.close();
                write = new BufferedWriter(new FileWriter("./data/tradingpost/items/" + sale.getId() + ".txt"));
                write.write(newLine.toString());
                write.close();
                //newLine = new StringBuilder();
                write = new BufferedWriter(new FileWriter("./data/tradingpost/sales/" + sale.getSaleId() + ".txt"));
                newLine = new StringBuilder(sale.getName() + "\t" + sale.getId() + "\t" + sale.getQuantity() + "\t" + sale.getTotalSold() + "\t" + sale.getPrice() + "\t" + sale.getLastCollectedAmount() + "\t" + sale.hasSold());
                write.write(newLine.toString());
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays the 6 sales based on pages and item name/player name and recent
     *
     * @param sales  list of all sales
     * @param player to display the sales to
     */
    private static void displayResults(List<Sale> sales, Player player) {
        List<Integer> result = new ArrayList<>();
        int total = 0, skipped = 0, start = 48022;
        for (Sale sale : sales) {
            if (sale.hasSold() || sale.getTotalSold() == sale.getQuantity()) continue;
            if (skipped < (player.pageId - 1) * 6) {
                skipped++;
                continue;
            }
            result.add(sale.getSaleId());
            player.getPA().sendTradingPost(48021, sale.getId(), total, sale.getQuantity() - sale.getTotalSold());
            player.getPA().sendFrame126(ItemAssistant.getItemName(sale.getId()), start);
            start++;
            String each = sale.getQuantity() - sale.getTotalSold() > 1 ? " each" : "";
            player.getPA().sendFrame126(Misc.format(sale.getPrice()) + each, start);
            start++;
            player.getPA().sendFrame126(sale.getName(), start);
            start++;
            player.getPA().sendFrame126(Integer.toString(sale.getTotalSold()), start);
            start++;
            total++;
            if (total == 6) {
                //System.out.println("Reached 6 recent sales");
                break;
            }
        }
        for (int k = total; k < 6; k++) {
            player.getPA().sendTradingPost(48021, -1, k, -1);
        }
        for (int i = start; i < 48046; i++) {
            player.getPA().sendFrame126("", i);
        }
        player.saleResults = result;
    }

    /**
     * Loads the recent sales
     *
     * @param player to load the recent listings for
     */
    private static void loadRecent(Player player) {
        player.pageId = 0;
        player.searchCategory = Category.RECENT;
        player.getPA().sendFrame126("Trading Post - Recent listings", 48019);
        player.getPA().showInterface(48000);
        List<Sale> sales = new LinkedList<>();
        int total = 0;

        for (int i = NEXT_SALE_ID - 1; i > 0; i--) {
            Sale sale = getSale(i);
            if (sale == null || sale.hasSold()) continue;
            total++;
            sales.add(sale);
            if (total == 60)
                break;
        }
        displayResults(sales, player);
    }

    /**
     * This method handles the buying of a listing
     *
     * @param player that buys a listing
     * @param slot   of the sale in the sales list
     * @param amount of the item in the listing
     */
    public static void buyListing(Player player, int slot, int amount) {
        if (!player.getMode().isTradingPermitted(null, null)) {
            player.sendMessage("You are not permitted to make use of this.");
            return;
        }

        Sale sales = getSale(player.saleResults.get(slot));

        if (sales == null || sales.getQuantity() == sales.getTotalSold())
            return;

        if (sales.getName().equalsIgnoreCase(player.playerName)) {
            player.sendMessage("[@red@Trading Post@bla@] You cannot buy your own listings.");
            return;
        }

        if (amount > sales.getQuantity())
            amount = sales.getQuantity();

        if (!player.getItems().playerHasItem(995, sales.getPrice() * amount)) {
            player.sendMessage("[@red@Trading Post@bla@] You need atleast " + Misc.format(sales.getPrice() * amount) + " coins to buy the " + amount + "x " + ItemAssistant.getItemName(sales.getId()) + ".");
            return;
        }
        int slotsNeeded = amount;

        int saleItem = sales.getId();

        if (amount > 1 && Item.itemIsNote[sales.getId() + 1]) {
            saleItem++;
        }

        if (player.getItems().freeSlots() < slotsNeeded && (!Item.itemIsNote[sales.getId() + 1] && !Item.itemStackable[sales.getId()])) {
            player.sendMessage("[@red@Trading Post@bla@] You need atleast " + slotsNeeded + " free slots to buy this.");
            return;
        }

        player.getItems().deleteItem(995, sales.getPrice() * amount);
        player.getItems().addItem(saleItem, amount);
        player.sendMessage("[@red@Trading Post@bla@] You succesfully purchase " + amount + "x " + ItemAssistant.getItemName(sales.getId()) + ".");
        player.getItems().resetItems(3214);
        PlayerSave.save(player);

        player.sendMessage("saleId: " + sales.getSaleId());

        player.sendMessage("collect: " + sales.getLastCollectedAmount());
        player.sendMessage("total sold: " + sales.getTotalSold());

        sales.setLastCollectedSold(sales.getLastCollectedAmount() + amount);
        sales.setTotalSold(sales.getTotalSold() + amount);

        player.sendMessage("collect 2: " + sales.getLastCollectedAmount());
        player.sendMessage("total sold 2: " + sales.getTotalSold());
        save(sales);

        if (PlayerHandler.getPlayerID(sales.getName()) != -1) {
            Player seller = PlayerHandler.players[PlayerHandler.getPlayerID(sales.getName())];
            if (seller != null) {
                if (seller.playerName.equalsIgnoreCase(sales.getName())) {
                    if (sales.getTotalSold() < sales.getQuantity())
                        seller.sendMessage("[@red@Trading Post@bla@] You succesfully sold " + amount + "x of your " + ItemAssistant.getItemName(sales.getId()) + ".");
                    else
                        seller.sendMessage("[@red@Trading Post@bla@] Finished selling your " + ItemAssistant.getItemName(sales.getId()) + ".");

                    PlayerSave.save(seller);
                    if (seller.isListing) {
                        loadPlayersListings(seller);
                    }
                }
            }
        }

        for (Player otherPlayer : PlayerHandler.getPlayers()) {
            if (otherPlayer.searchCategory != null) {
                refreshInterface(otherPlayer, saleItem, sales.getName());
            }
        }

    }

    /**
     * Loads the sales via playerName
     *
     * @param c          The player that looks up a player name
     * @param playerName The player name that is searched
     */
    public static void loadPlayerName(Player c, String playerName) {
        c.lookup = playerName;
        c.searchCategory = Category.PLAYER;
        c.getPA().showInterface(48000);
        c.getPA().sendFrame126("Trading Post - Searching for player: " + playerName, 48019);

        List<Sale> sales = new LinkedList<>();

        for (String s : Objects.requireNonNull(new File("./data/tradingpost/players/").list())) {
            s = s.substring(0, s.indexOf(".")).toLowerCase();
            if (s.contains(playerName.toLowerCase()))
                sales.addAll(getSales(s));
        }
        displayResults(sales, c);
    }

    /**
     * Loads the sales via itemName
     *
     * @param c        The player that looks up an item
     * @param itemName The item name that is being searched
     */
    public static void loadItemName(Player c, String itemName) {
        c.lookup = itemName;
        itemName = itemName.replace("_", " ");
        c.searchCategory = Category.ITEM;
        c.getPA().showInterface(48000);
        c.getPA().sendFrame126("Trading Post - Searching for item: " + itemName, 48019);

        List<Integer> items = new LinkedList<>();
        List<Sale> sales = new LinkedList<>();

        for (String s : Objects.requireNonNull(new File("./data/tradingpost/items/").list()))
            items.add(Integer.parseInt(s.substring(0, s.indexOf("."))));
        for (int item : items) {
            //System.out.println("item: "+ItemAssistant.getItemName(item)+", itemName: " + itemName);
            if (ItemAssistant.getItemName(item).toLowerCase().contains(itemName.toLowerCase())) {
                sales.addAll(getSales(item));
            }
        }

        displayResults(sales, c);
    }

    /**
     * Resets all the necessary stuff;
     *
     * @param c The player to reset everything for
     */
    public static void resetEverything(Player c) {
        c.inSelecting = false;
        c.isListing = true;
        c.insidePost = false;
        c.searchCategory = null;
        c.setSidebarInterface(3, 3213);
    }

    /**
     * Handles the opening of the interface for offering an item
     *
     * @param c The player to open the listing for
     */

    private static void openNewListing(Player c) {
        c.getPA().showInterface(48599);
        c.setSidebarInterface(3, 48500); // backpack tab
        for (int k = 0; k < 28; k++) {
            c.getPA().sendTradingPost(48501, c.playerItems[k] - 1, k, c.playerItemsN[k]);
        }
    }

    /**
     * Handles the buttons for the post interface
     *
     * @param c      The player that presses the button
     * @param button The button id that is pressed
     */
    public static void postButtons(Player c, int button) {
        switch (button) {
            case 189237:
                if (!c.getMode().isTradingPermitted(null, null)) {
                    c.sendMessage("You are not permitted to make use of this.");
                    return;
                }
                LinkedList<Sale> sales = (LinkedList<Sale>) getSales(c.playerName);
                int total = sales.size();
                if (c.amDonated <= 9 && total >= 6) {
                    c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 6 listings as a regular player.");
                    return;
                } else if (c.amDonated >= 10 && c.amDonated <= 149 && total >= 10) {
                    c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 10 listings as a low tier donator.");
                    return;
                } else if (c.amDonated >= 150 && total >= 15) {
                    c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 15 listings.");
                    return;
                }
                if (!c.inSelecting) {
                    openNewListing(c);
                    c.inSelecting = true;
                    c.getPA().sendFrame106(3);
                } else {
                    resetEverything(c);
                    c.getPA().showInterface(48600);
                    c.getPA().sendFrame106(3);
                }
                break;

            case 59229: //Close select item
                c.getPA().closeAllWindows();
                resetEverything(c);
                c.searchCategory = null;
                break;

            case 191072:
                if (!c.getMode().isTradingPermitted(null, null)) {
                    c.sendMessage("You are not permitted to make use of this.");
                    return;
                }
                //synchronized (c) {
                    c.outStream.createFrame(191);
                //}
                c.xInterfaceId = 191072;
                break;

            case 191075: // Removed quantity button
                if (!c.getMode().isTradingPermitted(null, null)) {
                    c.sendMessage("You are not permitted to make use of this.");
                    return;
                }
                //synchronized (c) {
                    c.outStream.createFrame(192);
                //}
                c.xInterfaceId = 191075;
                break;

            case 191078:
                if (!c.getMode().isTradingPermitted(null, null)) {
                    c.sendMessage("You are not permitted to make use of this.");
                    return;
                }
                confirmListing(c);
                break;

            case 189223:
                if (!c.getMode().isTradingPermitted(null, null)) {
                    c.sendMessage("You are not permitted to make use of this.");
                    return;
                }
                collectMoney(c);
                break;

            case 189234:
                if (!c.getMode().isTradingPermitted(null, null)) {
                    c.sendMessage("You are not permitted to make use of this.");
                    return;
                }
                loadRecent(c);
                break;

            case 187133:
                if (!c.getMode().isTradingPermitted(null, null)) {
                    c.sendMessage("You are not permitted to make use of this.");
                    return;
                }
                openPost(c, false, false);
                break;

            case 187136:
                if (!c.getMode().isTradingPermitted(null, null)) {
                    c.sendMessage("You are not permitted to make use of this.");
                    return;
                }
                if (c.pageId > 1)
                    c.pageId--;
                //System.out.println("id: "+c.searchId+" lookup: " + c.lookup);
                switch (c.searchCategory) {
                    case ITEM:
                        loadItemName(c, c.lookup);
                        break;
                    case PLAYER:
                        loadPlayerName(c, c.lookup);
                        break;
                    case RECENT:
                        loadRecent(c);
                        break;
                }
                break;

            case 187139:
                if (!c.getMode().isTradingPermitted(null, null)) {
                    c.sendMessage("You are not permitted to make use of this.");
                    return;
                }
                c.pageId++;
                //System.out.println("id: "+c.searchId+" lookup: " + c.lookup);
                switch (c.searchCategory) {
                    case ITEM:
                        loadItemName(c, c.lookup);
                        break;
                    case PLAYER:
                        loadPlayerName(c, c.lookup);
                        break;
                    case RECENT:
                        loadRecent(c);
                        break;
                }
                break;
        }
    }

    /*
     *
     * This method makes it so it cleans out the history and my offers.
     * Incase you had a diffrent account with more listings.
     *
     */

    private static void emptyInterface(Player c, boolean emptyLines) {
        for (int i = 0; i < 15; i++) {
            c.getPA().sendTradingPost(48847, -1, i, -1);
        }
        if (emptyLines) {
            for (int i = 48636; i < 48656; i++) {
                c.getPA().sendFrame126("", i);
            }
        }
        for (int i = 48787; i < 48847; i++) {
            c.getPA().sendFrame126("", i);
        }
    }

    /*
     *
     * Turns the 100,000,000 into 100m etc.
     *
     */

    private static String zerosIntoMills(int j) {
        if (j >= 0 && j < 1000)
            return String.valueOf(j);
        if (j >= 1000 && j < 10000000)
            return j / 1000 + "K";
        if (j >= 10000000 && j < 2147483647)
            return j / 1000000 + "M";
        return String.valueOf(j);
    }

    private static void updateHistory(Player c, int itemId, int amount, int price) {
        //System.out.println("itemId - " + itemId);
        //System.out.println("amount - " + amount);
        //System.out.println("price - " + price);
        c.saleItems.add(0, itemId);
        c.saleItems.remove(c.saleItems.size() - 1);
        c.saleAmount.add(0, amount);
        c.saleAmount.remove(c.saleAmount.size() - 1);
        c.salePrice.add(0, price);
        c.salePrice.remove(c.salePrice.size() - 1);
        loadHistory(c);
    }

}