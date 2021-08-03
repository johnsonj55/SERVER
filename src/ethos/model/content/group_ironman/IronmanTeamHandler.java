package ethos.model.content.group_ironman;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ethos.Config;
import ethos.model.content.group_ironman.sorts.IronmanTeamAverageSort;
import ethos.model.content.group_ironman.sorts.IronmanTeamDateSort;
import ethos.model.players.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author optimum on 14/05/2020
 */
public final class IronmanTeamHandler {
    private static final Logger log = LoggerFactory.getLogger( IronmanTeamHandler.class );

    /**
     * The full list of ironmen
     */
    private static List<IronmanTeam> ironManTeams = new ArrayList<>();
    private static final Gson GSON = new Gson();
    private static final String SAVE_FILE = "./Data/groups/all_teams.json";

    /**
     * Gets a players team
     * @param player - the player we are getting team for
     * @return
     */
    public static Optional<IronmanTeam> getPlayersTeam(Player player) {
        return ironManTeams.stream().filter(e -> e.playerExists(player)).findFirst();
    }


    /**
     * Handles the logout of a player
     * @param player
     */
    public static void handleLogout(Player player) {
        Optional<IronmanTeam> getTeam = getPlayersTeam(player);
        if (getTeam.isPresent()) {
            getTeam.get().updatePlayer(player);
        }

        saveTeams();
    }

    /**
     * Creates an ironman team
     *
     * @param player - the player creating the team
     * @return - true if created
     */
    public static Optional<IronmanTeam> createIronmanTeam(Player player) {
        IronmanTeam newTeam = IronmanTeam.createTeam(player);

        if(getTeamByName(player.getName()).isPresent()) {
            return Optional.empty();
        }

        if(getPlayersTeam(player).isPresent()) {
            return Optional.empty();
        }

        ironManTeams.add(newTeam);
        return Optional.of(newTeam);
    }

    /**
     * Gets a team by it's name
     * @param name - the team name
     * @return
     */
    public static Optional<IronmanTeam> getTeamByName(String name) {
        return ironManTeams.stream().filter(e -> e.getTeamName().equalsIgnoreCase(name)).findFirst();
    }

    /**
     * Checks if a person already has invitation
     * @param username - the username to check for invitation
     * @return - true if the person has an invitation
     */
    public static boolean hasInvitation(Player player) {
        return getInvitation(player).isPresent();
    }
    
    /**
     * Accepts a ironman's invitation
     * @param player - the player accepted
     */
    public static void acceptInvitation(Player player) {
    	Optional<IronmanTeam> invitation = getInvitation(player);
    	if(invitation.isPresent()) {
    		invitation.get().acceptInvitation(player);
    		player.sendMessage("@gre@You've accepted the invitation and joined the group!");
    	}
    }
    
    public static Optional<IronmanTeam> getInvitation(Player player) {
    	for (IronmanTeam team: ironManTeams) {
            Optional<String> invitation = team.getInvitation();
            if (invitation.isPresent()) {
                if(player.playerName.equalsIgnoreCase(team.getInvitation().get())) {
                    return Optional.of(team);
                }
            }
        }
    	return Optional.empty();
    }


    /**
     * Returns a list of the latest ironment teams created
     * @return
     */
    public static List<IronmanTeam> getLatestIronmanTeams(int amount) {
        List<IronmanTeam> teams = new ArrayList<>(ironManTeams);
        Collections.sort(teams, IronmanTeamDateSort.DATE_SORT.reversed());
        return teams.stream().filter(e -> e.getMembers().size() > 1)
        		.limit(amount).collect(Collectors.toList());
    }

    public static List<IronmanTeam> getBestIronmenTeams(int count) {
        List<IronmanTeam> teams = new ArrayList<>(ironManTeams);
        Collections.sort(teams, IronmanTeamAverageSort.AVERAGE_SORT);
        return teams.stream().filter(e -> e.getMembers().size() > 1)
        		.limit(count).collect(Collectors.toList());
    }
    
    public static void deleteClan(IronmanTeam team, Player player) {
    	ironManTeams = ironManTeams.parallelStream()
    			.filter(e -> !e.equals(team))
    			.collect(Collectors.toList());
    	
    	player.sendMessage("You have deleted your clan.");
    }
    
    
    public static void deleteClan(Player player) {
    	Optional<IronmanTeam> getTeam = getPlayersTeam(player);
    	if (getTeam.isPresent()) {
			deleteClan(getTeam.get(), player);
		}
    }

    /**
     * Loads all the teams
     */
    public static void loadTeams() {
        try {
            File loadDirectory = new File(SAVE_FILE);
            List<String> contents = Files.readAllLines(loadDirectory.toPath());
            ironManTeams = GSON.fromJson(contents.get(0), new TypeToken<List<IronmanTeam>>(){}.getType());
            log.info("Loaded " + ironManTeams.size() + "ironmen teams");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves all teams to the designated file
     */
    public static void saveTeams() {
        try {
            File loadDirectory = new File(SAVE_FILE);
            String contents = GSON.toJson(ironManTeams);
            Files.write(loadDirectory.toPath(), contents.getBytes());
            ironManTeams = GSON.fromJson(contents, new TypeToken<List<IronmanTeam>>(){}.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private IronmanTeamHandler() throws IllegalAccessException {
        throw new IllegalAccessException("cannot init this class");
    }
}
