package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Fade extends BasicAction {
	public static final String ATTR_FADE_PAST_TIME = "fade-past-time";

	private long time = -1;
	private float startAlpha = -1;
	private float endAlpha = -1;

	public void recycle() {
		getSprite().removeAttribute(ATTR_FADE_PAST_TIME);
		super.recycle();
	}

	public Fade() {
	}

	public Fade(long time, float endAlpha) {
		this.time = time;
		this.endAlpha = endAlpha;
	}

	public Fade(long time, float startAlpha, float endAlpha) {
		this.time = time;
		this.startAlpha = startAlpha;
		this.endAlpha = endAlpha;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getStartAlpha() {
		return startAlpha;
	}

	public void setStartAlpha(float startAlpha) {
		this.startAlpha = startAlpha;
	}

	public float getEndAlpha() {
		return endAlpha;
	}

	public void setEndAlpha(float endAlpha) {
		this.endAlpha = endAlpha;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (startAlpha == -1)
			startAlpha = getSprite().getAlpha();
		if (endAlpha == -1)
			endAlpha = getSprite().getAlpha();

		getSprite().setAttribute(ATTR_FADE_PAST_TIME, new Long(0));
		getSprite().setAlpha(startAlpha);
	}

	@Override
	protected void onUpdate(Time time) {
		Sprite node = getSprite();
		Long pastTime = (Long) node.getAttribute(ATTR_FADE_PAST_TIME);
		if (pastTime == null)
			pastTime = 0l;
		pastTime += time.getCurrentTicks() - time.getLastTicks();
		if (pastTime >= this.time) {
			node.setAlpha(endAlpha);
			this.done();
			return;
		} else {
			node.setAlpha(startAlpha + (endAlpha - startAlpha) * pastTime
					/ this.time);
		}

		node.setAttribute(ATTR_FADE_PAST_TIME, pastTime);
	}

}
