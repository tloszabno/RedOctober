package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Torpedo extends MovingObject {

	private final double range = 50.0d;
	private double distance = 0.0d;
	private String striker_nick;

	public Torpedo(String striker_nick, double x, double y, double deltaX, double deltaY) {
		super(x, y, deltaX, deltaY);
		this.striker_nick = striker_nick;
	}

	public boolean isMoving() {
		return distance < range;
	}

	public void move() {
		super.move();
		distance += Math.sqrt(getDeltaX() * getDeltaX() + getDeltaY()
				* getDeltaY());
	}
	
	@JsonProperty("user_nick")
	public String getUserNick(){
		return striker_nick;
	}
	
	public void setUserNick(String nick){
		this.striker_nick = nick;
	}
	
	@JsonProperty("is_exploded")
	public boolean getIsExploded(){
		return !isMoving();
	}
	
	public void setIsExploded(boolean expl){
		//do nothing
	}
}
