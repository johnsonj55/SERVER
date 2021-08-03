package ethos.model.content.group_ironman;

import ethos.model.players.Player;
import ethos.util.Misc;

import java.time.Instant;
import java.util.*;

/**
 * Represents an ironman team
 *
 * @author optimum on 14/05/2020
 */
public class IronmanTeam {

    private String teamName;
    public String leaderName;
    private List<IronmanTeamMember> members;
    private Date dateStated;
    private Optional<String> invitation = Optional.empty();

    /**
     * Creates a new team from a player
     * @param player - the player creating the team
     * @return
     */
    public static IronmanTeam createTeam(Player player) {
        List<IronmanTeamMember> members = new ArrayList<>();
        members.add(new IronmanTeamMember(player));
        return new IronmanTeam().setDateStated(Date.from(Instant.now()))
                .setLeaderName(player.getName())
                .setTeamName(player.getName())
                .setMembers(members);
    }
    
    
    public void acceptInvitation(Player player) {
    	invitation = Optional.empty();
    	members.add(new IronmanTeamMember(player));
    }

    public boolean isOnline() {
        return !getOnlineMembers().isEmpty();
    }

    public String getOnlineStatusText() {
        return isOnline() ? "@gre@Online" : "@red@Offline";
    }

    public void setTeamName(Player player, String teamName) {
        if(player.getName().equalsIgnoreCase(leaderName)) {
            this.teamName = teamName;
        }
    }
    /**
     * Updates the player
     *
     * @param player - the player too update
     */
    public void updatePlayer(Player player) {
        Optional<IronmanTeamMember> member = getMember(player.getName());
        if (!member.isPresent()) {
            return;
        }
        member.get().update(player);
    }

    
    public Optional<IronmanTeamMember> getMember(String username) {
        return members.stream().filter(e -> e.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    /**
     * Checks a player exists within the team
     * @param player - the player we are checking
     * @return true if the player exists
     */
    public boolean playerExists(Player player) {
        return members.stream().filter(
                e -> e.getUsername().equalsIgnoreCase(player.playerName)).findAny().isPresent();
    }

    public boolean isTeamMember(String name) {
        return members.stream().filter(e -> e.getUsername().equalsIgnoreCase(name)).findFirst().isPresent();
    }

    public boolean isTeamMember(Player player) {
        return isTeamMember(player.playerName);
    }

    /**
     * Gets a list of online players
     *
     * @return - a list of online players
     */
    public List<Player> getOnlineMembers() {
        List<Player> players = new ArrayList<>();
        for (IronmanTeamMember member : members) {
            Optional<Player> online = member.getPlayer();
            if (online.isPresent()) {
                players.add(online.get());
            }
        }
        return players;
    }

    /**
     * A method to try and invite a player to a clan
     * @param inviter - the person inviting
     * @param target - the target to invite
     * @return - true if the target can be invited
     */
    public boolean canInvite(Player inviter, Player target) {
        if(target.totalLevel > 500) {
            inviter.sendMessage("@red@You can't invite this player, they need to be below 500 total level.");
            return false;
        }

        if(!target.getMode().isGroupIronman()) {
            inviter.sendMessage("@red@This player isn't a group Ironman.");
            return false;
        }

        if(IronmanTeamHandler.hasInvitation(target)) {
            inviter.sendMessage("@red@This player already has a pending request.");
            return false;
        }

        if(isTeamMember(target)) {
            inviter.sendMessage("@red@This player is already on your team.");
            return false;
        }

        if(!leaderName.equalsIgnoreCase(inviter.playerName)) {
            inviter.sendMessage("@red@You have to be group leader to invite others.");
            return false;
        }

        if(isTeamFull()) {
            inviter.sendMessage("@red@Your team is already full.");
            return false;
        }

        if(IronmanTeamHandler.getPlayersTeam(target).isPresent()) {
            inviter.sendMessage("@red@This player already has a team.");
            return false;
        }

        return true;
    }

    /**
     * Sends an invitation to the person
     *
     * @param target - the target player
     */
    public void sendInvitation(Player inviter, Player target) {
        invitation = Optional.of(target.playerName);
        target.sendMessage(inviter.getName() + ":invite:");
        inviter.sendMessage("Request sent to " + target.getName());
    }

    public boolean isTeamFull() {
        return members.size() >= 4;
    }

    public int getAverageCombatLevel() {
        int count = members.size();
        int total = members.stream().mapToInt(IronmanTeamMember::getCombatLevel).sum();
        return (int)Math.abs((float) total / ((float) count));
    }

    public long getAverageTotalXp() {
        int count = members.size();
        long total = members.stream().mapToLong(IronmanTeamMember::getTotalXp).sum();
        return (long)Math.abs((float) total / ((float) count));
    }

    public int getAverageTotalLevel() {
        int count = members.size();
        int total = members.stream().mapToInt(IronmanTeamMember::getTotalLevel).sum();
        return (int)Math.abs((float) total / ((float) count));
    }

    public String getTeamName() {
        return teamName;
    }

    public IronmanTeam setTeamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public IronmanTeam setLeaderName(String leaderName) {
        this.leaderName = leaderName;
        return this;
    }

    public List<IronmanTeamMember> getMembers() {
        return members;
    }

    public IronmanTeam setMembers(List<IronmanTeamMember> members) {
        this.members = members;
        return this;
    }

    public Date getDateStated() {
        return dateStated;
    }

    public IronmanTeam setDateStated(Date dateStated) {
        this.dateStated = dateStated;
        return this;
    }


    public Optional<String> getInvitation() {
        return invitation;
    }

    public IronmanTeam setInvitation(Optional<String> invitation) {
        this.invitation = invitation;
        return this;
    }

    public String getMembersAsString() {
        StringBuilder builder = new StringBuilder();
        for (IronmanTeamMember member : members) {
            builder.append(member.getUsername() + ", ");
        }
        return Misc.optimizeText(builder.toString().substring(0, builder.length() - 2));
    }

}
