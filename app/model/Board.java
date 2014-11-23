package model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
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

	@JsonProperty("width")
	public double getWidth() {
		return width;
	}

	@JsonProperty("height")
	public double getHeight() {
		return height;
	}

	@JsonProperty("playerRadius")
	public double getPlayerRadius() {
		return playerRadius;
	}

	@JsonIgnore
	public double getRandomX() {
		return Math.random()*getWidth();
	}

	@JsonIgnore
	public double getRandomY() {
		return Math.random()*getHeight();
	}

}
