package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.geometry.Vector;

import android.util.Log;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public abstract class Move extends BasicAction {
	public static final String ATTR_VELOCITY = "velocity";
	public static final String ATTR_DIRECTION = "direction";

	private static final Vector DEFAULT_DIRECTION = new Vector();

	public void recycle() {
		getSprite().removeAttribute(ATTR_VELOCITY);
		getSprite().removeAttribute(ATTR_DIRECTION);
		super.recycle();
	}

	protected float calDistance(Time time) {
		Sprite node = getSprite();
		long interval = time.getCurrentTicks() - time.getLastTicks();
		Float velocity = (Float) node.getAttribute(ATTR_VELOCITY);
		if (velocity == null)
			velocity = 0f;

		return velocity * interval / 1000;
	}

	protected void moveDistance(float distance) {
		Sprite node = getSprite();
		Vector direction = (Vector) node.getAttribute(ATTR_DIRECTION);
		if (direction == null)
			direction = DEFAULT_DIRECTION;

		direction.move(node.getPosition(), distance, node.getPosition());
	}
}
