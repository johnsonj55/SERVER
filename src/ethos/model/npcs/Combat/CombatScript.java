package ethos.model.npcs.Combat;

import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;

public abstract class CombatScript {
	/**
	 * If the npc can attack back.
	 */
	private boolean canAttack = true;
	
	/**
	 * Handles an attack for an {@link NPC} vs another {@link Entity}.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param source
	 * 			the target.
	 * @return
	 * 			the delay of the attack in ticks.
	 */
	public abstract int attack(final NPC npc, final Entity target);
	
	/**
	 * Gets the attack distance for the npc.
	 * 
	 * @param npc
	 * 			the npc.
	 * @return
	 * 			the attack distance.
	 */
}