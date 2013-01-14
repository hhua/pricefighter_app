package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Action;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.texture.Texture;

import android.util.Log;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Wait extends Action {
	private long duration = 0l;
	private Texture texture = null;

	private long waitedTime = 0;

	public Wait() {
	}

	public Wait(Texture texture, long duration) {
		this.texture = texture;
		this.duration = duration;
	}

	/* (non-Javadoc)
	 * @see org.kevinth.kth2d.Action#onReset()
	 */
	@Override
	public void recycle() {
		duration = 0l;
		texture = null;
		waitedTime = 0;
		super.recycle();
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	@Override
	protected void onStart() {
		super.onStart();
		waitedTime = 0;
		getSprite().setTexture(texture);
	}

	@Override
	protected void onUpdate(Time time) {
		super.onUpdate(time);
		if (waitedTime > duration) {
			this.done();
		}

		waitedTime += time.getCurrentTicks() - time.getLastTicks();
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " - duration:" + duration + ", "
				+ texture.toString();
	}

}
