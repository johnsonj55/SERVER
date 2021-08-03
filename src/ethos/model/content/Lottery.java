package ethos.model.content;

import java.util.HashMap;
import java.util.Random;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

public class Lottery {

	public static int lotteryAmount = 300000000; //returns 300m, costs 350m total for the lottery.
	public static int winningNumber = 0;
	public static int numberOfGamblers = 0;
	static HashMap<String, Integer> lottoEntries = new HashMap<String, Integer>();

	public static void startLottery() {
		Random r = new Random();
		winningNumber = 1+ r.nextInt(100);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = (Player)PlayerHandler.players[j];
				c2.sendMessage("@blu@Mistex Lottery has been started. Talk to the gambler to enter. Good luck!");
			}
		}
	}
	
	public static void resetLottery() {
		winningNumber = 0;
		lotteryAmount = 0;
		numberOfGamblers = 0;
		lottoEntries.clear();
	}
	
	public static int checkNumber(){
		return winningNumber;
	}
	
	public static int checkLottoAmount(){
		return lotteryAmount;
	}
	public static void buyIn(int number, Player c) {
		
		if(c.getItems().playerHasItem(995, 25000000)) {
			lottoEntries.put((c.playerName), number);
			c.sendMessage("Thank you and good luck.");
			//c.getItems().addItem(ticketid, 1);
			numberOfGamblers++;
			if(numberOfGamblers == 14) {
				drawWinner(c);
			}
		} else {
			c.sendMessage("You must have 25m to enter the lottery.");
		}

	}
	
	public static void drawWinner(Player c) {
		Random rand = new Random();
		winningNumber = rand.nextInt(14);
		winLottery(winningNumber);
	}
	
	public static void winLottery(int iD){
		Player c = PlayerHandler.players[iD];
		c.getItems().addItem(995, lotteryAmount);
		c.getItems().updateInventory();
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = (Player)PlayerHandler.players[j];
				c2.sendMessage("Congratulations " + c2.playerName + " for winning the Mistex Jackpot! Enjoy your @blu@ " + lotteryAmount + " coins.");
			}
		}
		resetLottery();
	}
	
}