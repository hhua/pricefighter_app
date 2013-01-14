package org.kevinth.kth2d.geometry;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Point {
	private float x;

	private float y;

	public Point() {
	}

	public Point(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public Point setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public Point setY(float y) {
		this.y = y;
		return this;
	}

	public Point set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Point set(Point point) {
		this.x = point.x;
		this.y = point.y;
		return this;
	}

	public static float distance(float sx, float sy, float dx, float dy) {
		float tx = dx - sx;
		float ty = dy - sy;
		return (float) Math.sqrt(tx * tx + ty * ty);
	}

	public float distance(Point target) {
		return distance(x, y, target.x, target.y);
	}

	public float distance(float x, float y) {
		return distance(this.x, this.y, x, y);
	}

	public void move(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public void reset() {
		this.x = 0;
		this.y = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Point))
			return false;
		Point op = (Point) o;
		if (x == op.x && y == op.y)
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return new Float(x).hashCode() ^ new Float(y).hashCode();
	}

	@Override
	public String toString() {
		return new StringBuffer().append("x:").append(x).append(", y:").append(
				y).toString();
	}

}
