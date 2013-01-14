package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Accelerate extends BasicAction {
	public static final String ATTR_ROTATE_PAST_TIME = "acc-past-time";
	private long time = -1;
	private float acceleration = 0;

	public Accelerate() {
	}

	public Accelerate(float acceleration) {
		this.acceleration = acceleration;
	}

	public Accelerate(float acceleration, long time) {
		this.acceleration = acceleration;
		this.time = time;
	}

	public void recycle() {
		getSprite().removeAttribute(ATTR_ROTATE_PAST_TIME);

		super.recycle();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	@Override
	protected void onStart() {
		super.onStart();
		getSprite().setAttribute(ATTR_ROTATE_PAST_TIME, new Long(0));
	}

	@Override
	protected void onUpdate(Time time) {
		Sprite node = getSprite();
		Long pastTime = (Long) node.getAttribute(ATTR_ROTATE_PAST_TIME);
		if (pastTime == null)
			pastTime = 0l;
		pastTime += time.getCurrentTicks() - time.getLastTicks();
		if (this.time > 0 && pastTime >= this.time) {
			this.done();
			return;
		}
		Float velocity = (Float) node.getAttribute(Move.ATTR_VELOCITY);
		if (velocity != null) {
			velocity += acceleration * pastTime / 1000;
			node.setAttribute(Move.ATTR_VELOCITY, velocity);
			if (velocity <= 0) {
				this.done();
				return;
			}
		}
		node.setAttribute(ATTR_ROTATE_PAST_TIME, pastTime);
	}
}
