package ethos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ethos.Config;
import ethos.model.players.Player;


public class Votes implements Runnable {

	public static final String	HOST		= "107.180.51.86";
	public static final String	USER		= "votes";
	public static final String	PASS		= "12341234";
	public static final String	DATABASE	= "votes";

	private Player		player;
	private Connection	conn;
	private Statement	stmt;

	public Votes(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		if (Config.BLOCK_SQL == true) {
			return;
		}
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String		name	= player.getName().replace(" ", "_");
			ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='"+name+"' AND claimed=0 AND callback_date IS NOT NULL");
			int			voteCount = 0;
			while (rs.next()) {
				if (rs == null) 
					return; 
				int		siteId		= rs.getInt("site_id");
				player.votePoints += Config.DOUBLE_VOTE_INCENTIVES ? 2 : 1;
				player.sendMessage("You currently have " + player.votePoints + " voting points.");
				if (siteId >= 0 && siteId <= 100) { player.getItems().addItem(995, Config.DOUBLE_VOTE_INCENTIVES ? 500000 : 250000);
				} 
				voteCount += Config.DOUBLE_VOTE_INCENTIVES ? 2 : 1;
				//System.out.println("[Vote] Vote claimed by " + name + ". (sid: " + siteId + ", ip: " + ipAddress + ")");
				rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
				rs.updateRow();
			}

			if (voteCount >= 4) {    }
			if (voteCount > 0){   }

			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Failing connecting to database!");
			return false;
		}
	}

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
