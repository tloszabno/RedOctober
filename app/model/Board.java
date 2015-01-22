package model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Board {

	private double width = 1000.0;
	private double height = 500.0;
	private double radar_radius = 120.0;

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
		for(MovingObject player : players.values()) {
			player.move();
		}
	}

	@JsonProperty("type")
	public String getType(){
		return "map_init_configuration";
	}

	@JsonProperty("type")
	public void setType(String type) {
		type = type.replaceAll("\"", "");
		if (!type.equalsIgnoreCase("map_init_configuration")){
			String message = String.format("Expected JSON object of type \"map_init_configuration\" not \"%s\"",type);
			throw new RuntimeException(message);
		}
	}

	@JsonProperty("x_size")
	public double getWidth() {
		return width;
	}

	public void setWidth(double w){
		this.width = w;
	}

	@JsonProperty("y_size")
	public double getHeight() {
		return height;
	}

	public void setHeight(double w){
		this.height = w;
	}
	
	@JsonProperty("radar_radius")
	public double getRadarRadius(){
		return this.radar_radius;
	}
	
	public void setRadarRadius(double radius){
		this.radar_radius = radius;
	}
	
	//@JsonProperty("initial_velocity")
	/*
	@JsonIgnore
	public double[] getInitialVelocity(){
		return new double[]{randomV(),randomV()};
	}
	
	public void setInitialVelocity(double[] v){
		//Ignore values
	}

	private double randomV() {
		return Math.random()*10.0-5.0;
	}*/

	@JsonIgnore
	public double getRandomX() {
		return Math.random()*getWidth();
	}

	@JsonIgnore
	public double getRandomY() {
		return Math.random()*getHeight();
	}

}
