package ethos.model.content.preset;

import java.util.Arrays;

import ethos.model.players.Player;
import ethos.util.Misc;
 
public class PresetManager {

	private final Player player;
	
	public PresetManager(Player player) {
		this.player = player;
		for (int i = 0; i < 6; i++)
			presets[i] = new PresetClass();

		for (PresetClass preset : presets) {
			Arrays.fill(preset.inventoryN, 1);
			Arrays.fill(preset.equipmentN, 1);
		}
	}
	
	public static PresetClass[] purchaseable = new PresetClass[4];
	
	static {

		for (int i = 0; i < 4; i++)
			purchaseable[i] = new PresetClass();
		
		for (PresetClass preset : purchaseable) {
			Arrays.fill(preset.inventoryN, 1);
			Arrays.fill(preset.equipmentN, 1);
		}
		for (int i = 0; i < 15; i++) {
		purchaseable[0].title = "Berserker loadout (600k)";
		purchaseable [0].equipment[i] = -1;
		purchaseable[0].equipment[1] = 3751;
		purchaseable[0].equipment[3] = 1052;
		purchaseable[0].equipment[9] = 7458;
		purchaseable[0].price = 600_000;
		purchaseable[1].title = "Pure loadout (400k)";
		purchaseable [1].equipment[i] = -1;
		purchaseable[1].equipment[1] = 662;
		purchaseable[1].equipment[3] = 6111;
		purchaseable[1].equipment[9] = 7458;
		purchaseable[1].price = 400_000;
		purchaseable[2].title = "Max loadout (500k)";
	purchaseable [2].equipment[i] = -1;
		purchaseable[2].equipment[1] = 10828;
		purchaseable[2].equipment[3] = 6111;
		purchaseable[2].equipment[9] = 7458;
		purchaseable[2].price = 500_000;
		purchaseable[3].title = "Skiller (300k)";
		purchaseable [3].equipment[i] = -1;
		purchaseable[3].inventory[0] = 1755;
		purchaseable[3].inventory[1] = 946;
		purchaseable[3].price = 300_000;
		}
	}
	
	public PresetClass[] presets = new PresetClass[6];

	public int selectedClass, selectedPurchaseable = -1;
	
	public boolean bankItems, reOpen;

	public boolean clickButton(int buttonId) {
		if (buttonId >= 125006 && buttonId <= 125027) {
			selectedClass = (buttonId - 125006)/3;
			selectedPurchaseable = -1;
			player.getPA().sendString("Gear up!", 32053);
			displayPreset();
		}
		if (buttonId >= 125075 && buttonId <= 125078) {
			selectedPurchaseable = buttonId - 125075;
			player.getPA().sendString("Buy (" + purchaseable[selectedPurchaseable].price/1000 +"K)", 32053);
			displayPreset();
		}
		switch(buttonId) {
		case 125059:
			selectedPurchaseable = -1;
			player.getPA().sendString("Gear up!", 32053);
			clear();
			return true;
		case 125062:
			reOpen = !reOpen;
			player.getPA().sendConfig(345, reOpen?1:0);
			return true;
		case 125064:
			bankItems = !bankItems;
			player.getPA().sendConfig(346, bankItems?1:0);
			return true;
		case 125054:
			upload();
			return true;
		case 125050:
			gearUp();
			return true;
		}
		return false;
	}
	
	public void clear() {
		presets[selectedClass] = new PresetClass();
		Arrays.fill(presets[selectedClass].inventoryN, 1);
		Arrays.fill(presets[selectedClass].equipmentN, 1);
		displayPreset();
		selectedPurchaseable = -1;
		displayTitles();
	}
	
	public void setTitle(String title) {
		presets[selectedClass].title = title;
		displayTitles();
	}
	
	public void open() {
		displayTitles();
		displayPreset();
		player.getPA().sendConfig(345, reOpen?1:0);
		player.getPA().sendConfig(346, bankItems?1:0);
		for (int idx = 0; idx < 4; idx++)
			player.getPA().sendString(purchaseable[idx].title, 32071+idx);
		player.getPA().showInterface(32000);
	}
	
	public void displayTitles() {
		for (int idx = 0; idx < presets.length; idx++) {
			String title = presets[idx].title;
			if (title == "New class")
				title = "Preset " + (idx + 1);
			player.getPA().sendString(title, 32024+idx);
		}
	}
	
	public void displayPreset() {
		PresetClass preset = selectedPurchaseable >= 0 ? purchaseable[selectedPurchaseable] : presets[selectedClass];
		player.getPA().sendItems(player, 32035, preset.inventory, preset.inventoryN, 28, false);  
		player.getPA().sendItems(player, 32036, preset.equipment, preset.equipmentN, 11, true);  
		player.getPA().sendString(preset.spellbook, 32068);
		player.getPA().sendString(preset.quickprayers != null && anySet() ? "Set" : "Not set!", 32070);
	} 

	public boolean anySet() {
		PresetClass preset = selectedPurchaseable >= 0 ? purchaseable[selectedPurchaseable] : presets[selectedClass];
		for (boolean prayer : preset.quickprayers)
			if (prayer)
				return true;
		return false;
	}
	
	public int getIndex(int index) {
		switch (index) {
		case 0:
			return 1;
		case 1:
			return 3;
		case 2:
			return 4;
		case 7:
			return 10;
		case 9:
			return 12;
		case 10:
			return 13; 
		case 12:
			return 14;
		case 13:
			return 5;
		}
		if (index >= 3 && index <= 8)
			return index + 3;
		return 0;
	}

	public void upload() {
		presets[selectedClass].inventory = Arrays.copyOf(player.playerItems, player.playerItems.length);
		presets[selectedClass].inventoryN = Arrays.copyOf(player.playerItemsN, player.playerItemsN.length);
		for (int idx = 0; idx < presets[selectedClass].inventoryN.length; idx++) {
			if (presets[selectedClass].inventoryN[idx] == 0)
				presets[selectedClass].inventoryN[idx] = 1;
		} 
		for (int idx = 0; idx < player.playerEquipment.length; idx++) {
			if (player.playerEquipmentN[idx] > 0)
				presets[selectedClass].equipmentN[getIndex(idx)] = player.playerEquipmentN[idx];
			presets[selectedClass].equipment[getIndex(idx)] = player.playerEquipment[idx];
		}
		int book = player.playerMagicBook;
		presets[selectedClass].spellbook = book == 0 ? "Modern" : book == 1 ? "Ancient" : "Lunar";
		presets[selectedClass].quickprayers = player.getQuick().normal;
		displayPreset();
	}
	
	public void gearUp() {
		boolean purchase = selectedPurchaseable >= 0;
		PresetClass preset = purchase ? purchaseable[selectedPurchaseable] : presets[selectedClass];
		if (purchase) {
			if (player.getItems().playerHasItem(995, preset.price)) {
				player.getItems().deleteItem(995, preset.price);
			} else {
				player.sendMessage("You must have at least " + Misc.format(preset.price) + " coins to purchase this class.");
				return;
			}
		}
		if (bankItems || purchase) {
			for (int idx = 0; idx < player.playerItems.length; idx++)
				if (player.playerItems[idx] > 0)
					player.getItems().addItemToBank(player.playerItems[idx]-1, player.playerItemsN[idx]);
			player.playerItems = new int[28];
			for (int idx = 0; idx < player.playerEquipment.length; idx++)
				if (player.playerEquipment[idx] > 0) {
					player.getItems().addItemToBank(player.playerEquipment[idx], player.playerEquipmentN[idx]);
					player.playerEquipment[idx] = -1;
					player.playerEquipmentN[idx] = 1; 
					player.getItems().updateEquipment(idx);
				}
		} 
		for (int idx = 0; idx < preset.inventory.length; idx++)
			if (purchase || player.getItems().bankContains(preset.inventory[idx]-1, preset.inventoryN[idx]))
				if (purchase || player.getItems().removeFromAnyTabWithoutAdding(preset.inventory[idx]-1, preset.inventoryN[idx], false)) {
					player.playerItems[idx] = preset.inventory[idx];
					player.playerItemsN[idx] = preset.inventoryN[idx];
				}
		player.getItems().updateInventory();
		for (int idx = 0; idx < player.playerEquipment.length; idx++)
			if (idx != 6 && idx != 8 && (purchase || player.getItems().bankContains(preset.equipment[getIndex(idx)], preset.equipmentN[getIndex(idx)])))
				if (player.playerEquipment[idx] <= 0 && preset.equipment[getIndex(idx)] > 0)
					if (purchase || player.getItems().removeFromAnyTabWithoutAdding(preset.equipment[getIndex(idx)], preset.equipmentN[getIndex(idx)], false))
						player.getItems().wearItem(preset.equipment[getIndex(idx)], preset.equipmentN[getIndex(idx)], idx);
		if (preset.spellbook.equals("Modern")) {
			player.sendMessage("You switch to modern magic.");
			player.setSidebarInterface(6, 938); // modern
			player.playerMagicBook = 0;
		} else if (preset.spellbook.equals("Ancient")) {
			player.sendMessage("You switch to ancient magic.");
			player.setSidebarInterface(6, 838); // ancient
			player.playerMagicBook = 1;
		} else if (preset.spellbook.equals("Lunar")) {
			player.sendMessage("You switch to lunar magic.");
			player.setSidebarInterface(6, 29999);
			player.playerMagicBook = 2;
		}
		player.setQuick(preset.quickprayers);
	}
}
