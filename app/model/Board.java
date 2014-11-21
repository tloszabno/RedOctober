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

	public boolean addPlayer(Player player) {
		if (players.containsKey(player.getNick())) {
			return false;
		}

		players.put(player.getNick(), player);
		return true;
	}

	public boolean removePlayer(Player player) {
		if (!players.containsKey(player.getNick())) {
			return false;
		}
		players.remove(player.getNick());
		return true;
	}

	public void changePlayerDirection(Player player, double xDirection,
			double yDirection) { // nie wiem czy tu nie lepiej jakies delty
									// przekazywac
		player.setxDirection(xDirection);
		player.setyDirection(yDirection);
	}
	
	public void nextStep() {
		for(Player player : players.values()) {
			player.move();
		}
	}

}
