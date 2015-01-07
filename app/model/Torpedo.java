package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Torpedo extends MovingObject {

	private final double range = 50.0d;
	private double distance = 0.0d;

	public Torpedo() { super(0, 0, 0, 0);}
	public Torpedo(double x, double y, double deltaX, double deltaY) {
		super(x, y, deltaX, deltaY);
	}

	@JsonIgnore
	public boolean isMoving() {
		return distance < range;
	}

	public void move() {
		super.move();
		distance += Math.sqrt(getDeltaX() * getDeltaX() + getDeltaY()
				* getDeltaY());
	}
}
