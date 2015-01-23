package model;

public class Rock extends GameObject {

	public Rock(double x, double y) {
		super(x, y, 15.0d);
	}

	public double getSizeRadius() {
		return sizeRadius;
	}

	public void setSizeRadius(double sizeRadius) {
		this.sizeRadius = sizeRadius;
	}
}