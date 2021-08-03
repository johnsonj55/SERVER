package ethos.model.players.combat.specials;
import ethos.Config;
import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.Special;
import ethos.model.players.combat.magic.MagicData;
import ethos.model.players.mode.ModeType;
import ethos.util.Misc;

public class EldritchStaff extends Special {

	public EldritchStaff() {
		super(5.0, 2.10, 1.30, new int[] { 24425 }); 
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.startAnimation(3299);
		player.getPA().addSkillXP(6, 3, true);
		if (damage.getAmount() > 75) {
			damage.setAmount(75);
		}
		
//		target.appendDamage(Misc.random(25), Misc.random(25) > 0 ? Hitmark.HIT : Hitmark.MISS);
//		target.addDamageTaken(target, Misc.random(25)); //25
		
		if (target instanceof Player) {
			((Player) target).gfx0(77);
		}
		
		if (target instanceof NPC) {
			((NPC) target).gfx0(77);
		}
	}

	@Override
	
	public void hit(Player player, Entity target, Damage damage) {
		
	}

}
