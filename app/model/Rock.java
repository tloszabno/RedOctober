package model;

public class Rock extends GameObject {

	private double sizeRadius = 15.0d;

	public Rock(double x, double y, double sizeRadius) {
		super(x, y);
		this.sizeRadius = sizeRadius;
	}

	public double getSizeRadius() {
		return sizeRadius;
	}

	public void setSizeRadius(double sizeRadius) {
		this.sizeRadius = sizeRadius;
	}
}