package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MovingObject extends GameObject{

	protected double deltaX, deltaY;

	
	public MovingObject(double x, double y, double deltaX, double deltaY, double sizeRadius) {
		super(x, y, sizeRadius);
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	@JsonProperty("xDirection")
	public double getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(double deltaX) {
		this.deltaX = deltaX;
	}

	@JsonProperty("yDirection")
	public double getDeltaY() {
		return deltaY;
	}

	@JsonIgnore
	public void setDeltaY(double deltaY) {
		this.deltaY = deltaY;
	}

	public void move() {
		setX(getX() + getDeltaX());
		setY(getY() + getDeltaY());
	}
}