package org.kevinth.kth2d.geometry;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Vector {
	private float x;

	private float y;

	public Vector() {
	}

	public Vector(Vector vector) {
		this.x = vector.x;
		this.y = vector.y;
	}

	public Vector(Point point) {
		this.x = point.getX();
		this.y = point.getY();
	}

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public Vector setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public Vector setY(float y) {
		this.y = y;
		return this;
	}

	public Vector set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector set(Vector vector) {
		this.x = vector.x;
		this.y = vector.y;
		return this;
	}

	public void move(Point src, float distance, Point dest) {
		distance = Math.abs(distance);
		float d = (float) Math.sqrt(x * x + y * y);
		dest.setX(x / d * distance + src.getX());
		dest.setY(y / d * distance + src.getY());
	}

	public float getDegrees() {
		return calDegrees(x, y);
	}

	public static float calDegrees(float x, float y) {
		float r = (float) Math.atan(y / x);
		float d = (float) Math.toDegrees(r);
		if (x < 0)
			return 180 + d;
		else
			return (d < 0) ? 360 + d : d;
	}

	public void reset() {
		this.x = 0;
		this.y = 0;
	}

	@Override
	public String toString() {
		return new StringBuffer().append("x:").append(x).append(", y:").append(
				y).toString();
	}

}