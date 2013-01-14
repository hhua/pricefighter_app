package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Rotate extends BasicAction {
	public static final String ATTR_ROTATE_PAST_TIME = "rotate-past-time";
	public static final String ATTR_ROTATE_VELOCITY = "rotate-velocity";

	private long time = -1;
	private float startAngle = Float.MAX_VALUE;
	private float endAngle = Float.MAX_VALUE;
	private float velocity = 0;

	public void recycle() {
		getSprite().removeAttribute(ATTR_ROTATE_PAST_TIME);
		getSprite().removeAttribute(ATTR_ROTATE_VELOCITY);
		super.recycle();
	}

	public Rotate() {
	}

	public Rotate(float velocity) {
		this.velocity = velocity;
	}

	public Rotate(float velocity, long time) {
		this.velocity = velocity;
		this.time = time;
	}

	public Rotate(long time, float endAngle) {
		this.time = time;
		this.endAngle = endAngle;
	}

	public Rotate(long time, float startAngle, float endAngle) {
		this.time = time;
		this.startAngle = startAngle;
		this.endAngle = endAngle;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(float startAngle) {
		this.startAngle = startAngle;
	}

	public float getEndAngle() {
		return endAngle;
	}

	public void setEndAngle(float endAngle) {
		this.endAngle = endAngle;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (startAngle == Float.MAX_VALUE)
			startAngle = getSprite().getDrawAngle();
		if (endAngle == Float.MAX_VALUE)
			endAngle = getSprite().getDrawAngle();

		if (velocity == 0) {
			getSprite().setAttribute(ATTR_ROTATE_PAST_TIME, new Long(0));
			getSprite().setDrawAngle(startAngle);
		} else {
			getSprite().setAttribute(ATTR_ROTATE_VELOCITY, velocity);
		}
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	@Override
	protected void onUpdate(Time time) {
		Sprite node = getSprite();
		Long pastTime = (Long) node.getAttribute(ATTR_ROTATE_PAST_TIME);
		if (pastTime == null)
			pastTime = 0l;
		pastTime += time.getCurrentTicks() - time.getLastTicks();

		if (velocity == 0) {
			if (pastTime >= this.time) {
				node.setDrawAngle(endAngle);
				this.done();
				return;
			} else {
				node.setDrawAngle(startAngle + (endAngle - startAngle)
						* pastTime / this.time);
			}
		} else {
			Float velocity = (Float) node.getAttribute(ATTR_ROTATE_VELOCITY);
			if (this.time > 0 && pastTime >= this.time) {
				this.done();
				return;
			} else {
				float angle = node.getDrawAngle();
				float delta = velocity
						* (time.getCurrentTicks() - time.getLastTicks()) / 1000;
				angle += delta;
				node.setDrawAngle(angle);
			}
		}

		node.setAttribute(ATTR_ROTATE_PAST_TIME, pastTime);

	}
}
