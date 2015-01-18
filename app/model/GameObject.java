package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameObject {

	protected double x, y;

	public GameObject(double x, double y) {
		this.x = x;
		this.y = y;
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
}