package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Torpedo extends MovingObject {

	private final double range = 250.0d;
	private double distance = 0.0d;
	@JsonIgnore
	private boolean hit = false;
	private String striker_nick;

	public Torpedo() { super(0, 0, 0, 0, 5.0d);}
	public Torpedo(String striker_nick, double x, double y, double deltaX, double deltaY) {
		super(x, y, deltaX, deltaY, 5.0d);
		this.striker_nick = striker_nick;
	}

	@JsonIgnore
	public boolean isMoving() {
		return hit || distance < range;
	}

	public void explode() {
		hit = true;
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
