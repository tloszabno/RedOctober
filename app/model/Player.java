package model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Player {

	private String nick;
	private String team;
	private double xPosition, yPosition;
	private double xDirection, yDirection;
	
	public Player(String nick, String team, double xPosition, double yPosition) {
		this.nick = nick;
		this.team = team;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.xDirection = randomV();
		this.yDirection = randomV();
	}

	private double randomV() {
		return Math.random()*10.0-5.0;
	}
	
	@JsonProperty("x")
	public double getxPosition() {
		return xPosition;
	}
	public void setxPosition(double xPosition) {
		this.xPosition = xPosition;
	}
	@JsonProperty("y")
	public double getyPosition() {
		return yPosition;
	}
	public void setyPosition(double yPosition) {
		this.yPosition = yPosition;
	}
	@JsonProperty("xDirection")
	public double getxDirection() {
		return xDirection;
	}
	public void setxDirection(double xDirection) {
		this.xDirection = xDirection;
	}
	@JsonProperty("yDirection")
	public double getyDirection() {
		return yDirection;
	}
	@JsonIgnore
	public void setyDirection(double yDirection) {
		this.yDirection = yDirection;
	}

	@JsonProperty("user_nick")
	public String getNick() {
		return nick;
	}
	
	@JsonProperty("team")
	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}
	
	public void move() {
		xPosition += xDirection;
		yPosition += yDirection;
	}

	@Override
	public String toString(){
		return "name:"+nick+" team:"+team;
	}

}
