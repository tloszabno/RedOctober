package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameObject {

	protected double x, y, sizeRadius;

	public GameObject(double x, double y, double sizeRadius) {
		this.x = x;
		this.y = y;
		this.sizeRadius = sizeRadius;
	}

	@JsonProperty("x")
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@JsonProperty("y")
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getSizeRadius() {
		return sizeRadius;
	}

	public void setSizeRadius(double sizeRadius) {
		this.sizeRadius = sizeRadius;
	}
	
	
}