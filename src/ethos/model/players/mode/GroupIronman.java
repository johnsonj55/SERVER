package ethos.model.players.mode;

import java.util.Objects;
import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.content.group_ironman.IronmanTeam;
import ethos.model.items.GroundItem;
import ethos.model.minigames.pest_control.PestControlRewards.RewardButton;

public class GroupIronman extends Mode {

	/**
	 * @param type
	 */
	public GroupIronman(ModeType type) {
		super(type);
	}

	public boolean isTradingPermitted(Player player, Player other) {
		if (player == null || other == null) {
			return false;
		}
		
		// and again, we have to get it's optional
		 
		Optional<IronmanTeam> getTeam = player.getIronmanTeam();
		
		//So now, because this is optional, it could be null
		
		if(getTeam.isPresent()) {
			// If it has a value
			return getTeam.get().isTeamMember(other);
		}
		return false;
	}

	@Override
	public boolean isStakingPermitted() {
		return false;
	}

	public boolean isItemScavengingPermitted(Player player, GroundItem item) {
		if (Objects.isNull(player) || Objects.isNull(item))
			return false;
		
		Optional<IronmanTeam> getTeam = player.getIronmanTeam();
		//And again, we have to check if they even have a team, you can do this one
		if (getTeam.isPresent()) {
			return getTeam.get().isTeamMember(item.getController());
		}
		return false;
	}

	@Override
	public boolean isPVPCombatExperienceGained() {
		return true;
	}

	@Override
	public boolean isDonatingPermitted() {
		return true;
	}

	@Override
	public boolean isVotingPackageClaimable(String packageName) {
		return Mode.forType(ModeType.IRON_MAN).isVotingPackageClaimable(packageName);
	}

	@Override
	public boolean isShopAccessible(int shopId) {
		return Mode.forType(ModeType.IRON_MAN).isShopAccessible(shopId);
	}

	@Override
	public boolean isItemPurchasable(int shopId, int itemId) {
		return Mode.forType(ModeType.IRON_MAN).isItemPurchasable(shopId, itemId);
	}

	@Override
	public boolean isItemSellable(int shopId, int itemId) {
		return Mode.forType(ModeType.IRON_MAN).isItemSellable(shopId, itemId);
	}

	@Override
	public boolean isRewardSelectable(RewardButton reward) {
		return Mode.forType(ModeType.IRON_MAN).isRewardSelectable(reward);
	}

	@Override
	public boolean isBankingPermitted() {
		return true;
	}
	
	@Override
	public int getModifiedShopPrice(int shopId, int itemId, int price) {
		return Mode.forType(ModeType.IRON_MAN).getModifiedShopPrice(shopId, itemId, price);
	}

}
