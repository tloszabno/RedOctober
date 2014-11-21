package model;

import java.util.HashMap;
import java.util.Map;

public class Board {

	private final double width = 1000.0;
	private final double height = 500.0;
	private final double playerRadius = 10.0;
	private Map<String, Player> players;

	public Board() {
		players = new HashMap<String, Player>();
	}

	public boolean addPlayer(String nick, double xPosition, double yPosition) {
		if (players.containsKey(nick)) {
			return false;
		}

		Player newPlayer = new Player(nick, xPosition, yPosition);
		players.put(nick, newPlayer);
		return true;
	}

	public boolean removePlayer(String nick) {
		if (!players.containsKey(nick)) {
			return false;
		}
		players.remove(nick);
		return true;
	}

	public void changePlayerDirection(String nick, double xDirection,
			double yDirection) { // nie wiem czy tu nie lepiej jakies delty
									// przekazywac
		Player player = players.get(nick);
		player.setxDirection(xDirection);
		player.setyDirection(yDirection);
	}
	
	public void nextStep() {
		for(Player player : players.values()) {
			player.move();
		}
	}

}
