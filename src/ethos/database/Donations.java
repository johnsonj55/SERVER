package ethos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ethos.Config;
import ethos.model.players.Player;
 

/**
 * Using this class:
 * To call this class, it's best to make a new thread. You can do it below like so:
 * new Thread(new Donation(player)).start();
 */
public class Donations implements Runnable {

	public static final String	HOST		= "107.180.51.86";	// website ip address
	public static final String	USER		= "donate";
	public static final String	PASS		= "12341234";
	public static final String	DATABASE	= "donate";
	


	private Player		player;
	private Connection	conn;
	private Statement	stmt;

	/**
	 * The constructor
	 * 
	 * @param player
	 */
	public Donations(Player player) {
		this.player = player;
	}

	public static int totalAmountDonated = Config.totalAmountDonated;

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String		name	= player.getName().replace("_", " ");
			ResultSet	rs		= executeQuery("SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");

			while (rs.next()) {
				int		item_number	= rs.getInt("item_number");
				int		quantity	= rs.getInt("quantity")  ;

				//int costAmount = (int) paid;
				int costAmount = item_number == 18 ? 1 : item_number == 19 ? 30 : item_number == 20 ? 30 : item_number == 21 ? 30 : item_number == 22 ? 30 : item_number == 23 ? 20 : item_number == 24 ? 50 : item_number == 26 ? 25 : item_number == 27 ? 25 : item_number == 28 ? 4 : item_number == 29 ? 4  : item_number == 31 ? 200 : item_number == 32 ? 27 : 0;
				// add products according to their ID in the ACP
				switch (item_number) {
					// Donator Shards
					case 18: player.getItems().addItemUnderAnyCircumstance(32950, quantity);
					break;
					//Chaotic maul // item id from shop
					case 19:player.amDonated += costAmount; player.getItems().addItemUnderAnyCircumstance(33089, quantity);
					break; 
					// x3 rare box deal * amount purchased.
					case 23: player.amDonated += costAmount;  player.getItems().addItemUnderAnyCircumstance(13346, 3 * quantity);
					break;
					//Divine  
					//bandos set
					case 26:
						player.amDonated += costAmount;
						player.getItems().addItemUnderAnyCircumstance(11834, quantity);
						player.getItems().addItemUnderAnyCircumstance(11836, quantity);
						player.getItems().addItemUnderAnyCircumstance(11832, quantity);

						break;
					//Armadyl set
					case 27:
						player.amDonated += costAmount;
						player.getItems().addItemUnderAnyCircumstance(11828, quantity);
						player.getItems().addItemUnderAnyCircumstance(11830, quantity);
						player.getItems().addItemUnderAnyCircumstance(11826, quantity);

						break;
					//Superior dragon bones
					case 35: player.amDonated += costAmount;player.getItems().addItemUnderAnyCircumstance(22125, 150 * quantity);
					break;
					//Anglerfish
					case 29: player.amDonated += costAmount; player.getItems().addItemUnderAnyCircumstance(13442, 1000 * quantity);
					break;
					case 34:
						player.amDonated += 8;
						player.getItems().addItemUnderAnyCircumstance(13346, 1 * quantity);
						break;
						 
					case 31:
						player.amDonated += costAmount;
						player.getItems().addItemUnderAnyCircumstance(33563, 1 * quantity);
						player.getItems().addItemUnderAnyCircumstance(33564, 1 * quantity);
						player.getItems().addItemUnderAnyCircumstance(33565, 1 * quantity);
						break;
					case 32:
						player.amDonated += costAmount;
						player.getItems().addItemUnderAnyCircumstance(32998, 1 * quantity);
						player.getItems().addItemUnderAnyCircumstance(32999, 1 * quantity);
						player.getItems().addItemUnderAnyCircumstance(33000, 1 * quantity);
						player.getItems().addItemUnderAnyCircumstance(33001, 1 * quantity);
						break;
					case 33:
						player.amDonated += 125;
						player.getItems().addItemToBank(7630, 1 * quantity);
						break;
				} 
 
				player.getPA().yell(player.getName() + " has just donated $" + costAmount * quantity +" to the server!");
				Config.totalAmountDonated += quantity ;

				rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
				rs.updateRow();
			}

			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param host
	 *            the host ip address or url
	 * @param database
	 *            the name of the database
	 * @param user
	 *            the user attached to the database
	 * @param pass
	 *            the users password
	 * @return true if connected
	 */
	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Failing connecting to database!");
			return false;
		}
	}

	/**
	 * Disconnects from the MySQL server and destroy the connection
	 * and statement instances
	 */
	public void destroy() {
		try {
			conn.close();
			conn = null;
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes an update query on the database
	 * 
	 * @param query
	 * @see {@link Statement#executeUpdate}
	 */
	public int executeUpdate(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			int results = stmt.executeUpdate(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	/**
	 * Executres a query on the database
	 * 
	 * @param query
	 * @see {@link Statement#executeQuery(String)}
	 * @return the results, never null
	 */
	public ResultSet executeQuery(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			ResultSet results = stmt.executeQuery(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
