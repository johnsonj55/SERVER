/*package ethos.model.items.item_combinations;

import java.util.List;
import java.util.Optional;

import ethos.model.items.GameItem;
import ethos.model.items.ItemCombination;
import ethos.model.players.Player;

public class BrimstoneRing extends ItemCombination {

	public BrimstoneRing(GameItem outcome, Optional<List<GameItem>> revertedItems, GameItem[] items) {
		super(outcome, revertedItems, items);
	}

	@Override
	public void combine(Player player) {
		items.forEach(item -> player.getItems().deleteItem2(item.getId(), item.getAmount()));
		player.getItems().addItem(outcome.getId(), outcome.getAmount());
		player.startAnimation(6929);
		player.getDH().sendItemStatement("You combined the items and created a pair of Brimstone Ring.", outcome.getId());
		player.setCurrentCombination(Optional.empty());
		player.nextChat = -1;
	}

	@Override
	public void showDialogue(Player player) {
		player.getDH().sendStatement("Combining these is final.", "You cannot revert this.");
	}

}
*/
