package org.kevinth.kth2d;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Time {
	private long currentTicks = 0l;
	private long lastTicks = 0l;
	private float timeFactor = 1;

	public long getCurrentTicks() {
		return currentTicks;
	}

	public void setCurrentTicks(long currentTicks) {
		this.currentTicks = currentTicks;
	}

	public long getLastTicks() {
		return lastTicks;
	}

	public void setLastTicks(long lastTicks) {
		this.lastTicks = lastTicks;
	}

	public float getTimeFactor() {
		return timeFactor;
	}

	public void setTimeFactor(float timeFactor) {
		this.timeFactor = timeFactor;
	}

	public long getInterval() {
		return this.currentTicks - this.lastTicks;
	}
}
