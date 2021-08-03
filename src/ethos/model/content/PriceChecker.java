/*package ethos.model.content;

import java.text.NumberFormat;

import ethos.model.content.PriceChecker.PriceType;
import ethos.model.content.interfaces.InterfaceConstants;
import ethos.model.item.Item;
import main.battleos.game.model.entity.item.container.Container;
import main.battleos.game.model.entity.item.container.Container.ContainerType;
import ethos.model.players.Player;
import main.battleos.game.model.entity.mobile.player.Player.PlayerAttributes;
import ethos.model.location.Area;
import main.battleos.network.packet.outgoing.impl.SendConfig;
import main.battleos.network.packet.outgoing.impl.SendInventoryInterface;
import main.battleos.network.packet.outgoing.impl.SendItemOnInterface;
import main.battleos.network.packet.outgoing.impl.SendItemOnInterfaceSlot;
import main.battleos.network.packet.outgoing.impl.SendMessage;
import main.battleos.network.packet.outgoing.impl.SendRemoveInterface;
import main.battleos.network.packet.outgoing.impl.SendString;
import ethos.model.utility.Utility;

public class PriceChecker extends Container {

	private int[] STRINGS = { 48550, 48551, 48552, 48553, 48554, 48555, 48556, 48557, 48558, 48559, 48560, 48561, 48562, 48563, 48564, 48565, 48566, 48567, 48568, 48569, 48570, 48571, 48572, 48573, 48574, 48575, 48576, 48577, };

	public enum PriceType {
		VALUE(),
		HIGH_ALCH_VALUE();
	}

	public Player player;

	private PriceType priceType;

	private boolean priceGuide;
	
	private Item itemSearching;

	public PriceChecker(Player player) {
		super(28, ContainerType.DEFAULT);
		this.player = player;
		setPriceType(PriceType.VALUE);
		setPriceGuide(false);
	}

	public void open() {
		if (!Area.inSafezone(player)) {
			player.send(new SendMessage("Please enter a safe zone to open the price checker."));
			return;
		}
		
		if (player.getCombat().inCombat()) {
			player.send(new SendMessage("You can not open the price checker while in combat."));
			return;
		}
		
		for (final int id : STRINGS) {
			player.send(new SendString("", id));
		}
		player.send(new SendConfig(237, getPriceType() == PriceType.VALUE ? 1 : 0));
		player.send(new SendItemOnInterface(48581, new Item[] { null }));
		player.send(new SendString("", 48582));
		player.send(new SendString("", 48583));
		refresh();
		player.attr().put(PlayerAttributes.PRICE_CHECKER, Boolean.TRUE);
		player.send(new SendInventoryInterface(48500, 5063));
	}

	public void close() {
		player.send(new SendRemoveInterface());
		withdrawAll();
		clear(false);
		player.attr().put(PlayerAttributes.PRICE_CHECKER, false);
		setItemSearching(null);
	}
	
	public void searchItem(Item item) {
		setItemSearching(item);
		Item[] itemSearch = { getItemSearching() };
		player.send(new SendItemOnInterface(48581, itemSearch));
		player.send(new SendString("<col=ffb000>" + getItemSearching().getName() + ":", 48582));
		player.send(new SendString(Utility.formatDigits((player.getPriceChecker().getPriceType() == PriceType.VALUE ? getItemSearching().getValue() : getItemSearching().getHighAlch())) + "", 48583));
		player.send(new SendMessage("Now displaying <col=ff0000>" + (player.getPriceChecker().getPriceType() == PriceType.VALUE ? "item value" : "item high alch") + "</col> price information for " + getItemSearching().getName() + "."));
	}

	public int deposit(int id, int slot, int amount) {
		return deposit(id, slot, amount, true);
	}

	public int deposit(int id, int slot, int amount, boolean refresh) {
		if (player.getInterfaceManager().getMain() != 48500) {
			return 0;
		}
		
		Item item = player.getInventory().get(slot);

		if (item == null || item.getId() != id) {
			return 0;
		}

		if (!item.isTradeable()) {
			player.send(new SendMessage("This is item is untradeable!"));
			return 0;
		}

		if (item.getAmount() < amount) {
			amount = item.getAmount();
		}

		final int count = player.getInventory().getAmount(id);

		if (count < amount) {
			amount = count;
		}

		final int nextSlot = nextSlot() - (item.isStackable() && contains(id) ? -1 : 0);

		final int added = add(item.getId(), amount, refresh);

		if (item.isStackable() || amount == 1) {
			player.getInventory().removeSlot(slot, added, refresh);
		} else {
			player.getInventory().remove(id, added, refresh);
		}

		if (added <= 0) {
			return 0;
		}

		final int newNextSlot = nextSlot();

		for (int index = nextSlot; index < newNextSlot; index++) {
			if (index < 0) {
				continue;
			}

			if (index >= STRINGS.length) {
				break;
			}

			item = get(index);
			if (item != null) {
				if (getPriceType() == PriceType.VALUE) {
					if (item.isStackable()) {
						player.send(new SendString(NumberFormat.getInstance().format(item.getAmount()) + " x " + NumberFormat.getInstance().format(item.getValue()) + "\\n" + "= " + NumberFormat.getInstance().format(item.getValue() * item.getAmount()), STRINGS[index]));
					} else {
						player.send(new SendString(NumberFormat.getInstance().format(item.getValue()), STRINGS[index]));
					}
				} else {
					if (item.isStackable()) {
						player.send(new SendString(NumberFormat.getInstance().format(item.getAmount()) + " x " + NumberFormat.getInstance().format(item.getHighAlch()) + "\\n" + "= " + NumberFormat.getInstance().format(item.getHighAlch() * item.getAmount()), STRINGS[index]));
					} else {
						player.send(new SendString(NumberFormat.getInstance().format(item.getHighAlch()), STRINGS[index]));
					}
				}
			} else {
				player.send(new SendString("", STRINGS[index]));
			}
		}

		if (refresh) {
			refresh();
		}

		return added;
	}

	public void depositAll() {
		if (player.getInterfaceManager().getMain() != 48500) {
			return;
		}
		
		final Item[] items = player.getInventory().toArray();
		for (int slot = 0; slot < items.length; slot++) {
			final Item item = items[slot];
			if (item == null) {
				continue;
			}
			final int added = deposit(item.getId(), slot, item.getAmount(), false);
			player.getInventory().removeSlot(slot, added, false);
		}
		refresh();
		player.send(new SendMessage("You have deposited some of the items in your inventory."));
	}

	public int withdraw(int id, int slot, int amount) {
		return withdraw(id, slot, amount, true);
	}

	public int withdraw(int id, int slot, int amount, boolean refresh) {
		if (player.getInterfaceManager().getMain() != 48500) {
			return 0;
		}
		
		Item item = get(slot);

		if (item == null || item.getId() != id) {
			return 0;
		}

		if (amount > item.getAmount()) {
			amount = item.getAmount();
		}

		final int added = player.getInventory().add(id, amount, false);

		if (item.isStackable() || amount == 1) {
			removeSlot(slot, added, false);
		} else {
			remove(id, added, false);
		}

		shift(refresh);

		final int nextSlot = nextSlot();

		for (int index = slot; index <= nextSlot; index++) {
			if (index < 0) {
				continue;
			}

			if (index >= STRINGS.length) {
				break;
			}

			item = get(index);
			if (item != null) {
				if (getPriceType() == PriceType.VALUE) {
					if (item.isStackable()) {
						player.send(new SendString(NumberFormat.getInstance().format(item.getAmount()) + " x " + NumberFormat.getInstance().format(item.getValue()) + "\\n" + "= " + NumberFormat.getInstance().format(item.getValue() * item.getAmount()), STRINGS[index]));
					} else {
						player.send(new SendString(NumberFormat.getInstance().format(item.getValue()), STRINGS[index]));
					}
				} else {
					if (item.isStackable()) {
						player.send(new SendString(NumberFormat.getInstance().format(item.getAmount()) + " x " + NumberFormat.getInstance().format(item.getHighAlch()) + "\\n" + "= " + NumberFormat.getInstance().format(item.getHighAlch() * item.getAmount()), STRINGS[index]));
					} else {
						player.send(new SendString(NumberFormat.getInstance().format(item.getHighAlch()), STRINGS[index]));
					}
				}
			} else {
				player.send(new SendString("", STRINGS[index]));
			}
		}

		return added;
	}

	public void withdrawAll() {
		if (player.getInterfaceManager().getMain() != 48500) {
			return;
		}
		
		final int slots = getTakenSlots();
		for (int index = 0; index < slots; index++) {
			if (index >= STRINGS.length) {
				break;
			}

			Item item = get(index);

			if (item != null) {
				final int added = player.getInventory().add(item.getId(), item.getAmount(), false);
				remove(item.getId(), added, false);
				item = get(index);
			}

			if (item != null) {
				if (getPriceType() == PriceType.VALUE) {
					if (item.isStackable()) {
						player.send(new SendString(NumberFormat.getInstance().format(item.getAmount()) + " x " + NumberFormat.getInstance().format(item.getValue()) + "\\n" + "= " + NumberFormat.getInstance().format(item.getValue() * item.getAmount()), STRINGS[index]));
					} else {
						player.send(new SendString(NumberFormat.getInstance().format(item.getValue()), STRINGS[index]));
					}
				} else {
					if (item.isStackable()) {
						player.send(new SendString(NumberFormat.getInstance().format(item.getAmount()) + " x " + NumberFormat.getInstance().format(item.getHighAlch()) + "\\n" + "= " + NumberFormat.getInstance().format(item.getHighAlch() * item.getAmount()), STRINGS[index]));
					} else {
						player.send(new SendString(NumberFormat.getInstance().format(item.getHighAlch()), STRINGS[index]));
					}
				}
			} else {
				player.send(new SendString("", STRINGS[index]));
			}
		}
		refresh();
	}

	@Override
	public void onFillContainer() {
		player.send(new SendMessage("Your price checker is full!"));
	}

	@Override
	public void onMaxStack() {
		player.send(new SendMessage("You do not have enough space to hold that."));
	}

	@Override
	public void refresh() {
		player.getInventory().refresh();
		player.send(new SendItemOnInterface(InterfaceConstants.INVENTORY_STORE, player.getInventory().toArray()));
		player.send(new SendItemOnInterface(48542, toArray()));
		player.send(new SendString("" + (getPriceType() == PriceType.VALUE ? NumberFormat.getInstance().format(containerValue()) : NumberFormat.getInstance().format(containerHighAlchValue())), 48513));
	}

	@Override
	public void refresh(int... slots) {
		for (final int slot : slots) {
			player.send(new SendItemOnInterfaceSlot(48542, stack[slot], slot));
		}
	}

	public PriceType getPriceType() {
		return priceType;
	}

	public void setPriceType(PriceType priceType) {
		this.priceType = priceType;
	}

	public boolean isPriceGuide() {
		return priceGuide;
	}

	public void setPriceGuide(boolean priceGuide) {
		this.priceGuide = priceGuide;
	}
	
	public Item getItemSearching() {
		return itemSearching;
	}

	public void setItemSearching(Item itemSearching) {
		this.itemSearching = itemSearching;
	}

}
*/
