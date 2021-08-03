package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class Dragon2H extends Special {

	public Dragon2H() {
		super(6.0, 1.0, 1.0, new int[] { 7158 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.startAnimation(3157);
		player.gfx0(559);
		player.specBarId = 7586;
		player.specAmount = player.specAmount - 6.0;
		player.usingSpecial = false;
		player.getItems().updateSpecialBar();
		player.getItems().addSpecialBar(player.playerEquipment[player.playerWeapon]);
		
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {
		player.sendMessage("AAA");
	}

}