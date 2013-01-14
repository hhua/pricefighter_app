package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.geometry.Vector;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class MoveTowards extends Move {
	private final Vector direction = new Vector();
	private float velocity = -1;

	public void recycle() {
		direction.reset();
		velocity = -1;

		super.recycle();
	}

	public MoveTowards() {
	}

	public MoveTowards(Vector direction, float velocity) {
		this.direction.set(direction);
		this.velocity = velocity;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public Vector getDirection() {
		return direction;
	}

	@Override
	public void onStart() {
		Sprite node = getSprite();
		node.setAttribute(Move.ATTR_VELOCITY, velocity);

		Vector v = (Vector) node.getAttribute(Move.ATTR_DIRECTION);
		if (v == null) {
			v = new Vector();
			node.setAttribute(Move.ATTR_DIRECTION, v);
		}
		v.set(direction);
	}

	@Override
	public void onUpdate(Time time) {
		Sprite node = getSprite();
		Float v = (Float) node.getAttribute(Move.ATTR_VELOCITY);
		if (v <= 0)
			this.done();
		else
			this.moveDistance(this.calDistance(time));
	}
}
