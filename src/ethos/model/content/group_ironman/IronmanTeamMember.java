package ethos.model.content.group_ironman;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

import java.util.Optional;

/**
 * Represents an ironman team member
 *
 * @author optimum on 14/05/2020
 */
public class IronmanTeamMember {

    private String username;
    private int combatLevel;
    private long totalXp;
    private int totalLevel;

    /**
     * updates setting with a player object
     * @param player - the player
     */
    public void update(Player player) {
        this.username = player.playerName;
        this.combatLevel = player.combatLevel;
        this.totalXp = player.getTotalExperience();
        this.totalLevel = player.getTotalLevel();
    }

    public IronmanTeamMember() {
    }

    public IronmanTeamMember(Player player) {
        update(player);
    }

    public String getUsername() {
        return username;
    }

    public Optional<Player> getPlayer() {
        return PlayerHandler.getOptionalPlayer(this.username);
    }

    public IronmanTeamMember setUsername(String username) {
        this.username = username;
        return this;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public IronmanTeamMember setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
        return this;
    }

    public long getTotalXp() {
        return totalXp;
    }

    public IronmanTeamMember setTotalXp(long totalXp) {
        this.totalXp = totalXp;
        return this;
    }

    public int getTotalLevel() {
        return totalLevel;
    }

    public IronmanTeamMember setTotalLevel(int totalLevel) {
        this.totalLevel = totalLevel;
        return this;
    }
}
