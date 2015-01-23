package model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Player extends MovingObject{

	private String nick;
	private String team;
	@JsonIgnore
	private boolean isShot = false;
	//private double xPosition, yPosition;
	//private double xDirection, yDirection;
	
	//Możliwe że Player powinien rozszerzać MovingObject, ale wtedy w tym konstruktorze
	//musiałyby być też przemieszczenia x i y łodzi
	public Player(String nick, String team, double xPosition, double yPosition, double xDirection, double yDirection) {
		super(xPosition, yPosition, xDirection, yDirection, 30.0d);
		this.nick = nick;
		this.team = team;
		//this.xPosition = xPosition;
		//this.yPosition = yPosition;

		//this.xDirection = xDirection; //randomV();
		//this.yDirection = yDirection; //randomV();
	}

	/*private double randomV() {
		return Math.random()*10.0-5.0;
	}*/
	
	@JsonProperty("x")
	public double getxPosition() {
		return this.x;
	}
	public void setxPosition(double xPosition) {
		this.x = xPosition;
	}
	@JsonProperty("y")
	public double getyPosition() {
		return y;
	}
	public void setyPosition(double yPosition) {
		this.y = yPosition;
	}
	@JsonProperty("xDirection")
	public double getxDirection() {
		return this.deltaX;
	}
	public void setxDirection(double xDirection) {
		this.deltaX = xDirection;
	}
	@JsonProperty("yDirection")
	public double getyDirection() {
		return this.deltaY;
	}

	@JsonIgnore
	public void setyDirection(double yDirection) {
		this.deltaY = yDirection;
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
	
	@Override
	public String toString(){
		return "name:"+nick+" team:"+team;
	}

	@JsonProperty("is_shot")
	public boolean getIsShot(){
		return isShot;
	}
	
	public void shot() {
		isShot = true;
	}
	
}
