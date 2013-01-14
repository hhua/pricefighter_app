package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Scale extends BasicAction {
	public static final String ATTR_SCALE_PAST_TIME = "scale-past-time";

	private long time = -1;
	private float startRatio = 1;
	private float endRatio = 1;

	public void recycle() {
		getSprite().removeAttribute(ATTR_SCALE_PAST_TIME);

		super.recycle();
	}

	public Scale() {
	}

	public Scale(long time, float endRatio) {
		this(time, 1, endRatio);
	}

	public Scale(long time, float startRatio, float endRatio) {
		this.time = time;
		this.startRatio = startRatio;
		this.endRatio = endRatio;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getStartRatio() {
		return startRatio;
	}

	public void setStartRatio(float startRatio) {
		this.startRatio = startRatio;
	}

	public float getEndRatio() {
		return endRatio;
	}

	public void setEndRatio(float endRatio) {
		this.endRatio = endRatio;
	}

	@Override
	protected void onStart() {
		super.onStart();
		getSprite().setAttribute(ATTR_SCALE_PAST_TIME, new Long(0));
		getSprite().setScaleRatio(startRatio);
	}

	@Override
	protected void onUpdate(Time time) {
		Sprite node = getSprite();
		Long pastTime = (Long) node.getAttribute(ATTR_SCALE_PAST_TIME);
		if (pastTime == null)
			pastTime = 0l;
		pastTime += time.getCurrentTicks() - time.getLastTicks();
		if (pastTime >= this.time) {
			node.setScaleRatio(endRatio);
			this.done();
			return;
		} else {
			node.setScaleRatio(startRatio + (endRatio - startRatio) * pastTime
					/ this.time);
		}

		node.setAttribute(ATTR_SCALE_PAST_TIME, pastTime);
	}

}
